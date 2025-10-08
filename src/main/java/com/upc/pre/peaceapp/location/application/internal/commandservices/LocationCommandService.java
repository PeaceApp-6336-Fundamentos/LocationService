package com.upc.pre.peaceapp.location.application.internal.commandservices;

import com.upc.pre.peaceapp.location.domain.model.commands.CreateLocationCommand;
import com.upc.pre.peaceapp.location.domain.model.commands.UpdateLocationCommand;
import com.upc.pre.peaceapp.location.domain.model.commands.DeleteLocationCommand;

public interface LocationCommandService {
    Long handle(CreateLocationCommand command);
    void handle(UpdateLocationCommand command);
    void handle(DeleteLocationCommand command);
}
