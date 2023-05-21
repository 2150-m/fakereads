package wpproject.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wpproject.project.model.BookGenre;
import wpproject.project.repository.GenreRepository;

import java.util.List;
import java.util.Optional;

@Service
public class BookGenreService {
    @Autowired
    private GenreRepository genreRepository;

    public List<BookGenre> findAll() {
        return genreRepository.findAll();
    }

    public BookGenre findOne(String name) {
        Optional<BookGenre> genre = genreRepository.findByName(name);
        return genre.orElse(null);
    }

    public BookGenre findOne(Long id) {
        Optional<BookGenre> genre = genreRepository.findById(id);
        return genre.orElse(null);
    }
}
