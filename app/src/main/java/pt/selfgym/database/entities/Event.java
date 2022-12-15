package pt.selfgym.database.entities;

import static androidx.room.ForeignKey.CASCADE;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.sql.Timestamp;
import java.util.Date;

@Entity(tableName = "events", foreignKeys = @ForeignKey(entity = Workout.class,
        parentColumns = "workoutId",
        childColumns = "workoutevents_id",
        onDelete = CASCADE
))
public class Event {
    @PrimaryKey(autoGenerate = true)
    public Long eventId;

    @ColumnInfo(name = "workoutevents_id")
    @NonNull
    public Long workoutevents_id;

    @ColumnInfo(name = "starts")
    @NonNull
    public String starts;

    @ColumnInfo(name = "event_date")
    @NonNull
    public String event_date;

    @ColumnInfo(name = "repeat")
    public String repeat;

    @ColumnInfo(name = "until")
    public String until;
}
