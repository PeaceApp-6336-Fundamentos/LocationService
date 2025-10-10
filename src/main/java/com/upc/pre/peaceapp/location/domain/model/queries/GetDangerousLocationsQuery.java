package com.upc.pre.peaceapp.location.domain.model.queries;

public record GetDangerousLocationsQuery(int minReports) {
    public GetDangerousLocationsQuery {
        if (minReports < 1)
            throw new IllegalArgumentException("minReports must be >= 1");
    }
}
