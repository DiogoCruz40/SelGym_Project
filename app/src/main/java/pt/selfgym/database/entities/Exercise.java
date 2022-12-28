package pt.selfgym.database.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;


@Entity(tableName = "exercises", indices = {@Index(value = {"name_exercise"}, unique = true)})
public class Exercise {

    @PrimaryKey(autoGenerate = true)
    public Long exerciseId;

    @ColumnInfo(name = "name_exercise")
    @NonNull
    public String name_exercise;

    @ColumnInfo(name = "urlImage")
    public String urlImage;

    @ColumnInfo(name = "type")
    @NonNull
    public String type;
}
