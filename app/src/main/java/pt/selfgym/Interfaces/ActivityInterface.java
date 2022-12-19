package pt.selfgym.Interfaces;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import pt.selfgym.MainActivity;
import pt.selfgym.ui.workouts.EditWorkoutFragment;


public interface ActivityInterface {
    MainActivity getMainActivity();
    void changeFrag(Fragment fr, Bundle bundle);
}
