package dev.bestzige.simplemcplugin.command;

import dev.bestzige.simplemcplugin.SimpleMcPlugin;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.DefaultFor;
import revxrsal.commands.annotation.Description;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.bukkit.BukkitCommandActor;
import revxrsal.commands.bukkit.annotation.CommandPermission;

/**
 * @author BestZige
 * Created: 7/2/2026
 */

@RequiredArgsConstructor
@Command({"simplemc", "smc"})
@CommandPermission("simplemc.admin")
public class SimpleMcCommand {

    private final SimpleMcPlugin plugin;

    @DefaultFor({"simplemc", "smc"})
    @Description("Shows SimpleMCPlugin command usage.")
    public void usage(BukkitCommandActor actor) {
        actor.reply("/simplemc <reload|sync|wallet|player>");
    }

    @Subcommand("reload")
    @Description("Reloads SimpleMCPlugin configuration.")
    public void reload(BukkitCommandActor actor) {
        plugin.reloadComponents();
        plugin.getMessages().send(actor.getSender(), "reload");
    }

    @Subcommand("sync")
    @Description("Syncs your player profile with Simple MC API.")
    public void sync(BukkitCommandActor actor) {
        Player player = actor.requirePlayer();
        plugin.getMessages().send(player, "sync-started");
        plugin.getPlayerSyncService().sync(
                player,
                ignored -> plugin.getMessages().send(player, "sync-success"),
                exception -> plugin.getMessages().send(player, "sync-failed", "message", exception.getMessage())
        );
    }

    @Subcommand("wallet")
    @Description("Shows your Simple MC wallet balance.")
    public void wallet(BukkitCommandActor actor) {
        Player player = actor.requirePlayer();
        plugin.getWalletService().getWallet(
                player,
                wallet -> plugin.getMessages().send(
                        player,
                        "wallet",
                        "coins", wallet.getCoins().toPlainString(),
                        "gems", wallet.getGems().toPlainString()
                ),
                exception -> plugin.getMessages().send(player, "sync-failed", "message", exception.getMessage())
        );
    }

    @Subcommand("player")
    @Description("Looks up an offline player stored in Simple MC API by username.")
    public void player(BukkitCommandActor actor, String username) {
        CommandSender sender = actor.getSender();
        plugin.getPlayerLookupService().getByUsername(
                username,
                player -> plugin.getMessages().send(
                        sender,
                        "player-lookup",
                        "uuid", player.getUuid().toString(),
                        "username", player.getUsername(),
                        "rank", player.getRank()
                ),
                exception -> plugin.getMessages().send(sender, "player-lookup-failed", "message", exception.getMessage())
        );
    }
}
