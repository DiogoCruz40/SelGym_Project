package pt.selfgym;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;


import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import pt.selfgym.database.AppDatabase;
import pt.selfgym.database.entities.Exercise;
import pt.selfgym.dtos.ExerciseDTO;
import pt.selfgym.dtos.ExerciseWODTO;
import pt.selfgym.dtos.WorkoutDTO;
import pt.selfgym.mappers.Mapper;
import pt.selfgym.services.AppExecutors;


public class SharedViewModel extends AndroidViewModel {

    private AppDatabase mDb;
    private final MutableLiveData<List<WorkoutDTO>> workouts = new MutableLiveData<List<WorkoutDTO>>();
    private final MutableLiveData<List<ExerciseDTO>> exercises = new MutableLiveData<List<ExerciseDTO>>();
    private final MutableLiveData<String> toastMessageObserver = new MutableLiveData<String>();


    public SharedViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<String> getToastMessageObserver() {
        return this.toastMessageObserver;
    }

    public MutableLiveData<List<WorkoutDTO>> getWorkouts() {
        return workouts;
    }

    public MutableLiveData<List<ExerciseDTO>> getExercises() {
        return exercises;
    }

    public void startDB() {
        mDb = AppDatabase.getInstance(getApplication().getApplicationContext());

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                //how to get all workouts
                Mapper mapper = new Mapper();
                List<WorkoutDTO> workoutDTOList = mDb.DAO().getworkouts();
                List<ExerciseDTO> exerciseDTOList = mapper.toDTOs(mDb.DAO().getAllExercises(), ExerciseDTO.class);
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        if (workoutDTOList == null) {
                            workouts.setValue(new ArrayList<WorkoutDTO>());
                        } else {
                            workouts.setValue(workoutDTOList);
                        }
                        if (exerciseDTOList == null) {
                            exercises.setValue(new ArrayList<ExerciseDTO>());
                        } else {
                            exercises.setValue(exerciseDTOList);
                        }
                        if (exercises.getValue().isEmpty()){
                            ArrayList<ExerciseDTO> newExerciseDTOList = new ArrayList<ExerciseDTO>();
                            newExerciseDTOList.add(new ExerciseDTO("bicep curl","","pull"));
                            newExerciseDTOList.add(new ExerciseDTO("push up","","push"));
                            newExerciseDTOList.add(new ExerciseDTO("muscle up","","upper body"));
                            newExerciseDTOList.add(new ExerciseDTO("squat","","lower body"));
                            newExerciseDTOList.add(new ExerciseDTO("burpies","","full body"));
                            exercises.setValue(newExerciseDTOList);
                        }
                    }
                });
            }
        });
    }

    /**
     * Workouts
     **/
    public void insertWorkout(WorkoutDTO workoutDTO) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                //how insert a workout
                WorkoutDTO workoutDTOwithIds = mDb.DAO().insertWorkout(workoutDTO);
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        if (workoutDTOwithIds != null) {
                            List<WorkoutDTO> workoutDTOList = workouts.getValue();
                            if (workoutDTOList == null) {
                                workoutDTOList = new ArrayList<WorkoutDTO>();
                            }
                            workoutDTOList.add(workoutDTOwithIds);
                            workouts.setValue(workoutDTOList);
                        }
                    }
                });
            }
        });
    }

    public void updateWorkout(WorkoutDTO workoutDTO) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                //how update a workout
                WorkoutDTO workoutDTOwithIds = mDb.DAO().updateWorkout(workoutDTO);
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        if (workoutDTOwithIds != null) {
                            List<WorkoutDTO> workoutDTOList = workouts.getValue();
                            workoutDTOList.forEach(workout -> {
                                if (workout.getId().equals(workoutDTOwithIds.getId())) {
                                    workout = workoutDTOwithIds;
                                }
                            });
                            workouts.setValue(workoutDTOList);
                        }
                    }
                });
            }
        });
    }

    public void deleteWorkoutbyId(Long id_workout) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                //how delete a workout
                mDb.DAO().deleteWorkout(id_workout);
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        List<WorkoutDTO> workoutDTOList = workouts.getValue();
                        if (workoutDTOList != null) {
                            workoutDTOList = workoutDTOList.stream().filter(workoutDTO -> workoutDTO.getId() != id_workout).collect(Collectors.toList());
                            workouts.setValue(workoutDTOList);
                        }
                    }
                });
            }
        });
    }

    /**************************************************************************************************************************/

    /**
     * EXERCISES
     **/
    public void insertExercise(ExerciseDTO exerciseDTO) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                //how insert a exercise
                Mapper mapper = new Mapper();
                exerciseDTO.setId(mDb.DAO().insertexercise(mapper.toEntity(exerciseDTO, Exercise.class)));
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        List<ExerciseDTO> exerciseDTOList = exercises.getValue();
                        if (exerciseDTOList == null) {
                            exerciseDTOList = new ArrayList<ExerciseDTO>();
                        }
                        exerciseDTOList.add(exerciseDTO);
                        exercises.setValue(exerciseDTOList);
                    }
                });
            }
        });
    }

    public void getAllExercises() {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                //how to get all exercises
                Mapper mapper = new Mapper();
                List<ExerciseDTO> exerciseDTOList = mapper.toDTOs(mDb.DAO().getAllExercises(), ExerciseDTO.class);
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        if (exerciseDTOList == null) {
                            exercises.setValue(new ArrayList<ExerciseDTO>());
                        } else
                            exercises.setValue(exerciseDTOList);
                    }
                });
            }
        });
    }

    public void deleteExercisebyId(Long id_exercise) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                //how delete a exercise
                mDb.DAO().deleteExercisebyId(id_exercise);

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        List<ExerciseDTO> exerciseDTOList = exercises.getValue();
                        if (exerciseDTOList != null) {
                            exerciseDTOList = exerciseDTOList.stream().filter(exerciseDTO -> exerciseDTO.getId() != id_exercise).collect(Collectors.toList());
                            exercises.setValue(exerciseDTOList);
                        }
                    }
                });
            }
        });
    }
    /**************************************************************************************************************************/

//    public void deleteAllPoints() {
//        AppExecutors.getInstance().diskIO().execute(new Runnable() {
//            @Override
//            public void run() {
//                // delete all notes
//                List<Point> pointsDB = mDb.pointsDAO().getAll();
//
//                for (Point n : pointsDB) {
//                    mDb.pointsDAO().delete(n);
//                }
//
//            }
//        });
//    }
}