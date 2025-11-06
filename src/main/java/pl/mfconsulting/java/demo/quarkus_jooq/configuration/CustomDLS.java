package pl.mfconsulting.java.demo.quarkus_jooq.configuration;

import jakarta.inject.Qualifier;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Qualifier
@Target({FIELD, TYPE, METHOD, PARAMETER})
@Retention(RUNTIME)
public @interface CustomDLS {
}
