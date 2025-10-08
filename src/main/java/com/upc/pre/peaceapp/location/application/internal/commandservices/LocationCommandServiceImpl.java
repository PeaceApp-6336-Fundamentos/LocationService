package com.upc.pre.peaceapp.location.application.internal.commandservices;

import com.upc.pre.peaceapp.location.domain.model.aggregates.Location;
import com.upc.pre.peaceapp.location.domain.model.commands.CreateLocationCommand;
import com.upc.pre.peaceapp.location.domain.model.commands.UpdateLocationCommand;
import com.upc.pre.peaceapp.location.domain.model.commands.DeleteLocationCommand;
import com.upc.pre.peaceapp.location.domain.model.repositories.LocationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LocationCommandServiceImpl implements LocationCommandService {

    private final LocationRepository locations;

    public LocationCommandServiceImpl(LocationRepository locations) {
        this.locations = locations;
    }

    @Override
    public Long handle(CreateLocationCommand cmd) {
        var loc = Location.create(cmd.latitude(), cmd.longitude(), cmd.address(), cmd.district());
        return locations.save(loc).id();
    }

    @Override
    public void handle(UpdateLocationCommand cmd) {
        var loc = locations.findById(cmd.id())
                .orElseThrow(() -> new IllegalArgumentException("Location not found"));
        loc.update(cmd.latitude(), cmd.longitude(), cmd.address(), cmd.district());
        locations.save(loc);
    }

    @Override
    public void handle(DeleteLocationCommand cmd) {
        locations.deleteById(cmd.id());
    }
}
