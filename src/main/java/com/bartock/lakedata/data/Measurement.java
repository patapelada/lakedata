package com.bartock.lakedata.data;

import java.time.ZonedDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Entity
public class Measurement {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotNull
    @ManyToOne(cascade = { CascadeType.REFRESH }, fetch = FetchType.EAGER)
    private MeasurementType type;
    @NotNull
    private Double value;
    @NotNull
    private ZonedDateTime timestamp;
    @NotNull
    @ManyToOne(cascade = { CascadeType.REFRESH }, fetch = FetchType.EAGER)
    private Location location;

    public Measurement() {

    }

    public Measurement(@NotNull MeasurementType type, @NotNull Double value, @NotNull ZonedDateTime timestamp,
            @NotNull Location location) {
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

    public MeasurementType getType() {
        return type;
    }

    public void setType(MeasurementType type) {
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

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return String.format("Measurement [id=%s, location=%s, timestamp=%s, type=%s, value=%s]", id, location,
                timestamp, type, value);
    }
}
