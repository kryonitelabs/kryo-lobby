package org.kryonite.kryolobby.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import org.kryonite.kryolobby.velocity.listener.PlayerListener;

@Plugin(id = "kryo-lobby", name = "Kryo Lobby", authors = "Kryonite Labs", version = "0.1.0")
public class KryoLobbyPlugin {

  private final ProxyServer server;

  @Inject
  public KryoLobbyPlugin(ProxyServer server) {
    this.server = server;
  }

  @Subscribe
  public void onInitialize(ProxyInitializeEvent event) {
    server.getEventManager().register(this, new PlayerListener(server));
  }
}
