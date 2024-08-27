package de.rexlmanu.ranks.database.user;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import de.rexlmanu.ranks.database.activityprogress.ActivityProgressEntity;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@DatabaseTable(tableName = "users", daoClass = UserDaoImpl.class)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserEntity {
  @DatabaseField(generatedId = true, canBeNull = false)
  private int id;

  @EqualsAndHashCode.Exclude
  @DatabaseField(canBeNull = false, index = true)
  private String username;

  @DatabaseField(canBeNull = false, index = true)
  private UUID playerId;

  @DatabaseField(canBeNull = false)
  @Builder.Default
  private long experience = 0;

  @EqualsAndHashCode.Exclude
  @ForeignCollectionField(eager = true, foreignFieldName = "user")
  private ForeignCollection<ActivityProgressEntity> activityProgresses;
}
