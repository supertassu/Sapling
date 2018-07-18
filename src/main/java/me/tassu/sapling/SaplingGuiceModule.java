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
