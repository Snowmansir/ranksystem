package de.rexlmanu.ranks.command;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.rexlmanu.ranks.config.ConfigProvider;
import de.rexlmanu.ranks.config.message.MessageConfig;
import de.rexlmanu.ranks.config.message.MessageManager;
import de.rexlmanu.ranks.user.UserService;
import de.rexlmanu.ranks.utils.TaskScheduler;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.minimessage.tag.resolver.Formatter;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.cloud.annotations.Argument;
import org.incendo.cloud.annotations.Command;
import org.incendo.cloud.annotations.Permission;

@RequiredArgsConstructor(onConstructor = @__(@Inject))
@Singleton
public class RankAdminCommand {
  private final ConfigProvider configProvider;
  private final MessageManager messageManager;
  private final UserService userService;
  private final TaskScheduler taskScheduler;

  @Command("rankadmin reload")
  @Permission("ranks.command.rankadmin.reload")
  public void reload(CommandSender sender) {
    this.configProvider.reload();
    this.messageManager.send(sender, MessageConfig::pluginReloaded);
  }

  @Command("rankadmin give <player> <points>")
  @Permission("ranks.command.rankadmin.give")
  public void givePoints(
      CommandSender sender, @Argument("player") Player player, @Argument("points") long points) {
    this.taskScheduler.run(() -> this.userService.giveExperience(player, points));
    this.messageManager.send(
        sender,
        MessageConfig::adminPointsGiven,
        Placeholder.component("player", player.displayName()),
        Formatter.number("points", points));
  }

  @Command("rankadmin remove <player> <points>")
  @Permission("ranks.command.rankadmin.remove")
  public void removePoints(
      CommandSender sender, @Argument("player") Player player, @Argument("points") long points) {
    this.taskScheduler.run(() -> this.userService.removeExperience(player, points));
    this.messageManager.send(
        sender,
        MessageConfig::adminPointsRemoved,
        Placeholder.component("player", player.displayName()),
        Formatter.number("points", points));
  }

  @Command("rankadmin set <player> <points>")
  @Permission("ranks.command.rankadmin.set")
  public void setPoints(
      CommandSender sender, @Argument("player") Player player, @Argument("points") long points) {
    this.taskScheduler.run(() -> this.userService.setExperience(player, points));
    this.messageManager.send(
        sender,
        MessageConfig::adminPointsSet,
        Placeholder.component("player", player.displayName()),
        Formatter.number("points", points));
  }
}
