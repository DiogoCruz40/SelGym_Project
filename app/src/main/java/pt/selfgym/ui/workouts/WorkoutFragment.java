package pt.selfgym.ui.workouts;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
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
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import pt.selfgym.Interfaces.ActivityInterface;
import pt.selfgym.Interfaces.WorkoutsInterface;
import pt.selfgym.R;
import pt.selfgym.SharedViewModel;
import pt.selfgym.dtos.WorkoutDTO;

public class WorkoutFragment extends Fragment implements WorkoutsInterface {

    private SharedViewModel mViewModel;
    private ListAdapter adapter;
    private ActivityInterface activityInterface;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private Spinner spinnermqttpopup, spinnertopicshare;
    private EditText newNoteName, subscribetopic, topicname;
    private Button deleteNote, saveNewName, cancel, subscribe, unsubscribe, addtopic, removetopic, sharenote;
    private int id;
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
        View view = inflater.inflate(R.layout.fragment_workouts, container, false);
        setHasOptionsMenu(true);
        this.mViewModel = new ViewModelProvider(activityInterface.getMainActivity()).get(SharedViewModel.class);
        //TODO: ir buscar workouts ao viewmodel
        /*mViewModel.getNotes().observe(getViewLifecycleOwner(), notes -> {
            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.notes);
            adapter = new ListAdapter(notes, this);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(inflater.getContext()));
            recyclerView.setAdapter(adapter);
        });*/

