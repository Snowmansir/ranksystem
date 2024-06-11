package de.rexlmanu.ranks;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.AbstractModule;
import de.rexlmanu.ranks.command.CloudCommandModule;
import de.rexlmanu.ranks.config.ConfigProvider;
import de.rexlmanu.ranks.utils.TaskScheduler;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Server;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;

@RequiredArgsConstructor
public class RanksPluginModule extends AbstractModule {
  private final RanksPlugin plugin;

  @Override
  protected void configure() {
    this.install(new CloudCommandModule());
    this.bind(RanksPlugin.class).toInstance(this.plugin);
    this.bind(JavaPlugin.class).toInstance(this.plugin);
    this.bind(Logger.class).toInstance(this.plugin.getSLF4JLogger());
    this.bind(Gson.class).toInstance(new GsonBuilder().serializeNulls().create());
    this.bind(MiniMessage.class).toInstance(MiniMessage.miniMessage());
    this.bind(Server.class).toInstance(this.plugin.getServer());
    this.bind(TaskScheduler.class).toInstance(new TaskScheduler(this.plugin));
    this.bind(ConfigProvider.class).toInstance(this.plugin.configProvider());
    this.bind(ServicesManager.class).toInstance(this.plugin.getServer().getServicesManager());

    this.requestInjection(this.plugin.configProvider());
  }
}
