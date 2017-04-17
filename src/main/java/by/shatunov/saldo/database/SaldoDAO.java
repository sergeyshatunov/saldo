package by.shatunov.saldo.database;

import by.shatunov.saldo.model.Item;
import by.shatunov.saldo.model.Receipt;
import by.shatunov.saldo.util.DateUtil;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class SaldoDAO {

    public SaldoDAO() {
    }

    public Receipt getReceiptById(int receiptId) {
        Receipt receipt = null;
        Connection connection = null;
        Statement statement = null;
        Statement itemStatement = null;
        try {
            connection = ConnectorDB.getConnection();

            statement = connection.createStatement();
            ResultSet receiptRS = statement.executeQuery("SELECT * FROM receipt NATURAL JOIN place WHERE receipt_id = " + receiptId + ";");

            while (receiptRS.next()) {
                ArrayList<Item> items = new ArrayList<>();
                itemStatement = connection.createStatement();
                ResultSet itemsRS = itemStatement.executeQuery("SELECT * FROM item NATURAL JOIN category WHERE receipt_id = " + receiptId + ";");

                while (itemsRS.next()) {
                    items.add(new Item(
                            new SimpleIntegerProperty(itemsRS.getInt("item_id")),
                            new SimpleStringProperty(itemsRS.getString("item_title")),
                            new SimpleDoubleProperty(itemsRS.getDouble("cost")),
                            new SimpleIntegerProperty(itemsRS.getInt("category_id")),
                            new SimpleStringProperty(itemsRS.getString("category_title")),
                            new SimpleIntegerProperty(receiptId)
                    ));
                }
                receipt = new Receipt(
                        new SimpleIntegerProperty(receiptId),
                        new SimpleObjectProperty<>(DateUtil.parse(receiptRS.getString("date"))),
                        new SimpleDoubleProperty(receiptRS.getDouble("amount")),
                        new SimpleIntegerProperty(receiptRS.getInt("account_id")),
                        new SimpleIntegerProperty(receiptRS.getInt("place_id")),
                        new SimpleStringProperty(receiptRS.getString("title")),
                        items
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (itemStatement != null) {
                try {
                    itemStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return receipt;
    }

    public ArrayList<Receipt> getAllReceipts() {

        ArrayList<Receipt> receipts = new ArrayList<>();
        Connection connection = null;
        Statement statement = null;
        Statement itemStatement = null;

        try {
            connection = ConnectorDB.getConnection();

            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM receipt NATURAL JOIN place ORDER BY receipt_id DESC;");

            while (resultSet.next()) {
                int receipt_id = resultSet.getInt("receipt_id");

                ArrayList<Item> items = new ArrayList<>();

                try {
                    itemStatement = connection.createStatement();
                    ResultSet itemsRS = itemStatement.executeQuery("select * from item natural join category where receipt_id = " + receipt_id + ";");

                    while (itemsRS.next()) {
                        items.add(new Item(
                                new SimpleIntegerProperty(itemsRS.getInt("item_id")),
                                new SimpleStringProperty(itemsRS.getString("item_title")),
                                new SimpleDoubleProperty(itemsRS.getDouble("cost")),
                                new SimpleIntegerProperty(itemsRS.getInt("category_id")),
                                new SimpleStringProperty(itemsRS.getString("category_title")),
                                new SimpleIntegerProperty(receipt_id)
                        ));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    if (itemStatement != null) {
                        itemStatement.close();
                    }
                }
                receipts.add(new Receipt(
                        new SimpleIntegerProperty(receipt_id),
                        new SimpleObjectProperty<>(DateUtil.parse(resultSet.getString("date"))),
                        new SimpleDoubleProperty(resultSet.getDouble("amount")),
                        new SimpleIntegerProperty(resultSet.getInt("account_id")),
                        new SimpleIntegerProperty(resultSet.getInt("place_id")),
                        new SimpleStringProperty(resultSet.getString("title")),
                        items
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return receipts;
    }

    public Receipt addReceipt(Receipt receipt) {
        int lastId = 0;
        Connection connection = null;
        Statement statement = null;
        System.out.println("statement null");

        StringBuilder builder = new StringBuilder();
        System.out.println("new builder");
        builder.append("insert into item values ");
        for (Item item : receipt.getItems()) {
            builder.append("(NULL, '");
            builder.append(item.getTitle());
            builder.append("', ");
            builder.append(-item.getCost());
            builder.append(", 1, last_insert_id()),");
        }
        builder.deleteCharAt(builder.length() - 1);
        builder.append(";");
        System.out.println("new builder");

        try {
            connection = ConnectorDB.getConnection();
            connection.setAutoCommit(false);
            statement = connection.createStatement();
            statement.executeUpdate("INSERT INTO receipt VALUE (NULL, \'" + DateUtil.format(receipt.getDate()) + "\', " + receipt.getAmount() + ", 1, 1)");

            ResultSet resultSet = statement.executeQuery("SELECT last_insert_id()");
            while (resultSet.next()) {
                lastId = resultSet.getInt(1);
            }

            statement.executeUpdate("UPDATE account SET current_amount = current_amount + (SELECT amount FROM receipt WHERE receipt_id = last_insert_id())WHERE account_id = (SELECT account_id FROM receipt WHERE receipt_id = last_insert_id());");
            statement.executeUpdate(builder.toString());
            connection.commit();

            System.out.println(lastId);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return getReceiptById(lastId);
    }

    public void deleteReceipt(Receipt receipt) {
        Connection connection = null;
        Statement statement = null;

        try {
            connection = ConnectorDB.getConnection();
            connection.setAutoCommit(false);
            statement = connection.createStatement();
            statement.executeUpdate("DELETE FROM item WHERE receipt_id = " + receipt.getReceiptId() + ";");
            statement.executeUpdate("DELETE FROM receipt WHERE receipt_id = " + receipt.getReceiptId() + ";");
            statement.executeUpdate("UPDATE account SET current_amount = current_amount - " + receipt.getAmount() + " WHERE account_id = " + receipt.getAccountId() + ";");
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
