package pt.selfgym.database.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

import pt.selfgym.database.entities.Circuit;
import pt.selfgym.database.entities.Workout;

public class WO_Circuits {
    @Embedded
    public Workout workout;
    @Relation(
            parentColumn = "workoutId",
            entityColumn = "workoutcircuit_id"
    )
    public List<Circuit> circuitList;
}
