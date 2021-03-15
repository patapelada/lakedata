package com.bartock.lakedata.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import com.bartock.lakedata.data.MeasurementType;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class MeasurementTypeRepositoryTest {

    @Autowired
    private MeasurementTypeRepository measurementTypeRepository;

    @Test
    public void createMeasurementType() {
        // given
        MeasurementType measurementType = new MeasurementType("WT", "Water Temperature");

        // when
        MeasurementType result = measurementTypeRepository.save(measurementType);

        // then
        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getName());
    }

    @Test
    public void createMeasurementTypeEmptyName() {
        // given
        MeasurementType measurementType = new MeasurementType();
        measurementType.setId("WT");

        // when
        measurementType.setName(null);

        // then
        assertThrows(DataIntegrityViolationException.class, () -> measurementTypeRepository.save(measurementType));
    }

    @Test
    public void findMeasurementTypeById() {
        // given
        String id = "WT";
        MeasurementType measurementType = new MeasurementType(id, "Water Temperature");
        measurementTypeRepository.save(measurementType);

        // when
        Optional<MeasurementType> result = measurementTypeRepository.findById(id);

        // then
        assertTrue(result.isPresent());
        assertEquals(measurementType.getName(), result.get().getName());
    }
}
