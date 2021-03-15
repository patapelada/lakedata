package com.bartock.lakedata.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.bartock.lakedata.dto.MeasurementTypeDto;
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
public class MeasurementTypeControllerIntegrationTest extends AbstractControllerTest {
    @Autowired
    private MeasurementTypeService measurementTypeService;

    private final String baseUri = "/api/measurementType";

    private MeasurementTypeDto createTestMeasurementType(MeasurementTypeDto measurementType) {
        return measurementTypeService.saveMeasurementType(measurementType);
    }

    private MeasurementTypeDto getTestType() {
        return new MeasurementTypeDto("WT", "Water Temp");
    }

    @Test
    public void getMeasurementTypeList_emptyTest() throws Exception {
        // when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(baseUri))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        MeasurementTypeDto[] result = super.mapFromJson(content, MeasurementTypeDto[].class);

        // then
        assertEquals(0, result.length);
    }

    @Test
    public void getMeasurementTypeList_oneMeasurementTypeTest() throws Exception {
        // given
        MeasurementTypeDto type = createTestMeasurementType(getTestType());

        // when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(baseUri))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        MeasurementTypeDto[] result = super.mapFromJson(content, MeasurementTypeDto[].class);

        // then
        assertEquals(1, result.length);
        assertEquals(type.getId(), result[0].getId());
    }

    @Test
    public void createMeasurementType() throws Exception {
        // given
        MeasurementTypeDto type = getTestType();
        String inputJson = super.mapToJson(type);

        // when
        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders.post(baseUri).contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
                .andReturn();
        String content = mvcResult.getResponse().getContentAsString();

        MeasurementTypeDto result = super.mapFromJson(content, MeasurementTypeDto.class);

        // then
        assertEquals(HttpStatus.CREATED.value(), mvcResult.getResponse().getStatus());
        assertNotNull(result);
        assertEquals(type.getId(), result.getId());
        assertEquals(type.getName(), result.getName());
    }

    @Test
    public void updateMeasurementType() throws Exception {
        // given
        MeasurementTypeDto type = createTestMeasurementType(getTestType());
        type.setName("NEW");
        String inputJson = super.mapToJson(type);

        // when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put(baseUri + "/" + type.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();
        String content = mvcResult.getResponse().getContentAsString();

        // then
        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
        assertTrue(content.isEmpty());
        assertEquals("NEW", measurementTypeService.getMeasurementType(type.getId()).getName());
    }

    @Test
    public void deleteMeasurementType() throws Exception {
        // given
        MeasurementTypeDto measurementType = createTestMeasurementType(getTestType());

        // when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete(baseUri + "/" + measurementType.getId()))
                .andReturn();
        String content = mvcResult.getResponse().getContentAsString();

        // then
        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
        assertTrue(content.isEmpty());
        assertTrue(measurementTypeService.getAllMeasurementTypes().isEmpty());
    }

}
