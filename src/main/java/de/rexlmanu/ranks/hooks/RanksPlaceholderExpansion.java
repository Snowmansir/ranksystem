package de.rexlmanu.ranks.hooks;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.rexlmanu.ranks.database.activityprogress.Activity;
import de.rexlmanu.ranks.user.UserService;
import de.rexlmanu.ranks.user.activity.ActivityService;
import lombok.RequiredArgsConstructor;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Singleton
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class RanksPlaceholderExpansion extends PlaceholderExpansion {
  private final JavaPlugin javaPlugin;
  private final UserService userService;
  private final ActivityService activityService;

  @Override
  public @NotNull String getIdentifier() {
    return "ranks";
  }

  @Override
  public @NotNull String getAuthor() {
    return "rexlManu";
  }

  @Override
  public @NotNull String getVersion() {
    return this.javaPlugin.getPluginMeta().getVersion();
  }

  @Override
  public @Nullable String onRequest(OfflinePlayer player, @NotNull String params) {
    if (player.isOnline() && player.getPlayer() != null) {
      Player onlinePlayer = player.getPlayer();
      if (params.equals("rank")) {
        return String.valueOf(this.userService.getRank(onlinePlayer));
      }

      if (params.equals("points")) {
        return String.valueOf(this.userService.getPoints(onlinePlayer));
      }

      if (params.equals("points_for_next_level")) {
        return String.valueOf(this.userService.requiredPointsForNextLevel(onlinePlayer));
      }

      if (params.equals("remaining_points_for_next_level")) {
        return String.valueOf(this.userService.remainingPointsForNextLevel(onlinePlayer));
      }

      if (params.startsWith("progress_")) {
        String activityName = params.substring("progress_".length());
        Activity activity = Activity.valueOf(activityName.toUpperCase());

        return String.valueOf(this.activityService.getProgress(onlinePlayer, activity));
      }
    }

    if (params.startsWith("leaderboard_")) {
      String[] args = params.split("_");
      int position = Integer.parseInt(args[1]);
      String type = args[2];

      if (type.equals("points")) {
        long points = this.userService.getLeaderboardPoints(position);
        return points == -1 ? "N/A" : String.valueOf(points);
      }

      if (type.equals("level")) {
        int level = this.userService.getLeaderboardLevel(position);
        return level == -1 ? "N/A" : String.valueOf(level);
      }

      if (type.equals("name")) {
        return this.userService.getLeaderboardName(position);
      }
    }

    return null;
  }
}
