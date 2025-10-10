package com.upc.pre.peaceapp.location.interfaces.rest.transform;

import com.upc.pre.peaceapp.location.domain.model.aggregates.Location;
import com.upc.pre.peaceapp.location.interfaces.rest.resources.LocationResource;

public class LocationResourceFromEntityAssembler {
    public static LocationResource toResource(Location l) {
        return new LocationResource(
                l.getId(),
                l.getLatitude(),
                l.getLongitude(),
                l.getIdReport()
        );
    }
}
