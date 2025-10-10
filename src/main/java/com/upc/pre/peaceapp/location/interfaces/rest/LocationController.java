package com.upc.pre.peaceapp.location.interfaces.rest;

import com.upc.pre.peaceapp.location.domain.model.queries.GetAllLocationsQuery;
import com.upc.pre.peaceapp.location.domain.model.queries.GetDangerousLocationsQuery;
import com.upc.pre.peaceapp.location.domain.services.LocationCommandService;
import com.upc.pre.peaceapp.location.domain.services.LocationQueryService;
import com.upc.pre.peaceapp.location.interfaces.rest.resources.CreateLocationResource;
import com.upc.pre.peaceapp.location.interfaces.rest.transform.CreateLocationCommandFromResourceAssembler;
import com.upc.pre.peaceapp.location.interfaces.rest.transform.LocationResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/locations")
@Tag(name = "Locations", description = "Operations related to Location Management")
public class LocationController {

    private final LocationCommandService commands;
    private final LocationQueryService queries;

    public LocationController(LocationCommandService commands, LocationQueryService queries) {
        this.commands = commands;
        this.queries = queries;
    }

    // Crear una ubicación asociada a un reporte
    @PostMapping
    public ResponseEntity<?> create(@RequestBody CreateLocationResource body) {
        var location = commands.handle(CreateLocationCommandFromResourceAssembler.toCommand(body));

        return location
                .<ResponseEntity<?>>map(loc -> ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body(LocationResourceFromEntityAssembler.toResource(loc)))
                .orElseGet(() -> ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("{\"message\": \"Location could not be created\"}"));
    }

    // Obtener todas las ubicaciones
    @GetMapping
    public ResponseEntity<?> getAll() {
        var list = queries.handle(new GetAllLocationsQuery());
        var resources = list.stream()
                .map(LocationResourceFromEntityAssembler::toResource)
                .collect(Collectors.toList());
        return ResponseEntity.ok(resources);
    }

    // Obtener ubicaciones peligrosas
    @GetMapping("/dangerous")
    public ResponseEntity<?> getDangerousLocations(@RequestParam(defaultValue = "5") int quantityReports) {
        var list = queries.handle(new GetDangerousLocationsQuery(quantityReports));
        var resources = list.stream()
                .map(LocationResourceFromEntityAssembler::toResource)
                .collect(Collectors.toList());
        return ResponseEntity.ok(resources);
    }
}
