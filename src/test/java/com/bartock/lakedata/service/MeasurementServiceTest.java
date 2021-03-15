package com.bartock.lakedata.service;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.ZonedDateTime;

import javax.transaction.Transactional;

import com.bartock.lakedata.data.Location;
import com.bartock.lakedata.data.Measurement;
import com.bartock.lakedata.data.MeasurementType;
import com.bartock.lakedata.dto.LocationDto;
import com.bartock.lakedata.dto.MeasurementDto;
import com.bartock.lakedata.dto.MeasurementTypeDto;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
@AutoConfigureTestDatabase(replace = Replace.ANY)
public class MeasurementServiceTest {
    @Autowired
    private MeasurementService measurementService;

    @Autowired
    private LocationService locationService;

    @Autowired
    private MeasurementTypeService measurementTypeService;

    private MeasurementTypeDto createTestType() {
        return measurementTypeService.saveMeasurementType(new MeasurementTypeDto("WT", "water temp"));
    }

    private LocationDto createTestLocation() {
        return locationService.saveLocation(new LocationDto("CHAM01", "Hirsi"));
    }

    @Test
    public void entityToDtoConversionTest() {
        // given
        Measurement entity = new Measurement(new MeasurementType("WT", "water temp"), 15.5, ZonedDateTime.now(),
                new Location("CHAM01", "Hirsgarten"));
        entity.setId(2L);

        // when
        MeasurementDto result = measurementService.convertToDto(entity);

        // then
        assertEquals(entity.getId(), result.getId());
        assertEquals(entity.getType().getId(), result.getType().getId());
        assertEquals(entity.getType().getName(), result.getType().getName());
        assertEquals(entity.getLocation().getId(), result.getLocation().getId());
        assertEquals(entity.getLocation().getName(), result.getLocation().getName());
        assertEquals(entity.getTimestamp(), result.getTimestamp());
        assertEquals(entity.getValue(), result.getValue());
    }

    @Test
    public void saveMeasurementWithTypeId() {
        // given
        LocationDto location = createTestLocation();
        MeasurementTypeDto measurementType = createTestType();
        MeasurementDto dto = new MeasurementDto(measurementType, 15.5, ZonedDateTime.now(), location);

        // when
        dto.getType().setName(null);
        MeasurementDto result = measurementService.saveMeasurement(dto);

        // then
        assertNotNull(result.getLocation().getName());
    }

    @Test
    public void saveMeasurementWithLocationId() {
        // given
        LocationDto location = createTestLocation();
        MeasurementTypeDto measurementType = createTestType();
        MeasurementDto dto = new MeasurementDto(measurementType, 15.5, ZonedDateTime.now(), location);

        // when
        dto.getLocation().setName(null);
        MeasurementDto result = measurementService.saveMeasurement(dto);

        // then
        assertNotNull(result.getLocation().getName());
    }

    @Test
    public void saveMeasurementTest() {
        // given
        LocationDto location = createTestLocation();
        MeasurementTypeDto measurementType = createTestType();
        MeasurementDto dto = new MeasurementDto(measurementType, 15.5, ZonedDateTime.now(), location);

        // when
        MeasurementDto result = measurementService.saveMeasurement(dto);

        // then
        assertNotNull(result.getId());
        assertEquals(dto.getValue(), result.getValue());
        assertEquals(dto.getTimestamp(), result.getTimestamp());
    }

}
