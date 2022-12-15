package pt.selfgym.dtos;

public class ExerciseDTO {
    private int id;
    private String name;
    private String urlImage;
    private String type;

    public ExerciseDTO(int id, String name, String urlImage, String type) {
        this.id = id;
        this.name = name;
        this.urlImage = urlImage;
        this.type = type;
    }
}
