package com.bartock.lakedata.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import com.bartock.lakedata.data.ApplicationUser;
import com.bartock.lakedata.data.ApplicationUser.Role;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class ApplicationUserRepositoryTest {

    @Autowired
    private ApplicationUserRepository applicationUserRepository;

    @Test
    public void createApplicationUser() {
        // given
        ApplicationUser user = new ApplicationUser(Role.ADMIN, "ASDd", "admin user");

        // when
        ApplicationUser result = applicationUserRepository.save(user);

        // then
        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getApiKey());
        assertNotNull(result.getDescription());
    }

    @Test
    public void createApplicationUserEmptyDescription() {
        // given
        ApplicationUser user = new ApplicationUser();

        // when
        user.setDescription(null);

        // then
        assertThrows(DataIntegrityViolationException.class, () -> applicationUserRepository.save(user));
    }

    @Test
    public void findApplicationUserByApiKey() {
        // given
        ApplicationUser user = applicationUserRepository.save(new ApplicationUser(Role.ADMIN, "ASDd", "admin"));
        String apiKey = user.getApiKey();

        // when
        Optional<ApplicationUser> result = applicationUserRepository.findOneByApiKey(apiKey);

        // then
        assertTrue(result.isPresent());
        assertEquals(apiKey, result.get().getApiKey());
    }

}
