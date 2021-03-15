package com.bartock.lakedata.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import com.bartock.lakedata.data.Location;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class LocationRepositoryTest {

    @Autowired
    private LocationRepository locationRepository;

    @Test
    public void createLocation() {
        // given
        Location location = new Location("CHAM01", "Hirsgarten");

        // when
        Location result = locationRepository.save(location);

        // then
        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getName());
    }

    @Test
    public void createLocationEmptyName() {
        // given
        Location location = new Location();
        location.setId("CHAM02");

        // when
        location.setName(null);

        // then
        assertThrows(DataIntegrityViolationException.class, () -> locationRepository.save(location));
    }

    @Test
    public void findLocationById() {
        // given
        String id = "Cham01";
        Location location = new Location(id, "Hirsgarten");
        locationRepository.save(location);

        // when
        Optional<Location> result = locationRepository.findById(id);

        // then
        assertTrue(result.isPresent());
        assertEquals(location.getName(), result.get().getName());
    }

}
