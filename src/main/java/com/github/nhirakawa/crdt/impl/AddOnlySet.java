package com.github.nhirakawa.crdt.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.nhirakawa.crdt.models.ConvergentCrdt;
import com.github.nhirakawa.crdt.service.CrdtConfiguration;
import com.google.common.collect.Sets;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.util.HashSet;
import java.util.Set;

@Singleton
public class AddOnlySet extends ConvergentCrdt<AddOnlySet, Set<Integer>> {

  @Inject
  public AddOnlySet(
    CrdtConfiguration configuration,
    ObjectMapper objectMapper
  ) {
    super(
      new HashSet<>(),
      configuration.getNodeId(),
      objectMapper
        .getTypeFactory()
        .constructCollectionType(Set.class, Integer.class)
    );
  }

  @Override
  public String getNamespace() {
    return "addOnlySet";
  }

  @Override
  protected Set<Integer> update(
    Set<Integer> existingValue,
    Set<Integer> newValue
  ) {
    return Sets.union(existingValue, newValue);
  }

  @Override
  protected Set<Integer> merge(
    Set<Integer> existingValue,
    Set<Integer> otherValue
  ) {
    return Sets.union(existingValue, otherValue);
  }

  @Override
  protected Set<Integer> reduce(Set<Integer> v1, Set<Integer> v2) {
    return Sets.union(v1, v2);
  }
}
