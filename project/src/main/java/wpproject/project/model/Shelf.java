package wpproject.project.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Shelf implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column
    protected String name;

    @Column
    protected boolean isPrimary;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "SHELF_AND_ITEMS",
            joinColumns = @JoinColumn(name = "shelf_id"),
            inverseJoinColumns = @JoinColumn(name = "shelf_item_id")
    )
    private List<ShelfItem> shelfItems = new ArrayList<>();

    public Shelf() {}

    public Shelf(String name, boolean isPrimary) {
        this.name = name;
        this.isPrimary = isPrimary;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public boolean isPrimary() {
        return isPrimary;
    }
    public void setPrimary(boolean primary) {
        isPrimary = primary;
    }
    public List<ShelfItem> getShelfItems() {
        return shelfItems;
    }
    public void setShelfItems(List<ShelfItem> shelfItems) {
        this.shelfItems = shelfItems;
    }
}
