package wpproject.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wpproject.project.model.Book;
import wpproject.project.model.ShelfItem;
import wpproject.project.repository.Repository_Book;

import java.util.List;
import java.util.Optional;

@Service
public class Service_Book {
    @Autowired
    private Repository_Book repositoryBook;

    //#
    //# ESSENTIAL
    //#

    public Book findOne(String title) { return repositoryBook.findByTitle(title).orElse(null); }
    public Book findOne(Long id) { return repositoryBook.findById(id).orElse(null); }
    public Book findByIsbn(String isbn) { return repositoryBook.findByIsbn(isbn).orElse(null); }
    public List<Book> findAll() {
        return repositoryBook.findAll();
    }
    public Book save(Book book) {
        return repositoryBook.save(book);
    }
    public void remove(Book b) {
        repositoryBook.delete(b);
    }

    //#
    //# FUNCTIONAL
    //#




}
