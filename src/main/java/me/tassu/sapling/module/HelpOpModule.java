package me.tassu.sapling.module;

import com.google.common.base.Joiner;
import com.google.inject.Inject;
import me.tassu.sapling.SaplingModule;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.config.Configuration;

public class HelpOpModule extends Command implements SaplingModule {

    private static final Joiner JOINER = Joiner.on(' ');

    private final String sentMessage, receivedMessage, emptyMessage;

    @Inject
    public HelpOpModule(Configuration configuration) {
        super("helpop", "sapling.helpop.send", "helpop", "hop");

        sentMessage = configuration.getString("helpop.sent", "&7[&bBOT&7] &bClaus&7: Our elite samurai is now looking into your request.");
        receivedMessage = configuration.getString("helpop.received", "&2(HelpOP) &a%user% &7@ %server% >> &r%message%");
        emptyMessage = configuration.getString("helpop.empty", "&7[&bBOT&7] &bClaudia&7: Empty message is not very useful.");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        String message = JOINER.join(args);

        if (message.trim().isEmpty()) {
            send(sender, emptyMessage);
            return;
        }

        broadcastToPerm(receivedMessage
                        .replace("%user%", this.getName(sender))
                        .replace("%message%", message)
                        .replace("%server%", getServerName(sender)),
                "sapling.helpop.receive");
        send(sender, sentMessage);
    }

    private String getServerName(CommandSender sender) {
        if (!(sender instanceof ProxiedPlayer)) {
            return "(none)";
        }

        return ((ProxiedPlayer) sender).getServer().getInfo().getName();
    }
}
