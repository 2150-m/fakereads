package wpproject.project.dto;

import wpproject.project.model.BookReview;

import java.time.LocalDate;

public class DTO_BookReviewNoShelves {
    private Long id;
    private double rating;
    private String text;
    private LocalDate reviewDate;
    private DTO_AccountNoShelves account;

    public DTO_BookReviewNoShelves(BookReview review) {
        this.id = review.getId();
        this.rating = review.getRating();
        this.text = review.getText();
        this.reviewDate = review.getReviewDate();
        if (review.getAccount() != null) {
            this.account = new DTO_AccountNoShelves(review.getAccount());
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
    public LocalDate getReviewDate() { return reviewDate; }
    public void setReviewDate(LocalDate reviewDate) { this.reviewDate = reviewDate; }
    public DTO_AccountNoShelves getAccount() { return account; }
    public void setAccount(DTO_AccountNoShelves account) { this.account = account; }
}
