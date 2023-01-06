package pt.selfgym.ui.workouts;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;


import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import pt.selfgym.Interfaces.ActivityInterface;
import pt.selfgym.Interfaces.ButtonsInterface;
import pt.selfgym.Interfaces.WorkoutsInterface;
import pt.selfgym.R;
import pt.selfgym.SharedViewModel;
import pt.selfgym.databinding.FragmentWorkoutsBinding;
import pt.selfgym.dtos.ExerciseDTO;
import pt.selfgym.dtos.ExerciseWODTO;
import pt.selfgym.dtos.WorkoutDTO;

public class WorkoutFragment extends Fragment implements WorkoutsInterface, ButtonsInterface {

    private SharedViewModel mViewModel;
    private WorkoutViewModel workoutViewModel;
    private FragmentWorkoutsBinding binding;
    private ListAdapter adapter;
    private ActivityInterface activityInterface;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private Spinner spinnermqttpopup, spinnertopicshare;
    private EditText newWorkoutName;
    private EditText newNoteName, subscribetopic, topicname;
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

        this.mViewModel = new ViewModelProvider(activityInterface.getMainActivity()).get(SharedViewModel.class);
        this.workoutViewModel = new ViewModelProvider(activityInterface.getMainActivity()).get(WorkoutViewModel.class);

        mViewModel.getWorkouts().observe(getViewLifecycleOwner(), workouts -> {

//                Log.w("id", ((ExerciseWODTO) workouts.get(1).getWorkoutComposition().get(0)).getId().toString());
            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.workouts);
            RecyclerView.ItemDecoration itemDecoration = new RecyclerView.ItemDecoration() {
                @Override
                public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                    outRect.bottom = 15;
                }
            };
            recyclerView.addItemDecoration(itemDecoration);
            adapter = new ListAdapter(workouts, this);
            recyclerView.setNestedScrollingEnabled(false);
            recyclerView.setLayoutManager(new LinearLayoutManager(inflater.getContext()));
            recyclerView.setAdapter(adapter);

        });

