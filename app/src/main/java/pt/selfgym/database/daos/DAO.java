package pt.selfgym.database.daos;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.ArrayList;
import java.util.List;

import pt.selfgym.database.entities.Circuit;
import pt.selfgym.database.entities.Exercise;
import pt.selfgym.database.entities.ExerciseWO;
import pt.selfgym.database.entities.Workout;
import pt.selfgym.dtos.WorkoutDTO;

@Dao
public interface DAO {

    @Query("SELECT * FROM exercises")
    List<Exercise> getAllExercises();

    @Query("Select * from exercises where exerciseId = :id_exercise")
    Exercise getExercisebyId(int id_exercise);

    @Delete
    void deleteExercise(Exercise exercise);

    @Insert
    long insertexercise(Exercise exercise);

    /* @Query("SELECT * FROM Point WHERE id_note IN (:userIds)")
     List<Note> loadAllByIds(int[] userIds);

     @Query("SELECT * FROM Point WHERE title LIKE :titulo")
     Note findByTitle(String titulo);

     @Query("UPDATE Point SET title = :newTitle WHERE id_note = :id_nota")
     void updateTitle(int id_nota, String newTitle);

     @Query("UPDATE Point SET description = :newNote WHERE id_note = :id_nota")
     void updateNote(int id_nota, String newNote); */
    @Insert
    void insertworkout(Workout workout);

    @Insert
    long insertexwo(ExerciseWO exerciseWO);

    @Query("Select * from workouts")
    List<Workout> getAllworkouts();

    @Query("Select * from circuits where workoutcircuit_id = :id_workout")
    List<Circuit> getcircuitsbyworkout(Long id_workout);

    @Query("Select * from exerciseswo where circuitexwo_id=:id_circuit order by order_exwo asc,exerciseWOId asc")
    List<ExerciseWO> getExsWObycircuit(Long id_circuit);

    @Query("Select * from exerciseswo where workoutexwo_id=:id_workout ORDER BY order_exwo asc")
    List<ExerciseWO> getexercisesbyWorkoutId(Long id_workout);

    @Query("Select name_exercise,urltoimage,pushpulllegs,upperlowerbody from exerciseswo Join exercises on exercise_id=exercises.exerciseId where exerciseId=:id_exercise order by order_exwo asc, exerciseWOId asc")
    List<Exercise> getExercisesofcircuit(Long id_exercise);

//    @Query("Select exerciseSetId,sets,variable,weight,order_set from exercise_sets")

    // TODO: Buscar os Workouts a BD
    @Transaction
    default List<WorkoutDTO> getworkouts() {
        List<Workout> workouts = getAllworkouts();
        for (Workout workout : workouts) {
            List<Circuit> circuitList = getcircuitsbyworkout(workout.workoutId);
            List<ExerciseWO> exerciseWOList = getexercisesbyWorkoutId(workout.workoutId);
            for (ExerciseWO exerciseWO : exerciseWOList) {
                if (exerciseWO.circuitexwo_id != null) {
                    for (Circuit circuit : circuitList) {
                        List<ExerciseWO> exWOCircuitList = getExsWObycircuit(circuit.circuitId);
                        for (ExerciseWO exerciseWOcircuit : exWOCircuitList) {
                            List<Exercise> exerciseList = getExercisesofcircuit(exerciseWOcircuit.exercise_id);
                        }
                    }
                }
            }
        }
        return null;
    }


//    @Transaction
//    @Query("SELECT * FROM workouts")
//    List<WO_Circuits> getWorkoutsandCircuits();
//
//    @Transaction
//    @Query("SELECT * FROM workouts WHERE workoutId = :id_workout")
//    List<WO_Circuits> getWoandCircuits(int id_workout);

}
