package de.rexlmanu.ranks.utils;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import lombok.RequiredArgsConstructor;
import org.bukkit.plugin.java.JavaPlugin;

@RequiredArgsConstructor
public class TaskScheduler {
  private final JavaPlugin plugin;

  public Executor syncExecutor() {
    return command -> this.plugin.getServer().getScheduler().runTask(this.plugin, command);
  }

  public Executor asyncExecutor() {
    return command ->
        this.plugin.getServer().getScheduler().runTaskAsynchronously(this.plugin, command);
  }

  public CompletableFuture<Void> run(Runnable runnable) {
    return CompletableFuture.runAsync(runnable, this.syncExecutor());
  }

  public CompletableFuture<Void> runAsync(Runnable runnable) {
    return CompletableFuture.runAsync(runnable, this.asyncExecutor());
  }
}
