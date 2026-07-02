package dev.bestzige.simplemcplugin.service;

import com.github.Anon8281.universalScheduler.UniversalScheduler;
import com.github.Anon8281.universalScheduler.scheduling.schedulers.TaskScheduler;
import dev.bestzige.simplemcplugin.SimpleMcPlugin;
import dev.bestzige.simplemcplugin.api.ApiClientException;
import dev.bestzige.simplemcplugin.api.SimpleMcApiClient;
import dev.bestzige.simplemcplugin.api.dto.WalletResponse;
import org.bukkit.entity.Player;

import java.util.function.Consumer;

/**
 * @author BestZige
 * Created: 7/2/2026
 */

public class WalletService {

    private final SimpleMcApiClient apiClient;
    private final TaskScheduler scheduler;

    public WalletService(SimpleMcPlugin plugin, SimpleMcApiClient apiClient) {
        this.apiClient = apiClient;
        this.scheduler = UniversalScheduler.getScheduler(plugin);
    }

    public void getWallet(Player player, Consumer<WalletResponse> success, Consumer<Exception> failure) {
        scheduler.runTaskAsynchronously(() -> {
            try {
                WalletResponse response = apiClient.execute(
                        apiClient.api().getWallet(apiClient.authorization(), player.getUniqueId())
                );
                scheduler.runTask(player, () -> success.accept(response));
            } catch (ApiClientException exception) {
                scheduler.runTask(player, () -> failure.accept(exception));
            }
        });
    }
}
