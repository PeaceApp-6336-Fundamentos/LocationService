package com.upc.pre.peaceapp.location.application.internal.queryservices;

import com.upc.pre.peaceapp.location.domain.model.aggregates.Location;

import java.util.List;
import java.util.Optional;

public interface LocationQueryService {
    Optional<Location> getById(Long id);
    List<Location> getAll();
    List<Location> getByDistrict(String district);
}
