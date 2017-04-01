package com.github.nhirakawa.crdt.models;

import org.immutables.value.Value.Immutable;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@Immutable
@JsonDeserialize
public abstract class CrdtUpdateRequest<V> {

  public abstract V getValue();
}
