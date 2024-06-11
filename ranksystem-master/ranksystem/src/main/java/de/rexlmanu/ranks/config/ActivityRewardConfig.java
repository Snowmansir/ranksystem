package de.rexlmanu.ranks.config;

import de.exlll.configlib.Comment;
import de.exlll.configlib.Configuration;
import de.rexlmanu.ranks.database.activityprogress.Activity;
import de.rexlmanu.ranks.user.activity.ActivityReward;
import de.rexlmanu.ranks.user.activity.RewardTrackingType;
import java.util.List;
import java.util.Map;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Configuration
public class ActivityRewardConfig {
  @Comment({
    "The rewards for each activity.",
    "The key is the activity, the value is a list of rewards.",
    "Possible activities: MINED_BLOCKS, FISH_CAUGHT, ANIMALS_BRED, WALKED_METERS, PLACED_BLOCKS, KILLED_ENTITIES, JUMPS, MINUTES_PLAYED"
  })
  Map<Activity, List<ActivityReward>> rewards =
      Map.of(
          Activity.MINED_BLOCKS,
          List.of(
              new ActivityReward(
                  RewardTrackingType.MILESTONE,
                  100,
                  List.of(
                      "give %player% diamond 1",
                      "say %player% has mined 100 blocks", "ranksadmin give %player% 100")),
              new ActivityReward(
                  RewardTrackingType.RECURRING, 10, List.of("say %player% has mined 10 blocks"))));
}
