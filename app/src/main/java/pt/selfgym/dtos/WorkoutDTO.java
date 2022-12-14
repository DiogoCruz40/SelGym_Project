package pt.selfgym.dtos;

public class WorkoutDTO {
    private int id;
    private String name;
    private String observation;
    private String type;

    public WorkoutDTO(int id, String name, String observation, String type) {
        this.id = id;
        this.name = name;
        this.observation = observation;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
