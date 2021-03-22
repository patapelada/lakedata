package com.bartock.lakedata.security;

import java.util.Arrays;

import com.bartock.lakedata.dto.ApplicationUserDto;
import com.bartock.lakedata.service.ApplicationUserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private ApplicationUserService applicationUserService;

    @Override
    public UserDetails loadUserByUsername(String apiKey) throws UsernameNotFoundException {
        ApplicationUserDto user = applicationUserService.getApplicationUserByApiKey(apiKey);
        if (user == null) {
            throw new UsernameNotFoundException("User not found ");
        }
        GrantedAuthority authority = new SimpleGrantedAuthority(user.getRole().name());
        return new User(user.getDescription(), user.getApiKey(), Arrays.asList(authority));
    }
}
