package com.github.nhirakawa.crdt.impl;

import java.util.function.BinaryOperator;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.nhirakawa.crdt.models.ConvergentCrdt;
import com.github.nhirakawa.crdt.service.CrdtConfiguration;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class IncrementOnlyCounter extends ConvergentCrdt<IncrementOnlyCounter, Integer> {

  private static final int IDENTITY = 0;
  private static final BinaryOperator<Integer> SUM = (a, b) -> a + b;

  @Inject
  public IncrementOnlyCounter(CrdtConfiguration configuration,
                              ObjectMapper objectMapper) {
    super(
        IDENTITY,
        SUM,
        SUM,
        Math::max,
        configuration.getNodeId(),
        objectMapper.getTypeFactory().constructType(new TypeReference<Integer>(){})
    );
  }

  @Override
  public String getNamespace() {
    return "incrementOnlyCounter";
  }
}
