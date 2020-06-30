package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
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
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.util.Duration;
import sun.audio.AudioPlayer;
import util.Book;
import util.Borrow;
import util.DBConnection;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Optional;

public class IssueBookController {
    private static final AudioClip informationAlertSound = new AudioClip(AudioPlayer.class.getResource("/resource/sounds/windows_10_notify.mp3").toString());
    private static final AudioClip errorAlertSound = new AudioClip(AudioPlayer.class.getResource("/resource/windows_error.mp3").toString());
    private static final AudioClip success = new AudioClip(AudioPlayer.class.getResource("/resource/sounds/success.mp3").toString());

    public AnchorPane issueBook;
    public JFXButton btnNewIssue;
    public JFXButton btnIssueBook;
    public Button btnAddBook;
    public Label date;
    public JFXComboBox<Book> cmbIsbn;
    public JFXTextField txtNicNumber;
    public JFXTextField txtPhone;
    public JFXTextField txtBookName;
    public JFXTextField txtName;
    public JFXTextField txtReturnDate;
    public TableView<Borrow> tblBorrow;
    public Label lblMaximumBook;
    public Label boorowID;
    public TableView<Borrow> tbleBorrowBookList;
    public LocalDate output;
    int count = 0;
    int bookCount;

    public void initialize() throws SQLException {
        btnIssueBook.setDisable(true);
        lblMaximumBook.setVisible(false);
        generateId();
        cmbIsbn.setDisable(true);
        txtBookName.setDisable(true);
        txtReturnDate.setDisable(true);
        txtName.setDisable(true);
        txtPhone.setDisable(true);
        date.setText(LocalDate.now() + "");

        ObservableList isbn = cmbIsbn.getItems();
        ResultSet resultSet = DBConnection.getInstance().getConnection().createStatement().executeQuery("SELECT  * FROM book WHERE status='not issued'");
        while (resultSet.next()) {
            isbn.add(new Book(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3)));
        }

        cmbIsbn.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Book>() {
            @Override
            public void changed(ObservableValue<? extends Book> observable, Book oldValue, Book newValue) {
                Book selectedBook = cmbIsbn.getSelectionModel().getSelectedItem();
                if (selectedBook == null) {
                    return;
                }
                txtBookName.setText(newValue.getTitle());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Calendar c = Calendar.getInstance();
                try {
                    c.setTime(sdf.parse(date.getText())); // Now use today date.
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                c.add(Calendar.DATE, 7); // Adding 5 days
                output = LocalDate.parse(sdf.format(c.getTime()));
                txtReturnDate.setText(output + "");
                LocalDate now = LocalDate.now();


            }
        });

