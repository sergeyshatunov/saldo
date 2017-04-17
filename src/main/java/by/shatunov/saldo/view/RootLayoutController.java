package by.shatunov.saldo.view;

import by.shatunov.saldo.Main;
import by.shatunov.saldo.database.SaldoDAO;
import by.shatunov.saldo.model.Item;
import by.shatunov.saldo.model.Receipt;
import by.shatunov.saldo.util.DateUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class RootLayoutController {

    private Main main;
    private SaldoDAO dao = new SaldoDAO();
    private ArrayList<Receipt> receiptsList;
    private ObservableList<Receipt> receiptsData = FXCollections.observableArrayList();

    @FXML
    private TableView<Receipt> receiptTable;
    @FXML
    private TableColumn<Receipt, String> placeColumn;
    @FXML
    private TableColumn<Receipt, String> dateColumn;
    @FXML
    private TableColumn<Receipt, String> amountColumn;
    @FXML
    private VBox vBoxReceipts;


    public RootLayoutController() {
    }

    public void setMainApp(Main main) {
        this.main = main;

        receiptsList = dao.getAllReceipts();
        receiptsData.addAll(receiptsList);
        receiptTable.setItems(receiptsData);
    }

    @FXML
    private void initialize() {
        placeColumn.setCellValueFactory(param -> param.getValue().placeTitleProperty());
        dateColumn.setCellValueFactory(param -> new SimpleStringProperty(DateUtil.format(param.getValue().getDate()))  );
        amountColumn.setCellValueFactory(param -> new SimpleStringProperty(Double.toString(param.getValue().getAmount())));

        receiptTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> showReceipt(newValue));
    }

    private void showReceipt(Receipt receipt) {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.TOP_LEFT);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        ColumnConstraints column1 = new ColumnConstraints(200,200,Double.MAX_VALUE);
        column1.setHgrow(Priority.ALWAYS);
        ColumnConstraints column2 = new ColumnConstraints(200);
        grid.getColumnConstraints().addAll(column1, column2);

        int row = 0;
        for (Item item : receipt.getItems()) {
            grid.addRow(row, new Label(item.getTitle()), new Label(Double.toString(item.getCost())));
            row++;
        }

        vBoxReceipts.getChildren().clear();
        vBoxReceipts.getChildren().add(grid);
    }

    @FXML
    private void handleNewReceipt() {
        System.out.println("handleNewReceipt");
        Receipt temp = new Receipt();
        boolean okClicked = main.showReceiptEditDialog(temp, receiptsList);
        if (okClicked) {
            Receipt addedReceipt = dao.addReceipt(temp);
            receiptsData.add(addedReceipt);
        }
    }

    @FXML
    private void handleDeleteReceipt() {
        Receipt selectedReceipt = receiptTable.getSelectionModel().getSelectedItem();
        if (selectedReceipt != null) {
            dao.deleteReceipt(selectedReceipt);
            receiptsData.remove(selectedReceipt);
        } else {
            showAlert();
        }
    }

    private void showAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.initOwner(main.getPrimaryStage());
        alert.setTitle("No Selection");
        alert.setHeaderText("No Receipt Selected");
        alert.setContentText("Please select a receipt in the table");

        alert.showAndWait();
    }
}
