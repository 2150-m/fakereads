package wpproject.project.dto;

import jakarta.persistence.*;
import wpproject.project.model.Book;
import wpproject.project.model.BookReview;
import wpproject.project.model.ShelfItem;

import java.util.ArrayList;
import java.util.List;

public class ShelfItemDTO {
    private Long id;
    private Book book;
    private List<BookReviewDTO> bookReviews = new ArrayList<>();

    public ShelfItemDTO(ShelfItem shelfItem) {
        this.id = shelfItem.getId();
        this.book = shelfItem.getBook();
        for (BookReview b : shelfItem.getBookReviews()) {
            bookReviews.add(new BookReviewDTO(b));
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<BookReviewDTO> getBookReviews() {
        return bookReviews;
    }

    public void setBookReviews(List<BookReviewDTO> bookReviews) {
        this.bookReviews = bookReviews;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }
}
