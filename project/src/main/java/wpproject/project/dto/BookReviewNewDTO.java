package wpproject.project.dto;

import jakarta.persistence.*;
import wpproject.project.model.Account;
import wpproject.project.model.BookReview;

import java.time.LocalDate;

public class BookReviewNewDTO {
    private Long id;
    private double rating;
    private String text;

    public BookReviewNewDTO(double rating, String text) {
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
