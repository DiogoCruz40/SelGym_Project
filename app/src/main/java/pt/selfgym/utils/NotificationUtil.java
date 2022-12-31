package pt.selfgym.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;


public class NotificationUtil {

    private static NotificationUtil NOTIFICATIONUTIL;

    public static NotificationUtil getNotificationUtil() {

        if(NOTIFICATIONUTIL == null)
        {
            NOTIFICATIONUTIL = new NotificationUtil();
        }
        return NOTIFICATIONUTIL;
    }

    public String createNotificationChannel(NotificationManager notificationManager, String channelId, String channelName) {
        NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
        channel.setImportance(NotificationManager.IMPORTANCE_HIGH);
        channel.enableVibration(true);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        notificationManager.createNotificationChannel(channel);
        return channelId;
    }
}
