package pt.selfgym.dtos;

public class ExerciseDTO {
    private Long exerciseId;
    private String name_exercise;
    private String urlImage;
    private String type;

    public ExerciseDTO() {

    }

    public ExerciseDTO(String name, String urlImage, String type) {
        this.name_exercise = name;
        this.urlImage = urlImage;
        this.type = type;
    }


    public Long getId() {
        return exerciseId;
    }

    public void setId(Long id) {
        this.exerciseId = id;
    }

    public String getName() {
        return name_exercise;
    }

    public void setName(String name) {
        this.name_exercise = name;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
