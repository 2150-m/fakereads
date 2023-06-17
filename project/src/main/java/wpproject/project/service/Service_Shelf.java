package wpproject.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wpproject.project.model.Shelf;
import wpproject.project.repository.Repository_Shelf;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class Service_Shelf {
    @Autowired
    private Repository_Shelf repositoryShelf;

    //#
    //# ESSENTIAL
    //#

    public Shelf findOne(String name) { return repositoryShelf.findByName(name).orElse(null); }
    public Shelf findOne(Long id) { return repositoryShelf.findById(id).orElse(null); }
    public List<Shelf> findAll() {
        return repositoryShelf.findAll();
    }

    public List<Shelf> getPrimaries() { // if null is returned failed to retrive one of the shelfs
        List<Shelf> shelves = new ArrayList<>();
        Shelf p1 = repositoryShelf.findByName("WantToRead").orElse(null);
        if (p1 == null) return null;
        shelves.add(p1);
        Shelf p2 = repositoryShelf.findByName("CurrentlyReading").orElse(null);
        if (p2 == null) return null;
        shelves.add(p2);
        Shelf p3 = repositoryShelf.findByName("Read").orElse(null);
        if (p3 == null) return null;
        shelves.add(p3);
        return shelves;
    }

    public Shelf save(Shelf shelf) {
        return repositoryShelf.save(shelf);
    }
    public List<Shelf> save(List<Shelf> shelves) {
        return repositoryShelf.saveAll(shelves);
    }


    //#
    //# FUNCTIONAL
    //#




}
