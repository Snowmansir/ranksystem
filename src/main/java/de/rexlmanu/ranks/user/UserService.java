package de.rexlmanu.ranks.user;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.rexlmanu.ranks.config.ConfigProvider;
import de.rexlmanu.ranks.config.PluginConfig;
import de.rexlmanu.ranks.database.user.UserDao;
import de.rexlmanu.ranks.database.user.UserEntity;
import de.rexlmanu.ranks.event.UserLevelUpEvent;
import de.rexlmanu.ranks.user.level.LevelService;
import java.sql.SQLException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

@Singleton
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class UserService {
  private final UserManager userManager;
  private final LevelService levelService;
  private final ConfigProvider configProvider;

  public long pointsLimit() {
    return this.configProvider.get(PluginConfig.class).maxPoints();
  }

  public void giveExperience(Player player, long experience) {
    UserEntity user = this.userManager.getUser(player);
    long beforeExperience = user.experience();
    long afterExperience = Math.min(beforeExperience + experience, this.pointsLimit());
    user.experience(afterExperience);

    this.checkForLevelUp(player, beforeExperience, afterExperience, user);
  }

  private void checkForLevelUp(
      Player player, long beforeExperience, long afterExperience, UserEntity user) {
    int levelBefore = this.levelService.calculateLevel(beforeExperience);
    int levelAfter = this.levelService.calculateLevel(afterExperience);

    // handle level up
    if (levelBefore < levelAfter) {
      new UserLevelUpEvent(player, user, levelBefore, levelAfter).callEvent();
    }
  }

  public void setExperience(Player player, long experience) {
    UserEntity user = this.userManager.getUser(player);
    long beforeExperience = user.experience();
    user.experience(Math.min(experience, this.pointsLimit()));

    this.checkForLevelUp(player, beforeExperience, user.experience(), user);
  }

  public void removeExperience(Player player, long experience) {
    UserEntity user = this.userManager.getUser(player);
    long beforeExperience = user.experience();
    long afterExperience = beforeExperience - experience;
    user.experience(Math.max(afterExperience, 0));
  }

  public long getPoints(Player player) {
    return this.userManager.getUser(player).experience();
  }

  public long requiredPointsForNextLevel(Player player) {
    long points = this.levelService.calculateExperience(this.getRank(player) + 1);
    return Math.min(points, this.pointsLimit());
  }

  public long remainingPointsForNextLevel(Player player) {
    return this.requiredPointsForNextLevel(player) - this.getPoints(player);
  }

  public int getRank(Player player) {
    return this.levelService.calculateLevel(this.getPoints(player));
  }

  public long getLeaderboardPoints(long position) {
    return this.getLeaderboardUser(position).map(UserEntity::experience).orElse(-1L);
  }

  public String getLeaderboardName(long position) {
    return this.getLeaderboardUser(position).map(UserEntity::username).orElse("N/A");
  }

  public Optional<UserEntity> getLeaderboardUser(long position) {
    UserDao userDao = this.userManager.databaseManager().userDao();

    // get the user with the given position in the leaderboard based on total experience

    try {
      return Optional.ofNullable(
          userDao
              .queryBuilder()
              .orderBy("experience", false)
              .offset(position - 1)
              .limit(1L)
              .queryForFirst());
    } catch (SQLException e) {
      return Optional.empty();
    }
  }

  public int getLeaderboardLevel(int position) {
    return this.getLeaderboardUser(position).map(user -> this.levelService.calculateLevel(user.experience())).orElse(-1);
  }
}
