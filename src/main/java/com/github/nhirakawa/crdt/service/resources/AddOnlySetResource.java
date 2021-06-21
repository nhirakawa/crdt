package com.github.nhirakawa.crdt.service.resources;

import com.github.nhirakawa.crdt.impl.AddOnlySet;
import com.github.nhirakawa.crdt.service.ConvergentCrdtResource;
import com.github.nhirakawa.crdt.service.StatePoller;
import com.google.inject.Inject;
import java.util.Set;
import javax.ws.rs.Path;

@Path("/crdt/addOnlySet")
public class AddOnlySetResource
  extends ConvergentCrdtResource<AddOnlySet, Set<Integer>> {

  @Inject
  public AddOnlySetResource(
    AddOnlySet crdt,
    StatePoller<AddOnlySet, Set<Integer>> statePoller
  ) {
    super(crdt, statePoller);
  }
}
