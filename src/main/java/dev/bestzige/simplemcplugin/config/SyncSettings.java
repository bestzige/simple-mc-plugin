package dev.bestzige.simplemcplugin.config;

import lombok.Builder;

/**
 * @author BestZige
 * Created: 7/2/2026
 */

@Builder
public record SyncSettings(
        boolean playerJoin,
        long retryDelayTicks,
        boolean announceFailuresToConsole
) {
}
