package com.bartock.lakedata.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import com.bartock.lakedata.data.Location;
import com.bartock.lakedata.data.Measurement;
import com.bartock.lakedata.data.MeasurementType;
import com.bartock.lakedata.dto.MeasurementDto;
import com.bartock.lakedata.repository.MeasurementRepository;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class MeasurementService {
    @Autowired
    private MeasurementRepository measurementRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private EntityManager entityManager;

    public List<MeasurementDto> getAllMeasurements() {
        return StreamSupport.stream(measurementRepository.findAll().spliterator(), false).map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<MeasurementDto> getAllMeasurementsByLocationId(String id) {
        return StreamSupport.stream(measurementRepository.findByLocationId(id).spliterator(), false)
                .map(this::convertToDto).collect(Collectors.toList());
    }

    public MeasurementDto getMeasurement(Long id) {
        Optional<Measurement> measurement = measurementRepository.findById(id);
        if (measurement.isPresent()) {
            return convertToDto(measurement.get());
        }
        return null;
    }

    public MeasurementDto saveMeasurement(MeasurementDto measurement) {
        // fetch references to not update any of their fields
        MeasurementType measurementType = entityManager.getReference(MeasurementType.class,
                measurement.getType().getId());
        Location location = entityManager.getReference(Location.class, measurement.getLocation().getId());

        // assign to entity
        Measurement toBeSaved = convertToEntity(measurement);
        toBeSaved.setLocation(location);
        toBeSaved.setType(measurementType);

        // save
        Measurement savedMeasurement = measurementRepository.save(toBeSaved);

        return convertToDto(savedMeasurement);
    }

    public void deleteMeasurement(MeasurementDto measurement) {
        deleteMeasurement(measurement.getId());
    }

    public void deleteMeasurement(Long id) {
        measurementRepository.deleteById(id);
    }

    public boolean existsMeasurement(Long id) {
        return measurementRepository.existsById(id);
    }

    public MeasurementDto convertToDto(Measurement measurement) {
        return modelMapper.map(measurement, MeasurementDto.class);
    }

    public Measurement convertToEntity(MeasurementDto measurement) {
        return modelMapper.map(measurement, Measurement.class);
    }
}
