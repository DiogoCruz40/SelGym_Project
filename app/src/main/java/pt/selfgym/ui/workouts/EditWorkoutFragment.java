package pt.selfgym.ui.workouts;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

import pt.selfgym.Interfaces.ActivityInterface;
import pt.selfgym.Interfaces.EditWorkoutInterface;
import pt.selfgym.R;
import pt.selfgym.SharedViewModel;
import pt.selfgym.database.entities.ExerciseSet;
import pt.selfgym.database.entities.ExerciseWO;
import pt.selfgym.dtos.CircuitDTO;
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
        }

        workoutViewModel.getWorkout().observe(getViewLifecycleOwner(), workout -> {
            this.workout = workout;
//            mViewModel.setResult(new AtomicBoolean(false));
            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.exercises);
//            Log.w("help",workout.getWorkoutComposition().toString());
            RecyclerView.ItemDecoration itemDecoration = new RecyclerView.ItemDecoration() {
                @Override
                public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                    outRect.bottom = 15;
                }
            };
            recyclerView.addItemDecoration(itemDecoration);
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
            if (workout.getType().equals("full body")) {
                type.setSelection(0);
            } else if (workout.getType().equals("upper body")) {
                type.setSelection(1);
            } else if (workout.getType().equals("lower body")) {
                type.setSelection(2);
            } else if (workout.getType().equals("push")) {
                type.setSelection(3);
            } else if (workout.getType().equals("pull")) {
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

                        if (newWorkout.getName().isEmpty()) {
                            mViewModel.getToastMessageObserver().setValue("Your Workout name can't be empty");
                        } else {
                            boolean trigger = false;
                            boolean trigger2 = false;
                            boolean trigger3 = false;
                            boolean trigger4 = false;
                            for (Object obj : newWorkout.getWorkoutComposition()) {
                                if (obj instanceof CircuitDTO) {
                                    if (((CircuitDTO) obj).getExerciseList().isEmpty()) {
                                        trigger = true;
                                        break;
                                    } else {
                                        for (ExerciseWODTO exerciseWODTO : ((CircuitDTO) obj).getExerciseList()) {
                                            if ((exerciseWODTO.isVariableSetsReps() || exerciseWODTO.isVariableSetsTime()) && exerciseWODTO.getSetsList().isEmpty()) {
                                                trigger2 = true;
                                                break;
                                            } else if ((exerciseWODTO.isFixedSetsReps() || exerciseWODTO.isFixedSetsTime()) && exerciseWODTO.getDuration() == 0 && exerciseWODTO.getReps() == 0) {
                                                trigger3 = true;
                                                break;
                                            } else if (exerciseWODTO.getSetsList() != null) {
                                                for (SetsDTO setsDTO : exerciseWODTO.getSetsList()) {
                                                    if (setsDTO.getVariable() == 0) {
                                                        trigger3 = true;
                                                        break;
                                                    }
                                                }
                                                if (trigger3)
                                                    break;
                                            }
                                        }
                                        if (trigger2 || trigger3) {
                                            break;
                                        }
                                    }
                                } else {
                                    if (((ExerciseWODTO) obj).getReps() == 0 && ((ExerciseWODTO) obj).getDuration() == 0) {
                                        trigger3 = true;
                                        break;
                                    } else if (((ExerciseWODTO) obj).getSets() == 0) {
                                        trigger4 = true;
                                        break;
                                    }
                                }
                            }

                            if (trigger) {
                                mViewModel.getToastMessageObserver().setValue("Your circuits can't be empty");

                            } else if (trigger2) {
                                mViewModel.getToastMessageObserver().setValue("You cant have empty sets");
                            } else if (trigger3) {
                                mViewModel.getToastMessageObserver().setValue("Time/Reps must be greater than 0");
                            } else if (trigger4) {
                                mViewModel.getToastMessageObserver().setValue("Sets must be greater than 0");
                            } else {
                                if (workout.getId() == null) {
                                    mViewModel.insertWorkout(newWorkout);

                                    mViewModel.getGetResultInsert().observe(getViewLifecycleOwner(), new Observer<AtomicBoolean>() {
                                        @Override
                                        public void onChanged(AtomicBoolean atomicBoolean) {
                                            if (atomicBoolean.get()) {
                                                mViewModel.updateStats(null, type.getSelectedItem().toString(), workout.getNrOfConclusions());
                                                mViewModel.top5Workouts();
                                                activityInterface.changeFrag(new WorkoutFragment(), null);
                                            } else
                                                mViewModel.getToastMessageObserver().setValue("This name of workout already exists");
                                        }
                                    });

                                } else {
                                    mViewModel.updateWorkout(newWorkout);

                                    mViewModel.getGetResultUpdate().observe(getViewLifecycleOwner(), result -> {
                                        if (result.get()) {
                                            mViewModel.updateStats(workout.getType(), type.getSelectedItem().toString(), workout.getNrOfConclusions());
                                            mViewModel.top5Workouts();
                                            activityInterface.changeFrag(new WorkoutFragment(), null);
                                        } else
                                            mViewModel.getToastMessageObserver().setValue("This name of workout already exists");
                                    });
                                }
                            }

                        }
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