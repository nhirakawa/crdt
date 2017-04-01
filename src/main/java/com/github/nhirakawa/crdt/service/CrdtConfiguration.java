package com.github.nhirakawa.crdt.service;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.dropwizard.Configuration;

public class CrdtConfiguration extends Configuration {

  private Set<String> clusterHosts;
  private String nodeId;
  private int pollInterval;

  @JsonProperty
  public Set<String> getClusterHosts() {
    return clusterHosts;
  }

  @JsonProperty
  public void setClusterHosts(Set<String> clusterHosts) {
    this.clusterHosts = clusterHosts;
  }

  @JsonProperty
  public String getNodeId() {
    return nodeId;
  }

  @JsonProperty
  public void setNodeId(String nodeId) {
    this.nodeId = nodeId;
  }

  public int getPollInterval() {
    return pollInterval;
  }

  public void setPollInterval(int pollInterval) {
    this.pollInterval = pollInterval;
  }
}
