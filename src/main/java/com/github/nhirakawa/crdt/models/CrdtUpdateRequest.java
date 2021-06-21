package com.github.nhirakawa.crdt.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.immutables.value.Value.Immutable;

@Immutable
@JsonDeserialize
public abstract class CrdtUpdateRequest<V> {

  public abstract V getValue();
}
