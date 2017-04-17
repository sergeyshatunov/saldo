package by.shatunov.saldo.model;

import javafx.beans.property.*;

public class Item {
    
    private final SimpleIntegerProperty itemId;
    private final SimpleStringProperty title;
    private final SimpleDoubleProperty cost;
    private final SimpleIntegerProperty categoryId;
    private final SimpleStringProperty categoryTitle;
    private final SimpleIntegerProperty receiptId;

    public Item(SimpleIntegerProperty itemId, SimpleStringProperty title, SimpleDoubleProperty cost, SimpleIntegerProperty categoryId, SimpleStringProperty categoryTitle, SimpleIntegerProperty receiptId) {
        this.itemId = itemId;
        this.title = title;
        this.cost = cost;
        this.categoryId = categoryId;
        this.categoryTitle = categoryTitle;
        this.receiptId = receiptId;
    }

    public Item() {
        this.itemId = null;
        this.title = null;
        this.cost = null;
        this.categoryId = null;
        this.categoryTitle = null;
        this.receiptId = null;
    }

    public int getItemId() {
        return itemId.get();
    }

    public SimpleIntegerProperty itemIdProperty() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId.set(itemId);
    }

    public String getTitle() {
        return title.get();
    }

    public SimpleStringProperty titleProperty() {
        return title;
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public double getCost() {
        return cost.get();
    }

    public SimpleDoubleProperty costProperty() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost.set(cost);
    }

    public int getCategoryId() {
        return categoryId.get();
    }

    public SimpleIntegerProperty categoryIdProperty() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId.set(categoryId);
    }

    public String getCategoryTitle() {
        return categoryTitle.get();
    }

    public SimpleStringProperty categoryTitleProperty() {
        return categoryTitle;
    }

    public void setCategoryTitle(String categoryTitle) {
        this.categoryTitle.set(categoryTitle);
    }

    public int getReceiptId() {
        return receiptId.get();
    }

    public SimpleIntegerProperty receiptIdProperty() {
        return receiptId;
    }

    public void setReceiptId(int receiptId) {
        this.receiptId.set(receiptId);
    }
}
