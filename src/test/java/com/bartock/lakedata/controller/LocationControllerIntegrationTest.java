package com.bartock.lakedata.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.ZonedDateTime;

import com.bartock.lakedata.dto.LocationDto;
import com.bartock.lakedata.dto.MeasurementDto;
import com.bartock.lakedata.dto.MeasurementTypeDto;
import com.bartock.lakedata.service.LocationService;
import com.bartock.lakedata.service.MeasurementService;
import com.bartock.lakedata.service.MeasurementTypeService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class LocationControllerIntegrationTest extends AbstractControllerTest {
    @Autowired
    private MeasurementService measurementService;
    @Autowired
    private LocationService locationService;
    @Autowired
    private MeasurementTypeService measurementTypeService;

    private final String baseUri = "/api/location";

    private LocationDto createTestLocation(LocationDto location) {
        return locationService.saveLocation(location);
    }

    private LocationDto getTestLocation() {
        return new LocationDto("CHAM01", "Hirsi");
    }

    private MeasurementTypeDto createTestMeasurementType(MeasurementTypeDto measurementType) {
        return measurementTypeService.saveMeasurementType(measurementType);
    }

    private MeasurementTypeDto getTestType() {
        return new MeasurementTypeDto("WT", "Water Temp");
    }

    private MeasurementDto createTestMeasurement(MeasurementDto measurement) {
        return measurementService.saveMeasurement(measurement);
    }

    private MeasurementDto getTestMeasurement(LocationDto location, MeasurementTypeDto type) {
        return new MeasurementDto(type, 12.6, ZonedDateTime.now(), location);
    }

    @Test
    public void getLocationList_emptyTest() throws Exception {
        // when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(baseUri))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        LocationDto[] result = super.mapFromJson(content, LocationDto[].class);

        // then
        assertEquals(0, result.length);
    }

    @Test
    public void getLocationList_oneLocationTest() throws Exception {
        // given
        LocationDto location = createTestLocation(getTestLocation());

        // when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(baseUri))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        LocationDto[] result = super.mapFromJson(content, LocationDto[].class);

        // then
        assertEquals(1, result.length);
        assertEquals(location.getId(), result[0].getId());
    }

    @Test
    public void createLocation() throws Exception {
        // given
        String inputJson = super.mapToJson(getTestLocation());

        // when
        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders.post(baseUri).contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
                .andReturn();
        String content = mvcResult.getResponse().getContentAsString();

        LocationDto result = super.mapFromJson(content, LocationDto.class);

        // then
        assertEquals(HttpStatus.CREATED.value(), mvcResult.getResponse().getStatus());
        assertNotNull(result);
        assertEquals(getTestLocation().getId(), result.getId());
    }

    @Test
    public void updateLocation() throws Exception {
        // given
        LocationDto location = createTestLocation(getTestLocation());
        location.setName("NEW");
        String inputJson = super.mapToJson(location);

        // when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put(baseUri + "/" + location.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();
        String content = mvcResult.getResponse().getContentAsString();

        // then
        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
        assertTrue(content.isEmpty());
        assertEquals("NEW", locationService.getLocation(location.getId()).getName());
    }

    @Test
    public void deleteLocation() throws Exception {
        // given
        LocationDto location = createTestLocation(getTestLocation());

        // when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete(baseUri + "/" + location.getId()))
                .andReturn();
        String content = mvcResult.getResponse().getContentAsString();

        // then
        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
        assertTrue(content.isEmpty());
        assertTrue(locationService.getAllLocations().isEmpty());
    }

    @Test
    public void getMeasurementList_emptyMeasurementTest() throws Exception {
        // given
        LocationDto location = createTestLocation(getTestLocation());

        // when
        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders.get(baseUri + "/" + location.getId() + "/measurement"))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        MeasurementDto[] result = super.mapFromJson(content, MeasurementDto[].class);

        // then
        assertEquals(0, result.length);
    }

    @Test
    public void getMeasurementList_oneMeasurementTest() throws Exception {
        // given
        LocationDto location = createTestLocation(getTestLocation());
        MeasurementDto measurement = createTestMeasurement(
                getTestMeasurement(location, createTestMeasurementType(getTestType())));

        // when
        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders.get(baseUri + "/" + location.getId() + "/measurement")).andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        MeasurementDto[] result = super.mapFromJson(content, MeasurementDto[].class);

        // then
        assertEquals(1, result.length);
        MeasurementDto resultingMeasurement = result[0];
        assertNotNull(resultingMeasurement.getId());
        assertEquals(measurement.getLocation(), resultingMeasurement.getLocation());
        assertEquals(measurement.getTimestamp().toLocalDate(), resultingMeasurement.getTimestamp().toLocalDate());
        assertEquals(measurement.getType().getId(), resultingMeasurement.getType().getId());
        assertEquals(measurement.getValue(), resultingMeasurement.getValue());
    }

    @Test
    public void createMeasurement_test() throws Exception {
        // given
        LocationDto location = createTestLocation(getTestLocation());
        MeasurementDto measurement = getTestMeasurement(null, createTestMeasurementType(getTestType()));
        String inputJson = super.mapToJson(measurement);

        // when
        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders.post(baseUri + "/" + location.getId() + "/measurement")
                        .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
                .andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        System.out.println(content);

        MeasurementDto result = super.mapFromJson(content, MeasurementDto.class);

        // then
        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(location.getId(), result.getLocation().getId());
        assertEquals(measurement.getTimestamp().toLocalDate(), result.getTimestamp().toLocalDate());
        assertEquals(measurement.getType().getId(), result.getType().getId());
        assertEquals(measurement.getValue(), result.getValue());
    }
}
