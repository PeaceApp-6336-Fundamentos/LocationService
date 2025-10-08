package com.upc.pre.peaceapp.location.infrastructure.persistence.jpa;

import com.upc.pre.peaceapp.location.domain.model.aggregates.Location;
import com.upc.pre.peaceapp.location.domain.model.repositories.LocationRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class LocationRepositoryJpaAdapter implements LocationRepository {

    private final SpringDataLocationRepository jpa;

    public LocationRepositoryJpaAdapter(SpringDataLocationRepository jpa) {
        this.jpa = jpa;
    }

    // mapping helpers
    private static Location toDomain(LocationJpaEntity e) {
        var l = Location.create(e.getLatitude(), e.getLongitude(), e.getAddress(), e.getDistrict());
        l.setId(e.getId());
        return l;
    }

    private static LocationJpaEntity toEntity(Location l) {
        var e = new LocationJpaEntity();
        e.setId(l.id());
        e.setLatitude(l.latitude());
        e.setLongitude(l.longitude());
        e.setAddress(l.address());
        e.setDistrict(l.district());
        return e;
    }

    @Override public Location save(Location location) { return toDomain(jpa.save(toEntity(location))); }
    @Override public Optional<Location> findById(Long id) { return jpa.findById(id).map(LocationRepositoryJpaAdapter::toDomain); }
    @Override public List<Location> findAll() { return jpa.findAll().stream().map(LocationRepositoryJpaAdapter::toDomain).toList(); }
    @Override public List<Location> findByDistrict(String district) { return jpa.findByDistrictIgnoreCase(district).stream().map(LocationRepositoryJpaAdapter::toDomain).toList(); }
    @Override public void deleteById(Long id) { jpa.deleteById(id); }
}
