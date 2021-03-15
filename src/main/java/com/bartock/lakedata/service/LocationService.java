package com.bartock.lakedata.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.transaction.Transactional;

import com.bartock.lakedata.data.Location;
import com.bartock.lakedata.dto.LocationDto;
import com.bartock.lakedata.dto.MeasurementDto;
import com.bartock.lakedata.repository.LocationRepository;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class LocationService {

    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private MeasurementService measurementService;
    @Autowired
    private ModelMapper modelMapper;

    public List<LocationDto> getAllLocations() {
        return StreamSupport.stream(locationRepository.findAll().spliterator(), false).map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public LocationDto getLocationWithMeasurements(String locationId) {
        LocationDto locationDto = getLocation(locationId);
        locationDto.setMeasurements(measurementService.getAllMeasurementsByLocationId(locationId));

        return locationDto;
    }

    public LocationDto getLocation(String id) {
        Optional<Location> location = locationRepository.findById(id);
        if (location.isPresent()) {
            return convertToDto(location.get());
        }
        return null;
    }

    public LocationDto saveLocation(LocationDto location) {
        Location savedLocation = locationRepository.save(convertToEntity(location));

        return convertToDto(savedLocation);
    }

    public void deleteLocation(LocationDto location) {
        deleteLocation(location.getId());
    }

    public void deleteLocation(String id) {
        List<MeasurementDto> measurements = measurementService.getAllMeasurementsByLocationId(id);
        // manually delete all measurements associated with asset to delete
        measurements.stream().forEach(measurementService::deleteMeasurement);
        locationRepository.deleteById(id);
    }

    public boolean existsLocation(String id) {
        return locationRepository.existsById(id);
    }

    public LocationDto convertToDto(Location location) {
        return modelMapper.map(location, LocationDto.class);
    }

    public Location convertToEntity(LocationDto location) {
        return modelMapper.map(location, Location.class);
    }
}
