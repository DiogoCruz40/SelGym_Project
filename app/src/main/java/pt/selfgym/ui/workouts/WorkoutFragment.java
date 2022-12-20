package pt.selfgym.ui.workouts;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;


import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import pt.selfgym.Interfaces.ActivityInterface;
import pt.selfgym.Interfaces.WorkoutsInterface;
import pt.selfgym.R;
import pt.selfgym.SharedViewModel;
import pt.selfgym.databinding.FragmentWorkoutsBinding;
import pt.selfgym.dtos.WorkoutDTO;

public class WorkoutFragment extends Fragment implements WorkoutsInterface {

    private SharedViewModel mViewModel;
    private FragmentWorkoutsBinding binding;
    private ListAdapter adapter;
    private ActivityInterface activityInterface;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private Spinner spinnermqttpopup, spinnertopicshare;
    private EditText newWorkoutName;
    private Button deleteWorkout, saveNewName, cancel, subscribe, unsubscribe, addtopic, removetopic, sharenote;
    private Long id;
    private WorkoutFilter workoutFilters = new WorkoutFilter();
    private String searchString = "";

    public WorkoutFragment() {

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activityInterface = (ActivityInterface) context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentWorkoutsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        //Toolbar myToolbar = (Toolbar) view.findViewById(R.id.workoutMenu);
        //AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
        //appCompatActivity.setSupportActionBar(myToolbar);
        this.mViewModel = new ViewModelProvider(activityInterface.getMainActivity()).get(SharedViewModel.class);
        /*mViewModel.getWorkouts().observe(getViewLifecycleOwner(), workouts -> {

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.workouts);
        adapter = new ListAdapter(workouts, this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(inflater.getContext()));
        recyclerView.setAdapter(adapter);
        });*/

        ArrayList<WorkoutDTO> workouts = new ArrayList<WorkoutDTO>();
        workouts.add(new WorkoutDTO("olá1", "hey", "full body"));
        workouts.add(new WorkoutDTO("olá2", "hey", "upper body"));
        workouts.add(new WorkoutDTO("olá3", "hey", "lower body"));
        workouts.add(new WorkoutDTO("olá4", "hey", "push"));

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.workouts);
        adapter = new ListAdapter(workouts, this);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(inflater.getContext()));
        recyclerView.setAdapter(adapter);

        ImageButton addWorkoutButton = (ImageButton) view.findViewById(R.id.addWorkoutButton);
        addWorkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: Create new workout and set the id to be passed
                EditWorkoutFragment fr = new EditWorkoutFragment();
                activityInterface.changeFrag(new EditWorkoutFragment(), null);
            }
        });
        Toolbar toolbar = activityInterface.getMainActivity().findViewById(R.id.toolbar);
        toolbar.addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.menu_workouts, menu);
                MenuItem menuItem = menu.findItem(R.id.searchbar);
                MenuItem filterItem = menu.findItem(R.id.filtermenu);
                SearchView searchView = (SearchView) menuItem.getActionView();
                searchView.setQueryHint("Type here to search");
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

                    @Override
                    public boolean onQueryTextSubmit(String s) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String s) {
                        searchString = s; //filter function needs access to this string
                        List<WorkoutDTO> workouts = adapter.getWorkouts();
                        if (s.length() != 0) {
                            List<WorkoutDTO> filteredList = new ArrayList<WorkoutDTO>();
                            for (WorkoutDTO n : workouts) {
                                if (n.getName().toLowerCase().contains(s.toLowerCase())) {
                                    if (workoutFilters.filter(n.getType())) {
                                        filteredList.add(n);
                                    }
                                }
                            }
                            if (filteredList.isEmpty()) {
                                Toast.makeText(activityInterface.getMainActivity(), "No Results for your search", Toast.LENGTH_LONG).show();
                                adapter.setFilteredWorkouts(filteredList);
                            } else {
                                adapter.setFilteredWorkouts(filteredList);
                            }
                        } else {
                            List<WorkoutDTO> filteredList = new ArrayList<WorkoutDTO>();
                            for (WorkoutDTO n : workouts) {
                                if (workoutFilters.filter(n.getType())) {
                                    filteredList.add(n);
                                }
                            }
                            adapter.setFilteredWorkouts(filteredList);
                        }

                        return false;
                    }
                });
                searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                            BottomNavigationView bottomNavigationView = activityInterface.getMainActivity().findViewById(R.id.nav_view);
                        if (hasFocus) {
                            bottomNavigationView.setVisibility(View.GONE);
                        } else {
                            bottomNavigationView.setVisibility(View.VISIBLE);
                        }
                    }
                });
                filterItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(@NonNull MenuItem menuItem) {
                        createWorkoutFilterPopUp();
                        return false;
                    }
                });
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case android.R.id.home:
                        dialog.onBackPressed();
                        return true;
                }
                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);

        return view;
    }


    @Override
    public void onItemClick(int position, View v) {

        //System.out.println("click on item: " + adapter.getWorkouts().get(position).getName());

        EditWorkoutFragment fr = new EditWorkoutFragment();
        Bundle arg = new Bundle();
        if (adapter.getWorkouts().size() == adapter.getFilteredWorkouts().size())
            id = adapter.getWorkouts().get(position).getId();
        else {
            id = adapter.getFilteredWorkouts().get(position).getId();
        }
        arg.putLong("id", id);

        activityInterface.changeFrag(fr, arg);
    }

    @Override
    public void onLongItemClick(int position, View v) {
        //System.out.println("long click on item: " + adapter.getWorkouts().get(position).getName());
        if (adapter.getWorkouts().size() == adapter.getFilteredWorkouts().size())
            id = adapter.getWorkouts().get(position).getId();
        else {
            id = adapter.getFilteredWorkouts().get(position).getId();
        }
        createDeleteWorkoutPopUp();
    }


    public void createWorkoutFilterPopUp() {

        dialogBuilder = new AlertDialog.Builder(activityInterface.getMainActivity());
        final View createWorkoutFilterPopUp = getLayoutInflater().inflate(R.layout.popup_workout_filter, null);
        Button filterButton = (Button) createWorkoutFilterPopUp.findViewById(R.id.filterB);
        CheckBox fullBody = (CheckBox) createWorkoutFilterPopUp.findViewById(R.id.fullBodyCB);
        if (workoutFilters.isFullBody()) {
            fullBody.setChecked(true);
        } else {
            fullBody.setChecked(false);
        }
        CheckBox lowerBody = (CheckBox) createWorkoutFilterPopUp.findViewById(R.id.lowerBodyCB);
        if (workoutFilters.isLowerBody()) {
            lowerBody.setChecked(true);
        } else {
            lowerBody.setChecked(false);
        }
        CheckBox upperBody = (CheckBox) createWorkoutFilterPopUp.findViewById(R.id.upperBodyCB);
        if (workoutFilters.isUpperBody()) {
            upperBody.setChecked(true);
        } else {
            upperBody.setChecked(false);
        }
        CheckBox push = (CheckBox) createWorkoutFilterPopUp.findViewById(R.id.pushCB);
        if (workoutFilters.isPush()) {
            push.setChecked(true);
        } else {
            push.setChecked(false);
        }
        CheckBox pull = (CheckBox) createWorkoutFilterPopUp.findViewById(R.id.pullCB);
        if (workoutFilters.isPull()) {
            pull.setChecked(true);
        } else {
            pull.setChecked(false);
        }


        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fullBody.isChecked()) {
                    workoutFilters.setFullBody(true);
                } else {
                    workoutFilters.setFullBody(false);
                }
                if (upperBody.isChecked()) {
                    workoutFilters.setUpperBody(true);
                } else {
                    workoutFilters.setUpperBody(false);
                }
                if (lowerBody.isChecked()) {
                    workoutFilters.setLowerBody(true);
                } else {
                    workoutFilters.setLowerBody(false);
                }
                if (pull.isChecked()) {
                    workoutFilters.setPull(true);
                } else {
                    workoutFilters.setPull(false);
                }
                if (push.isChecked()) {
                    workoutFilters.setPush(true);
                } else {
                    workoutFilters.setPush(false);
                }

                List<WorkoutDTO> workouts = adapter.getWorkouts();
                if (searchString.length() != 0) {
                    List<WorkoutDTO> filteredList = new ArrayList<WorkoutDTO>();
                    for (WorkoutDTO n : workouts) {
                        if (n.getName().toLowerCase().contains(searchString.toLowerCase())) {
                            if (workoutFilters.filter(n.getType())) {
                                filteredList.add(n);
                            }
                        }
                    }
                    if (filteredList.isEmpty()) {
                        Toast.makeText(activityInterface.getMainActivity(), "No Results for your search", Toast.LENGTH_LONG).show();
                        adapter.setFilteredWorkouts(filteredList);
                    } else {
                        adapter.setFilteredWorkouts(filteredList);
                    }
                } else {
                    List<WorkoutDTO> filteredList = new ArrayList<WorkoutDTO>();
                    for (WorkoutDTO n : workouts) {
                        if (workoutFilters.filter(n.getType())) {
                            filteredList.add(n);
                        }
                    }
                    adapter.setFilteredWorkouts(filteredList);
                }

                dialog.dismiss();
            }
        });

        dialogBuilder.setView(createWorkoutFilterPopUp);
        dialog = dialogBuilder.create();
        dialog.show();
    }

    public void createDeleteWorkoutPopUp() {
        dialogBuilder = new AlertDialog.Builder(activityInterface.getMainActivity());
        final View DeleteWorkoutPopUp = getLayoutInflater().inflate(R.layout.popup_delete_workout, null);

        deleteWorkout = (Button) DeleteWorkoutPopUp.findViewById(R.id.deleteButton);
        cancel = (Button) DeleteWorkoutPopUp.findViewById(R.id.cancelButton);

        dialogBuilder.setView(DeleteWorkoutPopUp);
        dialog = dialogBuilder.create();
        dialog.show();

        deleteWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.deleteWorkoutbyId(id);
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
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}