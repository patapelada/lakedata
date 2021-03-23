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

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/user")
@IsAdmin
@Slf4j
public class ApplicationUserController extends AbstractRestController {

    @Autowired
    private ApplicationUserService applicationUserService;

    private void verifyExistence(Integer id) {
        if (!applicationUserService.existsApplicationUserById(id)) {
            throw new IllegalArgumentException("No user found with provided identifier");
        }
    }

    @GetMapping
    public List<ApplicationUserDto> findAll() {
        return applicationUserService.getAllApplicationUsers();
    }

    @GetMapping(value = "/{id}")
    public ApplicationUserDto findById(@PathVariable("id") Integer id) {
        verifyExistence(id);
        return applicationUserService.getApplicationUser(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApplicationUserDto create(@RequestBody @Valid ApplicationUserDto applicationUser) {
        log.info("requested to create user: {}", applicationUser);
        return Preconditions.checkNotNull(applicationUserService.saveApplicationUser(applicationUser));
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void update(@PathVariable("id") Integer id, @RequestBody @Valid ApplicationUserDto applicationUser) {
        verifyExistence(id);
        log.info("requested to update user to: {}", applicationUser);
        applicationUserService.saveApplicationUser(applicationUser);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable("id") Integer id) {
        verifyExistence(id);
        log.info("requested to delete user: {}", id);
        applicationUserService.deleteApplicationUser(id);
    }

}
