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

    //#
    //# ESSENTIAL
    //#

    public List<BookGenre> findAll() {
        return repositoryGenre.findAll();
    }
    public BookGenre findOne(String name) { return repositoryGenre.findByName(name).orElse(null); }
    public BookGenre findOne(Long id) { return repositoryGenre.findById(id).orElse(null); }
    public BookGenre save(BookGenre genre) {
        return repositoryGenre.save(genre);
    }

    //#
    //# FUNCTIONAL
    //#


}
