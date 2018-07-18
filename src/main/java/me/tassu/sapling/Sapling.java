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
