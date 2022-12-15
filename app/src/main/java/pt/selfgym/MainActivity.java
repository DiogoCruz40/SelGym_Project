package pt.selfgym;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import pt.selfgym.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import pt.selfgym.Interfaces.ActivityInterface;

public class MainActivity extends AppCompatActivity implements ActivityInterface {

    private ActivityMainBinding binding;
    private SharedViewModel model;
    private FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fm = getSupportFragmentManager();

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_calendar, R.id.navigation_workouts, R.id.navigation_statistics)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
        model  = new ViewModelProvider(this).get(SharedViewModel.class);
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
        fm.beginTransaction().replace(R.id.container, fr).addToBackStack(null).commit();
    }

}