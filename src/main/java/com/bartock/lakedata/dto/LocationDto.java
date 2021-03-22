package com.bartock.lakedata.dto;

import java.util.List;
import java.util.Objects;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.*;

public class LocationDto {

    @NotNull(message = "location identifier can't be null")
    private String id;
    @NotNull(message = "location name can't be null")
    private String name;
    @JsonInclude(Include.NON_NULL)
    private Double latitude;
    @JsonInclude(Include.NON_EMPTY)
    private Double longitude;
    private List<MeasurementDto> measurements;

    public LocationDto() {
    }

    public LocationDto(@NotNull String id, @NotNull String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return String.format("LocationDto [id=%s, latitude=%s, longitude=%s, name=%s]", id, latitude, longitude, name);
    }

    public List<MeasurementDto> getMeasurements() {
        return measurements;
    }

    public void setMeasurements(List<MeasurementDto> measurements) {
        this.measurements = measurements;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, latitude, longitude, name);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        LocationDto other = (LocationDto) obj;

        return Objects.equals(id, other.id) && Objects.equals(latitude, other.latitude)
                && Objects.equals(longitude, other.longitude) && Objects.equals(name, other.name);

    }
}
