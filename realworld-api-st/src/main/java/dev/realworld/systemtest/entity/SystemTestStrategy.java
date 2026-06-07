package dev.realworld.systemtest.entity;

import java.net.URI;
import java.util.Objects;

public record SystemTestStrategy(
        URI targetApiBaseUrl,
        AuthenticationMode authenticationMode,
        DataIsolationMode dataIsolationMode,
        VerificationBoundary verificationBoundary) {

    public static SystemTestStrategy defaults(URI targetApiBaseUrl) {
        return new SystemTestStrategy(
                Objects.requireNonNull(targetApiBaseUrl),
                AuthenticationMode.NONE,
                DataIsolationMode.SCENARIO_OWNED_UNIQUE_DATA,
                VerificationBoundary.STRATEGY_CONTRACT_ONLY);
    }
}
