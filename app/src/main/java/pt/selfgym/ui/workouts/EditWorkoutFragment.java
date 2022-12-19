package pt.selfgym.ui.workouts;

import android.content.Context;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import pt.selfgym.Interfaces.ActivityInterface;
import pt.selfgym.R;
import pt.selfgym.SharedViewModel;
import pt.selfgym.dtos.CircuitDTO;
import pt.selfgym.dtos.ExerciseDTO;
import pt.selfgym.dtos.ExerciseWODTO;
import pt.selfgym.dtos.SetsDTO;
import pt.selfgym.dtos.WorkoutDTO;


public class EditWorkoutFragment extends Fragment {

    private ActivityInterface activityInterface;
    private SharedViewModel mViewModel;
    private EditAdapter adapter;
    private EditText editTextNote, observations;
    private View view;
    private long id;
    private WorkoutDTO workout;


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


        if (getArguments() != null) {
            id = getArguments().getLong("id");

            //TODO: get workout by id
            //String note = mViewModel.getNoteContentById(id);

            //comment this if needed
            workout = new WorkoutDTO(1L, "olá" , "hell", "full body");
            ExerciseWODTO exercise1 = new ExerciseWODTO(1L,1,0.0,1,1,0,new ExerciseDTO(1L,"hell","sodqodmpaod", "sidno"));
            ExerciseWODTO exercise2 = new ExerciseWODTO(2L,2,0.0,1,0,new ExerciseDTO(1L,"hell","sodqodmpaod", "sidno"),1);
            SetsDTO setsDTO1 = new SetsDTO(1L,1,1,1,1);
            SetsDTO setsDTO2 = new SetsDTO(1L,1,1,1,2);
            ArrayList<SetsDTO> sets = new ArrayList<SetsDTO>();
            sets.add(setsDTO1);
            sets.add(setsDTO2);
            ExerciseWODTO exercise3 = new ExerciseWODTO(3L,3, 1, new ExerciseDTO(1L,"hell","sodqodmpaod", "sidno"), sets);
            ExerciseWODTO exercise4 = new ExerciseWODTO(4L,4, new ExerciseDTO(1L,"hell","sodqodmpaod", "sidno"), 1, sets);
            ArrayList<ExerciseWODTO> circuitComposition = new ArrayList<ExerciseWODTO>();
            circuitComposition.add(exercise1);
            circuitComposition.add(exercise2);
            circuitComposition.add(exercise3);
            circuitComposition.add(exercise4);
            CircuitDTO circuit = new CircuitDTO(5L,5,0, circuitComposition);
            ArrayList<Object> workoutComposition = new ArrayList<Object>();
            workoutComposition.add(exercise1);
            workoutComposition.add(exercise2);
            workoutComposition.add(exercise3);
            workoutComposition.add(exercise4);
            workoutComposition.add(circuit);
            workout.setWorkoutComposition(workoutComposition);
        } else {
            //new workout
            id = -1;
            //TODO: get the workout name that i told u to save in the view model here
            // no need to save it yet so the id is -1
            workout = new WorkoutDTO(id, "" , "", "full body");
        }


        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.exercises);
        adapter = new EditAdapter(workout);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(inflater.getContext()));
        recyclerView.setAdapter(adapter);

        observations = (EditText) view.findViewById(R.id.textAreaObservations);
        observations.setText(workout.getObservation());

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener( new View.OnKeyListener()
        {
            @Override
            public boolean onKey( View v, int keyCode, KeyEvent event )
            {
                if( keyCode == KeyEvent.KEYCODE_BACK )
                {
                    activityInterface.changeFrag(new WorkoutFragment(),null);
                    BottomNavigationView navBar = (BottomNavigationView) activityInterface.getMainActivity().findViewById(R.id.nav_view);
                    navBar.setVisibility(View.VISIBLE);
                    return true;
                }
                return false;
            }
        } );

        activityInterface.getMainActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.edit_workout_menu, menu);
                MenuItem saveItem = menu.findItem(R.id.savemenu);

                saveItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(@NonNull MenuItem menuItem) {
                        WorkoutDTO newWorkout = adapter.saveWorkoutChanges();

                        if (getArguments()==null){
                            //TODO: This means its a new workout and the id
                            // hast been generated yet
                        }

                        //TODO: Save the updated or new workout cause its the same

                        activityInterface.changeFrag(new WorkoutFragment(),null);
                        return false;
                    }
                });
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);

        return view;
    }
}