package pt.selfgym.dtos;

import java.util.Date;

public class EventDTO {
    public WorkoutDTO workoutDTO;
    public DateDTO date;
    public Integer hour, minute;
    public long id;
    public Integer repetitionNr;
    public String recurrence;


    public EventDTO(WorkoutDTO workoutDTO, DateDTO date, Integer hour, Integer minute , Integer repetitionNr, String recurrence) {
        this.workoutDTO = workoutDTO;
        this.date = date;
        this.hour = hour;
        this.minute = minute;
        this.repetitionNr = repetitionNr;
        this.recurrence = recurrence;
    }

    public String getWorkoutName(){
        return workoutDTO.getName();
    }

    public String getWorkoutType(){
        return workoutDTO.getType();
    }

    public Integer getHour() {
        return hour;
    }

    public void setHour(Integer hour) {
        this.hour = hour;
    }

    public Integer getMinute() {
        return minute;
    }

    public void setMinute(Integer minute) {
        this.minute = minute;
    }

    public DateDTO getDate() {
        return date;
    }

    public void setDate(DateDTO date) {
        this.date = date;
    }
}
