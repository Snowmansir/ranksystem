package de.rexlmanu.ranks.user;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.rexlmanu.ranks.database.DatabaseManager;
import de.rexlmanu.ranks.database.user.UserEntity;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

@Singleton
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class UserManager implements Listener {
  private final Server server;
  private final DatabaseManager databaseManager;
  private final Map<UUID, UserEntity> cachedUsers = new HashMap<>();

  @SneakyThrows
  @EventHandler
  public void handleJoin(PlayerJoinEvent event) {
    this.loadUser(event.getPlayer());
  }

  private void loadUser(Player player) throws SQLException {
    UserEntity user = this.databaseManager.userDao().findByPlayer(player.getUniqueId());

    if (user != null) {
      user.username(player.getName());
      this.cachedUsers.put(user.playerId(), user);
      this.databaseManager.userDao().update(user);
      return;
    }

    this.databaseManager
        .userDao()
        .create(
            UserEntity.builder().username(player.getName()).playerId(player.getUniqueId()).build());

    this.cachedUsers.put(
        player.getUniqueId(), this.databaseManager.userDao().findByPlayer(player.getUniqueId()));
  }

  @SneakyThrows
  @EventHandler
  public void handleQuit(PlayerQuitEvent event) {
    Player player = event.getPlayer();
    this.saveUser(player);
  }

  private void saveUser(Player player) throws SQLException {
    UserEntity user = this.cachedUsers.get(player.getUniqueId());
    if (user == null) return;
    this.databaseManager.userDao().update(user);
    user.activityProgresses().updateAll();
    this.cachedUsers.remove(player.getUniqueId());
  }

  public UserEntity getUser(Player player) {
    return this.cachedUsers.get(player.getUniqueId());
  }

  public void loadAll() {
    for (Player onlinePlayer : this.server.getOnlinePlayers()) {
      try {
        this.loadUser(onlinePlayer);
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }

  public void saveAll() {
    for (Player onlinePlayer : this.server.getOnlinePlayers()) {
      try {
        this.saveUser(onlinePlayer);
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }
}
