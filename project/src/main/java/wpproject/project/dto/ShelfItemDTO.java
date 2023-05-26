package wpproject.project.dto;

import jakarta.persistence.*;
import wpproject.project.model.Book;
import wpproject.project.model.BookReview;
import wpproject.project.model.ShelfItem;

import java.util.ArrayList;
import java.util.List;

public class ShelfItemDTO {
    private Long id;
    private List<BookReview> bookReviews = new ArrayList<>();
    private Book book;

    public ShelfItemDTO(ShelfItem shelfItem) {
        this.id = shelfItem.getId();
        this.bookReviews = shelfItem.getBookReviews();
        this.book = shelfItem.getBook();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<BookReview> getBookReviews() {
        return bookReviews;
    }

    public void setBookReviews(List<BookReview> bookReviews) {
        this.bookReviews = bookReviews;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }
}
