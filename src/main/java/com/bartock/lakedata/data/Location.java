package com.bartock.lakedata.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class Location {

    @Id
    private String id;
    @NotNull
    @Column(nullable = false)
    private String name;
    private Double latitude;
    private Double longitude;

    public Location(@NotNull String name) {
        this.name = name;
    }

    public Location(@NotNull String id, @NotNull String name) {
        this(name);
        this.id = id;
    }

    public Location() {

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
        return String.format("Location [id=%s, latitude=%s, longitude=%s, name=%s]", id, latitude, longitude, name);
    }

}
