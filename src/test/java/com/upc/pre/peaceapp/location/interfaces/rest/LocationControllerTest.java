package com.upc.pre.peaceapp.location.interfaces.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.upc.pre.peaceapp.location.domain.model.aggregates.Location;
import com.upc.pre.peaceapp.location.domain.model.commands.CreateLocationCommand;
import com.upc.pre.peaceapp.location.domain.model.commands.DeleteAllLocationsByIdReportCommand;
import com.upc.pre.peaceapp.location.domain.model.queries.GetAllLocationsQuery;
import com.upc.pre.peaceapp.location.domain.model.queries.GetDangerousLocationsQuery;
import com.upc.pre.peaceapp.location.domain.services.LocationCommandService;
import com.upc.pre.peaceapp.location.domain.services.LocationQueryService;
import com.upc.pre.peaceapp.location.interfaces.rest.resources.CreateLocationResource;
import com.upc.pre.peaceapp.location.interfaces.rest.resources.LocationResource;
import com.upc.pre.peaceapp.location.interfaces.rest.transform.CreateLocationCommandFromResourceAssembler;
import com.upc.pre.peaceapp.location.interfaces.rest.transform.LocationResourceFromEntityAssembler;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LocationController.class)
// @AutoConfigureMockMvc(addFilters = false) // descomenta si seguridad bloquea
class LocationControllerTest {

    private static final String BASE = "/api/v1/locations";

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @MockBean private LocationCommandService commands;
    @MockBean private LocationQueryService queries;

