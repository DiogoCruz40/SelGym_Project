package pt.selfgym.dtos;

import java.util.ArrayList;

public class ExerciseWODTO {
    private int id;
    private int order;
    private float weight;
    private int sets;
    private int reps;
    private TimeDTO rest;
    private TimeDTO duration;
    private ExerciseDTO exercise;
    private ArrayList<SetsDTO> setsList;

    public ExerciseWODTO(int id, int order, float weight, int sets, int reps, TimeDTO rest, ExerciseDTO exercise) {
        //use in case of exercise with fixed sets and reps
        this.id = id;
        this.order = order;
        this.weight = weight;
        this.sets = sets;
        this.reps = reps;
        this.rest = rest;
        this.duration = null;
        this.exercise = exercise;
        this.setsList = null;
    }

    public ExerciseWODTO(int id, int order, float weight, int sets, TimeDTO rest, TimeDTO duration, ExerciseDTO exercise) {
        //use in case of exercise with fixed sets and reps
        this.id = id;
        this.order = order;
        this.weight = weight;
        this.sets = sets;
        this.reps = 0;
        this.rest = rest;
        this.duration = duration;
        this.exercise = exercise;
        this.setsList = null;
    }

    public ExerciseWODTO(int id, int order, float weight, int reps, TimeDTO rest, ExerciseDTO exercise, ArrayList<SetsDTO> setsList) {
        //use in case of exercise with variable sets and reps
        this.id = id;
        this.order = order;
        this.weight = weight;
        this.sets = 0;
        this.reps = reps;
        this.rest = rest;
        this.duration = null;
        this.exercise = exercise;
        this.setsList = setsList;
    }

    public ExerciseWODTO(int id, int order, float weight, TimeDTO rest, TimeDTO duration, ExerciseDTO exercise, ArrayList<SetsDTO> setsList) {
        //use in case of exercise with variable sets and time
        this.id = id;
        this.order = order;
        this.weight = weight;
        this.sets = 0;
        this.reps = 0;
        this.rest = rest;
        this.duration = duration;
        this.exercise = exercise;
        this.setsList = setsList;
    }

    public boolean isFixedSetsReps(){
        if(setsList == null && duration == null){
            return true;
        } else {
            return false;
        }
    }

    public boolean isFixedSetsTime(){
        if(setsList == null && reps == 0){
            return true;
        } else {
            return false;
        }
    }

    public boolean isVariableSetsReps(){
        if(sets == 0 && duration == null){
            return true;
        } else {
            return false;
        }
    }

    public boolean isVariableSetsTime(){
        if(sets == 0 && reps == 0){
            return true;
        } else {
            return false;
        }
    }
}