        txtNicNumber.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (txtNicNumber.getText().equals("")) {
                    cmbIsbn.setDisable(true);
                    txtBookName.setDisable(true);
                    txtReturnDate.setDisable(true);
                    txtName.setDisable(true);
                    txtPhone.setDisable(true);

                    cmbIsbn.getSelectionModel().clearSelection();
                    txtReturnDate.clear();
                    txtName.clear();
                    txtPhone.clear();
                    txtReturnDate.clear();
                    txtBookName.clear();


                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            txtNicNumber.unFocusColorProperty().set(Paint.valueOf("#4d4d4d"));
                            txtNicNumber.focusColorProperty().set(Paint.valueOf("#4059a9"));
                        }
                    });

                    txtNicNumber.setStyle("-fx-text-fill: #4059a9");
                    return;
                }


                if (!(txtNicNumber.getText().trim().matches("^[0-9]{9}[x|X|v|V]|[0-9]{12}$"))) {
                    txtNicNumber.unFocusColorProperty().set(Paint.valueOf("red"));
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            txtNicNumber.requestFocus();
                            txtNicNumber.focusColorProperty().set(Paint.valueOf("red"));
                        }
                    });


                } else {

                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            txtNicNumber.unFocusColorProperty().set(Paint.valueOf("#4d4d4d"));
                            txtNicNumber.focusColorProperty().set(Paint.valueOf("#4059a9"));
                        }
                    });
                    txtNicNumber.setStyle("-fx-text-fill: #4059a9");

                }

            }
        });

        tblBorrow.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("borrowId"));
        tblBorrow.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("nic"));
        tblBorrow.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("name"));
        tblBorrow.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("phone"));
        tblBorrow.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("date"));
        tblBorrow.getColumns().get(5).setCellValueFactory(new PropertyValueFactory<>("returnDate"));
        tbleBorrowBookList.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("isbn"));
        tbleBorrowBookList.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("book"));

        tbleBorrowBookList.getItems().addListener(new ListChangeListener<Borrow>() {
            @Override
            public void onChanged(Change<? extends Borrow> c) {
                try {
                    ResultSet resultSet1 = DBConnection.getInstance().getConnection().createStatement().executeQuery("SELECT count(nic) as 'count' FROM librarymanagement.borrow where nic='" + txtNicNumber.getText().trim() + "' and status ='not returned'");
                    resultSet1.next();
                    if (resultSet1.getInt(1) == 1) {
                        bookCount = 2;
                    }
                    if (tbleBorrowBookList.getItems().size() == 2 || bookCount == 2) {
                        lblMaximumBook.setVisible(true);
                        btnAddBook.setDisable(true);
                        cmbIsbn.getSelectionModel().clearSelection();
                        txtBookName.clear();
                        txtReturnDate.clear();
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                cmbIsbn.setDisable(true);
                                btnIssueBook.requestFocus();
                            }
                        });

                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }


            }
        });


    }

    public void btnNewIssue_OnAction(ActionEvent event) {
    }

    public void btnIssueBook_OnAction(ActionEvent event) throws SQLException {
        ObservableList<Borrow> borrowerDetail = tblBorrow.getItems();
        ObservableList<Borrow> borrowedBook = tbleBorrowBookList.getItems();
        if (borrowedBook.size() == 2) {
            for (Borrow books : borrowedBook) {
                PreparedStatement statement = DBConnection.getInstance().getConnection().prepareStatement("INSERT  INTO borrow VALUES (?,?,?,?,?,DEFAULT )");
                statement.setObject(1, generateIdForOl());
                statement.setObject(2, borrowerDetail.get(0).getNic());
                statement.setObject(3, books.getIsbn());
                statement.setObject(4, borrowerDetail.get(0).getDate());
                statement.setObject(5, borrowerDetail.get(0).getReturnDate());
                statement.executeUpdate();
                PreparedStatement statement2 = DBConnection.getInstance().getConnection().prepareStatement("UPDATE book SET status=? WHERE isbn=?");
                statement2.setObject(1, "issued");
                statement2.setObject(2, books.getIsbn());
                statement2.executeUpdate();
            }
            success.play();
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Success!", ButtonType.OK);
            Optional<ButtonType> buttonType = alert.showAndWait();
            if (buttonType.get() == ButtonType.OK) {
                tblBorrow.getItems().clear();
                tbleBorrowBookList.getItems().clear();
            }
        } else {
            PreparedStatement statement = DBConnection.getInstance().getConnection().prepareStatement("INSERT  INTO borrow VALUES (?,?,?,?,?,DEFAULT )");
            statement.setObject(1, generateIdForOl());
            statement.setObject(2, borrowerDetail.get(0).getNic());
            statement.setObject(3, borrowedBook.get(0).getIsbn());
            statement.setObject(4, borrowerDetail.get(0).getDate());
            statement.setObject(5, borrowerDetail.get(0).getReturnDate());
            statement.executeUpdate();
            PreparedStatement statement2 = DBConnection.getInstance().getConnection().prepareStatement("UPDATE book SET status=? WHERE isbn=?");
            statement2.setObject(1, "issued");
            statement2.setObject(2, borrowedBook.get(0).getIsbn());
            statement2.executeUpdate();
            success.play();
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Success!", ButtonType.OK);
            Optional<ButtonType> buttonType = alert.showAndWait();
            if (buttonType.get() == ButtonType.OK) {
                tblBorrow.getItems().clear();
                tbleBorrowBookList.getItems().clear();
            }
        }

        txtNicNumber.clear();
        txtNicNumber.requestFocus();


    }

    public void btnAddBook_OnAction(ActionEvent event) throws SQLException {

        String nic = txtNicNumber.getText();
        String name = txtName.getText();
        int phone = Integer.parseInt(txtPhone.getText());
        String isbn = cmbIsbn.getSelectionModel().getSelectedItem().getIsbnNumber();
        String book = cmbIsbn.getSelectionModel().getSelectedItem().getTitle();
        LocalDate borrowDate = LocalDate.now();
        LocalDate returnDate = output;
        ObservableList<Borrow> borrowerDetails = tblBorrow.getItems();
        ObservableList<Borrow> borrowedBooks = tbleBorrowBookList.getItems();


        if (tblBorrow.getItems().size() == 0) {
            int borrowId = generateIdForOl();
            borrowerDetails.add(new Borrow(borrowId, nic, name, phone, borrowDate, returnDate));
        }
        borrowedBooks.add(new Borrow(isbn, book));
        cmbIsbn.getItems().remove(cmbIsbn.getSelectionModel().getSelectedItem());
        txtBookName.clear();
        txtReturnDate.clear();
        btnIssueBook.setDisable(false);


    }

    public void btnMouseEntered(MouseEvent mouseEvent) {
        if (mouseEvent.getSource() instanceof Button) {
            Button btn = (Button) mouseEvent.getSource();


            switch (btn.getText()) {
                case "ADD":
                    btnAddBook.setTextFill(Paint.valueOf("Blue"));
                    break;
                case "+ Issue":
                    btnNewIssue.setTextFill(Paint.valueOf("Blue"));
                    break;
                case "Issue Book":
                    btnIssueBook.setTextFill(Paint.valueOf("Blue"));
                    break;

            }

        }
    }

    public void btnMouseExit(MouseEvent mouseEvent) {
        if (mouseEvent.getSource() instanceof Button) {
            Button btn = (Button) mouseEvent.getSource();


            switch (btn.getText()) {
                case "ADD":
                    btnAddBook.setTextFill(Paint.valueOf("Black"));
                    break;
                case "+ Issue":
                    btnNewIssue.setTextFill(Paint.valueOf("White"));
                    break;
                case "Issue Book":
                    btnIssueBook.setTextFill(Paint.valueOf("Black"));
                    break;

            }

        }
    }

    public void Home_OnKeyPressed(KeyEvent keyEvent) throws IOException {
        if (keyEvent.getCode() == KeyCode.ESCAPE) {
            Parent root = FXMLLoader.load(this.getClass().getResource("/view/MainWindow.fxml"));
            Scene subScene = new Scene(root);
            Stage primaryStage = (Stage) this.issueBook.getScene().getWindow();
            primaryStage.setScene(subScene);
            primaryStage.centerOnScreen();

            TranslateTransition tt = new TranslateTransition(Duration.millis(350), subScene.getRoot());
            tt.setFromX(600);
            tt.setToX(0);
            tt.play();
        }
    }

    public void cmbIsbn_OnAction(ActionEvent event) throws SQLException {

    }

    public void txtNic_OnKeyPressed(KeyEvent keyEvent) throws SQLException {

        if (keyEvent.getCode() == KeyCode.ENTER) {
            if (!(txtNicNumber.getText().trim().matches("^[0-9]{9}[x|X|v|V]|[0-9]{12}$"))) {
                count++;
                if (count == 1) {
                    errorAlertSound.play();
                    Alert alert = new Alert(Alert.AlertType.WARNING, "Nic Number is Invalid", ButtonType.OK);
                    alert.show();
                    return;
                }
                errorAlertSound.play();
            } else {
                ResultSet resultSet1 = DBConnection.getInstance().getConnection().createStatement().executeQuery("SELECT count(nic) as 'count' FROM librarymanagement.borrow where nic='" + txtNicNumber.getText().trim() + "' and status ='not returned'");
                if (resultSet1.next()) {
                    if (resultSet1.getInt(1) == 2) {
                        informationAlertSound.play();
                        new Alert(Alert.AlertType.INFORMATION, "The Member already borrowed maximum allowed book!", ButtonType.OK).show();
                        return;
                    }
                }
                ResultSet resultSet = DBConnection.getInstance().getConnection().createStatement().executeQuery("SELECT * FROM `member` WHERE nic='" + txtNicNumber.getText().trim() + "' ");
                if (resultSet.next()) {
                    cmbIsbn.setDisable(false);
                    txtBookName.setDisable(false);
                    txtReturnDate.setDisable(false);
                    txtName.setDisable(false);
                    txtPhone.setDisable(false);
                    txtName.setText(resultSet.getString(2));
                    txtPhone.setText(resultSet.getString(3));
                    cmbIsbn.requestFocus();
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {

                            cmbIsbn.show();

                        }
                    });
                    cmbIsbn.getSelectionModel().clearSelection();
                } else {
                    informationAlertSound.play();
                    new Alert(Alert.AlertType.INFORMATION, "Member is not registered", ButtonType.OK).show();
                }
            }
        }

    }

    public void cmbOnKeyPressed(KeyEvent keyEvent) throws SQLException {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            if (cmbIsbn.getSelectionModel().getSelectedItem() == null) {
                informationAlertSound.play();
                new Alert(Alert.AlertType.WARNING, "Please select the ISBN number", ButtonType.OK).show();
                return;
            }
            int borrowId = generateIdForOl();
            String nic = txtNicNumber.getText();
            String name = txtName.getText();
            int phone = Integer.parseInt(txtPhone.getText());
            String isbn = cmbIsbn.getSelectionModel().getSelectedItem().getIsbnNumber();
            String book = cmbIsbn.getSelectionModel().getSelectedItem().getTitle();
            LocalDate borrowDate = LocalDate.now();
            LocalDate returnDate = output;
            ObservableList<Borrow> borrowerDetails = tblBorrow.getItems();
            ObservableList<Borrow> borrowedBooks = tbleBorrowBookList.getItems();
            if (tblBorrow.getItems().size() == 0) {
                borrowerDetails.add(new Borrow(borrowId, nic, name, phone, borrowDate, returnDate));
            }
            borrowedBooks.add(new Borrow(isbn, book));
            cmbIsbn.getItems().remove(cmbIsbn.getSelectionModel().getSelectedItem());
            txtBookName.clear();
            txtReturnDate.clear();
            btnIssueBook.setDisable(false);
        }
    }

    public void generateId() throws SQLException {
        // Generate ISBN Number
        ResultSet resultSet = DBConnection.getInstance().getConnection().createStatement().executeQuery("SELECT  borrowId FROM borrow ORDER BY 1 DESC");
        if (resultSet.next()) {
            int id = resultSet.getInt(1);
            int newId = id + 1;
            boorowID.setText(newId + "");

        } else {
            boorowID.setText("1");
        }
    }

    public int generateIdForOl() throws SQLException {
        int id = 0;
        ObservableList<Borrow> borrows = tblBorrow.getItems();
        ResultSet resultSet = DBConnection.getInstance().getConnection().createStatement().executeQuery("SELECT  borrowId FROM borrow ORDER BY 1 DESC");
        if (resultSet.next()) {
            id = resultSet.getInt(1);
        }

        return id + 1;
    }
}