//        ArrayList<WorkoutDTO> workouts = new ArrayList<WorkoutDTO>();
//        workouts.add(new WorkoutDTO("olá1", "hey", "full body"));
//        workouts.add(new WorkoutDTO("olá2", "hey", "upper body"));
//        workouts.add(new WorkoutDTO("olá3", "hey", "lower body"));
//        workouts.add(new WorkoutDTO("olá4", "hey", "push"));
//
//        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.workouts);
//        adapter = new ListAdapter(workouts, this);
//        recyclerView.setNestedScrollingEnabled(false);
//        recyclerView.setLayoutManager(new LinearLayoutManager(inflater.getContext()));
//        recyclerView.setAdapter(adapter);

        ImageButton addWorkoutButton = (ImageButton) view.findViewById(R.id.addWorkoutButton);
        addWorkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                workoutViewModel.newWorkout();
                activityInterface.changeFrag(new EditWorkoutFragment(), null);
            }
        });
        this.activityInterface.getMainActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.menu_workouts, menu);
                MenuItem menuItem = menu.findItem(R.id.searchbar);
                MenuItem filterItem = menu.findItem(R.id.filtermenu);
                MenuItem mqttItem = menu.findItem(R.id.mqttmenu);
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
                mqttItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(@NonNull MenuItem item) {
                        mqttPopUp();
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

    public void mqttPopUp() {
        dialogBuilder = new AlertDialog.Builder(activityInterface.getMainActivity());
        final View mqttPopUp = getLayoutInflater().inflate(R.layout.mqtt_popup, null);
        cancel = (Button) mqttPopUp.findViewById(R.id.cancelbuttonmqtt);
        Switch switchmqtt = mqttPopUp.findViewById(R.id.switchmqtt);
        if (mViewModel.checkStatemqtt()) {
            switchmqtt.setChecked(true);
        }
        subscribe = (Button) mqttPopUp.findViewById(R.id.subscribebuttonmqtt);
        unsubscribe = (Button) mqttPopUp.findViewById(R.id.unsubbuttonmqtt);
        spinnermqttpopup = (Spinner) mqttPopUp.findViewById(R.id.spinnermqtt);

        mViewModel.getTopics().observe(activityInterface.getMainActivity(), topics -> {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(activityInterface.getMainActivity(), android.R.layout.simple_spinner_item, topics);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnermqttpopup.setAdapter(adapter);
        });
        dialogBuilder.setView(mqttPopUp);
        dialog = dialogBuilder.create();
        dialog.show();

        switchmqtt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (switchmqtt.isChecked()) {
                    mViewModel.connmqtt(activityInterface.getMainActivity());
                } else {
                    mViewModel.setTopics(new ArrayList<String>());
                    mViewModel.disconmqtt();
                }
            }
        });
        subscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subscribetopic = (EditText) mqttPopUp.findViewById(R.id.subscribemqtt);
                if (subscribetopic.getText().toString().isBlank())
                    Toast.makeText(activityInterface.getMainActivity(), "Write a topic", Toast.LENGTH_SHORT).show();
                else if (mViewModel.subscribeToTopic(subscribetopic.getText().toString()))
                    subscribetopic.setText("");
                else
                    Toast.makeText(activityInterface.getMainActivity(), "Already subscribed", Toast.LENGTH_SHORT).show();
            }
        });

        unsubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spinnermqttpopup.getSelectedItem() != null)
                    mViewModel.unsubscribeToTopic(spinnermqttpopup.getSelectedItem().toString());
                else
                    Toast.makeText(activityInterface.getMainActivity(), "Nothing to unsubscribe", Toast.LENGTH_SHORT).show();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    public void createDeleteWorkoutPopUp() {
        dialogBuilder = new AlertDialog.Builder(activityInterface.getMainActivity());
        final View DeleteWorkoutPopUp = getLayoutInflater().inflate(R.layout.popup_delete_workout, null);

        deleteWorkout = (Button) DeleteWorkoutPopUp.findViewById(R.id.deleteButton);
        cancel = (Button) DeleteWorkoutPopUp.findViewById(R.id.cancelButton);
        addtopic = (Button) DeleteWorkoutPopUp.findViewById(R.id.addtopicbtn3);
        removetopic = (Button) DeleteWorkoutPopUp.findViewById(R.id.removetopicbtn3);
        sharenote = (Button) DeleteWorkoutPopUp.findViewById(R.id.sharenotebtn3);
        topicname = (EditText) DeleteWorkoutPopUp.findViewById(R.id.topicnameeditid3);
        spinnertopicshare = (Spinner) DeleteWorkoutPopUp.findViewById(R.id.spinnersharemqtt3);
        List<String> topics = new ArrayList<String>();

        dialogBuilder.setView(DeleteWorkoutPopUp);
        dialog = dialogBuilder.create();
        dialog.show();

        deleteWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.deleteWorkoutById(id);
                dialog.dismiss();
            }
        });
        addtopic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (topics.contains(topicname.getText().toString()))
                    Toast.makeText(activityInterface.getMainActivity(), "Already added", Toast.LENGTH_SHORT).show();
                else if (topicname.getText().toString().isBlank())
                    Toast.makeText(activityInterface.getMainActivity(), "Write a topic", Toast.LENGTH_SHORT).show();
                else {
                    topics.add(topicname.getText().toString());
                    topicname.setText("");
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(activityInterface.getMainActivity(), android.R.layout.simple_spinner_item, topics);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnertopicshare.setAdapter(adapter);
                }
            }
        });

        removetopic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (topics.size() > 0) {
                    topics.remove(spinnertopicshare.getSelectedItem().toString());
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(activityInterface.getMainActivity(), android.R.layout.simple_spinner_item, topics);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnertopicshare.setAdapter(adapter);
                } else
                    Toast.makeText(activityInterface.getMainActivity(), "There are no topics", Toast.LENGTH_SHORT).show();
            }
        });

        sharenote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (topics.size() > 0) {
                    WorkoutDTO workoutDTO;
                    for (WorkoutDTO wo : Objects.requireNonNull(mViewModel.getWorkouts().getValue())) {
                        if (wo.getId() == id) {
                            workoutDTO = wo;
                            topics.forEach(topic -> {
                                mViewModel.publishMessage(workoutDTO, topic);
                            });
                            break;
                        }
                    }
                    dialog.dismiss();
                } else
                    Toast.makeText(activityInterface.getMainActivity(), "Add a topic first", Toast.LENGTH_SHORT).show();
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
    public WorkoutFragment getContextfrag() {
        return this;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}