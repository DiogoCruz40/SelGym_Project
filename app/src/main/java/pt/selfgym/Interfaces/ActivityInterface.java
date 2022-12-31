package pt.selfgym.Interfaces;


import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.eclipse.paho.client.mqttv3.MqttMessage;

import pt.selfgym.MainActivity;
import pt.selfgym.ui.workouts.EditWorkoutFragment;


public interface ActivityInterface {
    MainActivity getMainActivity();
    void sendNotification(String title, @Nullable String msg);
    void msgmqttpopup(String topic, MqttMessage message);
    void changeFrag(Fragment fr, Bundle bundle);
}
