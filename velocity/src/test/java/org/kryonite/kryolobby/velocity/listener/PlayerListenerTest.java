package org.kryonite.kryolobby.velocity.listener;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.kryonite.kryolobby.velocity.listener.PlayerListener.LOBBY_PREFIX;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.velocitypowered.api.event.player.PlayerChooseInitialServerEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PlayerListenerTest {

  @InjectMocks
  private PlayerListener testee;

  @Mock(answer = Answers.RETURNS_DEEP_STUBS)
  private ProxyServer serverMock;

  @Test
  void shouldChooseServerWithLeastPlayers() {
    // Arrange
    RegisteredServer lobby1 = mock(RegisteredServer.class, Answers.RETURNS_DEEP_STUBS);
    RegisteredServer lobby2 = mock(RegisteredServer.class, Answers.RETURNS_DEEP_STUBS);
    RegisteredServer notLobby = mock(RegisteredServer.class, Answers.RETURNS_DEEP_STUBS);

    when(lobby1.getServerInfo().getName()).thenReturn(LOBBY_PREFIX + "1");
    when(lobby2.getServerInfo().getName()).thenReturn(LOBBY_PREFIX + "2");
    when(notLobby.getServerInfo().getName()).thenReturn("city-build-1");

    when(lobby1.getPlayersConnected().size()).thenReturn(5);
    when(lobby2.getPlayersConnected().size()).thenReturn(6);

    Set<RegisteredServer> servers = Set.of(lobby1, lobby2, notLobby);
    when(serverMock.getAllServers()).thenReturn(servers);

    PlayerChooseInitialServerEvent event = new PlayerChooseInitialServerEvent(mock(Player.class), null);

    // Act
    testee.onChooseInitialServer(event);

    // Assert
    assertTrue(event.getInitialServer().isPresent());
    assertEquals(lobby1, event.getInitialServer().get());
  }

  @Test
  void shouldDisconnectPlayer_WhenNoLobbyAvailable() {
    // Arrange
    Player player = mock(Player.class);
    PlayerChooseInitialServerEvent event = new PlayerChooseInitialServerEvent(player, null);

    RegisteredServer notLobby = mock(RegisteredServer.class, Answers.RETURNS_DEEP_STUBS);
    when(notLobby.getServerInfo().getName()).thenReturn("city-build-1");

    Set<RegisteredServer> servers = Set.of(notLobby);
    when(serverMock.getAllServers()).thenReturn(servers);

    // Act
    testee.onChooseInitialServer(event);

    // Assert
    assertFalse(event.getInitialServer().isPresent());
    verify(player).disconnect(any());
  }
}
