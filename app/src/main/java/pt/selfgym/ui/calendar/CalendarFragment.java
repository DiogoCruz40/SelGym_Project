package pt.selfgym.ui.calendar;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import pt.selfgym.Interfaces.ActivityInterface;
import pt.selfgym.Interfaces.ButtonsInterface;
import pt.selfgym.R;
import pt.selfgym.databinding.FragmentCalendarBinding;
import pt.selfgym.dtos.DateDTO;
import pt.selfgym.dtos.EventDTO;
import pt.selfgym.dtos.WorkoutDTO;
import pt.selfgym.ui.workouts.ListAdapter;

public class CalendarFragment extends Fragment implements ButtonsInterface {

    private FragmentCalendarBinding binding;
    private ActivityInterface activityInterface;
    private EventsAdapter adapter;
    private ArrayList<EventDTO> events;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activityInterface = (ActivityInterface) context;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        CalendarViewModel calendarViewModel =
//                new ViewModelProvider(this).get(CalendarViewModel.class);

        binding = FragmentCalendarBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

//        final TextView textView = binding.textHome;
//        calendarViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        //TODO: get Events from ViewModel
        events = new ArrayList<EventDTO>();
        events.add(new EventDTO(new WorkoutDTO("wo1","ob1","ty1"), new DateDTO(24,12,2022), 6, 46));
        events.add(new EventDTO(new WorkoutDTO("wo2","ob2","ty2"), new DateDTO(24,12,2022), 6, 45));
        events.add(new EventDTO(new WorkoutDTO("wo3","ob3","ty3"), new DateDTO(25,12,2022), 6, 45));



        CalendarView calendarView = view.findViewById(R.id.calendar);
        ArrayList<EventDTO> eventsDay = new ArrayList<EventDTO>();
        long millis = calendarView.getDate();

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        for (EventDTO e: events) {
            if (dayOfMonth == e.getDate().getDay() && month == e.getDate().getMonth() && year == e.getDate().getYear()){
                eventsDay.add(e);
            }
        }
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.eventList);
        adapter = new EventsAdapter(eventsDay, this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(inflater.getContext()));
        recyclerView.setAdapter(adapter);
        //não sei porquê mas sem isto aqui não coloca a lista na recycler view
        adapter.setEvents(eventsDay);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                ArrayList<EventDTO> eventsDay = new ArrayList<EventDTO>();
                for (EventDTO e: events) {
                    if (dayOfMonth == e.getDate().getDay() && (month + 1) == e.getDate().getMonth() && year == e.getDate().getYear()){
                        eventsDay.add(e);
                    }
                    adapter.setEvents(eventsDay);
                }

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

    }

    @Override
    public void onLongItemClick(int position, View v) {

    }
}