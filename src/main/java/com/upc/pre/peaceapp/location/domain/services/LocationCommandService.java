package com.upc.pre.peaceapp.location.domain.services;

import com.upc.pre.peaceapp.location.domain.model.aggregates.Location;
import com.upc.pre.peaceapp.location.domain.model.commands.CreateLocationCommand;
import com.upc.pre.peaceapp.location.domain.model.commands.DeleteAllLocationsByIdReportCommand;

import java.util.Optional;

public interface LocationCommandService {
    Optional<Location> handle(CreateLocationCommand command);
    void handle(DeleteAllLocationsByIdReportCommand command);
}
