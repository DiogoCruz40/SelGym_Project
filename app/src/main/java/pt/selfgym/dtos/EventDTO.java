package pt.selfgym.dtos;

import java.util.Date;

public class EventDTO {
    public Long eventId;
    public DateDTO date;
    public Integer hour, minute;


    public Integer repetitionNr;

    public Boolean concluded;


    public String recurrence;

    public WorkoutDTO workoutDTO;

    public EventDTO() {

    }

    public EventDTO(WorkoutDTO workoutDTO, DateDTO date, Integer hour, Integer minute, Integer repetitionNr, String recurrence) {
        this.workoutDTO = workoutDTO;
        this.date = date;
        this.hour = hour;
        this.minute = minute;
        this.concluded = false;
        this.repetitionNr = repetitionNr;
        this.recurrence = recurrence;
    }

    public String getWorkoutName() {
        return workoutDTO.getName();
    }

    public String getWorkoutType() {
        return workoutDTO.getType();
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public void setConcluded(Boolean concluded) {
        this.concluded = concluded;
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

    public String getRecurrence() {
        return recurrence;
    }

    public Integer getRepetitionNr() {
        return repetitionNr;
    }

    public WorkoutDTO getWorkoutDTO() {
        return workoutDTO;
    }

    public void setWorkoutDTO(WorkoutDTO workoutDTO) {
        this.workoutDTO = workoutDTO;
    }
}
