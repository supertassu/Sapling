package me.tassu.sapling.module;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import me.tassu.sapling.SaplingModule;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.config.Configuration;

import java.util.List;

public class WhisperModule extends Command implements SaplingModule {

    @Inject
    private ProxyServer bungee;

    private static final Joiner JOINER = Joiner.on(' ');
    private final String emptyMessage, playerMessage, sentMessage, receivedMessage;

    @Inject
    public WhisperModule(Configuration configuration) {
        super("whisper", "sapling.whisper", "w", "tell", "msg", "message");

        sentMessage = configuration.getString("whisper.sent", "&7&o(to %user%&7&o) &r%message%");
        playerMessage = configuration.getString("whisper.player",
                "&7[&bBOT&7] &bClaudia&7: &rThere are no players online with specified name.");
        receivedMessage = configuration.getString("whisper.received", "&7&o(from %user%&7&o) &r%message%");
        emptyMessage = configuration.getString("whisper.empty", "&7[&bBOT&7] &bClaudia&7: &r/msg <target> <message...>");
    }

    @Override
    public void execute(CommandSender sender, String[] arguments) {
        if (arguments.length < 2) {
            send(sender, emptyMessage);
            return;
        }

        List<String> args = Lists.newArrayList(arguments);

        String targetName = args.remove(0);

        ProxiedPlayer player = bungee.getPlayer(targetName);

        if (player == null) {
            send(sender, playerMessage);
            return;
        }

        String message = JOINER.join(args);

        send(player, receivedMessage
                .replace("%user%", this.getName(sender))
                .replace("%message%", message));
        send(sender, sentMessage
                .replace("%user%", this.getName(player))
                .replace("%message%", message));
    }
}
