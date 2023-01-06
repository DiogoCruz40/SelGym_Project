package pt.selfgym.dtos;

import java.util.ArrayList;

public class WorkoutDTO {
    private Long workoutId;
    private String name_wo;
    private String observations;
    private String type;
    private int nrOfConclusions;
    private ArrayList<Object> workoutComposition;

    public WorkoutDTO(){

    }

    public WorkoutDTO(String name, String observations, String type) {
        this.name_wo = name;
        this.observations = observations;
        this.type = type;
        this.workoutComposition = new ArrayList<Object>();
        this.nrOfConclusions = 0;
    }

    public WorkoutDTO(String name, String observations, String type, int nrOfConclusions) {
        this.name_wo = name;
        this.observations = observations;
        this.type = type;
        this.workoutComposition = new ArrayList<Object>();
        this.nrOfConclusions = nrOfConclusions;
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
        return observations;
    }

    public void setObservation(String observations) {
        this.observations = observations;
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

    public int getNrOfConclusions(){
        return this.nrOfConclusions;
    }

    public void setNrOfConclusions(int nrOfConclusions) {
        this.nrOfConclusions = nrOfConclusions;
    }

    public void addConclusion() {
        this.nrOfConclusions++;
    }
}
