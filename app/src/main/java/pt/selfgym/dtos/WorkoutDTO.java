package pt.selfgym.dtos;

import java.util.ArrayList;

public class WorkoutDTO {
    private int id;
    private String name;
    private String observation;
    private String type;
    private ArrayList<Object> workoutComposition;

    public WorkoutDTO(int id, String name, String observation, String type) {
        this.id = id;
        this.name = name;
        this.observation = observation;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}
