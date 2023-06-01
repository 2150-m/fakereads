package wpproject.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wpproject.project.model.*;
import wpproject.project.repository.Repository_BookReview;

import java.util.List;
import java.util.Optional;

@Service
public class BookReviewService {
    @Autowired
    private Repository_BookReview repositoryBookReview;

    public BookReview findOne(Long id) {
        Optional<BookReview> bookReview = repositoryBookReview.findById(id);
        return bookReview.orElse(null);
    }

    public List<BookReview> findAll() {
        return repositoryBookReview.findAll();
    }

    public List<BookReview> findByAccount(Account account) { return repositoryBookReview.findByAccount(account); }

    public BookReview save(BookReview bookReview) {
        return repositoryBookReview.save(bookReview);
    }

    public void saveAll(List<BookReview> list) {
        for (BookReview r : list) {
            repositoryBookReview.save(r);
        }
    }
}
