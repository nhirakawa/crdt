package com.github.nhirakawa.crdt.guice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

public class CrdtServiceModule extends AbstractModule {

  protected void configure() {
    bind(ObjectMapper.class).toInstance(new ObjectMapper());
  }

  @Provides
  @Singleton
  public CloseableHttpClient provideHttpClient() {
    return HttpClientBuilder.create().setUserAgent("crdt/1.0").build();
  }
}
