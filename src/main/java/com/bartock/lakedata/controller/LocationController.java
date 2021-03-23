package com.bartock.lakedata.controller;

import java.util.Collections;
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

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/location")
@Slf4j
public class LocationController extends AbstractRestController {

    @Autowired
    private LocationService locationService;
    @Autowired
    private MeasurementService measurementService;

    @GetMapping
    @IsMeasurementProvider
    public List<LocationDto> findAll(Authentication authentication) {
        ApplicationUserDto currentUser = (ApplicationUserDto) authentication.getPrincipal();
        if (Role.ADMIN.equals(currentUser.getRole())) {
            return locationService.getAllLocations();
        } else {
            return currentUser.getAllowedLocation() != null ? List.of(currentUser.getAllowedLocation())
                    : Collections.EMPTY_LIST;
        }
    }

    @GetMapping(value = "/{id}")
    @IsMeasurementProvider
    public LocationDto findById(@PathVariable("id") String id, Authentication authentication) {
        if (hasRightsForLocation(id, authentication)) {
            return locationService.getLocationWithMeasurements(id);
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
    @IsAdmin
    public LocationDto create(@RequestBody @Valid LocationDto location) {
        log.info("Requested to create location '{}'", location);
        return Preconditions.checkNotNull(locationService.saveLocation(location));
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    @IsAdmin
    public void update(@PathVariable("id") String id, @RequestBody @Valid LocationDto location) {
        verifyExistence(id);
        log.info("Requested to update location with id {} to '{}'", id, location);
        locationService.saveLocation(location);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    @IsAdmin
    public void delete(@PathVariable("id") String id) {
        verifyExistence(id);
        log.info("Requested to delete location with id {}", id);
        locationService.deleteLocation(id);
    }

    // Measurements
    @GetMapping(value = "/{id}/measurement")
    @IsMeasurementProvider
    public List<MeasurementDto> getMeasurements(@PathVariable("id") String id, Authentication authentication) {
        verifyExistence(id);
        if (hasRightsForLocation(id, authentication)) {
            return measurementService.getAllMeasurementsByLocationId(id);

        }
        throw new UnauthorizedException();
    }

    @PostMapping(value = "/{id}/measurement")
    @ResponseStatus(HttpStatus.OK)
    @IsMeasurementProvider
    public MeasurementDto createMeasurement(@PathVariable("id") String id,
            @RequestBody @Valid MeasurementDto measurement, Authentication authentication) {
        verifyExistence(id);
        if (hasRightsForLocation(id, authentication)) {
            log.info("Requested to add measurement '{}' to location with id {}", measurement, id);
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
