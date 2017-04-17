package by.shatunov.saldo.view;

import by.shatunov.saldo.model.Item;
import by.shatunov.saldo.model.Receipt;
import by.shatunov.saldo.util.DateUtil;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.ArrayList;

public class ReceiptEditDialogController {

    @FXML
    private TextField placeTextField;
    @FXML
    private TextField dateTextField;
    @FXML
    private GridPane itemsGridPane;
    @FXML
    private Button addItemButton;

    private Stage dialogStage;
    private Receipt receipt;
    private boolean okClicked = false;
    private int row;

    private ObservableList<Node> children;
    private ObservableList<String> placesList = FXCollections.observableArrayList();

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setReceipt(Receipt receipt, ArrayList<Receipt> receiptList) {
        this.receipt = receipt;

        for (Receipt receipt1 : receiptList) {
            placesList.add(receipt1.getPlaceTitle());
        }

        placeTextField.setText(receipt.getPlaceTitle());
        dateTextField.setText(DateUtil.format(receipt.getDate()));

        row = 0;
        for (Item item : receipt.getItems()) {
            itemsGridPane.addRow(row, new TextField(item.getTitle()), new TextField(Double.toString(item.getCost())));
            row++;
        }

        addItemButton.setOnMouseClicked(event -> {row++; itemsGridPane.addRow(row, new TextField(), new TextField());});
    }

    @FXML
    public void handleCancel() {
        dialogStage.close();
    }

    @FXML
    public void handleOk() {
        if (isInputValid()) {
            String title = null;
            double cost;
            double amount = 0;
            for (Node node : children) {

                if (GridPane.getColumnIndex(node) == 0) {
                    title = ((TextField) node).getText();
                } else if (GridPane.getColumnIndex(node) == 1) {
                    cost = Double.parseDouble(((TextField) node).getText());

                    if (receipt.getReceiptId() == 0) {
                        Item item = new Item(null, new SimpleStringProperty(title), new SimpleDoubleProperty(cost), null, null, null);
                        receipt.addItem(item);
                        amount += item.getCost();
                    }
                }
            }
            receipt.setAmount(-amount);
            receipt.setDate(DateUtil.parse(dateTextField.getText()));

            okClicked = true;
            dialogStage.close();
        }
    }

    private boolean isInputValid() {
        String errorMessage = "";
        children = itemsGridPane.getChildren();
        for (Node node : children) {
            if (GridPane.getColumnIndex(node) == 0) {
                if (((TextField) node).getText() == null || ((TextField) node).getText().length() == 0) {
                    errorMessage += "No valid title at row " + GridPane.getRowIndex(node) + "\n";
                }
            }

            if (GridPane.getColumnIndex(node) == 1) {
                if (((TextField) node).getText() == null || ((TextField) node).getText().length() == 0) {
                    errorMessage += "No valid cost at row " + GridPane.getRowIndex(node) + "\n";
                } else {
                    try {
                        Double.parseDouble(((TextField) node).getText());
                    } catch (NumberFormatException e) {
                        errorMessage += "No valid cost at row " + GridPane.getRowIndex(node) + " (must be an integer)\n";
                    }
                }
            }
        }

        if (dateTextField.getText() == null || dateTextField.getText().length() == 0) {
            errorMessage += "No valid date\n";
        } else {
            if (!DateUtil.validDate(dateTextField.getText())) {
                errorMessage += "No valid date. Use the format yyyy-mm-dd\n";
            }
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Please correct invalid fields");
            alert.setContentText(errorMessage);

            alert.showAndWait();

            return false;
        }
    }

    public boolean isOkClicked() {
        return okClicked;
    }
}
