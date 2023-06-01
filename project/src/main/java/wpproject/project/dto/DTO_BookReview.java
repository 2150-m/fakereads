package wpproject.project.dto;

import wpproject.project.model.BookReview;

import java.time.LocalDate;

public class DTO_BookReview {
    private double rating;
    private String text;
    private LocalDate reviewDate;
    private String author;

    public DTO_BookReview(BookReview bookReview) {
        this.rating = bookReview.getRating();
        this.text = bookReview.getText();
        this.reviewDate = bookReview.getReviewDate();
        this.author = bookReview.getAccount().getUsername();
    }
	
    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }
    public String getText() { return text;    }
    public void setText(String text) { this.text = text; }
    public LocalDate getReviewDate() { return reviewDate; }
    public void setReviewDate(LocalDate reviewDate) { this.reviewDate = reviewDate; }
    public String getAccount() { return author; }
    public void setAccount(String author) { this.author = author; }
}
