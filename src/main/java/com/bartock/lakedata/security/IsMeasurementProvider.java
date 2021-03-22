package com.bartock.lakedata.security;

import org.springframework.security.access.prepost.PreAuthorize;
import java.lang.annotation.Target;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.ElementType;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasAuthority('ADMIN') || hasAuthority('MEASUREMENT_PROVIDER')")
public @interface IsMeasurementProvider {
}
