package pt.selfgym.database;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

// Entities and DAOs
import pt.selfgym.database.daos.DAO;


import pt.selfgym.database.entities.Circuit;
import pt.selfgym.database.entities.Event;
import pt.selfgym.database.entities.Exercise;
import pt.selfgym.database.entities.ExerciseSet;
import pt.selfgym.database.entities.ExerciseWO;
import pt.selfgym.database.entities.Workout;


@Database(entities = {Exercise.class, Circuit.class, Event.class, ExerciseWO.class, ExerciseSet.class, Workout.class}, exportSchema = false, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static final String LOG_TAG = AppDatabase.class.getSimpleName();
    private static final String DB_NAME = "selfgym_db";
    private static AppDatabase sInstance;
    private static final Object LOCK = new Object();

    public static synchronized AppDatabase getInstance(Context context)
    {
        if (sInstance == null) {
            // only allows one thread at a time to run the follow operations
            synchronized (LOCK) {
                Log.d(LOG_TAG, "Creating new database instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                                AppDatabase.class, AppDatabase.DB_NAME)
                        .build();
            }
        }
        Log.d(LOG_TAG, "Getting the database instance");
        return sInstance;
    }

    public abstract DAO DAO();
}
