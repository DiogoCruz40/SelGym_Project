package pt.selfgym.database.entities;

import static androidx.room.ForeignKey.CASCADE;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.sql.Timestamp;
import java.util.Date;

@Entity(tableName = "exercise_sets", foreignKeys = @ForeignKey(entity = ExerciseWO.class,
        parentColumns = "exerciseWOId",
        childColumns = "exerciseWO_id",
        onDelete = CASCADE
))
public class ExerciseSet {
    @PrimaryKey(autoGenerate = true)
    public Long exerciseSetId;

    //FK of exercises in a workout
    @ColumnInfo(name = "exerciseWO_id")
    @NonNull
    public Long exerciseWO_id;

    @ColumnInfo(name = "rest")
    @NonNull
    public String rest;

    @ColumnInfo(name = "variable")
    public Integer variable;

    @ColumnInfo(name = "weight")
    @NonNull
    public Double weight;

    @ColumnInfo(name = "order_set")
    @NonNull
    public Integer order_set;

}
