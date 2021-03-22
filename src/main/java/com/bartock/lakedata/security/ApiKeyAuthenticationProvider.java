package com.bartock.lakedata.security;

import com.bartock.lakedata.dto.ApplicationUserDto;
import com.bartock.lakedata.service.ApplicationUserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class ApiKeyAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    @Autowired
    private ApplicationUserService applicationUserService;

    @Override
    protected void additionalAuthenticationChecks(final UserDetails d, final UsernamePasswordAuthenticationToken auth) {
        // Nothing to do
    }

    @Override
    protected UserDetails retrieveUser(final String username,
            final UsernamePasswordAuthenticationToken authentication) {
        final String apiKey = authentication.getCredentials().toString();

        ApplicationUserDto user = applicationUserService.getApplicationUserByApiKey(apiKey);
        if (user != null) {
            return user;
        }

        throw new UsernameNotFoundException("Cannot find user for provided api key");
    }
}
