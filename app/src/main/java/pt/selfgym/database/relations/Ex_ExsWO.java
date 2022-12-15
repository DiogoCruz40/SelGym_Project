package pt.selfgym.database.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

import pt.selfgym.database.entities.Exercise;
import pt.selfgym.database.entities.ExerciseWO;

public class Ex_ExsWO {
    @Embedded
    public Exercise exercise;
    @Relation(
            parentColumn = "exerciseId",
            entityColumn = "exercise_id"
    )
    public List<ExerciseWO> exerciseWOList;
}
