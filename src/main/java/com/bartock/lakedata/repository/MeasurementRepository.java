package com.bartock.lakedata.repository;

import java.util.List;

import com.bartock.lakedata.data.Measurement;

import org.springframework.data.repository.CrudRepository;

public interface MeasurementRepository extends CrudRepository<Measurement, Long> {

    public List<Measurement> findByLocationId(String id);
}
