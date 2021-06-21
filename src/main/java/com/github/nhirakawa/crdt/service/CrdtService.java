package com.github.nhirakawa.crdt.service;

import com.github.nhirakawa.crdt.guice.CrdtServiceModule;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.hubspot.dropwizard.guice.GuiceBundle;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class CrdtService extends Application<CrdtConfiguration> {

  @Override
  public void initialize(Bootstrap<CrdtConfiguration> bootstrap) {
    GuiceBundle<CrdtConfiguration> guiceBundle = GuiceBundle
      .<CrdtConfiguration>newBuilder()
      .addModule(new CrdtServiceModule())
      .enableAutoConfig(getClass().getPackage().getName())
      .setConfigClass(CrdtConfiguration.class)
      .build();
    bootstrap.addBundle(guiceBundle);
  }

  @Override
  public String getName() {
    return "crdt";
  }

  @Override
  public void run(CrdtConfiguration configuration, Environment environment)
    throws Exception {
    Preconditions.checkArgument(
      !Strings.isNullOrEmpty(configuration.getNodeId()),
      "must set nodeId in crdt.yaml"
    );
  }

  public static void main(String... args) throws Exception {
    new CrdtService().run(args);
  }
}
