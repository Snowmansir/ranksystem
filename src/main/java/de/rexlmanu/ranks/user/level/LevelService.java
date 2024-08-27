package de.rexlmanu.ranks.user.level;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.rexlmanu.ranks.config.ConfigProvider;
import de.rexlmanu.ranks.config.PluginConfig;
import lombok.RequiredArgsConstructor;

@Singleton
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class LevelService {
  private final ConfigProvider configProvider;

  public int calculateLevel(long experience) {
    PluginConfig config = this.configProvider.get(PluginConfig.class);

    long baseCost = config.baseLevelCost();
    double multiplier = config.levelCostMultiplier();

    return calculateLevel(experience, baseCost, multiplier) + 1;
  }

  public long calculateExperience(int level) {
    PluginConfig config = this.configProvider.get(PluginConfig.class);

    long baseCost = config.baseLevelCost();
    double multiplier = config.levelCostMultiplier();

    return calculateExperience(level - 1, baseCost, multiplier);
  }

  public static int calculateLevel(long experience, long baseCost, double multiplier) {
    if (experience < 0) {
      throw new IllegalArgumentException("Experience must be non-negative.");
    }

    int level = 0;
    int accumulatedExperience = 0;

    while (accumulatedExperience <= experience) {
      level++;
      accumulatedExperience += (int) (baseCost * Math.pow(multiplier, level - 1));
    }

    // If the loop goes one level too far, return the previous level
    return level - 1;
  }

  public static long calculateExperience(int level, long baseCost, double multiplier) {
    if (level < 1) {
      throw new IllegalArgumentException("Level must be 1 or higher.");
    }

    int experience = 0;
    for (int i = 1; i <= level; i++) {
      experience += (int) (baseCost * Math.pow(multiplier, i - 1));
    }

    return experience;
  }
}
