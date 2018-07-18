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

package me.tassu.sapling;

import com.google.inject.Inject;
import com.google.inject.Injector;
import me.tassu.sapling.module.HelpOpModule;
import me.tassu.sapling.module.JoinMessageModule;
import me.tassu.sapling.module.WhisperModule;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public final class Sapling extends Plugin {

    @Inject private Configuration configuration;

    @Inject private JoinMessageModule joinMessageModule;
    @Inject private WhisperModule whisperModule;
    @Inject private HelpOpModule helpOpModule;

    @Override
    public void onEnable() {
        SaplingGuiceModule module = new SaplingGuiceModule(this, createConfigFile());
        Injector injector = module.createInjector();
        injector.injectMembers(this);

        getProxy().getPluginManager().registerListener(this, joinMessageModule);

        getProxy().getPluginManager().registerCommand(this, helpOpModule);
        getProxy().getPluginManager().registerCommand(this, whisperModule);
    }

    private Configuration createConfigFile() {
        if (!getDataFolder().exists()) {
            if (!getDataFolder().mkdir()) throw new RuntimeException(new IOException("Could not create config file."));
        }

        File file = new File(getDataFolder(), "sapling.yml");

        if (!file.exists()) {
            try (InputStream in = getResourceAsStream("sapling.yml")) {
                Files.copy(in, file.toPath());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        Configuration configuration;

        try {
            configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return configuration;
    }

    @Override
    public void onDisable() {
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(configuration, new File(getDataFolder(), "config.yml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
