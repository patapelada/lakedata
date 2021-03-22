package com.bartock.lakedata.controller;

import java.util.List;

import javax.validation.Valid;

import com.bartock.lakedata.dto.ApplicationUserDto;
import com.bartock.lakedata.service.ApplicationUserService;
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
import com.bartock.lakedata.security.IsAdmin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

@RestController
@RequestMapping("/api/user")
@IsAdmin
public class ApplicationUserController extends AbstractRestController {

    @Autowired
    private ApplicationUserService applicationUserService;

    private void verifyExistence(Integer id) {
        if (!applicationUserService.existsApplicationUserById(id)) {
            throw new IllegalArgumentException("No user found with provided identifier");
        }
    }

    @GetMapping
    @Operation(summary = "Get all users")
    public List<ApplicationUserDto> findAll() {
        return applicationUserService.getAllApplicationUsers();
    }

    @GetMapping(value = "/{id}")
    @Operation(summary = "Get a user by its identifier")
    public ApplicationUserDto findById(
            @PathVariable("id") @Parameter(description = "identifier of user to be searched") Integer id) {
        verifyExistence(id);
        return applicationUserService.getApplicationUser(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a user")
    public ApplicationUserDto create(@RequestBody @Valid ApplicationUserDto applicationUser) {
        return Preconditions.checkNotNull(applicationUserService.saveApplicationUser(applicationUser));
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update a user by its identifier")
    public void update(@PathVariable("id") @Parameter(description = "identifier of user to be updated") Integer id,
            @RequestBody @Valid ApplicationUserDto applicationUser) {
        verifyExistence(id);
        applicationUserService.saveApplicationUser(applicationUser);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Delete a measurement type by its identifier")
    public void delete(@PathVariable("id") @Parameter(description = "identifier of type to be deleted") Integer id) {
        verifyExistence(id);
        applicationUserService.deleteApplicationUser(id);
    }

}
