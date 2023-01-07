package pt.selfgym;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;


import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import pt.selfgym.Interfaces.ActivityInterface;
import pt.selfgym.database.AppDatabase;
import pt.selfgym.database.entities.Exercise;
import pt.selfgym.database.entities.Workout;
import pt.selfgym.dtos.DateDTO;
import pt.selfgym.dtos.EventDTO;
import pt.selfgym.dtos.ExerciseDTO;
import pt.selfgym.dtos.ExerciseWODTO;
import pt.selfgym.dtos.WorkoutDTO;
import pt.selfgym.helpers.MQTTHelper;
import pt.selfgym.mappers.Mapper;
import pt.selfgym.services.AppExecutors;
import pt.selfgym.ui.workouts.EditWorkoutFragment;


public class SharedViewModel extends AndroidViewModel {

    private AppDatabase mDb;
    private final MutableLiveData<List<WorkoutDTO>> workouts = new MutableLiveData<List<WorkoutDTO>>();
    private final MutableLiveData<List<ExerciseDTO>> exercises = new MutableLiveData<List<ExerciseDTO>>();
    private final MutableLiveData<List<EventDTO>> events = new MutableLiveData<List<EventDTO>>();
    private final MutableLiveData<AtomicBoolean> getResultInsert = new MutableLiveData<AtomicBoolean>();
    private final MutableLiveData<AtomicBoolean> getResultUpdate = new MutableLiveData<AtomicBoolean>();
    private final MutableLiveData<String> toastMessageObserver = new MutableLiveData<String>();
    private final MutableLiveData<List<String>> topics = new MutableLiveData<List<String>>();
    private MQTTHelper mqttHelper;

    private final MutableLiveData<List<WorkoutDTO>> workoutsTop5 = new MutableLiveData<>();
    private final MutableLiveData<Dictionary<String, Integer>> stats = new MutableLiveData<>();


