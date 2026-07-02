    package dev.bestzige.simplemcplugin;

import dev.bestzige.simplemcplugin.api.SimpleMcApiClient;
import dev.bestzige.simplemcplugin.command.SimpleMcCommand;
import dev.bestzige.simplemcplugin.config.PluginSettings;
import dev.bestzige.simplemcplugin.listener.PlayerJoinListener;
import dev.bestzige.simplemcplugin.service.PlayerLookupService;
import dev.bestzige.simplemcplugin.service.PlayerSyncService;
import dev.bestzige.simplemcplugin.service.WalletService;
import dev.bestzige.simplemcplugin.util.Messages;
import lombok.Getter;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import revxrsal.commands.bukkit.BukkitCommandActor;
import revxrsal.commands.bukkit.BukkitCommandHandler;
import revxrsal.commands.bukkit.exception.SenderNotPlayerException;
import revxrsal.commands.exception.InvalidSubcommandException;
import revxrsal.commands.exception.NoPermissionException;

/**
 * @author BestZige
 * Created: 7/2/2026
 */

@Getter
public final class SimpleMcPlugin extends JavaPlugin {

    private PluginSettings settings;
    private Messages messages;
    private SimpleMcApiClient apiClient;
    private PlayerLookupService playerLookupService;
    private PlayerSyncService playerSyncService;
    private WalletService walletService;
    private BukkitCommandHandler commandHandler;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        reloadComponents();

        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        registerCommands();

        getLogger().info("SimpleMCPlugin enabled.");
    }

    @Override
    public void onDisable() {
        if (commandHandler != null) {
            commandHandler.unregisterAllCommands();
        }
        if (apiClient != null) {
            apiClient.close();
        }
        getLogger().info("SimpleMCPlugin disabled.");
    }

    public void reloadComponents() {
        reloadConfig();
        settings = PluginSettings.from(getConfig());
        messages = new Messages(settings.messages());

        if (apiClient != null) {
            apiClient.close();
        }

        apiClient = SimpleMcApiClient.create(settings.api(), getLogger());
        playerLookupService = new PlayerLookupService(this, apiClient);
        playerSyncService = new PlayerSyncService(this, apiClient);
        walletService = new WalletService(this, apiClient);
    }

    private void registerCommands() {
        commandHandler = BukkitCommandHandler.create(this);
        commandHandler.registerExceptionHandler(NoPermissionException.class,
                (actor, exception) -> messages.send(sender(actor), "no-permission"));
        commandHandler.registerExceptionHandler(SenderNotPlayerException.class,
                (actor, exception) -> messages.send(sender(actor), "player-only"));
        commandHandler.registerExceptionHandler(InvalidSubcommandException.class,
                (actor, exception) -> actor.reply("/simplemc <reload|sync|wallet|player>"));
        commandHandler.register(new SimpleMcCommand(this));
    }

    private CommandSender sender(revxrsal.commands.command.CommandActor actor) {
        if (actor instanceof BukkitCommandActor bukkitActor) {
            return bukkitActor.getSender();
        }
        return getServer().getConsoleSender();
    }
}
