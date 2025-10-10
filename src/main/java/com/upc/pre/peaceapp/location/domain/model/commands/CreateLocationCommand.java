package com.upc.pre.peaceapp.location.domain.model.commands;

public record CreateLocationCommand(
        String latitude,
        String longitude,
        Long idReport
) {
    public CreateLocationCommand {
        if (latitude == null || latitude.isBlank())
            throw new IllegalArgumentException("latitude cannot be null or empty");
        if (longitude == null || longitude.isBlank())
            throw new IllegalArgumentException("longitude cannot be null or empty");
        if (idReport == null || idReport <= 0)
            throw new IllegalArgumentException("idReport must be greater than 0");
    }
}
