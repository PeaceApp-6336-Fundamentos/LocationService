package com.upc.pre.peaceapp.location.domain.model.commands;

public record DeleteAllLocationsByIdReportCommand(Long idReport) {
    public DeleteAllLocationsByIdReportCommand {
        if (idReport == null || idReport <= 0)
            throw new IllegalArgumentException("idReport must be greater than 0");
    }
}
