package wpproject.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wpproject.project.dto.BookDTO;
import wpproject.project.model.Book;
import wpproject.project.model.BookGenre;
import wpproject.project.repository.BookRepository;
import wpproject.project.repository.GenreRepository;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;

    public Book findOne(String title) {
        Optional<Book> book = bookRepository.findByTitle(title);
        return book.orElse(null);
    }

    public Book findOne(Long id) {
        Optional<Book> book = bookRepository.findById(id);
        return book.orElse(null);
    }

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public Book save(Book book) {
        return bookRepository.save(book);
    }
}
