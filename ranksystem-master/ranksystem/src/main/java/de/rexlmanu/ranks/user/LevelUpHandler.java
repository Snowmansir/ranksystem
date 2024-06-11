package de.rexlmanu.ranks.user;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.rexlmanu.ranks.config.message.MessageConfig;
import de.rexlmanu.ranks.config.message.MessageManager;
import de.rexlmanu.ranks.event.UserLevelUpEvent;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.minimessage.tag.resolver.Formatter;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@Singleton
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class LevelUpHandler implements Listener {
  private final MessageManager messageManager;

  @EventHandler
  public void handle(UserLevelUpEvent event) {
    this.messageManager.send(
        event.getPlayer(), MessageConfig::rankedUp, Formatter.number("rank", event.levelAfter()));
  }
}
