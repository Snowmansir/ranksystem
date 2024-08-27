package de.rexlmanu.ranks.config.item;

import de.exlll.configlib.Configuration;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.bukkit.Material;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Configuration
@FieldDefaults(level = AccessLevel.PROTECTED)
public class ConfigItem {

  @Builder.Default
  String name = null;
  Material material;
  @Builder.Default
  List<String> lore = null;
  @Builder.Default
  String texture = null;
  @Builder.Default
  Integer amount = null;
  @Builder.Default
  String owner = null;
  @Builder.Default
  Integer customModelData = null;
}
