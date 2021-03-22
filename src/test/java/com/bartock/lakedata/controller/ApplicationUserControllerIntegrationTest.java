package com.bartock.lakedata.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.bartock.lakedata.data.ApplicationUser.Role;
import com.bartock.lakedata.dto.ApplicationUserDto;
import com.bartock.lakedata.dto.LocationDto;
import com.bartock.lakedata.service.ApplicationUserService;
import com.bartock.lakedata.service.LocationService;

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
public class ApplicationUserControllerIntegrationTest extends AbstractControllerTest {
    @Autowired
    private ApplicationUserService applicationUserService;

    @Autowired
    private LocationService locationService;

    private final String baseUri = "/api/user";

    private ApplicationUserDto createTestApplicationUser(ApplicationUserDto applicationUser) {
        return applicationUserService.saveApplicationUser(applicationUser);
    }

    private ApplicationUserDto getTestUser() {
        return new ApplicationUserDto(Role.ADMIN, "admin user");
    }

    private LocationDto createTestLocation(LocationDto location) {
        return locationService.saveLocation(location);
    }

    private LocationDto getTestLocation() {
        return new LocationDto("CHAM01", "hirsi");
    }

    @Test
    public void getApplicationUserList_emptyTest() throws Exception {
        // when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(baseUri))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        ApplicationUserDto[] result = super.mapFromJson(content, ApplicationUserDto[].class);

        // then
        assertEquals(0, result.length);
    }

    @Test
    public void getApplicationUserList_oneApplicationUserTest() throws Exception {
        // given
        ApplicationUserDto input = getTestUser();
        input.setAllowedLocation(createTestLocation(getTestLocation()));
        ApplicationUserDto user = createTestApplicationUser(input);

        // when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(baseUri))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        ApplicationUserDto[] result = super.mapFromJson(content, ApplicationUserDto[].class);

        // then
        assertEquals(1, result.length);
        assertNotNull(result[0].getId());
        assertNotNull(result[0].getApiKey());
        assertEquals(user.getDescription(), result[0].getDescription());
        assertEquals(user.getRole(), result[0].getRole());
        assertEquals(user.getAllowedLocation(), result[0].getAllowedLocation());
    }

    @Test
    public void createApplicationUser() throws Exception {
        // given
        ApplicationUserDto user = getTestUser();
        user.setAllowedLocation(createTestLocation(getTestLocation()));
        String inputJson = super.mapToJson(user);

        // when
        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders.post(baseUri).contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
                .andReturn();
        String content = mvcResult.getResponse().getContentAsString();

        ApplicationUserDto result = super.mapFromJson(content, ApplicationUserDto.class);

        // then
        assertEquals(HttpStatus.CREATED.value(), mvcResult.getResponse().getStatus());
        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getApiKey());
        assertEquals(user.getDescription(), result.getDescription());
        assertEquals(user.getRole(), result.getRole());
        assertEquals(user.getAllowedLocation(), result.getAllowedLocation());
    }

    @Test
    public void updateApplicationUser() throws Exception {
        // given
        ApplicationUserDto user = createTestApplicationUser(getTestUser());
        user.setDescription("NEW");
        String inputJson = super.mapToJson(user);

        // when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put(baseUri + "/" + user.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();
        String content = mvcResult.getResponse().getContentAsString();

        // then
        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
        assertTrue(content.isEmpty());
        assertEquals("NEW", applicationUserService.getApplicationUser(user.getId()).getDescription());
    }

    @Test
    public void deleteApplicationUser() throws Exception {
        // given
        ApplicationUserDto applicationUser = createTestApplicationUser(getTestUser());

        // when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete(baseUri + "/" + applicationUser.getId()))
                .andReturn();
        String content = mvcResult.getResponse().getContentAsString();

        // then
        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
        assertTrue(content.isEmpty());
        assertTrue(applicationUserService.getAllApplicationUsers().isEmpty());
    }

}
