package dev.bestzige.simplemcplugin.service;

import com.github.Anon8281.universalScheduler.UniversalScheduler;
import com.github.Anon8281.universalScheduler.scheduling.schedulers.TaskScheduler;
import dev.bestzige.simplemcplugin.SimpleMcPlugin;
import dev.bestzige.simplemcplugin.api.ApiClientException;
import dev.bestzige.simplemcplugin.api.SimpleMcApiClient;
import dev.bestzige.simplemcplugin.api.dto.PlayerResponse;

import java.util.function.Consumer;

/**
 * @author BestZige
 * Created: 7/2/2026
 */

public class PlayerLookupService {

    private final SimpleMcApiClient apiClient;
    private final TaskScheduler scheduler;

    public PlayerLookupService(SimpleMcPlugin plugin, SimpleMcApiClient apiClient) {
        this.apiClient = apiClient;
        this.scheduler = UniversalScheduler.getScheduler(plugin);
    }

    public void getByUsername(
            String username,
            Consumer<PlayerResponse> success,
            Consumer<Exception> failure
    ) {
        scheduler.runTaskAsynchronously(() -> {
            try {
                PlayerResponse response = apiClient.execute(
                        apiClient.api().getPlayerByUsername(apiClient.authorization(), username)
                );
                scheduler.runTask(() -> success.accept(response));
            } catch (ApiClientException exception) {
                scheduler.runTask(() -> failure.accept(exception));
            }
        });
    }
}
