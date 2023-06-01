package wpproject.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wpproject.project.model.Shelf;
import wpproject.project.repository.Repository_Shelf;

import java.util.List;
import java.util.Optional;

@Service
public class ShelfService {
    @Autowired
    private Repository_Shelf repositoryShelf;

    public Shelf findOne(String name) {
        Shelf shelf = repositoryShelf.findByName(name);
        if (shelf != null) { return shelf; }
        return null;
    }

    public Shelf findOne(Long id) {
        Optional<Shelf> shelf = repositoryShelf.findById(id);
        return shelf.orElse(null);
    }

    public List<Shelf> findAll() {
        return repositoryShelf.findAll();
    }

    public List<Shelf> getPrimaries() {
        List<Shelf> shelves = List.of(repositoryShelf.findByName("WantToRead"), repositoryShelf.findByName("CurrentlyReading"), repositoryShelf.findByName("Read"));
        if (shelves != null) { return shelves; }
        return null;
    }

    public Shelf save(Shelf shelf) {
        return repositoryShelf.save(shelf);
    }

    public List<Shelf> save(List<Shelf> shelves) {
        return repositoryShelf.saveAll(shelves);
    }

}
