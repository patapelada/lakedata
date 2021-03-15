package com.bartock.lakedata.dto;

import java.util.Objects;

import javax.validation.constraints.NotNull;

public class MeasurementTypeDto {
    @NotNull(message = "measurement type identifier can't be null")
    private String id;
    @NotNull(message = "measurement type identifier can't be null")
    private String name;

    public MeasurementTypeDto() {
    }

    public MeasurementTypeDto(@NotNull String id) {
        this();
        this.id = id;
    }

    public MeasurementTypeDto(@NotNull String id, @NotNull String name) {
        this(id);
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

    @Override
    public String toString() {
        return String.format("MeasurementTypeDto [id=%s, name=%s]", id, name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        MeasurementTypeDto other = (MeasurementTypeDto) obj;

        return Objects.equals(id, other.id) && Objects.equals(name, other.name);
    }

}
