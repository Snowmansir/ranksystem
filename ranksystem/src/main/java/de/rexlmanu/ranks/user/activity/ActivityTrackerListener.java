package de.rexlmanu.ranks.user.activity;

import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.rexlmanu.ranks.database.activityprogress.Activity;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityBreedEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerMoveEvent;

@Singleton
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class ActivityTrackerListener implements Listener {
  private final ActivityService activityService;

  @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
  public void handleBlockBreak(BlockBreakEvent event) {
    Player player = event.getPlayer();

    this.activityService.addProgress(player, Activity.MINED_BLOCKS, 1);
  }

  @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
  public void handleBlockPlace(BlockPlaceEvent event) {
    Player player = event.getPlayer();

    this.activityService.addProgress(player, Activity.PLACED_BLOCKS, 1);
  }

  @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
  public void onEntityDeath(EntityDeathEvent event) {
    Player killer = event.getEntity().getKiller();
    if (killer == null) return;

    this.activityService.addProgress(killer, Activity.KILLED_ENTITIES, 1);
  }

  @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
  public void onEntityBreed(EntityBreedEvent event) {
    if (!(event.getBreeder() instanceof Player player)) {
      return;
    }
    this.activityService.addProgress(player, Activity.ANIMALS_BRED, 1);
  }

  @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
  public void onPlayerFish(PlayerFishEvent event) {
    if (event.getState() != PlayerFishEvent.State.CAUGHT_FISH) {
      return;
    }
    this.activityService.addProgress(event.getPlayer(), Activity.FISH_CAUGHT, 1);
  }

  @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
  public void onPlayerJump(PlayerJumpEvent event) {
    this.activityService.addProgress(event.getPlayer(), Activity.JUMPS, 1);
  }

  @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
  public void onPlayerMove(PlayerMoveEvent event) {
    double distance = event.getFrom().distance(event.getTo());

    long meters = (long) (distance * 100D);

    this.activityService.addProgress(event.getPlayer(), Activity.WALKED_METERS, meters);
  }
}
