package de.rexlmanu.ranks.user.activity;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.rexlmanu.ranks.database.activityprogress.Activity;
import lombok.RequiredArgsConstructor;
import org.bukkit.Server;
import org.bukkit.entity.Player;

@Singleton
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class ActivityTimePlayedTask implements Runnable {
  private final ActivityService activityService;
  private final Server server;

  @Override
  public void run() {
    for (Player onlinePlayer : this.server.getOnlinePlayers()) {
      this.activityService.addProgress(onlinePlayer, Activity.MINUTES_PLAYED, 1);
    }
  }
}
