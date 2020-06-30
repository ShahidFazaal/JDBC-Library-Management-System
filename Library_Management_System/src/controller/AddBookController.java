package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;
import javafx.util.Duration;
import sun.audio.AudioPlayer;
import util.Book;
import util.DBConnection;

import javax.swing.*;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class AddBookController {
    public AnchorPane addBook;
    public TableView<Book> tblBook;
    public JFXTextField txtIsbn;
    public JFXTextField txtTitle;
    public JFXTextField txtAuthor;
    public JFXButton btnAddBook;
    public JFXButton btnDeleteBook;
    public JFXButton btnAddNewBook;
    public Button btnIssuedBook;
    public Label lbletableDecleration;

    private static final AudioClip informationAlertSound = new AudioClip(AudioPlayer.class.getResource("/resource/sounds/windows_10_notify.mp3").toString());


    public void initialize() throws SQLException {

        loadBooks();
        txtIsbn.setEditable(false);
        btnDeleteBook.setDisable(true);
        txtIsbn.setDisable(true);
        txtAuthor.setDisable(true);
        txtTitle.setDisable(true);
        btnAddBook.setDisable(true);

        tblBook.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("isbnNumber"));
        tblBook.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("title"));
        tblBook.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("author"));

        tblBook.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Book>() {
            @Override
            public void changed(ObservableValue<? extends Book> observable, Book oldValue, Book newValue) {
                Book selectedBook = tblBook.getSelectionModel().getSelectedItem();
                if (selectedBook == null) {
                    btnAddBook.setText("Add Book");
                    btnDeleteBook.setDisable(true);
                    txtIsbn.clear();
                    txtTitle.clear();
                    txtAuthor.clear();
                    return;
                }


                txtIsbn.setText(newValue.getIsbnNumber());
                txtTitle.setText(newValue.getTitle());
                txtAuthor.setText(newValue.getAuthor());
                btnAddBook.setText("Update Book");
                btnDeleteBook.setDisable(false);
                txtIsbn.setDisable(false);
                txtAuthor.setDisable(false);
                txtTitle.setDisable(false);
                btnAddBook.setDisable(false);
                try {
                    ResultSet rs = DBConnection.getInstance().getConnection().createStatement().executeQuery("SELECT * FROM book where status='issued'");
                    while (rs.next()) {
                        if (rs.getString(1).equals(selectedBook.getIsbnNumber())) {
                            btnDeleteBook.setDisable(true);
                            return;
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }


            }
        });
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                btnAddNewBook.requestFocus();
            }
        });

    }

    public void btnAddBook_OnAction(ActionEvent event) throws SQLException {
        String isbn = txtIsbn.getText();
        String title = txtTitle.getText();
        String author = txtAuthor.getText();
        if (btnAddBook.getText().equals("Add Book")) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Press Ok to add the book", ButtonType.OK, ButtonType.CANCEL);
           informationAlertSound.play();
            Optional<ButtonType> buttonType = alert.showAndWait();

            if (buttonType.get() == ButtonType.OK) {
                PreparedStatement statement = DBConnection.getInstance().getConnection().prepareStatement("INSERT INTO book VALUES (?,?,?,DEFAULT )");
                statement.setObject(1, isbn);
                statement.setObject(2, title);
                statement.setObject(3, author);
                statement.executeUpdate();
                loadBooks();
            }
        } else {
            PreparedStatement statement = DBConnection.getInstance().getConnection().prepareStatement("UPDATE book SET title=?,author=? WHERE isbn=?");
            statement.setObject(1, title);
            statement.setObject(2, author);
            statement.setObject(3, isbn);
            statement.executeUpdate();
            loadBooks();
        }
        txtIsbn.clear();
        txtTitle.clear();
        txtAuthor.clear();
        generateId();
        txtTitle.requestFocus();


    }

    public void btnDeleteBook_OnAction(ActionEvent event) throws SQLException {
        String isbnNumber = tblBook.getSelectionModel().getSelectedItem().getIsbnNumber();


        PreparedStatement statement = DBConnection.getInstance().getConnection().prepareStatement("DELETE FROM book where isbn=?");
        statement.setObject(1, isbnNumber);
        statement.executeUpdate();
        loadBooks();
        if (tblBook.getItems().size() == 0) {
            txtAuthor.setDisable(true);
            txtTitle.setDisable(true);
            txtIsbn.setDisable(true);
            btnAddBook.setDisable(true);
            btnDeleteBook.setDisable(true);
        }
        txtTitle.requestFocus();

    }

    public void btnAddNewBook_OnAction(ActionEvent event) throws SQLException {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                txtTitle.requestFocus();
                try {
                    generateId();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });

        txtTitle.clear();
        txtAuthor.clear();
        btnDeleteBook.setDisable(true);
        btnAddBook.setText("Add Book");
        txtIsbn.setDisable(false);
        txtAuthor.setDisable(false);
        txtTitle.setDisable(false);
        btnAddBook.setDisable(false);
        tblBook.getSelectionModel().clearSelection();
    }

    public void homeOnKeyPressed(KeyEvent keyEvent) throws IOException, SQLException {
        if (keyEvent.getCode() == KeyCode.ESCAPE) {
            Parent root = FXMLLoader.load(this.getClass().getResource("/view/MainWindow.fxml"));
            Scene subScene = new Scene(root);
            Stage primaryStage = (Stage) this.addBook.getScene().getWindow();
            primaryStage.setScene(subScene);
            primaryStage.centerOnScreen();

            TranslateTransition tt = new TranslateTransition(Duration.millis(350), subScene.getRoot());
            tt.setFromX(600);
            tt.setToX(0);
            tt.play();
        }
        if (keyEvent.getCode() == KeyCode.ENTER) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    txtTitle.requestFocus();
                }
            });
            generateId();
            txtTitle.clear();
            txtAuthor.clear();
            btnDeleteBook.setDisable(true);
            btnAddBook.setText("Add Book");
            txtIsbn.setDisable(false);
            txtAuthor.setDisable(false);
            txtTitle.setDisable(false);
            btnAddBook.setDisable(false);
            tblBook.getSelectionModel().clearSelection();
        }
    }

    public void loadBooks() throws SQLException {
        int countBook = 0;
        ObservableList<Book> books = tblBook.getItems();
        ResultSet resultSet = DBConnection.getInstance().getConnection().createStatement().executeQuery("SELECT * FROM book where status='not issued'");
        books.clear();
        while (resultSet.next()) {
            countBook++;
            books.add(new Book(resultSet.getString(1), resultSet.getNString(2), resultSet.getString(3)));
        }
    }


    public void generateId() throws SQLException {
        // Generate ISBN Number
        ResultSet resultSet = DBConnection.getInstance().getConnection().createStatement().executeQuery("SELECT  * FROM book ORDER BY 1 DESC");
        if (resultSet.next()) {
            String isbn = resultSet.getString(1);
            String number = isbn.substring(4, 7);
            int newItemCode = Integer.parseInt(number) + 1;
            if (newItemCode < 10) {

                txtIsbn.setText("ISBN00" + newItemCode);
            } else if (newItemCode < 100) {

                txtIsbn.setText("ISBN0" + newItemCode);
            } else {

                txtIsbn.setText("ISBN" + newItemCode);
            }
        } else {
            txtIsbn.setText("ISBN001");
        }
    }


    public void txtAuthor_OnAction(ActionEvent event) throws SQLException {
        btnAddBook_OnAction(event);
    }

    public void btnIssuedBook_OnAction(ActionEvent event) throws SQLException {
        ObservableList<Book> books = tblBook.getItems();
        if (btnIssuedBook.getText().equals("Issued Book")) {
            ResultSet resultSet = DBConnection.getInstance().getConnection().createStatement().executeQuery("SELECT  * from book where status='issued'");
            books.clear();
            while (resultSet.next()) {
                books.add(new Book(resultSet.getString(1), resultSet.getNString(2), resultSet.getString(3)));
            }
            btnIssuedBook.setText("Not Issued");
            lbletableDecleration.setText("Issued Books");


        } else {
            loadBooks();
            btnIssuedBook.setText("Issued Book");
            if(!txtIsbn.isDisabled()){
                generateId();
            }
            lbletableDecleration.setText("Not Issued Books");

            txtTitle.requestFocus();
        }
    }
}
