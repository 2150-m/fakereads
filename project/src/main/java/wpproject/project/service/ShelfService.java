package wpproject.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wpproject.project.model.Shelf;
import wpproject.project.model.Shelf;
import wpproject.project.repository.ShelfRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
public class ShelfService {
    @Autowired
    private ShelfRepository shelfRepository;

    public Shelf findOne(String name) {
        Shelf shelf = shelfRepository.findByName(name);
        if (shelf != null) { return shelf; }
        return null;
    }

    public Shelf findOne(Long id) {
        Optional<Shelf> shelf = shelfRepository.findById(id);
        return shelf.orElse(null);
    }

    public List<Shelf> findAll() {
        return shelfRepository.findAll();
    }

    public List<Shelf> getPrimaries() {
        List<Shelf> shelves = List.of(shelfRepository.findByName("WantToRead"), shelfRepository.findByName("CurrentlyReading"), shelfRepository.findByName("Read"));
        if (shelves != null) { return shelves; }
        return null;
    }

    public Shelf save(Shelf shelf) {
        return shelfRepository.save(shelf);
    }

    public List<Shelf> save(List<Shelf> shelves) {
        return shelfRepository.saveAll(shelves);
    }

}
