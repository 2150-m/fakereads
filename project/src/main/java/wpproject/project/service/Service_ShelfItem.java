package wpproject.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wpproject.project.model.Book;
import wpproject.project.model.BookReview;
import wpproject.project.model.ShelfItem;
import wpproject.project.repository.Repository_Book;
import wpproject.project.repository.Repository_ShelfItem;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
public class Service_ShelfItem {
    @Autowired
    private Repository_ShelfItem repositoryShelfItem;
    @Autowired
    private Repository_Book repositoryBook;


    //#
    //# ESSENTIAL
    //#

    public ShelfItem findByBook(Book book) {
        return repositoryShelfItem.findByBook(book);
    }
    public ShelfItem findOne(Long id) { return repositoryShelfItem.findById(id).orElse(null); }
    public List<ShelfItem> findAll() {
        return repositoryShelfItem.findAll();
    }
    public ShelfItem save(ShelfItem item) {
        return repositoryShelfItem.save(item);
    }
    public void remove(ShelfItem i) {
        repositoryShelfItem.delete(i);
    }

    //#
    //# FUNCTIONAL
    //#

}
