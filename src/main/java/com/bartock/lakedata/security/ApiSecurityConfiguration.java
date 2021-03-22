package com.bartock.lakedata.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

import static java.util.Objects.requireNonNull;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Order(1)
public class ApiSecurityConfiguration extends WebSecurityConfigurerAdapter {

    ApiKeyAuthenticationProvider provider;
    private static final RequestMatcher PUBLIC_URLS = new OrRequestMatcher(new AntPathRequestMatcher("/public/**"));
    private static final RequestMatcher PROTECTED_URLS = new NegatedRequestMatcher(PUBLIC_URLS);

    ApiSecurityConfiguration(final ApiKeyAuthenticationProvider provider) {
        super();
        this.provider = requireNonNull(provider);
    }

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(provider);
    }

    @Override
    public void configure(final WebSecurity web) {
        web.ignoring().requestMatchers(PUBLIC_URLS);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // don't create sessions
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        // proper 401 handling
        http.exceptionHandling().defaultAuthenticationEntryPointFor(forbiddenEntryPoint(), PROTECTED_URLS);
        // disable login and logout pages
        http.logout().disable();
        http.formLogin().disable();
        // basic auth not needed
        http.httpBasic().disable();
        // generally disable anonymous access
        http.anonymous().disable();
        // disable remember me functionality
        http.rememberMe().disable();
        http.csrf().disable();

        http.authenticationProvider(provider)
                .addFilterBefore(restAuthenticationFilter(), AnonymousAuthenticationFilter.class).authorizeRequests()
                .requestMatchers(PROTECTED_URLS).authenticated();

    }

    @Bean
    ApiKeyAuthenticationFilter restAuthenticationFilter() throws Exception {
        final ApiKeyAuthenticationFilter filter = new ApiKeyAuthenticationFilter(PROTECTED_URLS);
        filter.setAuthenticationManager(authenticationManager());
        filter.setAuthenticationSuccessHandler(successHandler());
        return filter;
    }

    @Bean
    SimpleUrlAuthenticationSuccessHandler successHandler() {
        final SimpleUrlAuthenticationSuccessHandler successHandler = new SimpleUrlAuthenticationSuccessHandler();
        successHandler.setRedirectStrategy(new NoRedirectStrategy());
        return successHandler;
    }

    /**
     * Disable Spring boot automatic filter registration.
     */
    @Bean
    @Primary
    FilterRegistrationBean<ApiKeyAuthenticationFilter> disableAutoRegistration(
            final ApiKeyAuthenticationFilter filter) {
        final FilterRegistrationBean<ApiKeyAuthenticationFilter> registration = new FilterRegistrationBean<>(filter);
        // Disable Spring boot automatic filter registration.
        registration.setEnabled(false);
        return registration;
    }

    @Bean
    AuthenticationEntryPoint forbiddenEntryPoint() {
        return new HttpStatusEntryPoint(FORBIDDEN);
    }
}
