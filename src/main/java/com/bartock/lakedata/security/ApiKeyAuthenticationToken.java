package com.bartock.lakedata.security;

import java.util.Collection;

import com.google.common.base.Objects;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.Transient;

@Transient
public final class ApiKeyAuthenticationToken extends AbstractAuthenticationToken {

    private static final long serialVersionUID = -6413431045816012803L;

    private final String apiKey;

    public ApiKeyAuthenticationToken(final String apiKey, final Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.apiKey = apiKey;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return apiKey;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(apiKey);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        ApiKeyAuthenticationToken other = (ApiKeyAuthenticationToken) obj;

        return Objects.equal(apiKey, other.apiKey);
    }
}
