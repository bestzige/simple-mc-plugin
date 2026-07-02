package dev.bestzige.simplemcplugin.service;

import com.github.Anon8281.universalScheduler.UniversalScheduler;
import com.github.Anon8281.universalScheduler.scheduling.schedulers.TaskScheduler;
import dev.bestzige.simplemcplugin.SimpleMcPlugin;
import dev.bestzige.simplemcplugin.api.ApiClientException;
import dev.bestzige.simplemcplugin.api.SimpleMcApiClient;
import dev.bestzige.simplemcplugin.api.dto.PlayerResponse;
import dev.bestzige.simplemcplugin.api.dto.PlayerSyncRequest;
import org.bukkit.entity.Player;

import java.util.function.Consumer;

/**
 * @author BestZige
 * Created: 7/2/2026
 */

public class PlayerSyncService {

    private final SimpleMcPlugin plugin;
    private final SimpleMcApiClient apiClient;
    private final TaskScheduler scheduler;

    public PlayerSyncService(SimpleMcPlugin plugin, SimpleMcApiClient apiClient) {
        this.plugin = plugin;
        this.apiClient = apiClient;
        this.scheduler = UniversalScheduler.getScheduler(plugin);
    }

    public void sync(Player player, Consumer<PlayerResponse> success, Consumer<Exception> failure) {
        PlayerSyncRequest request = PlayerSyncRequest.builder()
                .uuid(player.getUniqueId())
                .username(player.getName())
                .build();

        scheduler.runTaskAsynchronously(() -> {
            try {
                PlayerResponse response = apiClient.execute(apiClient.api().syncPlayer(apiClient.authorization(), request));
                scheduler.runTask(player, () -> success.accept(response));
            } catch (ApiClientException exception) {
                scheduler.runTask(player, () -> failure.accept(exception));
            }
        });
    }

    public void syncWithRetry(Player player) {
        sync(player, ignored -> {
        }, exception -> {
            if (plugin.getSettings().sync().announceFailuresToConsole()) {
                plugin.getLogger().warning("Player sync failed for " + player.getName() + ": " + exception.getMessage());
            }

            long retryDelayTicks = plugin.getSettings().sync().retryDelayTicks();
            if (retryDelayTicks > 0 && player.isOnline()) {
                scheduler.runTaskLater(player, () -> syncWithRetry(player), retryDelayTicks);
            }
        });
    }
}
