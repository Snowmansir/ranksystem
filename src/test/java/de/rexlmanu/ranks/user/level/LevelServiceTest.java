package de.rexlmanu.ranks.user.level;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class LevelServiceTest {
  @Test
  void testCalculatingRank() {
    long baseRankPointCost = 500;
    double pointCostMultiplier = 1.25;

    Assertions.assertEquals(
        500, LevelService.calculateExperience(1, baseRankPointCost, pointCostMultiplier));
    Assertions.assertEquals(
        1125, LevelService.calculateExperience(2, baseRankPointCost, pointCostMultiplier));

    Assertions.assertEquals(
        0, LevelService.calculateLevel(0, baseRankPointCost, pointCostMultiplier));

    Assertions.assertEquals(
        1, LevelService.calculateLevel(500, baseRankPointCost, pointCostMultiplier));
    Assertions.assertEquals(
        2, LevelService.calculateLevel(1125, baseRankPointCost, pointCostMultiplier));
  }
}
