package pt.selfgym.database.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "workouts")
public class Workout {

    @PrimaryKey(autoGenerate = true)
    public Long workoutId;

    @ColumnInfo(name = "name_wo")
    @NonNull
    public String name_wo;

    @ColumnInfo(name = "observations")
    public String observations;

    @ColumnInfo(name = "nrofconclusions")
    public Integer nrofconclusions;

    @ColumnInfo(name = "type")
    @NonNull
    public String type;

}
