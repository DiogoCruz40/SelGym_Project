package pt.selfgym.database.entities;

import static androidx.room.ForeignKey.CASCADE;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.sql.Timestamp;

@Entity(tableName = "circuits", foreignKeys = {@ForeignKey(entity = Workout.class,
        parentColumns = "workoutId",
        childColumns = "workoutcircuit_id",
        onDelete = CASCADE
)})
public class Circuit {
    @PrimaryKey(autoGenerate = true)
    public Long circuitId;

    //FK of circuits in a workout
    @ColumnInfo(name = "workoutcircuit_id")
    @NonNull public Long workoutcircuit_id;

    @ColumnInfo(name = "laps")
    @NonNull
    public Integer laps;

    @ColumnInfo(name = "rest")
    @NonNull
    public String rest;
}
