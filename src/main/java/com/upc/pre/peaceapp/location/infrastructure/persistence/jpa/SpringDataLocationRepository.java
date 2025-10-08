package com.upc.pre.peaceapp.location.infrastructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpringDataLocationRepository extends JpaRepository<LocationJpaEntity, Long> {
    List<LocationJpaEntity> findByDistrictIgnoreCase(String district);
}
