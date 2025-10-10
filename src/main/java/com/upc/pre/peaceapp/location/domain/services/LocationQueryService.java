package com.upc.pre.peaceapp.location.domain.services;

import com.upc.pre.peaceapp.location.domain.model.aggregates.Location;
import com.upc.pre.peaceapp.location.domain.model.queries.GetAllLocationsQuery;
import com.upc.pre.peaceapp.location.domain.model.queries.GetDangerousLocationsQuery;

import java.util.List;

public interface LocationQueryService {
    List<Location> handle(GetAllLocationsQuery query);
    List<Location> handle(GetDangerousLocationsQuery query);
}
