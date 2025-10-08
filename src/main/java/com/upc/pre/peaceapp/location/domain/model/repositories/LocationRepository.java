package com.upc.pre.peaceapp.location.domain.model.repositories;

import com.upc.pre.peaceapp.location.domain.model.aggregates.Location;

import java.util.List;
import java.util.Optional;

public interface LocationRepository {
    Location save(Location location);
    Optional<Location> findById(Long id);
    List<Location> findAll();
    List<Location> findByDistrict(String district);
    void deleteById(Long id);
}
