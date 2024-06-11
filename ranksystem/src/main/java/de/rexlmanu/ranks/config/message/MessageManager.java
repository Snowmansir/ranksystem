package de.rexlmanu.ranks.config.message;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.rexlmanu.ranks.config.ConfigProvider;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

@RequiredArgsConstructor(onConstructor = @__(@Inject))
@Singleton
public class MessageManager {
  private final ConfigProvider configProvider;
  private final MiniMessage miniMessage;

  public void send(
      Audience audience, Function<MessageConfig, String> messageMapper, TagResolver... resolvers) {
    Component component = this.getComponent(messageMapper, resolvers);

    audience.sendMessage(component);
  }

  public Component getComponent(
      Function<MessageConfig, String> messageMapper, TagResolver... resolvers) {
    return this.miniMessage.deserialize(
        messageMapper.apply(this.configProvider.get(MessageConfig.class)), resolvers);
  }
}
