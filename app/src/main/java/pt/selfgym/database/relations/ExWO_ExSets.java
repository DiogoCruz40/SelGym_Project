package pt.selfgym.database.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

import pt.selfgym.database.entities.ExerciseSet;
import pt.selfgym.database.entities.ExerciseWO;

public class ExWO_ExSets {
    @Embedded
    public ExerciseWO exerciseWO;
    @Relation(
            parentColumn = "exerciseWOId",
            entityColumn = "exerciseWO_id"
    )
    public List<ExerciseSet> exerciseSetList;
}
