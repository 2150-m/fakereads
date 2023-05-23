package wpproject.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wpproject.project.model.Shelf;
import wpproject.project.model.Shelf;
import wpproject.project.repository.ShelfRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ShelfService {
    @Autowired
    private ShelfRepository shelfRepository;

    public Shelf findOne(String name) {
        Optional<Shelf> shelf = shelfRepository.findByName(name);
        return shelf.orElse(null);
    }

    public Shelf findOne(Long id) {
        Optional<Shelf> shelf = shelfRepository.findById(id);
        return shelf.orElse(null);
    }

    public List<Shelf> findAll() {
        return shelfRepository.findAll();
    }

    public Shelf save(Shelf shelf) {
        return shelfRepository.save(shelf);
    }
}
