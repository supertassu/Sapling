package me.tassu.sapling.module;

import com.google.inject.Inject;
import me.tassu.sapling.Sapling;
import me.tassu.sapling.SaplingModule;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.event.EventHandler;

import java.util.concurrent.TimeUnit;

public class JoinMessageModule implements SaplingModule, Listener {

    private final String joinMessage, quitMessage, switchMessage;
    @Inject private Sapling sapling;

    @Inject
    public JoinMessageModule(Configuration configuration) {
        joinMessage = configuration.getString("join", "&a%user%&7 has joined the network at &2%server%&7.");
        quitMessage = configuration.getString("quit", "&a%user%&7 has left the network.");
        switchMessage = configuration.getString("switch", "&a%user%&7 has switched servers from &2%from%&7 to &2%to%&7.");
    }

    @EventHandler
    public void onPostLogin(PostLoginEvent event) {
        getProxyServer().getScheduler().schedule(sapling, () -> {
            if (event.getPlayer().getServer() == null) return;
            broadcast(joinMessage
                    .replace("%user%", getName(event.getPlayer()))
                    .replace("%server%", event.getPlayer().getServer().getInfo().getName()));
        }, 1, TimeUnit.SECONDS);
    }

    @EventHandler
    public void onQuit(PlayerDisconnectEvent event) {
        getProxyServer().getScheduler().schedule(sapling, () -> broadcast(quitMessage
                .replace("%user%", getName(event.getPlayer()))), 1, TimeUnit.SECONDS);
    }

    @EventHandler
    public void onSwitch(ServerSwitchEvent event) {
        String oldServer = event.getPlayer().getServer().getInfo().getName();

        getProxyServer().getScheduler().schedule(sapling, () -> {
            if (event.getPlayer().getServer() == null) return;
            if (oldServer.equals(event.getPlayer().getServer().getInfo().getName())) return;

            broadcast(switchMessage
                    .replace("%user%", getName(event.getPlayer()))
                    .replace("%from%", oldServer)
                    .replace("%to%", event.getPlayer().getServer().getInfo().getName()));
        }, 1, TimeUnit.SECONDS);
    }

}
