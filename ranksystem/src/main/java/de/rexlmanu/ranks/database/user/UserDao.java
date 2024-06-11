package de.rexlmanu.ranks.database.user;

import com.j256.ormlite.dao.Dao;
import java.util.UUID;

public interface UserDao extends Dao<UserEntity, Integer> {
  UserEntity findByPlayer(UUID playerId);
}
