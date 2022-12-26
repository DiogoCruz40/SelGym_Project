package pt.selfgym.ui.workouts;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.KeyEvent;
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

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

import pt.selfgym.Interfaces.ActivityInterface;
import pt.selfgym.Interfaces.EditWorkoutInterface;
import pt.selfgym.R;
import pt.selfgym.SharedViewModel;
import pt.selfgym.dtos.CircuitDTO;
import pt.selfgym.dtos.ExerciseDTO;
import pt.selfgym.dtos.ExerciseWODTO;
import pt.selfgym.dtos.SetsDTO;
import pt.selfgym.dtos.WorkoutDTO;


public class EditWorkoutFragment extends Fragment implements EditWorkoutInterface {

    private ActivityInterface activityInterface;
    private SharedViewModel mViewModel;
    private WorkoutViewModel workoutViewModel;
    private EditAdapter adapter;
    private WorkoutDTO workout;
    private EditText editTextNote, observations, name;
    private ImageButton addExercise;
    private Spinner type;
    private View view;
    private Long id;


    public EditWorkoutFragment() {
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
        this.workoutViewModel = new ViewModelProvider(activityInterface.getMainActivity()).get(WorkoutViewModel.class);

        if (getArguments() != null) {
            id = getArguments().getLong("id");
            Log.w("id", String.valueOf(id));
            for (WorkoutDTO workoutDTO : mViewModel.getWorkouts().getValue())
                if (workoutDTO.getId() == id) {
                    workoutViewModel.setWorkout(workoutDTO);
                    workout = workoutDTO;
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

        workoutViewModel.getWorkout().observe(getViewLifecycleOwner(), workout -> {
            this.workout = workout;
            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.exercises);
//            Log.w("help",workout.getWorkoutComposition().toString());
            adapter = new EditAdapter(workout, activityInterface.getMainActivity(), this, workoutViewModel);
            recyclerView.setNestedScrollingEnabled(false);
            recyclerView.setLayoutManager(new LinearLayoutManager(inflater.getContext()));
            recyclerView.setAdapter(adapter);
            name = (EditText) view.findViewById(R.id.editWorkoutName);
            name.setText(workout.getName());
            type = (Spinner) view.findViewById(R.id.workoutTypeSpinner);
            ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(activityInterface.getMainActivity(), android.R.layout.simple_spinner_item, Arrays.asList("full body", "upper body", "lower body", "push", "pull"));
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
            observations.setText(workout.getObservation());

            addExercise = (ImageButton) view.findViewById(R.id.addExercise);
            addExercise.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addExerciseCircuitPopup();
                }
            });

        });

        this.activityInterface.getMainActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.edit_workout_menu, menu);
                MenuItem saveItem = menu.findItem(R.id.savemenu);

                saveItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(@NonNull MenuItem menuItem) {
                        WorkoutDTO newWorkout = adapter.saveWorkoutChanges(workout);
                        newWorkout.setName(name.getText().toString());
                        newWorkout.setObservation(observations.getText().toString());
                        newWorkout.setType(type.getSelectedItem().toString());

                        if (workout.getId() == null)
                            mViewModel.insertWorkout(newWorkout);
                        else {
                            mViewModel.updateWorkout(newWorkout);
                        }
                        activityInterface.changeFrag(new WorkoutFragment(), null);
                        return false;
                    }
                });
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                return false;
            }
        }, getViewLifecycleOwner());

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
                workoutViewModel.updateworkoutparams(name.getText().toString(), type.getSelectedItem().toString(), observations.getText().toString());
                activityInterface.changeFrag(new AddExerciseFragment(), null);
                workoutViewModel.setWorkout(adapter.saveWorkoutChanges(workout));
                dialog.dismiss();
            }
        });

        addCircuitOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                workoutViewModel.updateworkoutparams(name.getText().toString(), type.getSelectedItem().toString(), observations.getText().toString());
                workoutViewModel.setWorkout(adapter.saveWorkoutChanges(workout));
                workout.addToWorkoutComposition(new CircuitDTO(0, 0, new ArrayList<ExerciseWODTO>()));
                adapter.setWorkout(workout);
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

    @Override
    public void addExercisetoCircuit(int position) {
        Bundle arg = new Bundle();
        arg.putInt("circuitposition", position);
        workoutViewModel.updateworkoutparams(name.getText().toString(), type.getSelectedItem().toString(), observations.getText().toString());
        activityInterface.changeFrag(new AddExerciseFragment(), arg);
        workoutViewModel.setWorkout(adapter.saveWorkoutChanges(workout));
    }

    @Override
    public void removeCircuit(int position) {
        workoutViewModel.updateworkoutparams(name.getText().toString(), type.getSelectedItem().toString(), observations.getText().toString());
        workoutViewModel.setWorkout(adapter.saveWorkoutChanges(workout));
        workout.getWorkoutComposition().remove(position);
        adapter.setWorkout(workout);
    }
}