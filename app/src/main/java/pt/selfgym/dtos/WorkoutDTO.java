package pt.selfgym.dtos;

import java.util.ArrayList;

public class WorkoutDTO {
    private Long workoutId;
    private String name_wo;
    private String observation;
    private String type;
    private ArrayList<Object> workoutComposition;

    public WorkoutDTO(){

    }
    public WorkoutDTO(String name, String observation, String type) {
        this.workoutId = -1L;
        this.name_wo = name;
        this.observation = observation;
        this.type = type;
        this.workoutComposition = new ArrayList<Object>();
    }

    public String getName() {
        return name_wo;
    }

    public void setName(String name) {
        this.name_wo = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getId() {
        return workoutId;
    }

    public void setId(Long id) {
        this.workoutId = id;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public ArrayList<Object> getWorkoutComposition() {
        return workoutComposition;
    }

    public void setWorkoutComposition(ArrayList<Object> workoutComposition) {
        this.workoutComposition = workoutComposition;
    }

    public void addToWorkoutComposition(Object ex){
        if(this.workoutComposition == null)
        {
            this.workoutComposition = new ArrayList<Object>();
        }
        workoutComposition.add(ex);
    }
}
