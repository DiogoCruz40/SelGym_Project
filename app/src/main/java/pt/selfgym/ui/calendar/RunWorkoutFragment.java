package pt.selfgym.ui.calendar;

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
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.stream.Collectors;

import pt.selfgym.Interfaces.ActivityInterface;
import pt.selfgym.Interfaces.EditWorkoutInterface;
import pt.selfgym.R;
import pt.selfgym.SharedViewModel;
import pt.selfgym.dtos.CircuitDTO;
import pt.selfgym.dtos.EventDTO;
import pt.selfgym.dtos.ExerciseDTO;
import pt.selfgym.dtos.ExerciseWODTO;
import pt.selfgym.dtos.SetsDTO;
import pt.selfgym.dtos.WorkoutDTO;
import pt.selfgym.ui.workouts.EditWorkoutFragment;
import pt.selfgym.ui.workouts.WorkoutViewModel;


public class RunWorkoutFragment extends Fragment {

    private ActivityInterface activityInterface;
    private SharedViewModel mViewModel;
    private WorkoutViewModel workoutViewModel;
    private RunAdapter adapter;
    private WorkoutDTO workout;
    private EditText observations;
    private ImageButton addExercise;
    private TextView type, name;
    private View view;
    private Long id;


    public RunWorkoutFragment() {
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
        view = inflater.inflate(R.layout.fragment_run, container, false);

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
            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.exercises);
//            Log.w("help",workout.getWorkoutComposition().toString());
            adapter = new RunAdapter(workout, activityInterface.getMainActivity());
            recyclerView.setNestedScrollingEnabled(false);
            recyclerView.setLayoutManager(new LinearLayoutManager(inflater.getContext()));
            recyclerView.setAdapter(adapter);
            name = (TextView) view.findViewById(R.id.workoutName);
            name.setText(workout.getName());
            type = (TextView) view.findViewById(R.id.workoutType);
            type.setText(workout.getType());
            observations = (EditText) view.findViewById(R.id.textAreaObservationsRun);
            if(workout.getObservation() != null)
            {
            observations.setText(workout.getObservation());
            }

        });

        this.activityInterface.getMainActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.run_workout_menu, menu);
                MenuItem concludeItem = menu.findItem(R.id.concludemenu);

                concludeItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(@NonNull MenuItem menuItem) {
                        createConcludeEventPopup();
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

    public void createConcludeEventPopup() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activityInterface.getMainActivity());
        final View AddEventPopup = getLayoutInflater().inflate(R.layout.popup_conclude_event, null);

        Button cancel = (Button) AddEventPopup.findViewById(R.id.cancelButton);
        Button concludeEvent = (Button) AddEventPopup.findViewById(R.id.concludeEvent);
        Button leaveEvent = (Button) AddEventPopup.findViewById(R.id.leaveEvent);

        dialogBuilder.setView(AddEventPopup);
        Dialog dialog = dialogBuilder.create();
        dialog.show();

        concludeEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Mark the event as concluded and
                // increment the number of workout conclusions
                // save the new observations to that workout

                CalendarFragment fr = new CalendarFragment();
                activityInterface.changeFrag(fr, null);
                dialog.dismiss();
            }
        });

        leaveEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: save the observations
                CalendarFragment fr = new CalendarFragment();
                activityInterface.changeFrag(fr, null);
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