package pt.selfgym.ui.calendar;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.adapters.ToolbarBindingAdapter;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import pt.selfgym.Interfaces.ActivityInterface;
import pt.selfgym.Interfaces.ButtonsInterface;
import pt.selfgym.R;
import pt.selfgym.SharedViewModel;
import pt.selfgym.databinding.FragmentCalendarBinding;
import pt.selfgym.dtos.CircuitDTO;
import pt.selfgym.dtos.DateDTO;
import pt.selfgym.dtos.EventDTO;
import pt.selfgym.dtos.ExerciseWODTO;
import pt.selfgym.dtos.SetsDTO;
import pt.selfgym.dtos.WorkoutDTO;
import pt.selfgym.ui.workouts.AddExerciseFragment;
import pt.selfgym.ui.workouts.EditWorkoutFragment;
import pt.selfgym.ui.workouts.ListAdapter;

public class CalendarFragment extends Fragment implements ButtonsInterface {

    private FragmentCalendarBinding binding;
    private ActivityInterface activityInterface;
    private EventsAdapter adapter;
//    private List<EventDTO> events;
    private SharedViewModel sharedViewModel;
    private CalendarView calendarView;
    private View view;
    private DateDTO selectedDate;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activityInterface = (ActivityInterface) context;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        this.sharedViewModel = new ViewModelProvider(activityInterface.getMainActivity()).get(SharedViewModel.class);

        binding = FragmentCalendarBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        sharedViewModel.getEventsCalendar();
        calendarView = view.findViewById(R.id.calendar);
        long millis = calendarView.getDate();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        selectedDate = new DateDTO(dayOfMonth, month, year);

        sharedViewModel.getEventsCa().observe(getViewLifecycleOwner(), events -> {

            calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                @Override
                public void onSelectedDayChange(@NonNull CalendarView cview, int year, int month, int dayOfMonth) {

                    ArrayList<EventDTO> eventsDay = new ArrayList<EventDTO>();
                    for (EventDTO e : events) {
                        if (dayOfMonth == e.getDate().getDay() && (month + 1) == e.getDate().getMonth() && year == e.getDate().getYear()) {
                            eventsDay.add(e);
                        }
                    }
                    adapter.setEvents(eventsDay);
                    TextView workoutNr = (TextView) view.findViewById(R.id.workoutNr);
                    workoutNr.setText(eventsDay.size() + "");
                    TextView workoutString = (TextView) view.findViewById(R.id.workoutString);
                    if (eventsDay.size() == 1) {
                        workoutString.setText("workout");
                    } else {
                        workoutString.setText("workouts");
                    }

                    selectedDate = new DateDTO(dayOfMonth, month + 1, year);

                }
            });

            ArrayList<EventDTO> eventsDay = new ArrayList<EventDTO>();

