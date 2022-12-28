package pt.selfgym.database.entities;

import static androidx.room.ForeignKey.CASCADE;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.sql.Timestamp;
import java.util.Date;

import pt.selfgym.dtos.DateDTO;

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

    @ColumnInfo(name = "events_date")
    @NonNull
    public String events_date;

    @ColumnInfo(name = "hour")
    public Integer hour;

    @ColumnInfo(name = "minute")
    public Integer minute;

    @ColumnInfo(name = "concluded")
    public Boolean concluded;

    @ColumnInfo(name = "repetitionNr")
    public Integer repetitionNr;

    @ColumnInfo(name = "recurrence")
    public String recurrence;
}

