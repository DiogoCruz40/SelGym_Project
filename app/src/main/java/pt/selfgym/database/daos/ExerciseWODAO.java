package pt.selfgym.database.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;

import pt.selfgym.database.entities.ExerciseWO;

@Dao
public interface ExerciseWODAO {
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @Insert
    long insertexwo(ExerciseWO exerciseWO);

}
