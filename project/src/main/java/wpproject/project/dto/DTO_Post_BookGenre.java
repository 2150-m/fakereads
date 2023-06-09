package wpproject.project.dto;

public class DTO_Post_BookGenre {

    private String name;

    public DTO_Post_BookGenre() {}

    public DTO_Post_BookGenre(String name) {
        this.name = name;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