        ArrayList<WorkoutDTO> workouts = new ArrayList<WorkoutDTO>();
        workouts.add(new WorkoutDTO(1,"olá1","hey","full body"));
        workouts.add(new WorkoutDTO(2,"olá2","hey","upper body"));
        workouts.add(new WorkoutDTO(3,"olá3","hey","lower body"));
        workouts.add(new WorkoutDTO(4,"olá4","hey","push"));
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.workouts);
        adapter = new ListAdapter(workouts, this);
        recyclerView.setHasFixedSize(true);


        recyclerView.setLayoutManager(new LinearLayoutManager(inflater.getContext()));
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_workouts, menu);
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
                            if(workoutFilters.filter(n.getType())){
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
                        if(workoutFilters.filter(n.getType())){
                            filteredList.add(n);
                        }
                    }
                    adapter.setFilteredWorkouts(filteredList);
                }

                return false;
            }
        });
        filterItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem menuItem) {
                createWorkoutFilterPopUp();
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public void onItemClick(int position, View v) {

//        System.out.println("click on item: " + adapter.getNotes().get(position).getTitle());

        /*FragmentTwo fr = new FragmentTwo();
        Bundle arg = new Bundle();
        if (adapter.getNotes().size() == adapter.getFilteredNotes().size())
            id = adapter.getNotes().get(position).getId();
        else {
            id = adapter.getFilteredNotes().get(position).getId();
        }
        arg.putInt("id", id);
        fr.setArguments(arg);

        activityInterface.changeFrag(fr);*/
    }

    @Override
    public void onLongItemClick(int position, View v) {
        /*if (adapter.getNotes().size() == adapter.getFilteredNotes().size())
            id = adapter.getNotes().get(position).getId();
        else {
            id = adapter.getFilteredNotes().get(position).getId();
        }
        createNewTitleDeletePopUp();
//        System.out.println("long click on item: " + adapter.getNotes().get(position).getTitle());
    */
    }


    public void createWorkoutFilterPopUp() {

        dialogBuilder = new AlertDialog.Builder(activityInterface.getMainActivity());
        final View createWorkoutFilterPopUp = getLayoutInflater().inflate(R.layout.workout_filter_popup, null);
        Button filterButton = (Button) createWorkoutFilterPopUp.findViewById(R.id.filterB);
        CheckBox fullBody = (CheckBox) createWorkoutFilterPopUp.findViewById(R.id.fullBodyCB);
        if (workoutFilters.isFullBody()){
            fullBody.setChecked(true);
        } else {
            fullBody.setChecked(false);
        }
        CheckBox lowerBody = (CheckBox) createWorkoutFilterPopUp.findViewById(R.id.lowerBodyCB);
        if (workoutFilters.isLowerBody()){
            lowerBody.setChecked(true);
        } else {
            lowerBody.setChecked(false);
        }
        CheckBox upperBody = (CheckBox) createWorkoutFilterPopUp.findViewById(R.id.upperBodyCB);
        if (workoutFilters.isUpperBody()){
            upperBody.setChecked(true);
        } else {
            upperBody.setChecked(false);
        }
        CheckBox push = (CheckBox) createWorkoutFilterPopUp.findViewById(R.id.pushCB);
        if (workoutFilters.isPush()){
            push.setChecked(true);
        } else {
            push.setChecked(false);
        }
        CheckBox pull = (CheckBox) createWorkoutFilterPopUp.findViewById(R.id.pullCB);
        if (workoutFilters.isPull()){
            pull.setChecked(true);
        } else {
            pull.setChecked(false);
        }


        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fullBody.isChecked()){
                    workoutFilters.setFullBody(true);
                } else {
                    workoutFilters.setFullBody(false);
                }
                if(upperBody.isChecked()){
                    workoutFilters.setUpperBody(true);
                } else {
                    workoutFilters.setUpperBody(false);
                }
                if(lowerBody.isChecked()){
                    workoutFilters.setLowerBody(true);
                } else {
                    workoutFilters.setLowerBody(false);
                }
                if(pull.isChecked()){
                    workoutFilters.setPull(true);
                } else {
                    workoutFilters.setPull(false);
                }
                if(push.isChecked()){
                    workoutFilters.setPush(true);
                } else {
                    workoutFilters.setPush(false);
                }

                List<WorkoutDTO> workouts = adapter.getWorkouts();
                if (searchString.length() != 0) {
                    List<WorkoutDTO> filteredList = new ArrayList<WorkoutDTO>();
                    for (WorkoutDTO n : workouts) {
                        if (n.getName().toLowerCase().contains(searchString.toLowerCase())) {
                            if(workoutFilters.filter(n.getType())){
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
                            if(workoutFilters.filter(n.getType())){
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


    /*public void createNewNotePopUp() {
        dialogBuilder = new AlertDialog.Builder(activityInterface.getMainActivity());
        final View newNotePopUp = getLayoutInflater().inflate(R.layout.new_note_popup, null);
        newNoteName = (EditText) newNotePopUp.findViewById(R.id.newTitle);

        saveNewName = (Button) newNotePopUp.findViewById(R.id.save);
        cancel = (Button) newNotePopUp.findViewById(R.id.cancel);

        dialogBuilder.setView(newNotePopUp);
        dialog = dialogBuilder.create();
        dialog.show();

        saveNewName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                newNoteName = (EditText) newNotePopUp.findViewById(R.id.newTitle);
                mViewModel.addNote(String.valueOf(newNoteName.getText()));

                dialog.dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }*/

    /*public void mqttPopUp() {
        dialogBuilder = new AlertDialog.Builder(activityInterface.getMainActivity());
        final View mqttPopUp = getLayoutInflater().inflate(R.layout.mqtt_popup, null);
        cancel = (Button) mqttPopUp.findViewById(R.id.cancelbuttonmqtt);
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
    }*/


    /*public void mqttMsgPopUp(String topic, MqttMessage message) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activityInterface.getMainActivity());
        final View mqttmesagePopUp = getLayoutInflater().inflate(R.layout.mqtt_message_popup, null);
        Button confirm = (Button) mqttmesagePopUp.findViewById(R.id.confirmmsgbtnmqtt);
        Button cancel = (Button) mqttmesagePopUp.findViewById(R.id.cancelmsgbtnmqtt2);
        TextView topico = (TextView) mqttmesagePopUp.findViewById(R.id.topicmsgmqtt);
        TextView titulo = (TextView) mqttmesagePopUp.findViewById(R.id.titlemsgmqtt);
        NoteDTO noteDTO = new Gson().fromJson(message.toString(), NoteDTO.class);
        topico.setText(topic);
        titulo.setText(noteDTO.getTitle());

        dialogBuilder.setView(mqttmesagePopUp);
        Dialog dialog = dialogBuilder.create();
        dialog.show();

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.insertMqttNote(noteDTO.getTitle(), noteDTO.getDescription());
                dialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }*/

}