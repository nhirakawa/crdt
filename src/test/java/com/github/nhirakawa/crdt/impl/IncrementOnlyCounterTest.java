package com.github.nhirakawa.crdt.impl;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.nhirakawa.crdt.service.CrdtConfiguration;
import java.util.HashMap;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;

public class IncrementOnlyCounterTest {
  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  private IncrementOnlyCounter counter1;
  private IncrementOnlyCounter counter2;

  @Before
  public void setup() {
    counter1 =
      new IncrementOnlyCounter(buildCrdtConfiguration("node1"), OBJECT_MAPPER);
    counter2 =
      new IncrementOnlyCounter(buildCrdtConfiguration("node2"), OBJECT_MAPPER);
  }

  @Test
  public void testIncrement() {
    counter1.update(1);
    assertThat(counter1.getValue()).isEqualTo(1);

    counter1.update(1);
    assertThat(counter1.getValue()).isEqualTo(2);
  }

  @Test
  public void testMerge() {
    counter1.update(1);
    counter2.update(2);

    counter1.merge(counter2.getValues());

    assertThat(counter1.getValue()).isEqualTo(3);
    Map<String, Integer> expectedValues = new HashMap<>();
    expectedValues.put("node1", 1);
    expectedValues.put("node2", 2);
    assertThat(counter1.getValues()).containsAllEntriesOf(expectedValues);

    counter2.merge(counter1.getValues());

    assertThat(counter2.getValue()).isEqualTo(3);
    assertThat(counter2.getValues()).containsAllEntriesOf(expectedValues);
  }

  private static CrdtConfiguration buildCrdtConfiguration(String nodeId) {
    CrdtConfiguration config = new CrdtConfiguration();
    config.setNodeId(nodeId);
    return config;
  }
}
