package pt.selfgym.database.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

import pt.selfgym.database.entities.Event;
import pt.selfgym.database.entities.Workout;

public class WO_Events {
    @Embedded
    public Workout workout;
    @Relation(
            parentColumn = "workoutId",
            entityColumn = "workoutevents_id"
    )
    public List<Event> eventsList;
}
