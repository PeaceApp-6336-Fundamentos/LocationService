package com.upc.pre.peaceapp.location.domain.model.commands;
public record CreateLocationCommand(Double latitude, Double longitude, String address, String district) {}
