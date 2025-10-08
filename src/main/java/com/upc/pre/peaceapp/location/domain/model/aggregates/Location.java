package com.upc.pre.peaceapp.location.domain.model.aggregates;

public class Location {
    private Long id;
    private Double latitude;
    private Double longitude;
    private String address;
    private String district;

    private Location(Double latitude, Double longitude, String address, String district) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.district = district;
    }

    public static Location create(Double latitude, Double longitude, String address, String district) {
        return new Location(latitude, longitude, address, district);
    }

    public void update(Double latitude, Double longitude, String address, String district) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.district = district;
    }

    // Getters estilo DDD
    public Long id() { return id; }
    public Double latitude() { return latitude; }
    public Double longitude() { return longitude; }
    public String address() { return address; }
    public String district() { return district; }

    // Setter para mapeo infra
    public void setId(Long id) { this.id = id; }
}
