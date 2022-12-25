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
import java.util.Dictionary;
import java.util.Hashtable;
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

    //TODO: ver isto melhor
    private final MutableLiveData<List<WorkoutDTO>> workoutsTop5 = new MutableLiveData<>();
    private final MutableLiveData<Dictionary> stats = new MutableLiveData<Dictionary>();
    private final MutableLiveData<Integer> nrFB = new MutableLiveData<>();
    private final MutableLiveData<Integer> nrUB = new MutableLiveData<>();
    private final MutableLiveData<Integer> nrLB = new MutableLiveData<>();
    private final MutableLiveData<Integer> nrPush = new MutableLiveData<>();
    private final MutableLiveData<Integer> nrPull = new MutableLiveData<>();


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

    public MutableLiveData<List<WorkoutDTO>> getWorkoutsTop5() {
        return this.workoutsTop5;
    }

    public MutableLiveData<Integer> getNrFB() {
        return this.nrFB;
    }

    public MutableLiveData<Integer> getNrLB() {
        return this.nrLB;
    }

    public MutableLiveData<Integer> getNrUB() {
        return this.nrUB;
    }

    public MutableLiveData<Integer> getNrPush() {
        return this.nrPush;
    }

    public MutableLiveData<Integer> getNrPull() {
        return this.nrPull;
    }

    /*public MutableLiveData<List<Integer>> getStats() {
        return this.stats;
    }*/

    public MutableLiveData<Dictionary> getStats() {
        System.out.println("FUCK " + this.stats.getValue().size());
        return this.stats;
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
                        if (exerciseDTOList.isEmpty()) {
                            insertExercise(new ExerciseDTO("burpies", "", "full body"));
                            insertExercise(new ExerciseDTO("squat", "", "lower body"));
                            insertExercise(new ExerciseDTO("muscle up", "", "upper body"));
                            insertExercise(new ExerciseDTO("push up", "", "push"));
                            insertExercise(new ExerciseDTO("bicep curl", "", "pull"));
                        } else {
                            exercises.setValue(exerciseDTOList);
                        }
                    }
                });
            }
        });

        //insertStats();
    }

    /**
     * Workouts
     **/
    public void insertWorkout(WorkoutDTO workoutDTO) {

        //TODO: insert statistics - MARIANA
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
        //TODO: update statistics - MARIANA
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

    /**
     * Statistics
     **/
    public void Top5Workouts() {

        //TODO: isto não devido ser necessário
        if(mDb == null){
            mDb = AppDatabase.getInstance(getApplication().getApplicationContext());
        }

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                Mapper mapper = new Mapper();
                List<WorkoutDTO> top5Workouts = mapper.toDTOs(mDb.DAO().getTop5Workouts(), WorkoutDTO.class);
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        if (top5Workouts == null) {
                            workoutsTop5.setValue(new ArrayList<WorkoutDTO>());
                        } else
                            workoutsTop5.setValue(top5Workouts);
                    }
                });
            }
        });
    }

    public void insertStats (){

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        int fb = 0, lb = 0, ub = 0, push = 0, pull = 0;
                        for(WorkoutDTO w: Objects.requireNonNull(workouts.getValue())){
                            switch (w.getType()){
                                case "full body":
                                    fb++;
                                    break;
                                case "upper body":
                                    ub++;
                                    break;
                                case "lower body":
                                    lb++;
                                    break;
                                case "pull":
                                    pull++;
                                    break;
                                case "push":
                                    push++;
                                    break;
                                default:
                                    break;
                            }
                        }

                        Dictionary<String, Integer> statsNrs = new Hashtable<String, Integer>();
                        statsNrs.put("fb", fb);
                        statsNrs.put("lb", lb);
                        statsNrs.put("ub", ub);
                        statsNrs.put("pull", pull);
                        statsNrs.put("push", push);

                        stats.setValue(statsNrs);

                        System.out.println("TESTE1 FB: " + stats.getValue().get("fb"));
                        stats.getValue().put("fb", (int) stats.getValue().get("fb") + 1);
                        System.out.println("TESTE2 FB: " + stats.getValue().get("fb"));
                    }
                });
            }
        });

        /*
        int fb = 0, lb = 0, ub = 0, push = 0, pull = 0;
        for(WorkoutDTO w: Objects.requireNonNull(workouts.getValue())){
            switch (w.getType()){
                case "full body":
                    fb++;
                    break;
                case "upper body":
                    ub++;
                    break;
                case "lower body":
                    lb++;
                    break;
                case "pull":
                    pull++;
                    break;
                case "push":
                    push++;
                    break;
                default:
                    break;
            }
        }

        Dictionary<String, Integer> statsNrs = new Hashtable<String, Integer>();
        statsNrs.put("fb", fb);
        statsNrs.put("lb", lb);
        statsNrs.put("ub", ub);
        statsNrs.put("pull", pull);
        statsNrs.put("push", push);

        stats.setValue(statsNrs);

        System.out.println("TESTE1 FB: " + stats.getValue().get("fb"));
        stats.getValue().put("fb", (int) stats.getValue().get("fb") + 1);
        System.out.println("TESTE2 FB: " + stats.getValue().get("fb"));*/


        /*ArrayList<Integer> statsNr = new ArrayList<>();
        statsNr.add(fb);
        statsNr.add(ub);
        statsNr.add(lb);
        statsNr.add(pull);
        statsNr.add(push);*/

        /*nrFB.setValue(fb);
        nrLB.setValue(lb);
        nrUB.setValue(ub);
        nrPull.setValue(pull);
        nrPush.setValue(push);*/
    }

    public void updateStats (String old, String update){
        //TODO: change this to dictionary
        if (old != null){
            try{
                switch (old){
                    case ("nrFB"):
                        nrFB.setValue(nrFB.getValue()-1);
                        break;
                    case ("nrUB"):
                        nrUB.setValue(nrUB.getValue()-1);
                        break;
                    case ("nrLB"):
                        nrLB.setValue(nrLB.getValue()-1);
                        break;
                    case ("nrPull"):
                        nrPull.setValue(nrPull.getValue()-1);
                        break;
                    case ("nrPush"):
                        nrPush.setValue(nrPush.getValue()-1);
                        break;
                }
            }
            catch (NullPointerException e){
                Log.w("updateStatsOld",e.getMessage());
            }
        }
        if (update != null){
            try{
                switch (update){
                    case ("nrFB"):
                        nrFB.setValue(nrFB.getValue()+1);
                        break;
                    case ("nrUB"):
                        nrUB.setValue(nrUB.getValue()+1);
                        break;
                    case ("nrLB"):
                        nrLB.setValue(nrLB.getValue()+1);
                        break;
                    case ("nrPull"):
                        nrPull.setValue(nrPull.getValue()+1);
                        break;
                    case ("nrPush"):
                        nrPush.setValue(nrPush.getValue()+1);
                        break;
                }
            }
            catch (NullPointerException e){
                Log.w("updateStatsUpdate",e.getMessage());
            }
        }
    }



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