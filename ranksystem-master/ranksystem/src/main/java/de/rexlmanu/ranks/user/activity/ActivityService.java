package de.rexlmanu.ranks.user.activity;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.rexlmanu.ranks.config.ActivityRewardConfig;
import de.rexlmanu.ranks.config.ConfigProvider;
import de.rexlmanu.ranks.config.PluginConfig;
import de.rexlmanu.ranks.database.activityprogress.Activity;
import de.rexlmanu.ranks.database.activityprogress.ActivityProgressEntity;
import de.rexlmanu.ranks.database.user.UserEntity;
import de.rexlmanu.ranks.user.UserManager;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.bukkit.Server;
import org.bukkit.entity.Player;

@Singleton
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class ActivityService {
  private final Server server;
  private final UserManager userManager;
  private final ConfigProvider configProvider;

  public ActivityProgressEntity getOrCreate(Player player, Activity activity) {
    String group = this.configProvider.get(PluginConfig.class).serverGroup();

    UserEntity user = this.userManager.getUser(player);

    return user.activityProgresses().stream()
        .filter(entity -> entity.group().equals(group))
        .filter(entity -> entity.activity().equals(activity))
        .findFirst()
        .orElseGet(
            () -> {
              ActivityProgressEntity entity =
                  ActivityProgressEntity.builder()
                      .group(group)
                      .activity(activity)
                      .user(user)
                      .build();
              user.activityProgresses().add(entity);
              return entity;
            });
  }

  public void addProgress(Player player, Activity activity, long progress) {
    List<ActivityReward> rewards =
        this.configProvider
            .get(ActivityRewardConfig.class)
            .rewards()
            .getOrDefault(activity, List.of());

    ActivityProgressEntity entity = this.getOrCreate(player, activity);
    long progressBefore = entity.progress();
    long progressAfter = progressBefore + progress;
    entity.progress(progressAfter);

    for (ActivityReward reward : rewards) {
      switch (reward.type()) {
        case MILESTONE -> {
          if (progressBefore < reward.progress() && progressAfter >= reward.progress()) {
            this.giveReward(reward, player);
          }
        }

        case RECURRING -> {
          for (long i = progressBefore; i < progressAfter; i++) {
            if (i % reward.progress() == 0) {
              this.giveReward(reward, player);
            }
          }
        }
      }
    }
  }

  public long getProgress(Player player, Activity activity) {
    return this.getOrCreate(player, activity).progress();
  }

  private void giveReward(ActivityReward reward, Player player) {
    reward.commands().stream()
        .map(s -> s.replace("%player%", player.getName()))
        .forEach(s -> this.server.dispatchCommand(this.server.getConsoleSender(), s));
  }
}
