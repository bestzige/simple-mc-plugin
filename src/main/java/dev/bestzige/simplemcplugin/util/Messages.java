package dev.bestzige.simplemcplugin.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;

import java.util.Map;

/**
 * @author BestZige
 * Created: 7/2/2026
 */

public class Messages {

    private final MiniMessage miniMessage = MiniMessage.miniMessage();
    private final Map<String, String> messages;
    private final String prefix;

    public Messages(Map<String, String> messages) {
        this.messages = messages;
        this.prefix = messages.getOrDefault("prefix", "");
    }

    public void send(CommandSender sender, String key, String... replacements) {
        sender.sendMessage(format(key, replacements));
    }

    public Component format(String key, String... replacements) {
        String message = prefix + messages.getOrDefault(key, key);
        for (int index = 0; index + 1 < replacements.length; index += 2) {
            message = message.replace("<" + replacements[index] + ">", replacements[index + 1]);
        }
        return miniMessage.deserialize(message);
    }
}
