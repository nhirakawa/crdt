package com.github.nhirakawa.crdt.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.nhirakawa.crdt.models.ConvergentCrdt;
import com.github.nhirakawa.crdt.service.CrdtConfiguration;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class IncrementOnlyCounter extends ConvergentCrdt<IncrementOnlyCounter, Integer> {

  @Inject
  public IncrementOnlyCounter(CrdtConfiguration configuration,
                              ObjectMapper objectMapper) {
    super(0, configuration.getNodeId(), objectMapper.getTypeFactory().constructType(new TypeReference<Integer>() {})
    );
  }

  @Override
  public String getNamespace() {
    return "incrementOnlyCounter";
  }

  @Override
  protected Integer update(Integer existingValue, Integer newValue) {
    return existingValue + newValue;
  }

  @Override
  protected Integer merge(Integer existingValue, Integer otherValue) {
    return Math.max(existingValue, otherValue);
  }

  @Override
  protected Integer reduce(Integer v1, Integer v2) {
    return v1 + v2;
  }
}
