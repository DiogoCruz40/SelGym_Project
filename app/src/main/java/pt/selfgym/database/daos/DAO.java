package pt.selfgym.database.daos;

import androidx.core.content.res.TypedArrayUtils;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import pt.selfgym.database.entities.Circuit;
import pt.selfgym.database.entities.Event;
import pt.selfgym.database.entities.Exercise;
import pt.selfgym.database.entities.ExerciseSet;
import pt.selfgym.database.entities.ExerciseWO;
import pt.selfgym.database.entities.Workout;
import pt.selfgym.dtos.CircuitDTO;
import pt.selfgym.dtos.DateDTO;
import pt.selfgym.dtos.EventDTO;
import pt.selfgym.dtos.ExerciseDTO;
import pt.selfgym.dtos.ExerciseWODTO;
import pt.selfgym.dtos.SetsDTO;
import pt.selfgym.dtos.WorkoutDTO;
import pt.selfgym.mappers.Mapper;

@Dao
public interface DAO {

    /**
     * Exercises
     **/

    @Query("SELECT * FROM exercises")
    List<Exercise> getAllExercises();

    @Query("Select * from exercises where exerciseId = :id_exercise")
    Exercise getExercisebyId(long id_exercise);

    @Query("Select * from exercises where name_exercise = :exercise_name")
    Exercise getExercisebyName(String exercise_name);

    @Query("Delete from exercises where exerciseId = :id_exercise")
    void deleteExercisebyId(long id_exercise);

    @Delete
    void deleteExercise(Exercise exercise);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertexercise(Exercise exercise);

    @Query("SELECT * FROM workouts ORDER BY nrofconclusions DESC LIMIT 5")
    List<Workout> getTop5Workouts();

    /*********************************************************************/

    /* @Query("SELECT * FROM Point WHERE id_note IN (:userIds)")
     List<Note> loadAllByIds(int[] userIds);

     @Query("SELECT * FROM Point WHERE title LIKE :titulo")
     Note findByTitle(String titulo);

     @Query("UPDATE Point SET title = :newTitle WHERE id_note = :id_nota")
     void updateTitle(int id_nota, String newTitle);

     @Query("UPDATE Point SET description = :newNote WHERE id_note = :id_nota")
     void updateNote(int id_nota, String newNote); */

    //    @Transaction
//    @Query("SELECT * FROM workouts")
//    List<WO_Circuits> getWorkoutsandCircuits();
//
//    @Transaction
//    @Query("SELECT * FROM workouts WHERE workoutId = :id_workout")
//    List<WO_Circuits> getWoandCircuits(int id_workout);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertwo(Workout workout);

    @Update
    void updatewo(Workout workout);

    @Query("Delete from workouts where workoutId=:id_workout")
    void deletewo(Long id_workout);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertexwo(ExerciseWO exerciseWO);

    @Update
    void updatexwo(ExerciseWO exerciseWO);

    @Query("Delete from exerciseswo where workoutexwo_id=:id_workout")
    void deletexswo(Long id_workout);

    @Query("Delete from events where workoutevents_id=:id_workout")
    void deleteeventsofwo(Long id_workout);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertset(ExerciseSet exerciseSet);

    @Update
    void updateset(ExerciseSet exerciseSet);

    @Query("Delete from exercise_sets where exerciseWO_id=:id_exwo")
    void deletesetsofexwo(Long id_exwo);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertcircuit(Circuit circuit);

    @Update
    void updatecircuit(Circuit circuit);

    @Query("Delete from circuits where workoutcircuit_id=:id_workout")
    void deletecircuits(Long id_workout);

    @Query("Select * from workouts")
    List<Workout> getAllworkouts();

    @Query("Select * from events")
    List<Event> getAllEvents();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertEvent(Event event);

    @Update
    void updateevent(Event event);

    @Query("Delete from events where eventId=:id_event")
    void deleteEvent(Long id_event);

    @Query("Select * from workouts where workoutId=:id_workout")
    Workout getWorkout(Long id_workout);

    @Query("Select * from circuits where workoutcircuit_id = :id_workout")
    List<Circuit> getcircuitsbyworkout(Long id_workout);

    @Query("Select * from exerciseswo where circuitexwo_id=:id_circuit order by order_exwo asc,exerciseWOId asc")
    List<ExerciseWO> getExsWObycircuit(Long id_circuit);

    @Query("Select * from exerciseswo where workoutexwo_id=:id_workout ORDER BY order_exwo asc,exerciseWOId asc")
    List<ExerciseWO> getexercisesbyWorkoutId(Long id_workout);

