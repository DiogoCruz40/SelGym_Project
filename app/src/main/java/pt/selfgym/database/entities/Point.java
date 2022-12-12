package pt.selfgym.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "points")
public class Point {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "timestamp")
    public String timestamp;

    @ColumnInfo(name = "temperature")
    public Float temperature;

    @ColumnInfo(name = "humidity")
    public Float humidity;

}