    public SharedViewModel(@NonNull Application application) {
        super(application);
        stats.setValue(new Hashtable<String, Integer>());
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

    public MutableLiveData<List<EventDTO>> getEventsCa() {
        return events;
    }

    public MutableLiveData<AtomicBoolean> getGetResultInsert() {
        return getResultInsert;
    }

    public MutableLiveData<AtomicBoolean> getGetResultUpdate() {
        return getResultUpdate;
    }

    public MutableLiveData<List<String>> getTopics() {
        return topics;
    }

    public void setTopics(List<String> topicslist) {
        topics.setValue(topicslist);
    }

    public MutableLiveData<List<WorkoutDTO>> getWorkoutsTop5() {
        return this.workoutsTop5;
    }

    public MutableLiveData<Dictionary<String, Integer>> getStats() {
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

                        insertStats();
                        top5Workouts();

                        if (exerciseDTOList.isEmpty()) {
                            insertExercise(new ExerciseDTO("burpies", "https://img.freepik.com/premium-vector/burpees-exercise-woman-workout-fitness-aerobic-exercises_476141-1406.jpg?w=826", "full body"));
                            insertExercise(new ExerciseDTO("squat", "data:image/jpeg;base64,/9j/4AAQSkZJRgABAQAAAQABAAD/2wCEAAkGBxEHBhERERMVEhUSFRUQFhYWEhYVGRAYGBIYGBcTFxUYHCggHh0nGxgWITIhJikrMC8uFyszODMtNygtLisBCgoKDg0OGhAQGy0mHyUvNS0tLTAvLS0tLS0tLS0tLS0rLS0rLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLS0tLf/AABEIALgBEgMBEQACEQEDEQH/xAAcAAEAAgMBAQEAAAAAAAAAAAAABQYDBAcCAQj/xABFEAACAQIEAgcDBwgJBQAAAAAAAQIDEQQFEiEGMQcTQVFhcZEigaEIFDJSgrHBI2NykqLR4fAVJTM0QkSys8IWJGJzo//EABoBAQADAQEBAAAAAAAAAAAAAAACAwQFAQb/xAAxEQEAAgIABAQDBwQDAAAAAAAAAQIDEQQSITETMkFhBSJRI3GBkaHB4RTR8PFCUrH/2gAMAwEAAhEDEQA/AO4gAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAUbjHjKeX4uWHw/szit5yg3u1tGmns2rq73t3c7UZcs16QvxYot1lQqXSJmmXY38pWjWs/ahKlBLyvCMX8RXJM9U5xV7OpcM8Z4XP6dJRloq1IyfVNO6cfpxUrWdlv323tztfHWIlmmNTMfRZA8AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAKzmeEhmvW0a0Yzpyb20SVu5qbfPxjyObe3zzMNtI+WHIOI+HquVUquqlJxpTSddtNVIzf5Nq7vfkmktmW0tue/4PZ7PnR1KVTjXBwXJVJVPSjO/7KZoraelfff6Kb1jrb11r9X6GLWcAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAialJxqtc93yXfujm5KTzzEQ21vE1iUFxFkceI8B1U5SppSUoyXZJbXceTW72ZGluWVltRCj9C+WOpxPXqyX93hKHlKctK+EZ+pvp3Zss9NO1lrOAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAYsVXjhaEpydklf+HmV5MlcdZtbtCVKWvblr3QnVddLVK93vZSaUPDbt8TkzXnnmt6+n0dHfLHLH+3nhXJaeU4vFyp/wCYqKs19X2bNL7WqX2jocLbcalk4jvErEamcAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAo/EWc/OcyUF/Z0pq/8A5NPd+7dHzPxDi/FzRWPLWfzn1/h3uC4Xkxc0+aY/KE3SqKrG6d1395uiYmNwxzWY6SzYCp/Waj+blL9qKX4l3DX+25faZ/WFWav2XN7/ALSlzpMQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABzXPMvnl+PkpLaTlKL7JK9/VXPkOL4e2HJMW7TuYfTcLnrlxxMd47t/hSXtVV+i/9RdwE9bR937qeNjyysmXQvmcpd1NL1m3+B1+Fr9tM+3/s/wAOZnt9lEe/7fylzpMQAAAAAAAAAAU3POknA5TiXTXWV5JuL6qMXGLXNapSSfuuQm8LIxWlvcMcb4PiSropOUKltXV1EoyaXNxs2nbwZ7Fol5ak1WQkgAAAAAAAAAAAAAAAAAAAAAARHEWDhjKMNd/Zbas7HP4/DXJWsWbeCyWpadIzLcDDCVJOF91Z3d+TMOLBXHMzVrzZbXiNp7LoJRk+12Xpy+9nU4WI1MudnnrENw1KAAAAAAAAABp5vBVMtqxblFShKLcbqSTVnpa3vbu3IZJmKzMJUjdocZ4x4P8A6KwnX4dylR2jKM09VG72leyvHs3V149mSl9zqWyVMo1nhsSpwe8HdNP+fFF9bamJQvXmrMOxdDOZ1MdlWJhKUpRpVvyeptuEZRuob9ity8S2J3M67bZ7xqI3311+90MkgAAAAAAAAAAAAAAAAAAAAAjs5nGFKN2le9rtK/IycVHSGnhvMj8NJSvZp+TMUNVknlleFWMoxkm4O0kmm43W112fwOhw3kYs/mbxoUgADzO+nbn2XPJ3ro9jW+rRWYNSs4q67LmT+ptE6mGjwI1uJbFPFxn4eZbXiKW9lVsVoZ001sXRMT2V60+noAYMatWGl/N12oqzRM0nSePXNG0POjB4ZwcY6GnFxsrNNbq3cc7bbrblHSdks8JWjiY6VSlow8YxjpdLTB2T7GrJ7+40Yr7jRaupXroay75lwiqj54ipKp9lewl+y37zXSOjHlndl8JqwAAAAAAAAAAAAAAAAAAAAHGvlCVr1cBT8K9T/aS/EjZdi9XzoP8AZynGPkuti/8A5q/wsYuI7w0VQ3Rfn8pdJ05J+zj5V1Jdj2nVpt+K02+0zVSNahVkjcO/lrMAAAHipRjVVpJMjasW7pVtNe0tSWWr/DKS8Huv3/Eotw1fRdHET6w150alB8vfH93Mz2w3p1j9FsXpf+XqljJLtT8z2vEXju8thq2qeNjLnsaK8RWe/RRbDaOzMq0ZLmvUt8Sk+qHJaPRq4qnSWHa28Lbv3FGSuOtJ0ux2yc0K5nGXSznLqlCpCCpzVrOT1JrdSTSsmnZ9pzIvnid1iPz6t2sUd5n8m/wDh5YHhqnQn9Kg50m/rWm5J/qyR1uHy+JSLa17OfnpyXmFiL1IAAAAAAAAAAAAAAAAAAAADifygl/WuBf5qt/rpkLLsXaW70a5dKh0d4iabjLEdfOLtySp9WmvfBsx5Z+eGiOyB6DMjlj+I/nbVqeFg7PvqVIOKj7oOTfmjZEdVOWdRp30mzgAAAAAAMNbDRqu7W/etn6kLY627wnXJavaWpVwMo/Rafg9viv3Ga3C/wDWV9c8f8oa0oyh9KEl5LUvgUWxXjvC2LVntMNSGZUJzsqkb8rN2+8zRmx71uF04bx6S21ui1W2sqmnKou5p+sV+418JbfNHv8Asz8RXXLP+d0gbGYAAAAAAAAAAAAAAAAAAAABxz5QlL2sBP8A98P9pkLLsU90jwxncafB2Fi9OmNClTk9kklGMZX9Kq84GG9fnlqiOi1dGmTRyTgrCU0rSnTjXqX5udRKTv5XUfKKOhHZivO7StB6gAAAAAAAAAAHL8yy2eXYxwn5xf11fZ3PjuJwWw3ml/w94fUcPmrlpFq/6Z8PmFWlBKM2vDZ+lyqvEZaRqLJWwY7dZhaOEYzUKrqJ3k4yu+b27vQ73wiL8t5v6zvq5HxKabrFPRYTsOYAAAAAAAAAAAAAAAAAAAAA5J8oOH/Y4GXdUqx9YRf/ABI2W4u8uQ0K1XEU44ZTloqThDTfa7lJLbzqT/WK9Rva/b9bUaapUoxXKKUV7lYuY3sAAAAAAAAAAAR2c5bDMKCcldwu47tea28jJxfC0z1+aOsdmnheItit0npKNwmFp0oJxik++2/qc6mHHTyw2ZMl7T80t/CT6usvHY14L8t/vZstd1Sh0WMAAAAAAAAAAAAAAAAAAAABy35QEE+HMJLtWK0+tCq/wRG3ZZjnq5JwlQ+c8VYKHfiaHwqxf4EF0zqJfq0tZQAAAAAAACPzvOsPkWBdbE1I0oJ2u+cn9WMVu34ITL2Imezj2ZdNeLlipfN8PQhTu1Hrdc5yV9m9EopNrs3t3shzLfCYqPTXjov28Phpfo9bH75Mc0nhR9W9R6caq+ngYP8ARxLX30mOY8L3Qed9K2LxaawtKGFUt22+ukv0W4qK/VZR4Nd7XzadRCn47O8XmMr1sRWm331ZWXlFOy9yLIrEdoebfoboz4pfFPDqnPatRao1e6UlFNVF4SW/nddhbE7Zr15ZW49QAAAAAAAAAAAAAAAAAAAArHSPkH/UfClWirKcXGtTk1fTOD81zi5Rv3SIXnVZlZi88Q5nwt0a43Ls0w+KqVKMZUasKvV3lK6jJNpzSsnbuuZ/6isS0TWZh3JcjWxvoAAAAAAAHE/lA6/6VwV79X1dTT3a9cdXvtoIWXYvVylLU7Ldvbz8CK18btKz2a5rtXuAAAAHa/k/4ScMrxlZ3UalSFOPc3CLcmv10vcTqpy94dYJKgAAAAAAAAAAAAAAAAAAAMdeHW0ZR7016o8tG4mHtZ1MSh6T1Uk/Bfccl0JTNF3pR8l9x1aTusMFu8vZJEAAAAAABB8X8MUeKsodCteLT1wmktVKaVtSvz2umu1M8mNpVtMSq3DvR1hOHK+t6q1aO6qTt7HjCK2Xnu/EwZr33yy2UmJ6wmI5dQr47q69GnVhU9m1SnGaT7Lal/Nzzh7avr6rM8c2PmjvDFjui/KcZ/lurf5qpOnb7Ken4HQ5YYIyWQWK6FMFUleniMTT8G6c0vWF/iecqXiy84XoTwcKqdTE16i+qurhfwbUW/Sw5TxZ+jo2VZbSyjAQoUIKnTprTGK7O1u73bbu23u2ySuZ33bYeAAAAAAAAAAAAAAAAAAAAGBBr6c13Tkv2m/xOXkjV5+9vjyx9yYw39hHyR0MXkhjv5pZCxAAAAAAAAA18XR62HiuXj4FObHzx07rMd+WUJjY2p6ls4+15WOd1iXQp16fVOYLExxeFjOLupK+3xXqdWtuaNudek0tNZZySAAAAAAAAAAAAAAAAAAAAAABixNZYehKbTair2irt+S7TyZ1G3ta80xCnZrxjOtLq8NTafLVON5e6G/x9DPbP6VdXD8PrHzZZ/CP7vXDkq+moq6ne6knKLV73vvbfsMl4nvKWfw+nh6/Bb8N/d4+R0MXkhysnmllLEAAAAAAAACA4gpYutLTQqRpxas7ppvvaqJO3oivJF58rXw18FeuSszP+ejxgcJUw2XxhVkptK11d3XZdvmzDlxzTuunLW95msaSOQYVYLLIwi20nLn2Xk3b4m3B5IZuJvNskzKRLWcAAAAAAAAAAAAAAAAAAAAAAAauIVKhFym1Fc227fE81EdXsbmdQq+J4qw6xemKnKP10vuT3t4mPPeto1Dp4eAyxHNPT2T+Q5lDMsK3C9oS0O6tfZO/xLuHndNfRj4nDOO/X1SZezgAAAAAAAEfmOc0Muv1lRJrfTzl6EZtEd5XY8GTJ5YU3NOMKmJrLqYqEE/8Su5+fcvL1MmXJzxr0dbB8PrSPnnc+yZ4QzmeY4mrCSStGM1pv32fN+XoS4ae8MvHYIx1rMLSa3NAAAAAAAAAAAAAAAAAAAAAAAFQzfhWtja7fziU4t6tM23pv3W2M18Vpnu6WDjqY48kRP1h9w3BNOFP25yk/C0V6WYjh/qX+JXmekQnMjyiOUUZRi5PU9XtNPst2JFuOkUhkz57ZpibeiSLFAAAAAAAABF5lkNDMquqpFauV1dP1RXbHW3dfi4nJjjVZ6PmDyChg/oQivFrU/WTYrirBk4nJfzS36GFhQk3FJN87JK/oTisQqm0z3lmPUQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA//Z", "lower body"));
                            insertExercise(new ExerciseDTO("muscle up", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTE_kSiKOtgF8bMDhmzGPRHKgPvlmzCKx542g&usqp=CAU", "upper body"));
                            insertExercise(new ExerciseDTO("push up", "https://img.freepik.com/free-vector/push-up-demostration_1133-392.jpg?w=740&t=st=1672230086~exp=1672230686~hmac=4a22e7705618b4ae843bdbd8e60754154839abd1d50864bd5b04fafb4bbef3b7", "push"));
                            insertExercise(new ExerciseDTO("bicep curl", "https://img.freepik.com/free-vector/dumbbell-curl-demostration_1133-363.jpg?w=740&t=st=1672230159~exp=1672230759~hmac=34d734f4663233db6082b2f106975114cdae3b4101afc311ad58d591e1891c77", "pull"));
                        } else {
                            exercises.setValue(exerciseDTOList);
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

        //TODO: insert statistics - MARIANA
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                //how insert a workout
                WorkoutDTO workoutDTOwithIds = mDb.DAO().insertWorkout(workoutDTO);
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        if (workoutDTOwithIds == null) {
                            getResultInsert.setValue(new AtomicBoolean(false));
                        } else {
                            List<WorkoutDTO> workoutDTOList = workouts.getValue();
                            if (workoutDTOList == null) {
                                workoutDTOList = new ArrayList<WorkoutDTO>();
                            }
                            workoutDTOList.add(workoutDTOwithIds);
                            getResultInsert.setValue(new AtomicBoolean(true));
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
                WorkoutDTO workoutDTOwithId = mDb.DAO().updateWorkout(workoutDTO);
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        if (workoutDTOwithId == null)
                            getResultUpdate.setValue(new AtomicBoolean(false));
                        else {
                            List<WorkoutDTO> workoutDTOList = workouts.getValue();
                            int i = 0;
                            for (WorkoutDTO workout : workoutDTOList) {
                                if (workout.getId().equals(workoutDTOwithId.getId())) {
                                    workoutDTOList.set(i, workoutDTOwithId);
                                }
                                i++;
                            }
                            getResultUpdate.setValue(new AtomicBoolean(true));
                            workouts.setValue(workoutDTOList);
                        }
                    }
                });
            }
        });
    }

    public void deleteWorkoutById(Long id_workout) {
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
                            List<WorkoutDTO> w = workoutDTOList.stream().filter(workoutDTO -> workoutDTO.getId() == id_workout).collect(Collectors.toList());
                            updateStats(w.get(0).getType(), null, w.get(0).getNrOfConclusions());

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
     * Events
     **/
    public void getEventsCalendar() {

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                //how to get all events
                List<EventDTO> eventDTOList = mDb.DAO().getEvents();

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        if (eventDTOList == null) {
                            events.setValue(new ArrayList<EventDTO>());
                        } else {
                            events.setValue(eventDTOList);
                        }

                    }
                });
            }
        });
    }

    public void setEventCalendar(EventDTO eventDTO) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                //how to get all events
                Mapper mapper = new Mapper();
                eventDTO.setEventId(mDb.DAO().setEvent(eventDTO));
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        List<EventDTO> eventDTOList = events.getValue();
                        if (eventDTOList == null) {
                            eventDTOList = new ArrayList<EventDTO>();
                        }
                        eventDTOList.add(eventDTO);
                        events.setValue(eventDTOList);
                    }
                });
            }
        });
    }

    public void updateEventCalendar(EventDTO eventDTO) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.DAO().updateEvent(eventDTO);
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        List<EventDTO> eventDTOList = events.getValue();
                        int i = 0;
                        for (EventDTO eDTO : eventDTOList) {
                            if (eDTO.getEventId().equals(eventDTO.getEventId())) {
                                eventDTOList.set(i, eventDTO);
                            }
                            i++;
                        }
                        events.setValue(eventDTOList);
                    }
                });
            }
        });
    }

    public void deleteEventCalendar(Long id_event) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                //how to get all events
                Mapper mapper = new Mapper();
                mDb.DAO().deleteEvent(id_event);
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        List<EventDTO> eventDTOList = events.getValue();
                        if (eventDTOList != null) {
                            eventDTOList = eventDTOList.stream().filter(eventDTO -> eventDTO.getEventId() != id_event).collect(Collectors.toList());
                            events.setValue(eventDTOList);
                        }

                    }
                });
            }
        });
    }

    /**************************************************************************************************************************/

    /**
     * MQTT
     **/
    public boolean checkStatemqtt() {
        if (mqttHelper != null)
            return mqttHelper.mqttAndroidClient.isConnected();
        return false;
    }

    public void connmqtt(ActivityInterface activityInterface) {
        mqttHelper = new MQTTHelper(getApplication().getApplicationContext(), MqttClient.generateClientId());

        mqttHelper.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {
                toastMessageObserver.setValue("MQTT conn successful");
                Log.w("mqtt", "connected");
            }

            @Override
            public void connectionLost(Throwable cause) {
                Log.w("mqtt", cause);
//                toastMessageObserver.setValue(cause.getMessage());
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                activityInterface.msgmqttpopup(topic, message);
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
        mqttHelper.connect();
    }

    public void disconmqtt() {
        mqttHelper.mqttAndroidClient.disconnect();
    }

    public boolean subscribeToTopic(String topic) {
        try {
            mqttHelper.subscribeToTopic(topic);
            List<String> listaux = topics.getValue();

            if (listaux == null)
                listaux = new ArrayList<String>();
            if (listaux.contains(topic))
                return false;

            listaux.add(topic);
            topics.setValue(listaux);
            return true;
        } catch (Exception e) {
            toastMessageObserver.setValue(e.getMessage());
            return false;
        }
    }

    public boolean unsubscribeToTopic(String topic) {
        try {
            mqttHelper.unsubscribeToTopic(topic);
            List<String> listaux = topics.getValue();
            listaux.remove(topic);
            topics.setValue(listaux);
            return true;
        } catch (Exception e) {
            toastMessageObserver.setValue(e.getMessage());
            return false;
        }
    }

    public void publishMessage(WorkoutDTO workoutDTO, String topic) {
        try {
            byte[] encodedPayload;
            String msg = (new Gson()).toJson(workoutDTO);
            Log.w("mqtt", msg);
            encodedPayload = msg.getBytes(StandardCharsets.UTF_8);
            MqttMessage message = new MqttMessage(encodedPayload);
            message.setQos(0);

            mqttHelper.mqttAndroidClient.publish(topic, message);
            // view set text to null
        } catch (Throwable e) {
            Log.w("mqtt", e.getMessage());
        }
    }
    /**************************************************************************************************************************/

    /**
     * Statistics
     **/
    public List<WorkoutDTO> top5Workouts() {

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

        return workoutsTop5.getValue();
    }

    public void insertStats() {

        int fb = 0, lb = 0, ub = 0, push = 0, pull = 0;
        for (WorkoutDTO w : Objects.requireNonNull(workouts.getValue())) {
            switch (w.getType()) {
                case "full body":
                    fb += w.getNrOfConclusions();
                    break;
                case "upper body":
                    ub += w.getNrOfConclusions();
                    break;
                case "lower body":
                    lb += w.getNrOfConclusions();
                    break;
                case "pull":
                    pull += w.getNrOfConclusions();
                    break;
                case "push":
                    push += w.getNrOfConclusions();
                    break;
                default:
                    break;
            }
        }

        Dictionary<String, Integer> statsNrs = new Hashtable<String, Integer>();
        statsNrs.put("Full Body", fb);
        statsNrs.put("Lower Body", lb);
        statsNrs.put("Upper Body", ub);
        statsNrs.put("Pull", pull);
        statsNrs.put("Push", push);

        stats.setValue(statsNrs);

    }

    public void updateStats(String old, String update, int oldNr) {

        if (old == null && update == null) {
            return;
        }

        if (old != null) {

            if (old.equals(update)) {
                return;
            }

            try {
                switch (old) {
                    case ("full body"):
                        stats.getValue().put("Full Body", (int) stats.getValue().get("Full Body") - oldNr);
                        break;
                    case ("upper body"):
                        stats.getValue().put("Upper Body", (int) stats.getValue().get("Upper Body") - oldNr);
                        break;
                    case ("lower body"):
                        stats.getValue().put("Lower Body", (int) stats.getValue().get("Lower Body") - oldNr);
                        break;
                    case ("pull"):
                        stats.getValue().put("Pull", (int) stats.getValue().get("Pull") - oldNr);
                        break;
                    case ("push"):
                        stats.getValue().put("Push", (int) stats.getValue().get("Push") - oldNr);
                        break;
                    default:
                        break;
                }
            } catch (NullPointerException e) {
                Log.w("updateStatsOld", e.getMessage());
            }
        }

        if (update != null) {
            try {
                switch (update) {
                    case ("full body"):
                        stats.getValue().put("Full Body", (int) stats.getValue().get("Full Body") + oldNr);
                        break;
                    case ("upper body"):
                        stats.getValue().put("Upper Body", (int) stats.getValue().get("Upper Body") + oldNr);
                        break;
                    case ("lower body"):
                        stats.getValue().put("Lower Body", (int) stats.getValue().get("Lower Body") + oldNr);
                        break;
                    case ("pull"):
                        stats.getValue().put("Pull", (int) stats.getValue().get("Pull") + oldNr);
                        break;
                    case ("push"):
                        stats.getValue().put("Push", (int) stats.getValue().get("Push") + oldNr);
                        break;
                    default:
                        break;
                }
            } catch (NullPointerException e) {
                Log.w("updateStatsUpdate", e.getMessage());
            }
        }
    }

    public void deleteAllWorkouts() {

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                // delete all workouts
                mDb.DAO().deleteAllWorkouts();
            }
        });
    }


}