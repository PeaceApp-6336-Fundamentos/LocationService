package com.upc.pre.peaceapp.location.application.internal.queryservices;

import com.upc.pre.peaceapp.location.domain.model.aggregates.Location;
import com.upc.pre.peaceapp.location.domain.model.repositories.LocationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LocationQueryServiceImpl implements LocationQueryService {

    private final LocationRepository locations;

    public LocationQueryServiceImpl(LocationRepository locations) {
        this.locations = locations;
    }

    @Override public Optional<Location> getById(Long id) { return locations.findById(id); }
    @Override public List<Location> getAll() { return locations.findAll(); }
    @Override public List<Location> getByDistrict(String district) { return locations.findByDistrict(district); }
}