            for (EventDTO e : events) {
                if (selectedDate.getDay() == e.getDate().getDay() && selectedDate.getMonth() == e.getDate().getMonth() && selectedDate.getYear() == e.getDate().getYear()) {
                    eventsDay.add(e);
                }
            }

            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.eventList);
            RecyclerView.ItemDecoration itemDecoration = new RecyclerView.ItemDecoration() {
                @Override
                public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                    outRect.bottom = 15;
                }
            };
            recyclerView.addItemDecoration(itemDecoration);
            adapter = new EventsAdapter(eventsDay, this);
            recyclerView.setLayoutManager(new LinearLayoutManager(inflater.getContext()));
            recyclerView.setAdapter(adapter);
            //não sei porquê mas sem isto aqui não coloca a lista na recycler view
            adapter.setEvents(eventsDay);


            TextView workoutNr = (TextView) view.findViewById(R.id.workoutNr);
            workoutNr.setText(eventsDay.size() + "");
            TextView workoutString = (TextView) view.findViewById(R.id.workoutString);
            if (eventsDay.size() == 1) {
                workoutString.setText("workout");
            } else {
                workoutString.setText("workouts");
            }
        });


        ImageButton addEvent = (ImageButton) view.findViewById(R.id.addNewEventButton);
        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewEventPopup();
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onItemClick(int position, View v) {
        EventDTO eventDTO = adapter.getEvents().get(position);
        if (!eventDTO.concluded) {
            RunWorkoutFragment fr = new RunWorkoutFragment();

            Long id = eventDTO.getWorkoutDTO().getId();
            Bundle arg = new Bundle();
            arg.putLong("id", id);
            arg.putLong("id_event", eventDTO.eventId);
            activityInterface.changeFrag(fr, arg);
        } else {
            sharedViewModel.getToastMessageObserver().setValue("This workout was previously concluded!");
        }
    }

    @Override
    public void onLongItemClick(int position, View v) {
        createDeleteEventPopup(position);
    }

    public void NewEventPopup() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activityInterface.getMainActivity());
        final View NewEventPopUp = getLayoutInflater().inflate(R.layout.popup_add_event, null);

        Button cancel = (Button) NewEventPopUp.findViewById(R.id.cancelButton);
        Button addEvent = (Button) NewEventPopUp.findViewById(R.id.addWorkoutEvent);

        TextView time = (TextView) NewEventPopUp.findViewById(R.id.eventStartTime);
        TextView nrOfRepetitions = (TextView) NewEventPopUp.findViewById(R.id.eventRepetition);
        nrOfRepetitions.setText("1");


        ArrayList<String> workoutNames = new ArrayList<String>();
        ArrayList<WorkoutDTO> workoutDTOS = (ArrayList<WorkoutDTO>) sharedViewModel.getWorkouts().getValue();
        for (WorkoutDTO w : workoutDTOS) {
            workoutNames.add(w.getName());
        }

        Spinner workout = (Spinner) NewEventPopUp.findViewById(R.id.selectTrainingSpinner);
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(activityInterface.getMainActivity(), android.R.layout.simple_spinner_item, workoutNames);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        workout.setAdapter(typeAdapter);

        Spinner Recurrence = (Spinner) NewEventPopUp.findViewById(R.id.recurrenceSpinner);
        ArrayAdapter<String> typeAdapter2 = new ArrayAdapter<>(activityInterface.getMainActivity(), android.R.layout.simple_spinner_item, Arrays.asList("Once", "Every Day", "Every Week", "Every Month"));
        typeAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Recurrence.setAdapter(typeAdapter2);

        dialogBuilder.setView(NewEventPopUp);
        Dialog dialog = dialogBuilder.create();
        dialog.show();

        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View aview) {
                try {
                    if (workout.getSelectedItem() == null) {
                        sharedViewModel.getToastMessageObserver().setValue("You don't have a workout selected");
                    } else if (time.getText().length() != 5 || time.getText().toString().charAt(2) != ':') {
                        sharedViewModel.getToastMessageObserver().setValue("Please right a correct Hours format");
                    } else if (nrOfRepetitions.getText().length() == 0 || Integer.parseInt(nrOfRepetitions.getText().toString()) == 0) {
                        sharedViewModel.getToastMessageObserver().setValue("Repetitions must be greater than 0");
                    } else {
                        Integer hour = Integer.parseInt(time.getText().toString().split(":")[0]);
                        Integer minute = Integer.parseInt(time.getText().toString().split(":")[1]);
                        Integer repeat = Integer.parseInt(nrOfRepetitions.getText().toString());
                        String recurrence = Recurrence.getSelectedItem().toString();
                        if (recurrence == "Once") {
                            repeat = 1;
                        }
                        String workoutName = workout.getSelectedItem().toString();
                        WorkoutDTO workoutDTO = new WorkoutDTO();
                        for (WorkoutDTO wo : sharedViewModel.getWorkouts().getValue()) {
                            if (wo.getName().equalsIgnoreCase(workoutName)) {
                                workoutDTO = wo;
                            }
                        }

                        DateDTO dateDTO = selectedDate;
                        for (int i = repeat; i > 0; i--) {
                            sharedViewModel.setEventCalendar(new EventDTO(workoutDTO, dateDTO, hour, minute, i, recurrence));
                            if (recurrence.equals("Every Month")) {
                                dateDTO = dateDTO.addOneMonth();
                            } else if (recurrence.equals("Every Week")) {
                                dateDTO = dateDTO.addOneWeek();
                            } else if (recurrence.equals("Every Day")) {
                                dateDTO = dateDTO.addOneDay();
                            } else {
                                break;
                            }
                        }

                        dialog.dismiss();
                    }
                } catch (Exception e) {
                    dialog.dismiss();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    public void createDeleteEventPopup(int position) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activityInterface.getMainActivity());
        final View AddEventPopup = getLayoutInflater().inflate(R.layout.popup_delete_event, null);

        Button cancel = (Button) AddEventPopup.findViewById(R.id.cancelButton);
        Button deleteEvent = (Button) AddEventPopup.findViewById(R.id.deleteEvent);
        Button deleteEventAll = (Button) AddEventPopup.findViewById(R.id.deleteEventAll);

        dialogBuilder.setView(AddEventPopup);
        Dialog dialog = dialogBuilder.create();
        dialog.show();

        deleteEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedViewModel.deleteEventCalendar(adapter.getEvents().get(position).getEventId());
                dialog.dismiss();
            }
        });

        deleteEventAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventDTO eventDTO = adapter.getEvents().get(position);
                DateDTO dateDTO = eventDTO.getDate();
                List<EventDTO> eventDTOList = sharedViewModel.getEventsCa().getValue();

                for (int i = eventDTO.getRepetitionNr(); i > 0; i--) {
                    if (eventDTO.getRecurrence().equals("Every Month")) {
                        for (EventDTO e : eventDTOList) {
                            if (e.getDate().isEqualTo(dateDTO) && e.getRepetitionNr() == i && e.getWorkoutDTO().getName().equalsIgnoreCase(eventDTO.getWorkoutName()) && e.getHour() == eventDTO.getHour() && e.getMinute() == eventDTO.getMinute()) {
                                sharedViewModel.deleteEventCalendar(e.getEventId());
                                break;
                            }
                        }
                        dateDTO = dateDTO.addOneMonth();
                    } else if (eventDTO.getRecurrence().equals("Every Week")) {
                        for (EventDTO e : eventDTOList) {
                            if (e.getDate().isEqualTo(dateDTO) && e.getRepetitionNr() == i && e.getWorkoutDTO().getName().equalsIgnoreCase(eventDTO.getWorkoutName()) && e.getHour() == eventDTO.getHour() && e.getMinute() == eventDTO.getMinute()) {
                                sharedViewModel.deleteEventCalendar(e.getEventId());
                                break;
                            }
                        }
                        dateDTO = dateDTO.addOneWeek();
                    } else if (eventDTO.getRecurrence().equals("Every Day")) {
                        for (EventDTO e : eventDTOList) {
                            if (e.getDate().isEqualTo(dateDTO) && e.getRepetitionNr() == i && e.getWorkoutDTO().getName().equalsIgnoreCase(eventDTO.getWorkoutName()) && e.getHour() == eventDTO.getHour() && e.getMinute() == eventDTO.getMinute()) {
                                sharedViewModel.deleteEventCalendar(e.getEventId());
                                break;
                            }
                        }
                        dateDTO = dateDTO.addOneDay();
                    } else {
                        sharedViewModel.deleteEventCalendar(eventDTO.getEventId());
                        break;
                    }
                }


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
