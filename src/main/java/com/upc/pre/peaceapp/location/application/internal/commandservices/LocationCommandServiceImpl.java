package com.upc.pre.peaceapp.location.application.internal.commandservices;

import com.upc.pre.peaceapp.location.domain.model.aggregates.Location;
import com.upc.pre.peaceapp.location.domain.model.commands.CreateLocationCommand;
import com.upc.pre.peaceapp.location.domain.model.commands.DeleteAllLocationsByIdReportCommand;
import com.upc.pre.peaceapp.location.domain.services.LocationCommandService;
import com.upc.pre.peaceapp.location.infrastructure.persistence.jpa.LocationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
@Transactional
public class LocationCommandServiceImpl implements LocationCommandService {

    private final LocationRepository locationRepository;

    public LocationCommandServiceImpl(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    @Override
    public Optional<Location> handle(CreateLocationCommand command) {
        var location = new Location(
                command.latitude(),
                command.longitude(),
                command.idReport()
        );

        var savedLocation = locationRepository.save(location);
        return Optional.of(savedLocation);
    }
    @Override
    public void handle(DeleteAllLocationsByIdReportCommand command) {
        locationRepository.deleteAllByIdReport(command.idReport());
    }
}
