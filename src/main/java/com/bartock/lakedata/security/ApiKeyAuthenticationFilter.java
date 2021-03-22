package com.bartock.lakedata.security;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import static com.google.common.net.HttpHeaders.AUTHORIZATION;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.removeStart;

import org.springframework.security.core.AuthenticationException;

public class ApiKeyAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private static final String API_KEY_PARAM = "apiKey";
    private static final String BEARER = "Bearer";

    protected ApiKeyAuthenticationFilter(RequestMatcher requiresAuthenticationRequestMatcher) {
        super(requiresAuthenticationRequestMatcher);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {
        final String param = ofNullable(request.getHeader(AUTHORIZATION)).orElse(request.getParameter(API_KEY_PARAM));

        final String apiKey = ofNullable(param).map(value -> removeStart(value, BEARER)).map(String::trim)
                .orElseThrow(() -> new BadCredentialsException("Missing API key"));

        final Authentication auth = new UsernamePasswordAuthenticationToken(apiKey, apiKey);
        return getAuthenticationManager().authenticate(auth);
    }

    @Override
    protected void successfulAuthentication(final HttpServletRequest request, final HttpServletResponse response,
            final FilterChain chain, final Authentication authResult) throws IOException, ServletException {
        super.successfulAuthentication(request, response, chain, authResult);
        chain.doFilter(request, response);
    }
}