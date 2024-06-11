package de.rexlmanu.ranks.database.activityprogress;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import de.rexlmanu.ranks.database.user.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@DatabaseTable(tableName = "activity_progresses")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ActivityProgressEntity {
  @DatabaseField(generatedId = true, canBeNull = false)
  private int id;

  @EqualsAndHashCode.Exclude
  @DatabaseField(canBeNull = false, foreign = true, foreignAutoRefresh = true, foreignAutoCreate = true)
  private UserEntity user;

  @EqualsAndHashCode.Exclude
  @DatabaseField(canBeNull = false)
  private Activity activity;

  @EqualsAndHashCode.Exclude
  @DatabaseField(canBeNull = false)
  private String group;

  @EqualsAndHashCode.Exclude
  @DatabaseField(canBeNull = false)
  @Builder.Default
  private long progress = 0;
}
