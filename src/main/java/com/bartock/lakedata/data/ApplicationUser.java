package com.bartock.lakedata.data;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
@Entity
public class ApplicationUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @NotNull
    private String description;
    @NotNull
    private String apiKey;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "location_id")
    private Location allowedLocation;
    @NotNull
    @Enumerated(EnumType.STRING)
    private Role role;

    public ApplicationUser() {

    }

    public ApplicationUser(Role role, String apiKey, String description) {
        this();
        this.role = role;
        this.apiKey = apiKey;
        this.description = description;
    }

    public enum Role {
        MEASUREMENT_PROVIDER, ADMIN
    }

}
