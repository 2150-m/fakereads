package wpproject.project.dto;

public class BookReviewDTO_New {
    private Long id;
    private double rating;
    private String text;

    public BookReviewDTO_New(double rating, String text) {
        this.rating = rating;
        this.text = text;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
}
