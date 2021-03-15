package com.bartock.lakedata.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.transaction.Transactional;

import com.bartock.lakedata.data.MeasurementType;
import com.bartock.lakedata.dto.MeasurementTypeDto;
import com.bartock.lakedata.repository.MeasurementTypeRepository;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class MeasurementTypeService {
    @Autowired
    private MeasurementTypeRepository measurementTypeRepository;
    @Autowired
    private ModelMapper modelMapper;

    public List<MeasurementTypeDto> getAllMeasurementTypes() {
        return StreamSupport.stream(measurementTypeRepository.findAll().spliterator(), false).map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public MeasurementTypeDto getMeasurementType(String id) {
        Optional<MeasurementType> measurementType = measurementTypeRepository.findById(id);
        if (measurementType.isPresent()) {
            return convertToDto(measurementType.get());
        }
        return null;
    }

    public MeasurementTypeDto saveMeasurementType(MeasurementTypeDto measurementType) {
        MeasurementType savedMeasurementType = measurementTypeRepository.save(convertToEntity(measurementType));

        return convertToDto(savedMeasurementType);
    }

    public void deleteMeasurementType(MeasurementTypeDto measurementType) {
        deleteMeasurementType(measurementType.getId());
    }

    public void deleteMeasurementType(String id) {
        measurementTypeRepository.deleteById(id);
    }

    public boolean existsMeasurementType(String id) {
        return measurementTypeRepository.existsById(id);
    }

    public MeasurementTypeDto convertToDto(MeasurementType measurementType) {
        return modelMapper.map(measurementType, MeasurementTypeDto.class);
    }

    public MeasurementType convertToEntity(MeasurementTypeDto measurementType) {
        return modelMapper.map(measurementType, MeasurementType.class);
    }
}
