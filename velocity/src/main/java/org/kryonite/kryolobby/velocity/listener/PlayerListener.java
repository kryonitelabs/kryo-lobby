package org.kryonite.kryolobby.velocity.listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChooseInitialServerEvent;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import java.util.Comparator;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.kyori.adventure.text.Component;

@Slf4j
@RequiredArgsConstructor
public class PlayerListener {

  protected static final String LOBBY_PREFIX = "lobby-";

  private final ProxyServer proxyServer;

  @Subscribe
  public void onChooseInitialServer(PlayerChooseInitialServerEvent event) {
    Optional<RegisteredServer> registeredServer = proxyServer.getAllServers().stream()
        .filter(server -> server.getServerInfo().getName().startsWith(LOBBY_PREFIX))
        .min(Comparator.comparingInt(server -> server.getPlayersConnected().size()));

    registeredServer.ifPresentOrElse(event::setInitialServer, () -> {
      event.getPlayer().disconnect(Component.text("No lobby server available! Try again later."));
      log.warn("No lobby server found!");
    });
  }
}
