package de.rexlmanu.ranks;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import de.rexlmanu.ranks.command.CommandContainer;
import de.rexlmanu.ranks.command.PointsCommand;
import de.rexlmanu.ranks.command.RankAdminCommand;
import de.rexlmanu.ranks.config.ActivityRewardConfig;
import de.rexlmanu.ranks.config.ConfigProvider;
import de.rexlmanu.ranks.config.PluginConfig;
import de.rexlmanu.ranks.config.message.MessageConfig;
import de.rexlmanu.ranks.database.DatabaseManager;
import de.rexlmanu.ranks.hooks.RanksPlaceholderExpansion;
import de.rexlmanu.ranks.listener.ExecuteCommandsForLevelupListener;
import de.rexlmanu.ranks.user.LevelUpHandler;
import de.rexlmanu.ranks.user.UserManager;
import de.rexlmanu.ranks.user.activity.ActivityTimePlayedTask;
import de.rexlmanu.ranks.user.activity.ActivityTrackerListener;
import java.util.Arrays;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
public class RanksPlugin extends JavaPlugin {
  private static final Logger log = LoggerFactory.getLogger(RanksPlugin.class);
  private final ConfigProvider configProvider = new ConfigProvider(this.getDataFolder().toPath());

  @Getter(AccessLevel.NONE)
  private Injector injector;

  @Inject private CommandContainer commandContainer;
  @Inject private DatabaseManager databaseManager;
  @Inject private UserManager userManager;

  @Override
  public void onEnable() {
    this.configProvider.register(PluginConfig.class, "config");
    this.configProvider.register(MessageConfig.class, "messages");
    this.configProvider.register(ActivityRewardConfig.class, "rewards");
    try {
      this.injector = Guice.createInjector(new RanksPluginModule(this));
      this.injector.injectMembers(this);
    } catch (Exception e) {
      log.error("Failed to enable plugin", e);
      this.getServer().getPluginManager().disablePlugin(this);
      return;
    }

    this.databaseManager.open();
    this.commandContainer.withCommands(RankAdminCommand.class, PointsCommand.class);

    this.registerListeners(LevelUpHandler.class, ActivityTrackerListener.class, UserManager.class, ExecuteCommandsForLevelupListener.class);

    Bukkit.getScheduler()
        .runTaskTimer(this, this.injector.getInstance(ActivityTimePlayedTask.class), 0, 20 * 60L);

    if (this.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
      this.injector.getInstance(RanksPlaceholderExpansion.class).register();
    }

    this.userManager.loadAll();
  }

  @Override
  public void onDisable() {
    // plugin didn't enable properly
    if (this.injector == null) {
      return;
    }
    this.userManager.saveAll();
    this.databaseManager.close();
  }

  @SafeVarargs
  private void registerListeners(Class<? extends Listener>... classes) {
    Arrays.stream(classes)
        .forEach(
            clazz ->
                this.getServer()
                    .getPluginManager()
                    .registerEvents(this.injector.getInstance(clazz), this));
  }
}