    // --------------------- POST /locations ---------------------
    @Test
    @DisplayName("POST /locations -> 201 Created con LocationResource")
    void create_created() throws Exception {
        var req = new CreateLocationResource("-12.046374", "-77.042793", 45L);

        var cmd = new CreateLocationCommand(req.latitude(), req.longitude(), req.idReport());

        // entidad devuelta por el servicio
        Location entity = Mockito.mock(Location.class);

        // resource de salida esperado
        var res = new LocationResource(7L, "-12.046374", "-77.042793", 45L);

        try (MockedStatic<CreateLocationCommandFromResourceAssembler> mCreate =
                     Mockito.mockStatic(CreateLocationCommandFromResourceAssembler.class);
             MockedStatic<LocationResourceFromEntityAssembler> mRes =
                     Mockito.mockStatic(LocationResourceFromEntityAssembler.class)) {

            mCreate.when(() -> CreateLocationCommandFromResourceAssembler.toCommand(any(CreateLocationResource.class)))
                    .thenReturn(cmd);

            when(commands.handle(any(CreateLocationCommand.class)))
                    .thenReturn(Optional.of(entity));

            mRes.when(() -> LocationResourceFromEntityAssembler.toResource(any(Location.class)))
                    .thenReturn(res);

            mockMvc.perform(post(BASE)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(req)))
                    .andExpect(status().isCreated())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id", is(7)))
                    .andExpect(jsonPath("$.idReport", is(45)));
        }
    }

    @Test
    @DisplayName("POST /locations -> 400 Bad Request cuando service no crea")
    void create_badRequest() throws Exception {
        var req = new CreateLocationResource("x", "y", 1L);

        try (MockedStatic<CreateLocationCommandFromResourceAssembler> mCreate =
                     Mockito.mockStatic(CreateLocationCommandFromResourceAssembler.class)) {

            mCreate.when(() -> CreateLocationCommandFromResourceAssembler.toCommand(any(CreateLocationResource.class)))
                    .thenReturn(Mockito.mock(CreateLocationCommand.class));

            when(commands.handle(any(CreateLocationCommand.class)))
                    .thenReturn(Optional.empty());

            mockMvc.perform(post(BASE)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(req)))
                    .andExpect(status().isBadRequest());
        }
    }

    // --------------------- GET /locations ---------------------
    @Test
    @DisplayName("GET /locations -> 200 OK con lista")
    void getAll_ok() throws Exception {
        Location e1 = Mockito.mock(Location.class);
        when(queries.handle(any(GetAllLocationsQuery.class)))
                .thenReturn(List.of(e1));

        var r1 = new LocationResource(1L, "-12", "-77", 10L);

        try (MockedStatic<LocationResourceFromEntityAssembler> mRes =
                     Mockito.mockStatic(LocationResourceFromEntityAssembler.class)) {
            mRes.when(() -> LocationResourceFromEntityAssembler.toResource(any(Location.class)))
                    .thenReturn(r1);

            mockMvc.perform(get(BASE))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$[0].idReport", is(10)));
        }
    }

    @Test
    @DisplayName("GET /locations -> 204 No Content cuando vacío")
    void getAll_noContent() throws Exception {
        when(queries.handle(any(GetAllLocationsQuery.class)))
                .thenReturn(List.of());

        mockMvc.perform(get(BASE))
                .andExpect(status().isNoContent());
    }

    // --------------------- GET /locations/dangerous?quantityReports=5 ---------------------
    @Test
    @DisplayName("GET /locations/dangerous -> 200 OK con lista")
    void getDangerous_ok() throws Exception {
        Location e1 = Mockito.mock(Location.class);
        Location e2 = Mockito.mock(Location.class);

        when(queries.handle(any(GetDangerousLocationsQuery.class)))
                .thenReturn(List.of(e1, e2));

        var r1 = new LocationResource(1L, "-12.05", "-77.04", 20L);
        var r2 = new LocationResource(2L, "-12.06", "-77.03", 21L);

        try (MockedStatic<LocationResourceFromEntityAssembler> mRes =
                     Mockito.mockStatic(LocationResourceFromEntityAssembler.class)) {
            mRes.when(() -> LocationResourceFromEntityAssembler.toResource(any(Location.class)))
                    .thenReturn(r1, r2);

            mockMvc.perform(get(BASE + "/dangerous").param("quantityReports", "5"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(2)))
                    .andExpect(jsonPath("$[0].id", is(1)))
                    .andExpect(jsonPath("$[1].id", is(2)));
        }
    }

    @Test
    @DisplayName("GET /locations/dangerous -> 204 No Content cuando vacío")
    void getDangerous_noContent() throws Exception {
        when(queries.handle(any(GetDangerousLocationsQuery.class)))
                .thenReturn(List.of());

        mockMvc.perform(get(BASE + "/dangerous").param("quantityReports", "3"))
                .andExpect(status().isNoContent());
    }

    // --------------------- DELETE /locations/report/{reportId} ---------------------
    @Test
    @DisplayName("DELETE /locations/report/{reportId} -> 200 OK con mensaje")
    void deleteAllByReport_ok() throws Exception {
        Mockito.doNothing().when(commands)
                .handle(any(DeleteAllLocationsByIdReportCommand.class));

        mockMvc.perform(delete(BASE + "/report/{reportId}", 45))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("deleted successfully")));
    }

    @Test
    @DisplayName("DELETE /locations/report/{reportId} -> 400 Bad Request cuando IllegalArgumentException")
    void deleteAllByReport_badRequest() throws Exception {
        Mockito.doThrow(new IllegalArgumentException("invalid report"))
                .when(commands).handle(any(DeleteAllLocationsByIdReportCommand.class));

        mockMvc.perform(delete(BASE + "/report/{reportId}", 45))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("invalid report")));
    }

    @Test
    @DisplayName("DELETE /locations/report/{reportId} -> 500 Internal Server Error cuando error inesperado")
    void deleteAllByReport_internalError() throws Exception {
        Mockito.doThrow(new RuntimeException("boom"))
                .when(commands).handle(any(DeleteAllLocationsByIdReportCommand.class));

        mockMvc.perform(delete(BASE + "/report/{reportId}", 45))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(containsString("boom")));
    }
}
