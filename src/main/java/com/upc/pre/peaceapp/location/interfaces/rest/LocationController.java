package com.upc.pre.peaceapp.location.interfaces.rest;

import com.upc.pre.peaceapp.location.application.internal.commandservices.LocationCommandService;
import com.upc.pre.peaceapp.location.application.internal.queryservices.LocationQueryService;
import com.upc.pre.peaceapp.location.domain.model.commands.DeleteLocationCommand;
import com.upc.pre.peaceapp.location.interfaces.rest.resources.CreateLocationResource;
import com.upc.pre.peaceapp.location.interfaces.rest.resources.UpdateLocationResource;
import com.upc.pre.peaceapp.location.interfaces.rest.transform.CreateLocationCommandFromResourceAssembler;
import com.upc.pre.peaceapp.location.interfaces.rest.transform.LocationResourceFromEntityAssembler;
import com.upc.pre.peaceapp.location.interfaces.rest.transform.UpdateLocationCommandFromResourceAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/locations")
public class LocationController {

    private final LocationCommandService commands;
    private final LocationQueryService queries;

    public LocationController(LocationCommandService commands, LocationQueryService queries) {
        this.commands = commands;
        this.queries = queries;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CreateLocationResource body) {
        var id = commands.handle(CreateLocationCommandFromResourceAssembler.toCommand(body));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(queries.getById(id).map(LocationResourceFromEntityAssembler::toResource));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody UpdateLocationResource body) {
        commands.handle(UpdateLocationCommandFromResourceAssembler.toCommand(id, body));
        return ResponseEntity.ok(queries.getById(id).map(LocationResourceFromEntityAssembler::toResource));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return queries.getById(id)
                .<ResponseEntity<?>>map(l -> ResponseEntity.ok(LocationResourceFromEntityAssembler.toResource(l)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("{\"message\":\"Location not found\"}"));
    }

    @GetMapping
    public ResponseEntity<?> getAll(@RequestParam(value = "district", required = false) String district) {
        var list = (district == null || district.isBlank())
                ? queries.getAll()
                : queries.getByDistrict(district);
        return ResponseEntity.ok(list.stream().map(LocationResourceFromEntityAssembler::toResource).toList());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        commands.handle(new DeleteLocationCommand(id));
        return ResponseEntity.ok().build();
    }
}