    @Query("Select * from exercise_sets where exerciseWO_id=:id_exwo ORDER BY order_set asc,exerciseSetId asc")
    List<ExerciseSet> getSetsExWO(Long id_exwo);

    @Query("DELETE FROM workouts")
    void deleteAllWorkouts();

    @Transaction
    default List<WorkoutDTO> getworkouts() {
        List<Workout> workouts = getAllworkouts();
        List<WorkoutDTO> workoutDTOList = new ArrayList<WorkoutDTO>();

        for (Workout workout : workouts) {
            WorkoutDTO workoutDTO = (new Mapper()).toDTO(workout, WorkoutDTO.class);
            List<Circuit> circuitList = getcircuitsbyworkout(workout.workoutId);
            List<ExerciseWO> exerciseWOList = getexercisesbyWorkoutId(workout.workoutId);
            List<Long> addedIdsCircuits = new ArrayList<Long>();

            for (ExerciseWO exerciseWO : exerciseWOList) {
                if (exerciseWO.circuitexwo_id == null) {
                    ExerciseWODTO exerciseWODTO = (new Mapper()).toDTO(exerciseWO, ExerciseWODTO.class);
                    exerciseWODTO.setExercise((new Mapper()).toDTO(getExercisebyId(exerciseWO.exercise_id), ExerciseDTO.class));
                    exerciseWODTO.setSetsList((new Mapper()).toDTOs(getSetsExWO(exerciseWO.exerciseWOId), SetsDTO.class));
                    workoutDTO.addToWorkoutComposition(exerciseWODTO);
                } else {
                    if (!addedIdsCircuits.contains(exerciseWO.circuitexwo_id)) {
                        addedIdsCircuits.add(exerciseWO.circuitexwo_id);

                        for (Circuit circuit : circuitList) {
                            if (circuit.circuitId == exerciseWO.circuitexwo_id) {
                                CircuitDTO circuitDTO = (new Mapper()).toDTO(circuit, CircuitDTO.class);

                                List<ExerciseWO> exWOCircuitList = getExsWObycircuit(circuit.circuitId);
                                for (ExerciseWO exerciseWOCircuit : exWOCircuitList) {
                                    ExerciseWODTO exerciseWODTO = (new Mapper()).toDTO(exerciseWOCircuit, ExerciseWODTO.class);
                                    exerciseWODTO.setExercise((new Mapper()).toDTO(getExercisebyId(exerciseWOCircuit.exercise_id), ExerciseDTO.class));
                                    exerciseWODTO.setSetsList((new Mapper()).toDTOs(getSetsExWO(exerciseWOCircuit.exerciseWOId), SetsDTO.class));
                                    circuitDTO.addToExerciseList(exerciseWODTO);
                                }

                                workoutDTO.addToWorkoutComposition(circuitDTO);
                                break;
                            }
                        }
                    }
                }
            }
            workoutDTOList.add(workoutDTO);
        }
        return workoutDTOList;
    }


    @Transaction
    default WorkoutDTO insertWorkout(WorkoutDTO workoutDTO) {
        try {
            List<Workout> workouts = getAllworkouts();
            for (Workout workout : workouts) {
                if (workout.name_wo.equalsIgnoreCase(workoutDTO.getName()))
                    return null;
            }
            Long id_workout = insertwo((new Mapper()).toEntity(workoutDTO, Workout.class));
            workoutDTO.setId(id_workout);
            if (workoutDTO.getWorkoutComposition() != null) {
                workoutDTO.getWorkoutComposition().forEach(obj -> {
                    if (obj instanceof ExerciseWODTO) {
                        ExerciseWO exerciseWO = (new Mapper()).toEntity(obj, ExerciseWO.class);
                        exerciseWO.workoutexwo_id = id_workout;
                        exerciseWO.exercise_id = ((ExerciseWODTO) obj).getExercise().getId();
                        Long id_exwo = insertexwo(exerciseWO);
                        ((ExerciseWODTO) obj).setId(id_exwo);

                        List<SetsDTO> setsDTOList = ((ExerciseWODTO) obj).getSetsList();
                        if (setsDTOList != null) {
                            setsDTOList.forEach(setsDTO -> {
                                ExerciseSet exerciseSet = (new Mapper()).toEntity(setsDTO, ExerciseSet.class);
                                exerciseSet.exerciseWO_id = id_exwo;
                                Long id_set = insertset(exerciseSet);
                                setsDTO.setId(id_set);
                            });
                        }
                    } else {
                        Circuit circuit = (new Mapper()).toEntity(obj, Circuit.class);
                        circuit.workoutcircuit_id = id_workout;
                        Long id_circuit = insertcircuit(circuit);
                        ((CircuitDTO) obj).setId(id_circuit);

                        if (((CircuitDTO) obj).getExerciseList() != null) {
                            ((CircuitDTO) obj).getExerciseList().forEach(exerciseWODTO -> {
                                ExerciseWO exerciseWO = (new Mapper()).toEntity(exerciseWODTO, ExerciseWO.class);
                                exerciseWO.workoutexwo_id = id_workout;
                                exerciseWO.exercise_id = exerciseWODTO.getExercise().getId();
                                exerciseWO.circuitexwo_id = id_circuit;
                                Long id_exwo = insertexwo(exerciseWO);
                                exerciseWODTO.setId(id_exwo);

                                if (exerciseWODTO.getSetsList() != null) {
                                    exerciseWODTO.getSetsList().forEach(setsDTO -> {
                                        ExerciseSet exerciseSet = (new Mapper()).toEntity(setsDTO, ExerciseSet.class);
                                        exerciseSet.exerciseWO_id = id_exwo;
                                        Long id_set = insertset(exerciseSet);
                                        setsDTO.setId(id_set);
                                    });
                                }
                            });
                        }

                    }
                });
            }
            return workoutDTO;
        } catch (Exception e) {
            return null;
        }

    }

