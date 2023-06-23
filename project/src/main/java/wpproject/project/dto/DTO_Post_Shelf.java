package wpproject.project.dto;

public class DTO_Post_Shelf {

    private String name;

    public DTO_Post_Shelf() {}

    public DTO_Post_Shelf(String name) {
        this.name = name;
    }

    public String getName() {return name; }
    public void setName(String name) { this.name = name; }
}
