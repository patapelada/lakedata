package com.bartock.lakedata.repository;

import java.util.Optional;

import com.bartock.lakedata.data.ApplicationUser;

import org.springframework.data.repository.CrudRepository;

public interface ApplicationUserRepository extends CrudRepository<ApplicationUser, Integer> {
    Boolean existsByApiKey(String apiKey);

    Optional<ApplicationUser> findOneByApiKey(String apiKey);
}
