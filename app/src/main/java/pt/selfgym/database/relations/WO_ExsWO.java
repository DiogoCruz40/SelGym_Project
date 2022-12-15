package pt.selfgym.database.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

import pt.selfgym.database.entities.ExerciseWO;
import pt.selfgym.database.entities.Workout;

public class WO_ExsWO {
    @Embedded
    public Workout workout;
    @Relation(
            parentColumn = "workoutId",
            entityColumn = "workoutexwo_id"
    )
    public List<ExerciseWO> exerciseWOList;
}