    @Transaction
    default WorkoutDTO updateWorkout(WorkoutDTO workoutDTO) {
        List<Workout> workouts = getAllworkouts();
        for (Workout workout : workouts) {
            if (workout.name_wo.equalsIgnoreCase(workoutDTO.getName()) && !workout.workoutId.equals(workoutDTO.getId()))
                return null;
        }
        updatewo((new Mapper()).toEntity(workoutDTO, Workout.class));
        List<ExerciseWO> exerciseWOList = getexercisesbyWorkoutId(workoutDTO.getId());
        exerciseWOList.forEach(exerciseWO -> {
            deletesetsofexwo(exerciseWO.exerciseWOId);
        });
        deletexswo(workoutDTO.getId());
        deletecircuits(workoutDTO.getId());
        if (workoutDTO.getWorkoutComposition() != null) {
            workoutDTO.getWorkoutComposition().forEach(obj -> {
                if (obj instanceof ExerciseWODTO) {
                    ExerciseWO exerciseWO = (new Mapper()).toEntity(obj, ExerciseWO.class);
                    exerciseWO.workoutexwo_id = workoutDTO.getId();
                    exerciseWO.exercise_id = ((ExerciseWODTO) obj).getExercise().getId();
                    Long id_exwo = insertexwo(exerciseWO);
                    ((ExerciseWODTO) obj).setId(id_exwo);

                    List<SetsDTO> setsDTOList = ((ExerciseWODTO) obj).getSetsList();
                    if (setsDTOList != null) {
                        setsDTOList.forEach(setsDTO -> {
                            ExerciseSet exerciseSet = (new Mapper()).toEntity(setsDTO, ExerciseSet.class);
                            exerciseSet.exerciseWO_id = id_exwo;
                            Long id_set = insertset(exerciseSet);
                            setsDTO.setId(id_set);
                        });
                    }
                } else {
                    Circuit circuit = (new Mapper()).toEntity(obj, Circuit.class);
                    circuit.workoutcircuit_id = workoutDTO.getId();
                    Long id_circuit = insertcircuit(circuit);
                    ((CircuitDTO) obj).setId(id_circuit);

                    if (((CircuitDTO) obj).getExerciseList() != null) {
                        ((CircuitDTO) obj).getExerciseList().forEach(exerciseWODTO -> {
                            ExerciseWO exerciseWO = (new Mapper()).toEntity(exerciseWODTO, ExerciseWO.class);
                            exerciseWO.workoutexwo_id = workoutDTO.getId();
                            exerciseWO.exercise_id = exerciseWODTO.getExercise().getId();
                            exerciseWO.circuitexwo_id = id_circuit;
                            Long id_exwo = insertexwo(exerciseWO);
                            exerciseWODTO.setId(id_exwo);

                            if (exerciseWODTO.getSetsList() != null) {
                                exerciseWODTO.getSetsList().forEach(setsDTO -> {
                                    ExerciseSet exerciseSet = (new Mapper()).toEntity(setsDTO, ExerciseSet.class);
                                    exerciseSet.exerciseWO_id = id_exwo;
                                    Long id_set = insertset(exerciseSet);
                                    setsDTO.setId(id_set);
                                });
                            }
                        });
                    }

                }
            });
        }

        return workoutDTO;
    }

