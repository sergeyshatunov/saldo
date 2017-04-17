package by.shatunov.saldo.model;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDate;
import java.util.ArrayList;

public class Receipt {

    private final SimpleIntegerProperty receiptId;
    private final SimpleObjectProperty<LocalDate> date;
    private final SimpleDoubleProperty amount;
    private final SimpleIntegerProperty accountId;
    private final SimpleIntegerProperty placeId;
    private final SimpleStringProperty placeTitle;
    private final ArrayList<Item> items;

    public Receipt(SimpleIntegerProperty receiptId, SimpleObjectProperty<LocalDate> date, SimpleDoubleProperty amount, SimpleIntegerProperty accountId, SimpleIntegerProperty placeId, SimpleStringProperty placeTitle, ArrayList<Item> items) {
        this.receiptId = receiptId;
        this.date = date;
        this.amount = amount;
        this.accountId = accountId;
        this.placeId = placeId;
        this.placeTitle = placeTitle;
        this.items = items;
    }


    public Receipt() {
        this.receiptId = new SimpleIntegerProperty(0);
        this.date = new SimpleObjectProperty<>(LocalDate.now());
        this.amount = new SimpleDoubleProperty(0.0);
        this.accountId = new SimpleIntegerProperty(0);
        this.placeId = new SimpleIntegerProperty(0);
        this.placeTitle = new SimpleStringProperty("Some place");
        this.items = new ArrayList<>();
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

    public LocalDate getDate() {
        return date.get();
    }

    public SimpleObjectProperty<LocalDate> dateProperty() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date.set(date);
    }

    public double getAmount() {
        return amount.get();
    }

    public SimpleDoubleProperty amountProperty() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount.set(amount);
    }

    public int getAccountId() {
        return accountId.get();
    }

    public SimpleIntegerProperty accountIdProperty() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId.set(accountId);
    }

    public int getPlaceId() {
        return placeId.get();
    }

    public SimpleIntegerProperty placeIdProperty() {
        return placeId;
    }

    public void setPlaceId(int placeId) {
        this.placeId.set(placeId);
    }

    public String getPlaceTitle() {
        return placeTitle.get();
    }

    public SimpleStringProperty placeTitleProperty() {
        return placeTitle;
    }

    public void setPlaceTitle(String placeTitle) {
        this.placeTitle.set(placeTitle);
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public void addItem(Item item) {
        items.add(item);
    }
}
