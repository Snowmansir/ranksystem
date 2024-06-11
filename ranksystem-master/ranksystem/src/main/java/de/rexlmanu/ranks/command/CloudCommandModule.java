package de.rexlmanu.ranks.command;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.incendo.cloud.CommandManager;
import org.incendo.cloud.SenderMapper;
import org.incendo.cloud.annotations.AnnotationParser;
import org.incendo.cloud.bukkit.CloudBukkitCapabilities;
import org.incendo.cloud.execution.ExecutionCoordinator;
import org.incendo.cloud.injection.GuiceInjectionService;
import org.incendo.cloud.paper.PaperCommandManager;

public class CloudCommandModule extends AbstractModule {
  @Provides
  @Singleton
  public CommandManager<CommandSender> provideCommandManager(
      JavaPlugin javaPlugin, Injector injector) {

    PaperCommandManager<CommandSender> commandManager =
        new PaperCommandManager<>(
            javaPlugin, ExecutionCoordinator.asyncCoordinator(), SenderMapper.identity());

    if (commandManager.hasCapability(CloudBukkitCapabilities.NATIVE_BRIGADIER)) {
      commandManager.registerBrigadier();
    } else if (commandManager.hasCapability(CloudBukkitCapabilities.ASYNCHRONOUS_COMPLETION)) {
      commandManager.registerAsynchronousCompletions();
    }



    commandManager
        .parameterInjectorRegistry()
        .registerInjectionService(GuiceInjectionService.create(injector));

    return commandManager;
  }

  @Provides
  @Singleton
  @Inject
  public AnnotationParser<CommandSender> provideAnnotationParser(
      CommandManager<CommandSender> commandManager) {
    return new AnnotationParser<>(commandManager, CommandSender.class);
  }
}
