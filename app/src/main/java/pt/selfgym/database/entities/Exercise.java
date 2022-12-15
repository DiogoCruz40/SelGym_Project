package pt.selfgym.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;


@Entity(tableName = "exercises", indices = {@Index(value = {"name_exercise"}, unique = true)})
public class Exercise {

    @PrimaryKey(autoGenerate = true)
    public Long exerciseId;

    @ColumnInfo(name = "name_exercise")
    public String name_exercise;

    @ColumnInfo(name = "urltoimage")
    public String urltoimage;

    @ColumnInfo(name = "pushpulllegs")
    public String pushpulllegs;

    @ColumnInfo(name = "upperlowerbody")
    public String upperlowerbody;

}