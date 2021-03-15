package com.bartock.lakedata.repository;

import com.bartock.lakedata.data.Location;

import org.springframework.data.repository.CrudRepository;

public interface LocationRepository extends CrudRepository<Location, String> {

}
