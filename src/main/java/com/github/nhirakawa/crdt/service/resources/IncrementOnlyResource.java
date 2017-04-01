package com.github.nhirakawa.crdt.service.resources;

import javax.ws.rs.Path;

import com.github.nhirakawa.crdt.impl.IncrementOnlyCounter;
import com.github.nhirakawa.crdt.service.ConvergentCrdtResource;
import com.github.nhirakawa.crdt.service.StatePoller;
import com.google.inject.Inject;

@Path("/crdt/incrementOnlyCounter")
public class IncrementOnlyResource extends ConvergentCrdtResource<IncrementOnlyCounter, Integer> {

  @Inject
  public IncrementOnlyResource(IncrementOnlyCounter crdt,
                               StatePoller<IncrementOnlyCounter, Integer> statePoller) {
    super(crdt, statePoller);
  }
}
