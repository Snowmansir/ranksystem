package de.rexlmanu.ranks.config.message;

import de.exlll.configlib.Configuration;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Configuration
public class MessageConfig {
  String pluginReloaded = "<gray>The plugin was <green>successfully</green> reloaded.";
  String adminPointsGiven =
      "<gray>You have given <green><player></green> <green><points></green> points.";

  String adminPointsRemoved =
      "<gray>You have removed <green><points></green> points from <green><player></green>.";

  String adminPointsSet =
      "<gray>You have set <green><player></green>'s points to <green><points></green>.";

  String rankedUp = "<gray>You have ranked up to <green><rank></green>!";

  String pointsOwn = "<gray>You have <green><points></green> points.";
  String pointsOther = "<gray><player> has <green><points></green> points.";
}
