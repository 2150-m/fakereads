package wpproject.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wpproject.project.model.Book;
import wpproject.project.repository.Repository_Book;

import java.util.List;
import java.util.Optional;

@Service
public class Service_Book {
    @Autowired
    private Repository_Book repositoryBook;

    public Book findOne(String title) {
        Optional<Book> book = repositoryBook.findByTitle(title);
        return book.orElse(null);
    }

    public Book findOne(Long id) {
        Optional<Book> book = repositoryBook.findById(id);
        return book.orElse(null);
    }

    public Book findByIsbn(String isbn) {
        Optional<Book> book = repositoryBook.findByIsbn(isbn);
        return book.orElse(null);
    }

    public List<Book> findAll() {
        return repositoryBook.findAll();
    }

    public Book save(Book book) {
        return repositoryBook.save(book);
    }
}
