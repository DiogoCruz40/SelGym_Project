package pt.selfgym.dtos;

import java.util.Date;

public class EventDTO {
    public WorkoutDTO workoutDTO;
    public Date date;



    public String getWorkoutName(){
        return workoutDTO.getName();
    }

    public String getTypeName(){
        return workoutDTO.getType();
    }
}
