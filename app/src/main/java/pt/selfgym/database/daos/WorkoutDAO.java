package pt.selfgym.database.daos;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import pt.selfgym.database.entities.Circuit;
import pt.selfgym.database.entities.ExerciseWO;
import pt.selfgym.database.entities.Workout;
import pt.selfgym.database.relations.WO_Circuits;

@Dao
public interface WorkoutDAO {
    @Insert
    void insert(Workout workout);

    @Transaction
    @Query("SELECT * FROM workouts")
    List<WO_Circuits> getWorkoutsandCircuits();

    @Transaction
    @Query("SELECT * FROM workouts WHERE workoutId = :id_workout")
    List<WO_Circuits> getWoandCircuits(int id_workout);

}
