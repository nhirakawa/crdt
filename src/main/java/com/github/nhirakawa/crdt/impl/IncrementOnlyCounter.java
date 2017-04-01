package com.github.nhirakawa.crdt.impl;

import java.util.function.BinaryOperator;

import com.github.nhirakawa.crdt.models.ConvergentCrdt;
import com.github.nhirakawa.crdt.service.CrdtConfiguration;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class IncrementOnlyCounter extends ConvergentCrdt<IncrementOnlyCounter, Integer> {

  private static final int IDENTITY = 0;
  private static final BinaryOperator<Integer> SUM = (a, b) -> a + b;

  @Inject
  public IncrementOnlyCounter(CrdtConfiguration configuration) {
    super(IDENTITY, SUM, SUM, Math::max, configuration.getNodeId());
  }

  @Override
  public String getResourceName() {
    return "incrementor";
  }

  @Override
  public Class<Integer> getValueClass() {
    return Integer.class;
  }
}
