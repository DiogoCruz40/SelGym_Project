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
}
