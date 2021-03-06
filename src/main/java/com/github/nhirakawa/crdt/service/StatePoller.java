package com.github.nhirakawa.crdt.service;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.nhirakawa.crdt.models.ConvergentCrdt;
import com.github.nhirakawa.crdt.models.CrdtModel;
import com.github.nhirakawa.crdt.models.ImmutableCrdtModel;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.io.InputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.Map;
import java.util.Optional;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class StatePoller<T extends ConvergentCrdt<T, V>, V> {
  private static final Logger LOG = LoggerFactory.getLogger(StatePoller.class);

  private final ConvergentCrdt<T, V> crdt;
  private final CloseableHttpClient httpClient;
  private final ObjectMapper objectMapper;
  private final CrdtConfiguration configuration;
  private final ScheduledExecutorService scheduledExecutorService;
  private final JavaType crdtModelType;

  private Optional<ScheduledFuture<?>> scheduledFuture;

  @Inject
  public StatePoller(
    T crdt,
    CrdtConfiguration configuration,
    CloseableHttpClient httpClient,
    ObjectMapper objectMapper
  ) {
    this.crdt = crdt;
    this.configuration = configuration;
    this.httpClient = httpClient;
    this.objectMapper = objectMapper;
    this.scheduledExecutorService = new ScheduledThreadPoolExecutor(1);
    this.crdtModelType =
      objectMapper
        .getTypeFactory()
        .constructParametricType(ImmutableCrdtModel.class, crdt.getValueType());
    this.scheduledFuture = Optional.empty();

    start();
  }

  public void start() {
    LOG.info("Starting state poller...");
    if (scheduledFuture.isPresent()) {
      throw new IllegalArgumentException("poller already started");
    }
    scheduledFuture =
      Optional.of(
        scheduledExecutorService.scheduleAtFixedRate(
          this::poll,
          0,
          configuration.getPollInterval(),
          TimeUnit.MILLISECONDS
        )
      );
  }

  public void poll() {
    LOG.debug("Polling servers...");
    for (String host : configuration.getClusterHosts()) {
      Map<String, V> otherCrdt = getOtherCrdt(host);
      if (!otherCrdt.isEmpty()) {
        crdt.merge(otherCrdt);
      }
    }
  }

  public void stop() {
    LOG.info("Stopping state poller...");
    scheduledFuture.ifPresent(future -> future.cancel(false));
    scheduledFuture = Optional.empty();
  }

  private Map<String, V> getOtherCrdt(String host) {
    try {
      HttpGet get = buildGet(host, crdt.getNamespace());
      CloseableHttpResponse response = httpClient.execute(get);
      return parseInputStream(response.getEntity().getContent()).getValues();
    } catch (Exception e) {
      LOG.warn(e.getMessage());
    }

    return Collections.emptyMap();
  }

  private static HttpGet buildGet(String host, String namespace) {
    return new HttpGet(
      String.format("http://%s/crdt/%s/full", host, namespace)
    );
  }

  private CrdtModel<V> parseInputStream(InputStream inputStream)
    throws IOException {
    return objectMapper.readValue(inputStream, crdtModelType);
  }
}
