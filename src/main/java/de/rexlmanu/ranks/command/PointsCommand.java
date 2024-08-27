package de.rexlmanu.ranks.command;

import com.google.inject.Inject;
import com.google.inject.Singleton;
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

@RequiredArgsConstructor(onConstructor = @__(@Inject))
@Singleton
public class PointsCommand {
  private final MessageManager messageManager;
  private final UserService userService;
  private final TaskScheduler taskScheduler;

  @Command(value = "points", requiredSender = Player.class)
  public void showOwnPoints(Player player) {
    this.taskScheduler.run(
        () ->
            this.messageManager.send(
                player,
                MessageConfig::pointsOwn,
                Formatter.number("points", this.userService.getPoints(player))));
  }

  @Command("points <player>")
  public void showPoints(CommandSender sender, @Argument("player") Player player) {
    this.taskScheduler.run(
        () ->
            this.messageManager.send(
                sender,
                MessageConfig::pointsOther,
                Placeholder.component("player", player.displayName()),
                Formatter.number("points", this.userService.getPoints(player))));
  }
}
