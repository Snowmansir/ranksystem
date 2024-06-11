package de.rexlmanu.ranks.database;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.field.DataPersisterManager;
import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.logger.Level;
import com.j256.ormlite.logger.Logger;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import de.rexlmanu.ranks.config.ConfigProvider;
import de.rexlmanu.ranks.config.PluginConfig;
import de.rexlmanu.ranks.database.activityprogress.ActivityProgressEntity;
import de.rexlmanu.ranks.database.user.UserDao;
import de.rexlmanu.ranks.database.user.UserEntity;
import java.sql.SQLException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Singleton
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class DatabaseManager {
  private final ConfigProvider configProvider;
  private ConnectionSource connectionSource;

  @Getter private UserDao userDao;
  @Getter private Dao<ActivityProgressEntity, Integer> activityProgressDao;

  public void open() {
    var config = this.configProvider.get(PluginConfig.class);
    try {
      this.connectionSource =
          new JdbcPooledConnectionSource(
              config.databaseUrl(), config.databaseUsername(), config.databasePassword());
    } catch (SQLException e) {
      log.error("Failed to open database connection", e);
      throw new RuntimeException("Failed to open database connection");
    }

    Logger.setGlobalLogLevel(Level.OFF);

    try {
      TableUtils.createTableIfNotExists(this.connectionSource, UserEntity.class);
      TableUtils.createTableIfNotExists(this.connectionSource, ActivityProgressEntity.class);

      this.userDao = DaoManager.createDao(this.connectionSource, UserEntity.class);
      this.activityProgressDao =
          DaoManager.createDao(this.connectionSource, ActivityProgressEntity.class);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

    DataPersisterManager.registerDataPersisters();
  }

  public void close() {
    try {
      this.connectionSource.close();
    } catch (Exception e) {
      log.error("Failed to close database connection", e);
    }
  }
}
