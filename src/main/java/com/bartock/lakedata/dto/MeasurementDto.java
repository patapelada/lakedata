package com.bartock.lakedata.dto;

import java.time.ZonedDateTime;

import javax.validation.constraints.NotNull;

import com.bartock.lakedata.jackson.MeasurementTypeDeserializer;
import com.bartock.lakedata.jackson.MeasurementTypeSerializer;
import com.bartock.lakedata.jackson.ZonedDateTimeDeserializer;
import com.bartock.lakedata.jackson.ZonedDateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class MeasurementDto {
    private Long id;
    @NotNull(message = "measurement type can't be null")
    @JsonSerialize(using = MeasurementTypeSerializer.class)
    @JsonDeserialize(using = MeasurementTypeDeserializer.class)
    private MeasurementTypeDto type;
    @NotNull(message = "value can't be null")
    private Double value;
    @NotNull(message = "timestamp identifier can't be null")
    @JsonDeserialize(using = ZonedDateTimeDeserializer.class)
    @JsonSerialize(using = ZonedDateTimeSerializer.class)
    private ZonedDateTime timestamp;
    private LocationDto location;

    public MeasurementDto() {
    }

    public MeasurementDto(@NotNull MeasurementTypeDto type, @NotNull Double value, @NotNull ZonedDateTime timestamp,
            @NotNull LocationDto location) {
        this.type = type;
        this.value = value;
        this.timestamp = timestamp;
        this.location = location;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MeasurementTypeDto getType() {
        return type;
    }

    public void setType(MeasurementTypeDto type) {
        this.type = type;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public ZonedDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(ZonedDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public LocationDto getLocation() {
        return location;
    }

    public void setLocation(LocationDto location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return String.format("MeasurementDto [id=%s, location=%s, timestamp=%s, type=%s, value=%s]", id, location,
                timestamp, type, value);
    }
}
