package me.tassu.sapling;

import com.google.common.base.Preconditions;
import me.lucko.luckperms.LuckPerms;
import me.lucko.luckperms.api.ChatMetaType;
import me.lucko.luckperms.api.LuckPermsApi;
import me.lucko.luckperms.api.User;
import me.lucko.luckperms.api.caching.MetaData;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public interface SaplingModule {

    default ProxyServer getProxyServer() {
        return ProxyServer.getInstance();
    }

    default String getChatMeta(ProxiedPlayer player, ChatMetaType type) {
        Preconditions.checkNotNull(player, "player");
        Preconditions.checkNotNull(type, "type");

        User user = getLuckPerms().getUserSafe(player.getUniqueId()).orElseThrow(() -> new IllegalArgumentException("No user data found. Try again later."));
        MetaData metaData = user.getCachedData().getMetaData(getLuckPerms().getContextsForPlayer(player));

        if (type == ChatMetaType.PREFIX) {
            return metaData.getPrefix() != null ? metaData.getPrefix() + " " : "";
        } else if (type == ChatMetaType.SUFFIX) {
            return metaData.getSuffix() != null ? " " + metaData.getSuffix() : "";
        } else {
            throw new IllegalArgumentException("wot");
        }
    }

    default LuckPermsApi getLuckPerms() {
        return LuckPerms.getApi();
    }

    default String getName(CommandSender sender) {
        if (!(sender instanceof ProxiedPlayer)) {
            return sender.getName();
        }

        ProxiedPlayer player = (ProxiedPlayer) sender;

        return getChatMeta(player, ChatMetaType.PREFIX)
                + player.getDisplayName()
                + getChatMeta(player, ChatMetaType.SUFFIX);
    }

    default void broadcast(String string) {
        getProxyServer().broadcast(TextComponent.fromLegacyText(
                ChatColor.translateAlternateColorCodes('&', string)
        ));
    }

    default void send(CommandSender sender, String string) {
        sender.sendMessage(TextComponent.fromLegacyText(
                ChatColor.translateAlternateColorCodes('&', string)
        ));
    }

    default void broadcastToPerm(String string, String permission) {
        BaseComponent[] text = TextComponent.fromLegacyText(
                ChatColor.translateAlternateColorCodes('&', string));

        getProxyServer().getConsole().sendMessage(text);

        getProxyServer().getPlayers()
                .stream()
                .filter(it -> it.hasPermission(permission))
                .forEach(it -> it.sendMessage(text));
    }

}
