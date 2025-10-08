package com.upc.pre.peaceapp.location.infrastructure.persistence.jpa;

import jakarta.persistence.*;

@Entity
@Table(name = "locations")
public class LocationJpaEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false) private Double latitude;
    @Column(nullable = false) private Double longitude;
    @Column(length = 120) private String address;
    @Column(length = 60) private String district;

    public Long getId() { return id; }
    public Double getLatitude() { return latitude; }
    public Double getLongitude() { return longitude; }
    public String getAddress() { return address; }
    public String getDistrict() { return district; }

    public void setId(Long id) { this.id = id; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
    public void setAddress(String address) { this.address = address; }
    public void setDistrict(String district) { this.district = district; }
}
