package com.bartock.lakedata.dto;

import java.util.Collection;
import java.util.Objects;

import javax.validation.constraints.NotNull;

import com.bartock.lakedata.data.ApplicationUser.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

public class ApplicationUserDto implements UserDetails {
    private static final long serialVersionUID = -6298376046145657342L;

    private Integer id;
    private String apiKey;
    @NotNull(message = "description can't be null")
    private String description;
    private LocationDto allowedLocation;
    @NotNull(message = "user role can't be null")
    private Role role;

    public ApplicationUserDto() {
        // needed for jackson deserialization
    }

    public ApplicationUserDto(Role role, String description) {
        this();
        this.description = description;
        this.role = role;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocationDto getAllowedLocation() {
        return allowedLocation;
    }

    public void setAllowedLocation(LocationDto allowedLocation) {
        this.allowedLocation = allowedLocation;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, role, description, apiKey, allowedLocation);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ApplicationUserDto other = (ApplicationUserDto) obj;

        return Objects.equals(id, other.id) && Objects.equals(apiKey, other.apiKey)
                && Objects.equals(description, other.description) && Objects.equals(role, other.role)
                && Objects.equals(allowedLocation, other.allowedLocation);
    }

    @Override
    public String toString() {
        return String.format("ApplicationUserDto [apiKey=%s, description=%s, id=%s, role=%s]", apiKey, description, id,
                role);
    }

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return AuthorityUtils.createAuthorityList(role.name());
    }

    @Override
    @JsonIgnore
    public String getPassword() {
        return null;
    }

    @Override
    @JsonIgnore
    public String getUsername() {
        return null;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }

}
