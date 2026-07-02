package dev.bestzige.simplemcplugin.listener;

import dev.bestzige.simplemcplugin.SimpleMcPlugin;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * @author BestZige
 * Created: 7/2/2026
 */

@RequiredArgsConstructor
public class PlayerJoinListener implements Listener {

    private final SimpleMcPlugin plugin;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (plugin.getSettings().sync().playerJoin()) {
            plugin.getPlayerSyncService().syncWithRetry(event.getPlayer());
        }
    }
}
