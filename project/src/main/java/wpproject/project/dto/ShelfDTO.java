//package wpproject.project.dto;
//
//import jakarta.persistence.*;
//import wpproject.project.model.Shelf;
//import wpproject.project.model.ShelfItem;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class ShelfDTO {
//    private Long id;
//    private String name;
//    private boolean isPrimary;
//    private List<ShelfItemDTO> shelfItemsDTO = new ArrayList<>();
//
//    public ShelfDTO(String name, boolean isPrimary) {
//        this.name = name;
//        this.isPrimary = isPrimary;
//    }
//
//    public ShelfDTO(Shelf shelf) {
//        this.id = shelf.getId();
//        this.name = shelf.getName();
//        this.isPrimary = shelf.isPrimary();
////        convertToDTOList(shelf.getShelfItems());
//    }
//
//    private void convertToDTOList(List<ShelfItemDTO> items) {
//        this.shelfItemsDTO = new ArrayList<>(items.size());
//
//        for (int i = 0; i < items.size(); i++) {
//            this.shelfItemsDTO.get(i).setId(items.get(i).getId());
//            this.shelfItemsDTO.get(i).setBook(items.get(i).getBook());
//            this.shelfItemsDTO.get(i).setBookReviews(items.get(i).getBookReviews());
//        }
//    }
//
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public boolean isPrimary() {
//        return isPrimary;
//    }
//
//    public void setPrimary(boolean primary) {
//        isPrimary = primary;
//    }
//
//    public List<ShelfItemDTO> getShelfItemsDTO() {
//        return shelfItemsDTO;
//    }
//
//    public void setShelfItemsDTO(List<ShelfItemDTO> shelfItemsDTO) {
//        this.shelfItemsDTO = shelfItemsDTO;
//    }
//}
