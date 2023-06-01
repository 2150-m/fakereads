package wpproject.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wpproject.project.model.Book;
import wpproject.project.model.ShelfItem;
import wpproject.project.repository.BookRepository;
import wpproject.project.repository.ShelfItemRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ShelfItemService {
    @Autowired
    private ShelfItemRepository shelfItemRepository;
    @Autowired
    private BookRepository bookRepository;

    public ShelfItem findByBook(Book book) {
        return shelfItemRepository.findByBook(book);
    }

    public ShelfItem findOne(Long id) {
        Optional<ShelfItem> item = shelfItemRepository.findById(id);
        return item.orElse(null);
    }

    public List<ShelfItem> findAll() {
        return shelfItemRepository.findAll();
    }

    public ShelfItem save(ShelfItem item) {
        return shelfItemRepository.save(item);
    }
}
