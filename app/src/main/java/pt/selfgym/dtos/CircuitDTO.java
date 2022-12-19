package pt.selfgym.dtos;

import java.util.ArrayList;

public class CircuitDTO {
    private Long circuitId;
    private int laps;
    private int rest;
    private ArrayList<ExerciseWODTO> exerciseList;

    public CircuitDTO(int laps, int rest, ArrayList<ExerciseWODTO> exerciseList) {
        this.circuitId = -1L;
        this.laps = laps;
        this.rest = rest;
        this.exerciseList = exerciseList;
    }

    public Long getId() {
        return circuitId;
    }

    public void setId(Long id) {
        this.circuitId = id;
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
    public void addToExerciseList(ExerciseWODTO exerciseWODTO)
    {
        if(this.exerciseList == null)
        {
            this.exerciseList = new ArrayList<ExerciseWODTO>();
        }
        exerciseList.add(exerciseWODTO);
    }
}
