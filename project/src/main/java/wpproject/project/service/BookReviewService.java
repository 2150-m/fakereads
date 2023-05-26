package wpproject.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wpproject.project.model.*;
import wpproject.project.repository.BookRepository;
import wpproject.project.repository.BookReviewRepository;

import java.util.List;
import java.util.Optional;

@Service
public class BookReviewService {
    @Autowired
    private BookReviewRepository bookReviewRepository;

    public BookReview findOne(Long id) {
        Optional<BookReview> bookReview = bookReviewRepository.findById(id);
        return bookReview.orElse(null);
    }

    public List<BookReview> findAll() {
        return bookReviewRepository.findAll();
    }

    public List<BookReview> findByAccount(Account account) { return bookReviewRepository.findByAccount(account); }

    public BookReview save(BookReview bookReview) {
        return bookReviewRepository.save(bookReview);
    }
}
