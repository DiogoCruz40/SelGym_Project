package pt.selfgym.ui.workouts;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import pt.selfgym.database.AppDatabase;
import pt.selfgym.dtos.ExerciseDTO;
import pt.selfgym.dtos.WorkoutDTO;
import pt.selfgym.mappers.Mapper;
import pt.selfgym.services.AppExecutors;

public class WorkoutViewModel  extends AndroidViewModel {

    private AppDatabase mDb;
    private final MutableLiveData<List<ExerciseDTO>> exercisestempofwo = new MutableLiveData<List<ExerciseDTO>>();

    public WorkoutViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<List<ExerciseDTO>> getExercisesDTOs() {
        return exercisestempofwo;
    }

    public void addExercisetoWorkout(String exercisename)
    {
        mDb = AppDatabase.getInstance(getApplication().getApplicationContext());
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                //how to get all workouts
                Mapper mapper = new Mapper();
                ExerciseDTO exerciseDTO = mapper.toDTO(mDb.DAO().getExercisebyName(exercisename),ExerciseDTO.class);

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        List<ExerciseDTO> exerciseDTOList = exercisestempofwo.getValue();
                        if (exercisestempofwo == null) {
                            exerciseDTOList = new ArrayList<ExerciseDTO>();
                        }
                        exerciseDTOList.add(exerciseDTO);
                        exercisestempofwo.setValue(exerciseDTOList);
                    }
                });
            }
        });
    }
}
