package de.rexlmanu.ranks.event;

import de.rexlmanu.ranks.database.user.UserEntity;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

@Getter
public class UserLevelUpEvent extends PlayerEvent {
  private static final HandlerList HANDLER_LIST = new HandlerList();
  private final UserEntity user;
  private final int levelBefore;
  private final int levelAfter;

  public UserLevelUpEvent(@NotNull Player who, UserEntity user, int levelBefore, int levelAfter) {
    super(who);
    this.user = user;
    this.levelBefore = levelBefore;
    this.levelAfter = levelAfter;
  }

  @Override
  public @NotNull HandlerList getHandlers() {
    return HANDLER_LIST;
  }

  public static HandlerList getHandlerList() {
    return HANDLER_LIST;
  }
}
