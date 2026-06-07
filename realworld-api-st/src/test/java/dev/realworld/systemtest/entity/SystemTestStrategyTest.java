package dev.realworld.systemtest.entity;

import org.junit.jupiter.api.Test;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SystemTestStrategyTest {

    @Test
    void definesBaselineStrategyDefaults() {
        var targetApi = URI.create("http://localhost:8080");

        var strategy = SystemTestStrategy.defaults(targetApi);

        assertEquals(targetApi, strategy.targetApiBaseUrl());
        assertEquals(AuthenticationMode.NONE, strategy.authenticationMode());
        assertEquals(DataIsolationMode.SCENARIO_OWNED_UNIQUE_DATA, strategy.dataIsolationMode());
        assertEquals(VerificationBoundary.STRATEGY_CONTRACT_ONLY, strategy.verificationBoundary());
    }
}
