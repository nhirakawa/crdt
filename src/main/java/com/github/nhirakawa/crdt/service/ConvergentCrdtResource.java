package com.github.nhirakawa.crdt.service;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.github.nhirakawa.crdt.models.ConvergentCrdt;
import com.github.nhirakawa.crdt.models.CrdtModel;

@Produces(MediaType.APPLICATION_JSON)
public abstract class ConvergentCrdtResource<T extends ConvergentCrdt<T, V>, V> {

  private final T crdt;
  private final StatePoller<T, V> statePoller;

  public ConvergentCrdtResource(T crdt,
                                StatePoller<T, V> statePoller) {
    this.crdt = crdt;
    this.statePoller = statePoller;
  }

  @GET
  public final V query() {
    return crdt.getValue();
  }

  @GET
  @Path("/full")
  public final CrdtModel<V> getFull() {
    return CrdtModel.from(crdt);
  }

  @POST
  @Path("/update/{value}")
  public final void update(@PathParam("value") V value) {
    crdt.update(value);
  }

  @POST
  @Path("/merge")
  public final void merge() {
    statePoller.poll();
  }
}
