package pt.selfgym.dtos;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DateDTO {
    private int day, month, year;

    public DateDTO(int day, int month, int year) {
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public DateDTO addOneMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.MONTH, month - 1);  // Calendar.MONTH is 0-based
        calendar.set(Calendar.YEAR, year);
        calendar.add(Calendar.MONTH, 1);

        // Extract the day, month, and year from the updated date
        int updatedDay = calendar.get(Calendar.DAY_OF_MONTH);
        int updatedMonth = calendar.get(Calendar.MONTH) + 1;  // Calendar.MONTH is 0-based
        int updatedYear = calendar.get(Calendar.YEAR);

        // Create and return a new DateDTO object with the updated date
        return new DateDTO(updatedDay, updatedMonth, updatedYear);
    }

    public DateDTO addOneWeek() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.MONTH, month - 1);  // Calendar.MONTH is 0-based
        calendar.set(Calendar.YEAR, year);
        calendar.add(Calendar.WEEK_OF_YEAR, 1);

        // Extract the day, month, and year from the updated date
        int updatedDay = calendar.get(Calendar.DAY_OF_MONTH);
        int updatedMonth = calendar.get(Calendar.MONTH) + 1;  // Calendar.MONTH is 0-based
        int updatedYear = calendar.get(Calendar.YEAR);

        // Create and return a new DateDTO object with the updated date
        return new DateDTO(updatedDay, updatedMonth, updatedYear);
    }

    public DateDTO addOneDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.MONTH, month - 1);  // Calendar.MONTH is 0-based
        calendar.set(Calendar.YEAR, year);
        calendar.add(Calendar.DATE, 1);

        // Extract the day, month, and year from the updated date
        int updatedDay = calendar.get(Calendar.DAY_OF_MONTH);
        int updatedMonth = calendar.get(Calendar.MONTH) + 1;  // Calendar.MONTH is 0-based
        int updatedYear = calendar.get(Calendar.YEAR);

        // Create and return a new DateDTO object with the updated date
        return new DateDTO(updatedDay, updatedMonth, updatedYear);
    }

    public int compareDate(DateDTO date) {

        Calendar actual = Calendar.getInstance();
        actual.set(Calendar.DAY_OF_MONTH, this.day);
        actual.set(Calendar.MONTH, this.month - 1);
        actual.set(Calendar.YEAR, this.year);

        Calendar other = Calendar.getInstance();
        other.set(Calendar.DAY_OF_MONTH, date.getDay());
        other.set(Calendar.MONTH, date.getMonth() - 1);
        other.set(Calendar.YEAR, date.getYear());

        return actual.compareTo(other);
    }

    public int difDays(DateDTO date) {

        Calendar actual = Calendar.getInstance();
        actual.set(Calendar.DAY_OF_MONTH, this.day);
        actual.set(Calendar.MONTH, this.month - 1);
        actual.set(Calendar.YEAR, this.year);

        Calendar other = Calendar.getInstance();
        other.set(Calendar.DAY_OF_MONTH, date.getDay());
        other.set(Calendar.MONTH, date.getMonth() - 1);
        other.set(Calendar.YEAR, date.getYear());

        return actual.get(Calendar.DAY_OF_YEAR) - other.get(Calendar.DAY_OF_YEAR);
    }

    public boolean isEqualTo(DateDTO other) {
        return this.day == other.day && this.month == other.month && this.year == other.year;
    }

}
