//package wpproject.project.dto;
//
//import jakarta.persistence.*;
//import wpproject.project.model.Book;
//import wpproject.project.model.BookReview;
//import wpproject.project.model.ShelfItem;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class ShelfItemDTO {
//    private Long id;
//    private List<BookReviewDTO> bookReviewsDTO;
//    private Book book;
//
//    public ShelfItemDTO(ShelfItem item) {
//        this.id = item.getId();
//        this.book = item.getBook();
//        convertToDTOList(item.getBookReviews()); // BookReview list but without the account variable
//    }
//
//    private void convertToDTOList(List<BookReview> originalReviews) {
//        this.bookReviewsDTO = new ArrayList<>(originalReviews.size());
//
//        for (int i = 0; i < originalReviews.size(); i++) {
//            this.bookReviewsDTO.get(i).setId(originalReviews.get(i).getId());
//            this.bookReviewsDTO.get(i).setReviewDate(originalReviews.get(i).getReviewDate());
//            this.bookReviewsDTO.get(i).setRating(originalReviews.get(i).getRating());
//            this.bookReviewsDTO.get(i).setText(originalReviews.get(i).getText());
//        }
//    }
//
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public List<BookReviewDTO> getBookReviews() {
//        return bookReviewsDTO;
//    }
//
//    public void setBookReviews(List<BookReviewDTO> bookReviewsDTO) {
//        this.bookReviewsDTO = bookReviewsDTO;
//    }
//
//    public Book getBook() {
//        return book;
//    }
//
//    public void setBook(Book book) {
//        this.book = book;
//    }
//}
