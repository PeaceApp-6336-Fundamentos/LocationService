package com.upc.pre.peaceapp.location.interfaces.rest.transform;

import com.upc.pre.peaceapp.location.domain.model.commands.CreateLocationCommand;
import com.upc.pre.peaceapp.location.interfaces.rest.resources.CreateLocationResource;

public class CreateLocationCommandFromResourceAssembler {
    public static CreateLocationCommand toCommand(CreateLocationResource r) {
        return new CreateLocationCommand(r.latitude(), r.longitude(), r.idReport());
    }
}
