package pt.selfgym.ui.statistics;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import pt.selfgym.Interfaces.ActivityInterface;
import pt.selfgym.R;
import pt.selfgym.SharedViewModel;
import pt.selfgym.dtos.CircuitDTO;
import pt.selfgym.dtos.ExerciseWODTO;
import pt.selfgym.dtos.WorkoutDTO;
import pt.selfgym.ui.workouts.AddExerciseFragment;
import pt.selfgym.ui.workouts.EditAdapter;
import pt.selfgym.ui.workouts.WorkoutFragment;


public class EditStatisticsFragment extends Fragment {

    private ActivityInterface activityInterface;
    private SharedViewModel mViewModel;
    private StatisticsViewModel statisticsViewModel;

    //private EditAdapter adapter;
    //private EditText editTextNote, observations, name;
    //private ImageButton addExercise;
    //private Spinner type;
    private View view;
    private Long id;
    private WorkoutDTO workout;


    public EditStatisticsFragment() {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activityInterface = (ActivityInterface) context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.edit_workout_fragment, container, false);

        this.mViewModel = new ViewModelProvider(activityInterface.getMainActivity()).get(SharedViewModel.class);
        this.statisticsViewModel = new ViewModelProvider(activityInterface.getMainActivity()).get(StatisticsViewModel.class);

        if (getArguments() != null) {

            id = getArguments().getLong("id");
            Log.w("id", String.valueOf(id));

            for (WorkoutDTO w : mViewModel.getWorkouts().getValue())
                if (Objects.equals(w.getId(), id)) {
                    statisticsViewModel.setWorkout(w);
                    break;
                }

            //comment this if needed
//            workout = new WorkoutDTO("olá", "hell", "upper body");
//            ExerciseWODTO exercise1 = new ExerciseWODTO(1, 0.0, 1, 1, 0, new ExerciseDTO("hell", "sodqodmpaod", "sidno"));
//            ExerciseWODTO exercise2 = new ExerciseWODTO(2, 0.0, 1, 0, new ExerciseDTO("hell", "sodqodmpaod", "sidno"), 1);
//            SetsDTO setsDTO1 = new SetsDTO(1, 1, 1, 1);
//            SetsDTO setsDTO2 = new SetsDTO(1, 1, 1, 2);
//            ArrayList<SetsDTO> sets = new ArrayList<SetsDTO>();
//            sets.add(setsDTO1);
//            sets.add(setsDTO2);
//            ExerciseWODTO exercise3 = new ExerciseWODTO(3, 1, new ExerciseDTO("hell", "sodqodmpaod", "sidno"), sets);
//            ExerciseWODTO exercise4 = new ExerciseWODTO(4, new ExerciseDTO("hell", "sodqodmpaod", "sidno"), 1, sets);
//            ArrayList<ExerciseWODTO> circuitComposition = new ArrayList<ExerciseWODTO>();
//            circuitComposition.add(exercise1);
//            circuitComposition.add(exercise2);
//            circuitComposition.add(exercise3);
//            circuitComposition.add(exercise4);
//            CircuitDTO circuit = new CircuitDTO(5, 0, circuitComposition);
//            ArrayList<Object> workoutComposition = new ArrayList<Object>();
//            workoutComposition.add(exercise1);
//            workoutComposition.add(exercise2);
//            workoutComposition.add(exercise3);
//            workoutComposition.add(exercise4);
//            workoutComposition.add(circuit);
//            workout.setWorkoutComposition(workoutComposition);
        }

        /*

        workout = statisticsViewModel.getWorkout();
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.exercises);
        adapter = new EditAdapter(workout, getContext());
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(inflater.getContext()));
        recyclerView.setAdapter(adapter);

        name = (EditText) view.findViewById(R.id.editWorkoutName);
        name.setText(workout.getName());

        type = (Spinner) view.findViewById(R.id.workoutTypeSpinner);
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(activityInterface.getMainActivity(), android.R.layout.simple_spinner_item, Arrays.asList("full body", "upper body", "lowerbody", "push", "pull"));
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        type.setAdapter(typeAdapter);
        if (workout.getType() == "full body") {
            type.setSelection(0);
        } else if (workout.getType() == "upper body") {
            type.setSelection(1);
        } else if (workout.getType() == "lower body") {
            type.setSelection(2);
        } else if (workout.getType() == "push") {
            type.setSelection(3);
        } else if (workout.getType() == "pull") {
            type.setSelection(4);
        }

        observations = (EditText) view.findViewById(R.id.textAreaObservations);
        observations.setText(workout.getObservation());*/

//        view.setFocusableInTouchMode(true);
//        view.requestFocus();
//        view.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if (keyCode == KeyEvent.KEYCODE_BACK) {
//                    activityInterface.changeFrag(new WorkoutFragment(), null);
//                    return true;
//                }
//                return false;
//            }
//        });

        /*
        addExercise = (ImageButton) view.findViewById(R.id.addExercise);
        addExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addExerciseCircuitPopup();
            }
        });*/

        return view;
    }

    public void addExerciseCircuitPopup() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activityInterface.getMainActivity());
        final View AddExerciseCircuitPopup = getLayoutInflater().inflate(R.layout.popup_add_exercise_circuit, null);

        Button cancel = (Button) AddExerciseCircuitPopup.findViewById(R.id.cancelButton);
        Button addExerciseOption = (Button) AddExerciseCircuitPopup.findViewById(R.id.addExerciseOption);
        Button addCircuitOption = (Button) AddExerciseCircuitPopup.findViewById(R.id.addCircuitOption);

        dialogBuilder.setView(AddExerciseCircuitPopup);
        Dialog dialog = dialogBuilder.create();
        dialog.show();

        addExerciseOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //workoutViewModel.updateworkoutparams(name.getText().toString(), type.getSelectedItem().toString(), observations.getText().toString());
                activityInterface.changeFrag(new AddExerciseFragment(), null);
                dialog.dismiss();
            }
        });

        addCircuitOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                workout.addToWorkoutComposition(new CircuitDTO(0, 0, new ArrayList<ExerciseWODTO>()));
                //adapter.setWorkout(workout);
                dialog.dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }


}