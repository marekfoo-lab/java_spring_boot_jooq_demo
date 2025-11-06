package pl.mfconsulting.java.demo.quarkus_jooq.config;

import io.quarkus.test.junit.QuarkusTestProfile;

/**
 * This is configuration for 'integration' profile. It uses custom settings.
 */
public class IntegrationTestProfile
        implements QuarkusTestProfile {
    @Override
    public String getConfigProfile() {
        return "integration";
    }
}
