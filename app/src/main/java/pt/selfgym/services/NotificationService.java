package pt.selfgym.services;

import static androidx.core.app.NotificationCompat.PRIORITY_HIGH;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import org.joda.time.DateTime;

import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import pt.selfgym.R;
import pt.selfgym.database.AppDatabase;
import pt.selfgym.dtos.EventDTO;
import pt.selfgym.utils.NotificationUtil;

public class NotificationService extends Service {

    private static final int ID_SERVICE = 101;
    public static boolean IS_SERVICE_RUNNING = false;
    private NotificationManager notificationManager;
    private final Timer timer = new Timer();
    private String channelId;
    private NotificationCompat.Builder notificationBuilder;
    private Notification notification;
    private AppDatabase mDB;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        IS_SERVICE_RUNNING = true;
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        channelId = NotificationUtil.getNotificationUtil().createNotificationChannel(notificationManager,"MyNotificationChannelId","Notification");
        notificationBuilder = new NotificationCompat.Builder(this, channelId);
        startTasks();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!IS_SERVICE_RUNNING)
            stopSelf();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
        timer.purge();
        IS_SERVICE_RUNNING = false;
    }

    private void startTasks() {
        mDB = AppDatabase.getInstance(getApplication().getApplicationContext());


        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                //Do something after 60000ms

                List<EventDTO> eventDTOList = mDB.DAO().getEvents();

                if (eventDTOList != null) {
                    for (EventDTO eventDTO : eventDTOList) {

                        DateTime dateTimeEvent = new DateTime(
                                eventDTO.getDate().getYear(),
                                eventDTO.getDate().getMonth(),
                                eventDTO.getDate().getDay(),
                                eventDTO.getHour(),
                                eventDTO.getMinute());

                        if (checkequals15minutes(dateTimeEvent)) {
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    notification = notificationBuilder
                                            .setContentTitle("Workout " + "\"" + eventDTO.getWorkoutName() + "\"" + " Reminder")
                                            .setContentText("Your workout " + "\"" + eventDTO.getWorkoutName() + "\"" + " is in 15 minutes.")
                                            .setSmallIcon(R.mipmap.ic_launcher)
                                            .setPriority(PRIORITY_HIGH)
                                            .setAutoCancel(true)
                                            .setCategory(NotificationCompat.CATEGORY_SERVICE)
                                            .build();
                                    notificationManager.notify((int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE), notification);

                                }
                            });

                        }
                    }

                }
            }
        }, 0, 60000);
    }

    public boolean checkequals15minutes(DateTime date) {
        return date.isBefore(DateTime.now().plusMinutes(15)) && date.isAfter(DateTime.now().plusMinutes(14));
    }
}
