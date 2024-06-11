package de.rexlmanu.ranks.config;

import de.exlll.configlib.Configuration;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Configuration
public class PluginConfig {
  String databaseUrl = "jdbc:sqlite:plugins/ranks/database.db";
  String databaseUsername = "root";
  String databasePassword = "";

  String serverGroup = "default";

  long baseLevelCost = 500;
  double levelCostMultiplier = 1.25;
  long maxPoints = 100000;

  List<String> levelUpCommands = List.of("say %player% has leveled up from %level-before% to %level-after%!");
}
