package de.rexlmanu.ranks.hooks;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.rexlmanu.ranks.database.activityprogress.Activity;
import de.rexlmanu.ranks.user.UserService;
import de.rexlmanu.ranks.user.activity.ActivityService;
import lombok.RequiredArgsConstructor;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
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
  public @Nullable String onPlaceholderRequest(Player player, @NotNull String params) {
    if (player == null) return null;

    if (params.equals("rank")) {
      return String.valueOf(this.userService.getRank(player));
    }

    if (params.equals("points")) {
      return String.valueOf(this.userService.getPoints(player));
    }

    if (params.equals("points_for_next_level")) {
      return String.valueOf(this.userService.requiredPointsForNextLevel(player));
    }

    if (params.equals("remaining_points_for_next_level")) {
      return String.valueOf(this.userService.remainingPointsForNextLevel(player));
    }

    if (params.startsWith("progress_")) {
      String activityName = params.substring("progress_".length());
      Activity activity = Activity.valueOf(activityName.toUpperCase());

      return String.valueOf(this.activityService.getProgress(player, activity));
    }

    return null;
  }
}
