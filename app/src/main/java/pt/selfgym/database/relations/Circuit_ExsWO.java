package pt.selfgym.database.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

import pt.selfgym.database.entities.Circuit;
import pt.selfgym.database.entities.ExerciseWO;

public class Circuit_ExsWO {
    @Embedded
    public Circuit circuit;
    @Relation(
            parentColumn = "circuitId",
            entityColumn = "circuitexwo_id"
    )
    public List<ExerciseWO> exerciseWOList;
}
