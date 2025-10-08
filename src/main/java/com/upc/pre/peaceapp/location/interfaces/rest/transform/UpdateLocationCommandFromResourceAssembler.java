package com.upc.pre.peaceapp.location.interfaces.rest.transform;

import com.upc.pre.peaceapp.location.domain.model.commands.UpdateLocationCommand;
import com.upc.pre.peaceapp.location.interfaces.rest.resources.UpdateLocationResource;

public class UpdateLocationCommandFromResourceAssembler {
    public static UpdateLocationCommand toCommand(Long id, UpdateLocationResource r) {
        return new UpdateLocationCommand(id, r.latitude(), r.longitude(), r.address(), r.district());
    }
}
