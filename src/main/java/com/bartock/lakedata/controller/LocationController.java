package com.bartock.lakedata.controller;

import java.util.List;

import javax.validation.Valid;

import com.bartock.lakedata.data.ApplicationUser.Role;
import com.bartock.lakedata.dto.ApplicationUserDto;
import com.bartock.lakedata.dto.LocationDto;
import com.bartock.lakedata.dto.MeasurementDto;
import com.bartock.lakedata.security.IsAdmin;
import com.bartock.lakedata.security.IsMeasurementProvider;
import com.bartock.lakedata.security.UnauthorizedException;
import com.bartock.lakedata.service.LocationService;
import com.bartock.lakedata.service.MeasurementService;
import com.google.common.base.Preconditions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

@RestController
@RequestMapping("/api/location")
public class LocationController extends AbstractRestController {

    @Autowired
    private LocationService locationService;
    @Autowired
    private MeasurementService measurementService;

    @GetMapping
    @Operation(summary = "Get all locations")
    @IsAdmin
    public List<LocationDto> findAll() {
        return locationService.getAllLocations();
    }

    @GetMapping(value = "/{id}")
    @Operation(summary = "Get a location by its identifier")
    @IsMeasurementProvider
    public LocationDto findById(
            @PathVariable("id") @Parameter(description = "identifier of location to be searched") String id,
            Authentication authentication) {
        if (hasRightsForLocation(id, authentication)) {
            return locationService.getLocation(id);
        }
        throw new UnauthorizedException();
    }

    private void verifyExistence(String id) {
        if (!locationService.existsLocation(id)) {
            throw new IllegalArgumentException("No location found with provided identifier");
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new location")
    @IsAdmin
    public LocationDto create(@RequestBody @Valid LocationDto location) {
        return Preconditions.checkNotNull(locationService.saveLocation(location));
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update existing location with provided identifier")
    @IsAdmin
    public void update(@PathVariable("id") @Parameter(description = "identifier of location to be searched") String id,
            @RequestBody @Valid LocationDto location) {
        verifyExistence(id);
        locationService.saveLocation(location);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Delete a location and its measurements by its identifier")
    @IsAdmin
    public void delete(
            @PathVariable("id") @Parameter(description = "identifier of location to be searched") String id) {
        verifyExistence(id);
        locationService.deleteLocation(id);
    }

    // Measurements
    @GetMapping(value = "/{id}/measurement")
    @Operation(summary = "Get all measurements from location with provided identifier")
    @IsMeasurementProvider
    public List<MeasurementDto> getMeasurements(
            @PathVariable("id") @Parameter(description = "identifier of location to be searched") String id,
            Authentication authentication) {
        verifyExistence(id);
        if (hasRightsForLocation(id, authentication)) {
            return measurementService.getAllMeasurementsByLocationId(id);

        }
        throw new UnauthorizedException();
    }

    @PostMapping(value = "/{id}/measurement")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Create measurement for location with provided identifier")
    @IsMeasurementProvider
    public MeasurementDto createMeasurement(
            @PathVariable("id") @Parameter(description = "identifier of location to be searched") String id,
            @RequestBody @Valid MeasurementDto measurement, Authentication authentication) {
        verifyExistence(id);
        if (hasRightsForLocation(id, authentication)) {
            measurement.setLocation(locationService.getLocation(id));
            return measurementService.saveMeasurement(measurement);

        }
        throw new UnauthorizedException();
    }

    public boolean hasRightsForLocation(String locationId, Authentication authentication) {
        ApplicationUserDto currentUser = (ApplicationUserDto) authentication.getPrincipal();
        return Role.ADMIN.equals(currentUser.getRole()) || (currentUser.getAllowedLocation() != null
                && locationId.equals(currentUser.getAllowedLocation().getId()));
    }

}
