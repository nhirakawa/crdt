package com.github.nhirakawa.crdt.service;

import com.github.nhirakawa.crdt.models.ConvergentCrdt;
import com.github.nhirakawa.crdt.models.CrdtModel;
import com.github.nhirakawa.crdt.models.ImmutableCrdtUpdateRequest;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;

@Produces(MediaType.APPLICATION_JSON)
public abstract class ConvergentCrdtResource<T extends ConvergentCrdt<T, V>, V> {
  private final T crdt;
  private final StatePoller<T, V> statePoller;

  public ConvergentCrdtResource(T crdt, StatePoller<T, V> statePoller) {
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
  @Path("/update")
  public final void update(ImmutableCrdtUpdateRequest<V> updateRequest) {
    crdt.update(updateRequest.getValue());
  }

  @POST
  @Path("/merge")
  public final void merge() {
    statePoller.poll();
  }
}
