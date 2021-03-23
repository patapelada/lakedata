package com.bartock.lakedata.controller;

import java.util.List;

import javax.validation.Valid;

import com.bartock.lakedata.dto.MeasurementTypeDto;
import com.bartock.lakedata.security.IsAdmin;
import com.bartock.lakedata.security.IsMeasurementProvider;
import com.bartock.lakedata.service.MeasurementTypeService;
import com.google.common.base.Preconditions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/measurementType")
public class MeasurementTypeController extends AbstractRestController {

    @Autowired
    private MeasurementTypeService measurementTypeService;

    private void verifyExistence(String id) {
        if (!measurementTypeService.existsMeasurementType(id)) {
            throw new IllegalArgumentException("No measurement type found with provided identifier");
        }
    }

    @GetMapping
    @IsMeasurementProvider
    public List<MeasurementTypeDto> findAll() {
        return measurementTypeService.getAllMeasurementTypes();
    }

    @GetMapping(value = "/{id}")
    @IsMeasurementProvider
    public MeasurementTypeDto findById(@PathVariable("id") String id) {
        verifyExistence(id);
        return Preconditions.checkNotNull(measurementTypeService.getMeasurementType(id),
                "No measurement type found with provided identifier");
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @IsAdmin
    public MeasurementTypeDto create(@RequestBody @Valid MeasurementTypeDto measurementType) {
        return Preconditions.checkNotNull(measurementTypeService.saveMeasurementType(measurementType));
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    @IsAdmin
    public void update(@PathVariable("id") String id, @RequestBody @Valid MeasurementTypeDto measurementType) {
        verifyExistence(id);
        measurementTypeService.saveMeasurementType(measurementType);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    @IsAdmin
    public void delete(@PathVariable("id") String id) {
        verifyExistence(id);
        measurementTypeService.deleteMeasurementType(id);
    }

}
