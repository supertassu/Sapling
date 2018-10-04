/*
 * The MIT License
 * Copyright © 2018 Tassu <hello@tassu.me>
 * Copyright © 2018 Contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package me.tassu.sapling.module;

import com.google.inject.Inject;
import me.tassu.sapling.Sapling;
import me.tassu.sapling.SaplingModule;
import net.md_5.bungee.api.event.*;
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
        if (event.getPlayer().getServer() == null) return;
        getProxyServer().getScheduler().schedule(sapling, () -> broadcast(quitMessage
                .replace("%user%", getName(event.getPlayer()))), 1, TimeUnit.SECONDS);
    }

    @EventHandler
    public void onSwitch(ServerConnectEvent event) {
        if (event.getPlayer().getServer() == null) return;
        if (event.getTarget().getName().equals(event.getPlayer().getServer().getInfo().getName())) return;
        String old = event.getPlayer().getServer().getInfo().getName();

        getProxyServer().getScheduler().schedule(sapling, () -> {
            if (!event.getTarget().getName().equals(event.getPlayer().getServer().getInfo().getName())) return;
            broadcast(switchMessage
                    .replace("%user%", getName(event.getPlayer()))
                    .replace("%from%", old)
                    .replace("%to%", event.getTarget().getName()));
        }, 1, TimeUnit.SECONDS);
    }

}
