package de.rexlmanu.ranks.listener;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.rexlmanu.ranks.config.ConfigProvider;
import de.rexlmanu.ranks.config.PluginConfig;
import de.rexlmanu.ranks.event.UserLevelUpEvent;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.bukkit.Server;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@Singleton
@RequiredArgsConstructor(onConstructor_ = @__(@Inject))
public class ExecuteCommandsForLevelupListener implements Listener {

  private final ConfigProvider configProvider;
  private final Server server;

  @EventHandler
  public void handle(UserLevelUpEvent event) {
    List<String> commands = this.configProvider.get(PluginConfig.class).levelUpCommands();

    commands.stream()
        .map(
            s ->
                s.replace("%player%", event.getPlayer().getName())
                    .replace("%level-before%", String.valueOf(event.levelBefore()))
                    .replace("%level-after%", String.valueOf(event.levelAfter())))
        .forEach(s -> this.server.dispatchCommand(this.server.getConsoleSender(), s));
  }
}
