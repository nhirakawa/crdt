package com.github.nhirakawa.crdt.models;

import com.fasterxml.jackson.databind.JavaType;
import com.google.common.base.MoreObjects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.Map;
import java.util.Map.Entry;

public abstract class ConvergentCrdt<T extends ConvergentCrdt, V> {
  private final V identity;
  private final String nodeId;
  private final JavaType valueType;

  private final ConcurrentMap<String, V> values;

  public ConvergentCrdt(V identity, String nodeId, JavaType valueType) {
    this.identity = identity;
    this.nodeId = nodeId;
    this.valueType = valueType;
    this.values = new ConcurrentHashMap<>();
  }

  public final V getValue() {
    return values.values().stream().reduce(identity, this::reduce);
  }

  public final void update(V value) {
    values.merge(nodeId, value, this::update);
  }

  public final void merge(Map<String, V> otherValues) {
    for (Entry<String, V> entry : otherValues.entrySet()) {
      values.merge(entry.getKey(), entry.getValue(), this::merge);
    }
  }

  public final Map<String, V> getValues() {
    return values;
  }

  public JavaType getValueType() {
    return valueType;
  }

  public abstract String getNamespace();

  protected abstract V update(V existingValue, V newValue);

  protected abstract V merge(V existingValue, V otherValue);

  protected abstract V reduce(V v1, V v2);

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("values", values).toString();
  }
}
