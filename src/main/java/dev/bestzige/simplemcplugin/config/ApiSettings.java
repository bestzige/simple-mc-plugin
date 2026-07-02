package dev.bestzige.simplemcplugin.config;

import lombok.Builder;

/**
 * @author BestZige
 * Created: 7/2/2026
 */

@Builder
public record ApiSettings(
        String baseUrl,
        String token,
        int connectTimeoutSeconds,
        int readTimeoutSeconds,
        int writeTimeoutSeconds,
        boolean debugLogging
) {
}