    @Transaction
    default void deleteWorkout(Long id_workout) {
        List<ExerciseWO> exerciseWOList = getexercisesbyWorkoutId(id_workout);
        exerciseWOList.forEach(exerciseWO -> {
            deletesetsofexwo(exerciseWO.exerciseWOId);
        });
        deletexswo(id_workout);
        deletecircuits(id_workout);
        deleteeventsofwo(id_workout);
        deletewo(id_workout);
    }


    @Transaction
    default WorkoutDTO getWorkoutbyId(Long id_workout) {
        Workout workout = getWorkout(id_workout);
        WorkoutDTO workoutDTO = (new Mapper()).toDTO(workout, WorkoutDTO.class);
        List<Circuit> circuitList = getcircuitsbyworkout(workout.workoutId);
        List<ExerciseWO> exerciseWOList = getexercisesbyWorkoutId(workout.workoutId);
        List<Long> addedIdsCircuits = new ArrayList<Long>();

        for (ExerciseWO exerciseWO : exerciseWOList) {
            if (exerciseWO.circuitexwo_id == null) {
                ExerciseWODTO exerciseWODTO = (new Mapper()).toDTO(exerciseWO, ExerciseWODTO.class);
                exerciseWODTO.setExercise((new Mapper()).toDTO(getExercisebyId(exerciseWO.exercise_id), ExerciseDTO.class));
                exerciseWODTO.setSetsList((new Mapper()).toDTOs(getSetsExWO(exerciseWO.exerciseWOId), SetsDTO.class));
                workoutDTO.addToWorkoutComposition(exerciseWODTO);
            } else {
                if (!addedIdsCircuits.contains(exerciseWO.circuitexwo_id)) {
                    addedIdsCircuits.add(exerciseWO.circuitexwo_id);

                    for (Circuit circuit : circuitList) {
                        if (circuit.circuitId == exerciseWO.circuitexwo_id) {
                            CircuitDTO circuitDTO = (new Mapper()).toDTO(circuit, CircuitDTO.class);

                            List<ExerciseWO> exWOCircuitList = getExsWObycircuit(circuit.circuitId);
                            for (ExerciseWO exerciseWOCircuit : exWOCircuitList) {
                                ExerciseWODTO exerciseWODTO = (new Mapper()).toDTO(exerciseWOCircuit, ExerciseWODTO.class);
                                exerciseWODTO.setExercise((new Mapper()).toDTO(getExercisebyId(exerciseWOCircuit.exercise_id), ExerciseDTO.class));
                                exerciseWODTO.setSetsList((new Mapper()).toDTOs(getSetsExWO(exerciseWOCircuit.exerciseWOId), SetsDTO.class));
                                circuitDTO.addToExerciseList(exerciseWODTO);
                            }

                            workoutDTO.addToWorkoutComposition(circuitDTO);
                            break;
                        }
                    }
                }
            }
        }
        return workoutDTO;
    }

    @Transaction
    default List<EventDTO> getEvents() {
        List<EventDTO> eventDTOList = new ArrayList<EventDTO>();
        List<Event> eventList = getAllEvents();
        if (eventList != null) {
            for (Event event : eventList) {
                WorkoutDTO workoutDTO = getWorkoutbyId(event.workoutevents_id);
                EventDTO eventDTO = (new Mapper()).toDTO(event, EventDTO.class);
                String[] values = event.events_date.split("-");
                DateDTO dateDTO = new DateDTO(Integer.parseInt(values[0]), Integer.parseInt(values[1]), Integer.parseInt(values[2]));
                eventDTO.setDate(dateDTO);
                eventDTO.setWorkoutDTO(workoutDTO);
                eventDTOList.add(eventDTO);
            }
        }

        return eventDTOList;
    }


    @Transaction
    default Long setEvent(EventDTO eventDTO) {

        Event event = (new Mapper()).toEntity(eventDTO, Event.class);
        event.workoutevents_id = eventDTO.getWorkoutDTO().getId();
        event.events_date = eventDTO.getDate().getDay() + "-" + eventDTO.getDate().getMonth() + "-" + eventDTO.getDate().getYear();
        return insertEvent(event);
    }

    @Transaction
    default void updateEvent(EventDTO eventDTO) {

        Event event = (new Mapper()).toEntity(eventDTO, Event.class);
        event.workoutevents_id = eventDTO.getWorkoutDTO().getId();
        event.events_date = eventDTO.getDate().getDay() + "-" + eventDTO.getDate().getMonth() + "-" + eventDTO.getDate().getYear();
        updateevent(event);
    }
}
