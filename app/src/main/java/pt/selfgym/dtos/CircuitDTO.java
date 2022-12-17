package pt.selfgym.dtos;

import java.util.ArrayList;

public class CircuitDTO {
    private int id;
    private int laps;
    private int rest;
    private ArrayList<ExerciseWODTO> exerciseList;

    public CircuitDTO(int id, int laps, int rest, ArrayList<ExerciseWODTO> exerciseList) {
        this.id = id;
        this.laps = laps;
        this.rest = rest;
        this.exerciseList = exerciseList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLaps() {
        return laps;
    }

    public void setLaps(int laps) {
        this.laps = laps;
    }

    public int getRest() {
        return rest;
    }

    public void setRest(int rest) {
        this.rest = rest;
    }

    public ArrayList<ExerciseWODTO> getExerciseList() {
        return exerciseList;
    }

    public void setExerciseList(ArrayList<ExerciseWODTO> exerciseList) {
        this.exerciseList = exerciseList;
    }
}
