package pt.selfgym.dtos;

import java.util.ArrayList;
import java.util.List;

public class ExerciseWODTO {
    private Long exerciseWOId;
    private int order_exwo;
    private double weight;
    private int sets;
    private int reps;
    private int rest;
    private int duration;
    private ExerciseDTO exercise;
    private List<SetsDTO> setsList;

    public ExerciseWODTO(Long id, int order, double weight, int sets, int reps, int rest, ExerciseDTO exercise) {
        //use in case of exercise with fixed sets and reps
        this.exerciseWOId = id;
        this.order_exwo = order;
        this.weight = weight;
        this.sets = sets;
        this.reps = reps;
        this.rest = rest;
        this.duration = 0;
        this.exercise = exercise;
        this.setsList = null;
    }

    public ExerciseWODTO(Long id, int order, double weight, int sets, int rest, ExerciseDTO exercise, int duration) {
        //use in case of exercise with fixed sets and reps
        this.exerciseWOId = id;
        this.order_exwo = order;
        this.weight = weight;
        this.sets = sets;
        this.reps = 0;
        this.rest = rest;
        this.duration = duration;
        this.exercise = exercise;
        this.setsList = null;
    }

    public ExerciseWODTO(Long id, int order,int reps, ExerciseDTO exercise, ArrayList<SetsDTO> setsList) {
        //use in case of exercise with variable sets and reps
        this.exerciseWOId = id;
        this.order_exwo = order;
        this.weight = 0;
        this.sets = 0;
        this.reps = -1;
        this.rest = 0;
        this.duration = 0;
        this.exercise = exercise;
        this.setsList = setsList;
    }

    public ExerciseWODTO(Long id, int order, ExerciseDTO exercise, int duration, ArrayList<SetsDTO> setsList) {
        //use in case of exercise with variable sets and time
        this.exerciseWOId = id;
        this.order_exwo = order;
        this.weight = 0;
        this.sets = 0;
        this.reps = 0;
        this.rest = 0;
        this.duration = -1;
        this.exercise = exercise;
        this.setsList = setsList;
    }

    public boolean isFixedSetsReps(){
        if(setsList == null && duration == 0){
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
        if(sets == 0 && duration == 0){
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

    public Long getId() {
        return exerciseWOId;
    }

    public void setId(Long id) {
        this.exerciseWOId = id;
    }

    public int getOrder() {
        return order_exwo;
    }

    public void setOrder(int order) {
        this.order_exwo = order;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public int getSets() {
        return sets;
    }

    public void setSets(int sets) {
        this.sets = sets;
    }

    public int getReps() {
        return reps;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

    public int getRest() {
        return rest;
    }

    public void setRest(int rest) {
        this.rest = rest;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public ExerciseDTO getExercise() {
        return exercise;
    }

    public void setExercise(ExerciseDTO exercise) {
        this.exercise = exercise;
    }

    public List<SetsDTO> getSetsList() {
        return setsList;
    }

    public void setSetsList(List<SetsDTO> setsList) {
        this.setsList = setsList;
    }
}
