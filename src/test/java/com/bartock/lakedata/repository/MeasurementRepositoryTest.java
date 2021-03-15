package com.bartock.lakedata.repository;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.ZonedDateTime;

import com.bartock.lakedata.data.Location;
import com.bartock.lakedata.data.Measurement;
import com.bartock.lakedata.data.MeasurementType;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class MeasurementRepositoryTest {
    @Autowired
    private MeasurementRepository measurementRepository;
    @Autowired
    private MeasurementTypeRepository measurementTypeRepository;
    @Autowired
    private LocationRepository locationRepository;

    private MeasurementType getMeasurementType() {
        return measurementTypeRepository.save(new MeasurementType("WT", "water temp"));
    }

    private Location getLocation() {
        return locationRepository.save(new Location("Cham01", "Hirsgarte"));
    }

    @Test
    public void createMeasurement() {
        // given
        MeasurementType measurementType = getMeasurementType();
        Location location = getLocation();
        Measurement measurement = new Measurement(measurementType, 4.5, ZonedDateTime.now(), location);

        // when
        Measurement result = measurementRepository.save(measurement);

        // then
        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLocation());
        assertNotNull(result.getTimestamp());
        assertNotNull(result.getType());
    }

    @Test
    public void createMeasurementNewLocation() {
        // given
        MeasurementType measurementType = getMeasurementType();
        Measurement measurement = new Measurement(measurementType, 4.5, ZonedDateTime.now(), null);

        // when
        measurement.setLocation(new Location("CHAM07", "Hü See"));

        // then
        assertThrows(InvalidDataAccessApiUsageException.class, () -> measurementRepository.save(measurement));
    }

    @Test
    public void createMeasurementNewType() {
        // given
        Location location = getLocation();
        Measurement measurement = new Measurement(null, 4.5, ZonedDateTime.now(), location);

        // when
        measurement.setType(new MeasurementType("AT", "air Temp"));

        // then
        assertThrows(InvalidDataAccessApiUsageException.class, () -> measurementRepository.save(measurement));
    }

}
