package com.upc.pre.peaceapp.location.application.internal.queryservices;

import com.upc.pre.peaceapp.location.domain.model.aggregates.Location;
import com.upc.pre.peaceapp.location.domain.model.queries.GetAllLocationsQuery;
import com.upc.pre.peaceapp.location.domain.model.queries.GetDangerousLocationsQuery;
import com.upc.pre.peaceapp.location.domain.services.LocationQueryService;
import com.upc.pre.peaceapp.location.infrastructure.persistence.jpa.LocationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationQueryServiceImpl implements LocationQueryService {

    private final LocationRepository locationRepository;

    public LocationQueryServiceImpl(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    @Override
    public List<Location> handle(GetAllLocationsQuery query) {
        return locationRepository.findAll();
    }

    @Override
    public List<Location> handle(GetDangerousLocationsQuery query) {
        return locationRepository.findDangerousLocations()
                .stream()
                .map(result -> {
                    String latitude = (String) result[0];
                    String longitude = (String) result[1];
                    Long idReport = 0L;
                    return new Location(latitude, longitude, idReport);
                })
                .toList();
    }
}
