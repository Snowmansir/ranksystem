package de.rexlmanu.ranks.config;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.exlll.configlib.ConfigLib;
import de.exlll.configlib.YamlConfigurations;
import de.rexlmanu.ranks.config.item.ItemFactory;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@RequiredArgsConstructor
@Singleton
public class ConfigProvider {
  private final Map<Class<?>, String> configNames = new HashMap<>();
  private final Map<Class<?>, Object> loadedConfigs = new HashMap<>();
  private final Path dataDirectory;

  @Inject private ItemFactory itemFactory;

  @SneakyThrows
  @Inject
  void init() {
    Files.createDirectories(this.dataDirectory);
  }

  public void register(Class<?> configClass, String name) {
    var path = this.dataDirectory.resolve(name + ".yml");

    this.configNames.put(configClass, name);
    this.loadedConfigs.put(
        configClass,
        YamlConfigurations.update(
            path,
            configClass,
            ConfigLib.BUKKIT_DEFAULT_PROPERTIES.toBuilder()
                .charset(StandardCharsets.UTF_8)
                .build()));
  }

  public <T> T get(Class<T> configClass) {
    var config = this.loadedConfigs.get(configClass);
    if (config == null) {
      throw new IllegalStateException("Config not loaded: " + configClass.getSimpleName());
    }
    return configClass.cast(config);
  }

  public void reload() {
    this.configNames.forEach(
        (configClass, name) -> {
          var path = this.dataDirectory.resolve(name + ".yml");
          this.loadedConfigs.put(
              configClass,
              YamlConfigurations.update(
                  path,
                  configClass,
                  ConfigLib.BUKKIT_DEFAULT_PROPERTIES.toBuilder()
                      .charset(StandardCharsets.UTF_8)
                      .build()));
        });
  }
}
