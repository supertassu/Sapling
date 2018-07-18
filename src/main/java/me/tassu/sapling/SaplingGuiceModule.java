package me.tassu.sapling;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.config.Configuration;

public class SaplingGuiceModule extends AbstractModule {

    private final Sapling plugin;
    private final Configuration configuration;

    public SaplingGuiceModule(Sapling plugin, Configuration configuration) {
        this.plugin = plugin;
        this.configuration = configuration;
    }

    public Injector createInjector() {
        return Guice.createInjector(this);
    }

    @Override
    protected void configure() {
        this.bind(Sapling.class).toInstance(this.plugin);
        this.bind(Configuration.class).toInstance(this.configuration);
        this.bind(ProxyServer.class).toInstance(ProxyServer.getInstance());
    }

}
