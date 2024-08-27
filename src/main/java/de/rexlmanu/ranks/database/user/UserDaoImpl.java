package de.rexlmanu.ranks.database.user;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;
import java.sql.SQLException;
import java.util.UUID;

public class UserDaoImpl extends BaseDaoImpl<UserEntity, Integer> implements UserDao {
  public UserDaoImpl(ConnectionSource connectionSource) throws SQLException {
    super(connectionSource, UserEntity.class);
  }

  @Override
  public UserEntity findByPlayer(UUID playerId) {
    try {
      return this.queryForFirst(this.queryBuilder().where().eq("playerId", playerId).prepare());
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
