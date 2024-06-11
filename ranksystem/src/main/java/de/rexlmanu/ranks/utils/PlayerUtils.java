package de.rexlmanu.ranks.utils;

import lombok.experimental.UtilityClass;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@UtilityClass
public class PlayerUtils {
  public void giveOrDropItem(Player player, ItemStack itemStack) {
    if (player.getInventory().firstEmpty() == -1) {
      player.getWorld().dropItem(player.getLocation(), itemStack);
    } else {
      player.getInventory().addItem(itemStack);
    }
  }
}
