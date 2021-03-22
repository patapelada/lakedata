package com.bartock.lakedata.service;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import javax.transaction.Transactional;

import com.bartock.lakedata.data.Location;
import com.bartock.lakedata.data.ApplicationUser.Role;
import com.bartock.lakedata.data.ApplicationUser;
import com.bartock.lakedata.dto.ApplicationUserDto;

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
public class ApplicationUserServiceTest {
    @Autowired
    private ApplicationUserService applicationUserService;

    @Test
    public void entityToDtoConversionTest() {
        // given
        ApplicationUser entity = new ApplicationUser(Role.ADMIN, "API KEY", "admin user");
        entity.setId(2);
        entity.setAllowedLocation(new Location("cham", "hirsi"));

        // when
        ApplicationUserDto result = applicationUserService.convertToDto(entity);

        // then
        assertEquals(entity.getId(), result.getId());
        assertEquals(entity.getAllowedLocation().getId(), result.getAllowedLocation().getId());
        assertEquals(entity.getApiKey(), result.getApiKey());
        assertEquals(entity.getDescription(), result.getDescription());
        assertEquals(entity.getRole(), result.getRole());
    }

    @Test
    public void saveApplicationUserWithoutAPIKey() {
        // given
        ApplicationUserDto dto = new ApplicationUserDto(Role.ADMIN, "admin user");

        // when
        ApplicationUserDto result = applicationUserService.saveApplicationUser(dto);

        // then
        assertNotNull(result.getApiKey());
    }

}