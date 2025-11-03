package com.upc.pre.peaceapp.location.interfaces.rest;

import com.upc.pre.peaceapp.location.domain.model.commands.CreateLocationCommand;
import com.upc.pre.peaceapp.location.domain.model.commands.DeleteAllLocationsByIdReportCommand;
import com.upc.pre.peaceapp.location.domain.model.queries.GetAllLocationsQuery;
import com.upc.pre.peaceapp.location.domain.model.queries.GetDangerousLocationsQuery;
import com.upc.pre.peaceapp.location.domain.services.LocationCommandService;
import com.upc.pre.peaceapp.location.domain.services.LocationQueryService;
import com.upc.pre.peaceapp.location.interfaces.rest.resources.CreateLocationResource;
import com.upc.pre.peaceapp.location.interfaces.rest.transform.CreateLocationCommandFromResourceAssembler;
import com.upc.pre.peaceapp.location.interfaces.rest.transform.LocationResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/locations", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Locations", description = "Operations related to Location Management")
@Slf4j
public class LocationController {

    private final LocationCommandService commands;
    private final LocationQueryService queries;

    public LocationController(LocationCommandService commands, LocationQueryService queries) {
        this.commands = commands;
        this.queries = queries;
    }

    // ----------------------------------------------------------------------
    // CREATE LOCATION
    // ----------------------------------------------------------------------
    @Operation(summary = "Create a new location",
            description = "Registers a new location associated with a specific report.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Location created successfully",
                    content = @Content(mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = CreateLocationResource.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> create(@RequestBody CreateLocationResource body) {
        log.info("Creating new location for report ID: {}", body.idReport());

        var location = commands.handle(CreateLocationCommandFromResourceAssembler.toCommand(body));

        return location
                .<ResponseEntity<?>>map(loc -> ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body(LocationResourceFromEntityAssembler.toResource(loc)))
                .orElseGet(() -> ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("{\"message\": \"Location could not be created\"}"));
    }

    // ----------------------------------------------------------------------
    // GET ALL LOCATIONS
    // ----------------------------------------------------------------------
    @Operation(summary = "Get all locations", description = "Retrieve all locations stored in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Locations retrieved successfully"),
            @ApiResponse(responseCode = "204", description = "No locations found")
    })
    @GetMapping
    public ResponseEntity<?> getAll() {
        var list = queries.handle(new GetAllLocationsQuery());
        if (list.isEmpty()) return ResponseEntity.noContent().build();

        var resources = list.stream()
                .map(LocationResourceFromEntityAssembler::toResource)
                .collect(Collectors.toList());
        return ResponseEntity.ok(resources);
    }

    // ----------------------------------------------------------------------
    // GET DANGEROUS LOCATIONS
    // ----------------------------------------------------------------------
    @Operation(summary = "Get dangerous locations",
            description = "Retrieve the most dangerous locations based on the number of associated reports.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dangerous locations retrieved successfully"),
            @ApiResponse(responseCode = "204", description = "No dangerous locations found")
    })
    @GetMapping("/dangerous")
    public ResponseEntity<?> getDangerousLocations(@RequestParam(defaultValue = "5") int quantityReports) {
        log.info("Fetching top {} dangerous locations", quantityReports);

        var list = queries.handle(new GetDangerousLocationsQuery(quantityReports));
        if (list.isEmpty()) return ResponseEntity.noContent().build();

        var resources = list.stream()
                .map(LocationResourceFromEntityAssembler::toResource)
                .collect(Collectors.toList());
        return ResponseEntity.ok(resources);
    }

    // ----------------------------------------------------------------------
    // DELETE LOCATIONS BY REPORT ID
    // ----------------------------------------------------------------------
    @Operation(summary = "Delete all locations by report ID",
            description = "Deletes all locations associated with a specific report.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Locations deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid report ID"),
            @ApiResponse(responseCode = "500", description = "Error deleting locations")
    })
    @DeleteMapping("/report/{reportId}")
    public ResponseEntity<?> deleteAllByReportId(@PathVariable Long reportId) {
        log.warn("Deleting all locations for report ID: {}", reportId);
        try {
            commands.handle(new DeleteAllLocationsByIdReportCommand(reportId));
            return ResponseEntity.ok("All locations for report ID " + reportId + " deleted successfully");
        } catch (IllegalArgumentException e) {
            log.error("Invalid report ID: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            log.error("Error deleting locations for report ID {}: {}", reportId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
