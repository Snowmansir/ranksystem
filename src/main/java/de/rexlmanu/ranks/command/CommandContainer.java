package de.rexlmanu.ranks.command;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;
import org.incendo.cloud.CommandManager;
import org.incendo.cloud.annotations.AnnotationParser;

@Singleton
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class CommandContainer {
  private final Injector injector;
  private final CommandManager<CommandSender> commandManager;
  private final AnnotationParser<CommandSender> annotationParser;
  private final MiniMessage miniMessage;

  public CommandContainer withCommands(Class<?>... commandClasses) {
    for (Class<?> commandClass : commandClasses) {
      this.annotationParser.parse(this.injector.getInstance(commandClass));
    }
    return this;
  }
}
