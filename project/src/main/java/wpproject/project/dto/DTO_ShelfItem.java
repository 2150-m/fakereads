package wpproject.project.dto;

import wpproject.project.model.Book;
import wpproject.project.model.BookReview;
import wpproject.project.model.ShelfItem;

import java.util.ArrayList;
import java.util.List;

public class DTO_ShelfItem {
    private Long id;
    private Book book;
    private List<DTO_BookReview> bookReviews = new ArrayList<>();

    public DTO_ShelfItem(ShelfItem shelfItem) {
        this.id = shelfItem.getId();
        this.book = shelfItem.getBook();
        for (BookReview b : shelfItem.getBookReviews()) {
            bookReviews.add(new DTO_BookReview(b));
        }
    }
	public Long getId() { return id;}
	public void setId(Long id) { this.id = id;}
	public List<DTO_BookReview> getBookReviews() { return bookReviews;}
	public void setBookReviews(List<DTO_BookReview> bookReviews) { this.bookReviews = bookReviews;}
	public Book getBook() { return book;}
	public void setBook(Book book) { this.book = book;}
}
