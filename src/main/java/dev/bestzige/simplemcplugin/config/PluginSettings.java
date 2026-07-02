package dev.bestzige.simplemcplugin.config;

import lombok.Builder;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Map;

/**
 * @author BestZige
 * Created: 7/2/2026
 */

@Builder
public record PluginSettings(
        ApiSettings api,
        SyncSettings sync,
        Map<String, String> messages
) {

    public static PluginSettings from(FileConfiguration config) {
        return PluginSettings.builder()
                .api(ApiSettings.builder()
                        .baseUrl(normalizeBaseUrl(config.getString("api.base-url", "http://localhost:8080/")))
                        .token(config.getString("api.token", "default_api_token"))
                        .connectTimeoutSeconds(config.getInt("api.connect-timeout-seconds", 5))
                        .readTimeoutSeconds(config.getInt("api.read-timeout-seconds", 10))
                        .writeTimeoutSeconds(config.getInt("api.write-timeout-seconds", 10))
                        .debugLogging(config.getBoolean("api.debug-logging", false))
                        .build())
                .sync(SyncSettings.builder()
                        .playerJoin(config.getBoolean("sync.player-join", true))
                        .retryDelayTicks(config.getLong("sync.retry-delay-ticks", 100L))
                        .announceFailuresToConsole(config.getBoolean("sync.announce-failures-to-console", true))
                        .build())
                .messages(Map.of(
                        "prefix", config.getString("messages.prefix", ""),
                        "reload", config.getString("messages.reload", "Configuration reloaded."),
                        "no-permission", config.getString("messages.no-permission", "You do not have permission."),
                        "player-only", config.getString("messages.player-only", "This command can only be used by a player."),
                        "sync-started", config.getString("messages.sync-started", "Sync queued."),
                        "sync-success", config.getString("messages.sync-success", "Player sync completed."),
                        "sync-failed", config.getString("messages.sync-failed", "Player sync failed: <message>"),
                        "player-lookup", config.getString("messages.player-lookup", "Player: <username> | UUID: <uuid> | Rank: <rank>"),
                        "player-lookup-failed", config.getString("messages.player-lookup-failed", "Player lookup failed: <message>"),
                        "wallet", config.getString("messages.wallet", "Coins: <coins> | Gems: <gems>")
                ))
                .build();
    }

    private static String normalizeBaseUrl(String baseUrl) {
        return baseUrl.endsWith("/") ? baseUrl : baseUrl + "/";
    }
}
