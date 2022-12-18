package pt.selfgym;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import pt.selfgym.databinding.ActivityMainBinding;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import pt.selfgym.Interfaces.ActivityInterface;
import pt.selfgym.ui.workouts.EditWorkoutFragment;
import pt.selfgym.ui.workouts.WorkoutFragment;

public class MainActivity extends AppCompatActivity implements ActivityInterface {

    private ActivityMainBinding binding;
    private SharedViewModel model;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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
        model = new ViewModelProvider(this).get(SharedViewModel.class);
//        model.startDB();
//        model.getToastMessageObserver().observe(this, message -> {
//            Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
//        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //model.startDB();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public MainActivity getMainActivity() {
        return this;
    }

    @Override
    public void changeFrag(Fragment fr) {
        if (fr instanceof EditWorkoutFragment)
            navController.navigate(R.id.editWorkoutFragment);
        else if (fr instanceof WorkoutFragment)
            navController.navigate(R.id.navigation_workouts);
    }
}