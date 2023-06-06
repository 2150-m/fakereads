package wpproject.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wpproject.project.model.Book;
import wpproject.project.model.BookGenre;
import wpproject.project.repository.Repository_Genre;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Service
public class Service_BookGenre {
    @Autowired
    private Repository_Genre repositoryGenre;

    public List<BookGenre> findAll() {
        return repositoryGenre.findAll();
    }

    public BookGenre findOne(String name) {
        Optional<BookGenre> genre = repositoryGenre.findByName(name);
        return genre.orElse(null);
    }

    public BookGenre findOne(Long id) {
        Optional<BookGenre> genre = repositoryGenre.findById(id);
        return genre.orElse(null);
    }

    public BookGenre save(BookGenre genre) {
        return repositoryGenre.save(genre);
    }
}
