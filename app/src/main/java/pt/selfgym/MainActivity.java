package pt.selfgym;

import static androidx.core.app.NotificationCompat.PRIORITY_HIGH;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import pt.selfgym.databinding.ActivityMainBinding;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;


import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.Date;

import pt.selfgym.Interfaces.ActivityInterface;
import pt.selfgym.dtos.WorkoutDTO;
import pt.selfgym.services.NotificationService;
import pt.selfgym.ui.calendar.CalendarFragment;
import pt.selfgym.ui.calendar.RunWorkoutFragment;
import pt.selfgym.ui.workouts.AddExerciseFragment;
import pt.selfgym.ui.workouts.EditWorkoutFragment;
import pt.selfgym.ui.workouts.WorkoutFragment;
import pt.selfgym.utils.NotificationUtil;

public class MainActivity extends AppCompatActivity implements ActivityInterface {

    private ActivityMainBinding binding;
    private SharedViewModel model;
    private NavController navController;
    Intent mServiceIntent;
    private NotificationService mService;
    private NotificationManager notificationManager;
    private NotificationCompat.Builder notificationBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        model = new ViewModelProvider(this).get(SharedViewModel.class);
        model.startDB();
        //model.deleteAllWorkouts();
        model.getToastMessageObserver().observe(this, message -> {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        });
        mService = new NotificationService();
        mServiceIntent = new Intent(this, mService.getClass());
        if (!NotificationService.IS_SERVICE_RUNNING) {
            startService(mServiceIntent);
        }

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


//        // assigning ID of the toolbar to a variable
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_calendar, R.id.navigation_workouts, R.id.navigation_statistics)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {

            @Override
            public void onDestinationChanged(@NonNull NavController navController, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                if (destination.getId() == R.id.navigation_workouts || destination.getId() == R.id.navigation_calendar || destination.getId() == R.id.navigation_statistics) {
                    navView.setVisibility(View.VISIBLE);
                } else {
                    navView.setVisibility(View.GONE);
                }
            }
        });

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Fragment f = getForegroundFragment();
        if (f instanceof EditWorkoutFragment)
            changeFrag(new WorkoutFragment(), null);
    }

    public Fragment getForegroundFragment() {
        Fragment navHostFragment = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_activity_main);
        return navHostFragment == null ? null : navHostFragment.getChildFragmentManager().getFragments().get(0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        //stopService(mServiceIntent);
//        Intent broadcastIntent = new Intent();
//        broadcastIntent.setAction("restartservice");
//        broadcastIntent.setClass(this, Restarter.class);
//        this.sendBroadcast(broadcastIntent);
        super.onDestroy();
    }

    @Override
    public MainActivity getMainActivity() {
        return this;
    }

    @Override
    public void sendNotification(String title, @Nullable String msg) {
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationBuilder = new NotificationCompat.Builder(this, "MyNotificationChannelId");
        Notification notification = notificationBuilder
                .setContentTitle(title)
                .setContentText(msg)
                .setSmallIcon(R.mipmap.ic_clock_small_black_24dp_round)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),
                        R.mipmap.ic_clock_small_black_24dp_round))
                .setPriority(PRIORITY_HIGH)
//                .setSound()
                .setAutoCancel(true)
                .build();
        notificationManager.notify((int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE), notification);
    }

    @Override
    public void msgmqttpopup(String topic, MqttMessage message){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        final View mqttmesagePopUp = getLayoutInflater().inflate(R.layout.mqtt_message_popup, null);
        Button confirm = (Button) mqttmesagePopUp.findViewById(R.id.confirmmsgbtnmqtt);
        Button cancel = (Button) mqttmesagePopUp.findViewById(R.id.cancelmsgbtnmqtt2);
        TextView topico = (TextView) mqttmesagePopUp.findViewById(R.id.topicmsgmqtt);
        TextView titulo = (TextView) mqttmesagePopUp.findViewById(R.id.titlemsgmqtt);
        WorkoutDTO workoutDTO = new Gson().fromJson(message.toString(), WorkoutDTO.class);
        topico.setText(topic);
        titulo.setText(workoutDTO.getName());
        dialogBuilder.setView(mqttmesagePopUp);
        Dialog dialog = dialogBuilder.create();
        dialog.show();

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                model.insertWorkout(workoutDTO);
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
    public void changeFrag(Fragment fr, Bundle bundle) {
        if (fr instanceof EditWorkoutFragment)
            navController.navigate(R.id.editWorkoutFragment, bundle);
        else if (fr instanceof WorkoutFragment)
            navController.navigate(R.id.navigation_workouts, bundle);
        else if (fr instanceof AddExerciseFragment) {
            navController.navigate(R.id.addExerciseFragment, bundle);
        } else if (fr instanceof CalendarFragment) {
            navController.navigate(R.id.navigation_calendar, bundle);
        } else if (fr instanceof RunWorkoutFragment) {
            navController.navigate(R.id.runExerciseFragment, bundle);
        }
    }

}