package pt.selfgym.ui.statistics;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import pt.selfgym.database.AppDatabase;
import pt.selfgym.dtos.CircuitDTO;
import pt.selfgym.dtos.ExerciseDTO;
import pt.selfgym.dtos.ExerciseWODTO;
import pt.selfgym.dtos.WorkoutDTO;

public class StatisticsViewModel extends AndroidViewModel {

    private AppDatabase mDb;
    private final MutableLiveData<WorkoutDTO> workout = new MutableLiveData<WorkoutDTO>();
    private final MutableLiveData<List<CircuitDTO>> circuits = new MutableLiveData<List<CircuitDTO>>();
    private final MutableLiveData<List<ExerciseWODTO>> exerciseswo = new MutableLiveData<List<ExerciseWODTO>>();

    public StatisticsViewModel(@NonNull Application application) {
        super(application);
    }

    public WorkoutDTO getWorkout() {
        return workout.getValue();
    }

    public void setWorkout(WorkoutDTO workoutDTO) {
        workout.setValue(workoutDTO);
    }
    public List<CircuitDTO> getCircuits() {
        return circuits.getValue();
    }

    public List<ExerciseWODTO> getExerciseswo() {
        return exerciseswo.getValue();
    }

    public void newWorkout() {
        this.workout.setValue(new WorkoutDTO("", "", "full body", 0));
        this.exerciseswo.setValue(new ArrayList<ExerciseWODTO>());
        this.circuits.setValue(new ArrayList<CircuitDTO>());
    }

    public void updateworkoutparams(String name, String type, String obs) {
        this.workout.getValue().setName(name);
        this.workout.getValue().setType(type);
        this.workout.getValue().setObservation(obs);
    }

    public List<WorkoutDTO> getTop5Workouts(){



        return null;
    }

}
