package com.github.nhirakawa.crdt.models;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BinaryOperator;

import com.fasterxml.jackson.databind.JavaType;
import com.google.common.base.MoreObjects;

public abstract class ConvergentCrdt<T extends ConvergentCrdt, V> {

  private final V identity;
  private final BinaryOperator<V> reducer;
  private final BinaryOperator<V> accumulator;
  private final BinaryOperator<V> merge;
  private final String nodeId;
  private final JavaType valueType;

  private final Map<String, AtomicReference<V>> values;

  public ConvergentCrdt(V identity,
                        BinaryOperator<V> reducer,
                        BinaryOperator<V> accumulator,
                        BinaryOperator<V> merge,
                        String nodeId,
                        JavaType valueType) {
    this.identity = identity;
    this.reducer = reducer;
    this.accumulator = accumulator;
    this.merge = merge;
    this.nodeId = nodeId;
    this.valueType = valueType;
    this.values = new HashMap<>();
  }

  public final V getValue() {
    return values.values().stream()
        .map(AtomicReference::get)
        .reduce(identity, reducer);
  }

  public final void update(V value) {
    values.computeIfAbsent(nodeId, key -> new AtomicReference<>(identity));
    values.get(nodeId).getAndAccumulate(value, accumulator);
  }

  public final void merge(Map<String, V> otherValues) {
    for (Entry<String, V> entry : otherValues.entrySet()) {
      values.putIfAbsent(entry.getKey(), new AtomicReference<>(identity));
      values.get(entry.getKey()).getAndAccumulate(entry.getValue(), merge);
    }
  }

  public final Map<String, V> getValues() {
    Map<String, V> result = new HashMap<>();

    for (Entry<String, AtomicReference<V>> entry : values.entrySet()) {
      result.put(entry.getKey(), entry.getValue().get());
    }

    return result;
  }

  public JavaType getValueType() {
    return valueType;
  }

  public abstract String getNamespace();

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("values", values)
        .toString();
  }
}
