package com.github.nhirakawa.crdt.models;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicReference;

import org.immutables.value.Value.Auxiliary;
import org.immutables.value.Value.Immutable;
import org.immutables.value.Value.Style;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@Immutable
@Style(jdkOnly = true)
@JsonDeserialize
public abstract class CrdtModel<V> {

  public abstract Map<String, V> getValues();

  public abstract V getValue();

  @Auxiliary
  public static <V> CrdtModel<V> from(ConvergentCrdt<?, V> crdt) {
    ImmutableCrdtModel.Builder<V> builder = ImmutableCrdtModel.builder();

    for(Entry<String, AtomicReference<V>> entry : crdt.getValues().entrySet()) {
      builder.putValues(entry.getKey(), entry.getValue().get());
    }

    builder.value(crdt.getValue());

    return builder.build();
  }

}
