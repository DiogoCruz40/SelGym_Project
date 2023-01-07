package pt.selfgym.ui.calendar;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.TextView;

import org.joda.time.DateTime;

import java.io.IOException;
import java.util.Calendar;

import pt.selfgym.Interfaces.ActivityInterface;
import pt.selfgym.R;
import pt.selfgym.SharedViewModel;
import pt.selfgym.dtos.DateDTO;
import pt.selfgym.dtos.EventDTO;
import pt.selfgym.dtos.WorkoutDTO;
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
    private Long id, id_event;
    private boolean timerRunning = false;
    private Thread timerThread;
    private View timerPopup;
    private Dialog dialog;


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
            id_event = getArguments().getLong("id_event");
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
            RecyclerView.ItemDecoration itemDecoration = new RecyclerView.ItemDecoration() {
                @Override
                public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                    outRect.bottom = 15;
                }
            };
            recyclerView.addItemDecoration(itemDecoration);
            adapter = new RunAdapter(workout, activityInterface.getMainActivity());
            recyclerView.setNestedScrollingEnabled(false);
            recyclerView.setLayoutManager(new LinearLayoutManager(inflater.getContext()));
            recyclerView.setAdapter(adapter);
            name = (TextView) view.findViewById(R.id.workoutName);
            name.setText(workout.getName());
            type = (TextView) view.findViewById(R.id.workoutType);
            type.setText(workout.getType());
            observations = (EditText) view.findViewById(R.id.textAreaObservationsRun);
            if (workout.getObservation() != null) {
                observations.setText(workout.getObservation());
            }

        });

        this.activityInterface.getMainActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.run_workout_menu, menu);
                MenuItem concludeItem = menu.findItem(R.id.concludemenu);
                MenuItem timerItem = menu.findItem(R.id.timermenu);

                concludeItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(@NonNull MenuItem menuItem) {
                        createConcludeEventPopup();
                        return false;
                    }
                });
                timerItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(@NonNull MenuItem menuItem) {
                        createTimerPopup();
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

        DateTime dateTime = new DateTime();

        concludeEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (EventDTO e : mViewModel.getEventsCa().getValue()) {
                    if (e.getEventId().equals(id_event)) {

                        DateDTO d = e.getDate();

                        DateTime dateEvent = new DateTime(d.getYear(), d.getMonth(), d.getDay(),
                                e.getHour(), e.getMinute());

                        if(dateEvent.isBefore(dateTime)){
                            e.setConcluded(true);
                            e.getWorkoutDTO().addConclusion();
                            mViewModel.top5Workouts();
                            mViewModel.updateEventCalendar(e);
                        }
                        else{
                            mViewModel.getToastMessageObserver().setValue("You can't conclude future event");
                        }
                        break;
                    }
                }
                workout.setNrOfConclusions(workout.getNrOfConclusions() + 1);
                workout.setObservation(((EditText) view.findViewById(R.id.textAreaObservationsRun)).getText().toString());
                mViewModel.updateWorkout(workout);
                CalendarFragment fr = new CalendarFragment();
                activityInterface.changeFrag(fr, null);
                dialog.dismiss();
            }
        });

        leaveEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                workout.setObservation(((EditText) view.findViewById(R.id.textAreaObservationsRun)).getText().toString());
                mViewModel.updateWorkout(workout);
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

    public void createTimerPopup() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activityInterface.getMainActivity());
        if (timerPopup == null){
            timerPopup = getLayoutInflater().inflate(R.layout.popup_timer, null);
        }

        NumberPicker minutesPicker = (NumberPicker) timerPopup.findViewById(R.id.minutes_picker);
        minutesPicker.setMinValue(00);
        minutesPicker.setMaxValue(59);
        minutesPicker.setDisplayedValues(new String[]{"00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59"});
        minutesPicker.setWrapSelectorWheel(true);
        minutesPicker.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                return String.format("%02d", value);
            }
        });

        NumberPicker secondsPicker = (NumberPicker) timerPopup.findViewById(R.id.seconds_picker);
        secondsPicker.setMinValue(00);
        secondsPicker.setMaxValue(59);
        secondsPicker.setDisplayedValues(new String[]{"00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59"});
        secondsPicker.setWrapSelectorWheel(true);
        secondsPicker.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                return String.format("%02d", value);
            }
        });

        Button cancel = (Button) timerPopup.findViewById(R.id.cancelButton);
        ImageButton startPause = (ImageButton) timerPopup.findViewById(R.id.playAndPauseButton);
        ImageButton reset = (ImageButton) timerPopup.findViewById(R.id.refreshTimerButton);

        if (dialog == null) {
            dialogBuilder.setView(timerPopup);
            dialog = dialogBuilder.create();
        }

        dialog.show();

        class TimerThread extends Thread {
            private static final int DELAY = 1000; // 1 second
            private boolean running;
            private long time;

            public long getTime() {
                return time;
            }

            public void setTime(long time) {
                this.time = time;
            }

            public boolean isRunning() {
                return running;
            }

            @Override
            public void run() {
                while (true) {
                    if (running) {
                        if (time > 0) {
                            ((Activity) getContext()).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    minutesPicker.setValue((int) (time / 60));
                                    secondsPicker.setValue((int) (time % 60));
                                }
                            });
                            try {
                                Thread.sleep(DELAY);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            time--;
                        } else if (time == 0) {
                            running = false;
                            //warn the user
                            ((Activity) getContext()).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    startPause.setImageResource(R.drawable.play_foreground);
                                    Uri ringtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
                                    Ringtone ringtone = RingtoneManager.getRingtone(getContext(), ringtoneUri);

                                    new CountDownTimer(6100, 1000) {
                                        public void onTick(long millisUntilFinished) {
                                            ringtone.play();
                                        }
                                        public void onFinish() {
                                            ringtone.stop();
                                        }
                                    }.start();

                                    activityInterface.getMainActivity().sendNotification("Times Up!",null);

                                    minutesPicker.setEnabled(true);
                                    minutesPicker.setValue(0);
                                    secondsPicker.setEnabled(true);
                                    secondsPicker.setValue(0);
                                }
                            });
                            time--;
                        }
                    }
                }
            }

            public void pauseTimer() {
                running = false;
            }

            public void continueTimer() {
                running = true;
            }
        }

        if (timerThread == null) {
            timerThread = new TimerThread();
            ((TimerThread) timerThread).setTime(-1);
            timerThread.start();
        }

        startPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!timerRunning) {
                    if (minutesPicker.getValue() != 0 || secondsPicker.getValue() != 0) {
                        minutesPicker.setEnabled(false);
                        secondsPicker.setEnabled(false);
                        startPause.setImageResource(R.drawable.pause_foreground);
                        ((TimerThread) timerThread).setTime(minutesPicker.getValue() * 60 + secondsPicker.getValue());
                        ((TimerThread) timerThread).continueTimer();
                        timerRunning = !timerRunning;
                    }
                } else {
                    startPause.setImageResource(R.drawable.play_foreground);
                    ((TimerThread) timerThread).pauseTimer();
                    timerRunning = !timerRunning;
                }

            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                minutesPicker.setValue(0);
                minutesPicker.setEnabled(true);
                secondsPicker.setValue(0);
                secondsPicker.setEnabled(true);
                ((TimerThread) timerThread).pauseTimer();
                ((TimerThread) timerThread).setTime(-1);
                startPause.setImageResource(R.drawable.play_foreground);
                timerRunning = false;
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
