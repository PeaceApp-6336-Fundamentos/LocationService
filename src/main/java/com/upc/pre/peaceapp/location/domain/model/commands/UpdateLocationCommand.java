package com.upc.pre.peaceapp.location.domain.model.commands;
public record UpdateLocationCommand(Long id, Double latitude, Double longitude, String address, String district) {}
