package pt.selfgym.dtos;

import java.util.Date;

public class EventDTO {
    public WorkoutDTO workoutDTO;
    public DateDTO date;
    public Integer hour, minute;
    public long id;


    public EventDTO(WorkoutDTO workoutDTO, DateDTO date, Integer hour, Integer minute) {
        this.workoutDTO = workoutDTO;
        this.date = date;
        this.hour = hour;
        this.minute = minute;
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
