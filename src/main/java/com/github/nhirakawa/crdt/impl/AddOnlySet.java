package com.github.nhirakawa.crdt.impl;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.nhirakawa.crdt.models.ConvergentCrdt;
import com.github.nhirakawa.crdt.service.CrdtConfiguration;
import com.google.common.collect.Sets;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class AddOnlySet extends ConvergentCrdt<AddOnlySet, Set<Integer>> {

  @Inject
  public AddOnlySet(CrdtConfiguration configuration, ObjectMapper objectMapper) {
    super(
        new HashSet<>(),
        Sets::union,
        Sets::union,
        Sets::union,
        configuration.getNodeId(),
        objectMapper.getTypeFactory().constructCollectionType(Set.class, Integer.class));
  }

  @Override
  public String getNamespace() {
    return "addOnlySet";
  }
}
