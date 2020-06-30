package controller;

import com.jfoenix.controls.JFXButton;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Paint;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import sun.audio.AudioPlayer;
import util.DBConnection;
import util.Return;

import java.io.IOException;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;

public class ReturnController {
    private static final AudioClip informationAlertSound = new AudioClip(AudioPlayer.class.getResource("/resource/sounds/windows_10_notify.mp3").toString());
    private static final AudioClip errorAlertSound = new AudioClip(AudioPlayer.class.getResource("/resource/windows_error.mp3").toString());
    private static final AudioClip success = new AudioClip(AudioPlayer.class.getResource("/resource/sounds/success.mp3").toString());
    public TableView<Return> tblNotReturn;
    public TableView<Return> tblReturned;
    public TextField txtNic;
    public Label lblDate;
    public AnchorPane Return;
    int count = 0;
    int id = 0;
    String payment = null;

    public void initialize() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                txtNic.requestFocus();
            }
        });


        lblDate.setText(LocalDate.now().toString());

        txtNic.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (txtNic.getText().trim().equals("")) {
                    txtNic.setStyle("-fx-border-color: #4d4d4d");
                    tblReturned.getItems().clear();
                    tblNotReturn.getItems().clear();
                    return;
                }

                if (!(txtNic.getText().trim().matches("^[0-9]{9}[x|X|v|V]|[0-9]{12}$"))) {
                    txtNic.setStyle("-fx-border-color: red");
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            txtNic.requestFocus();

                        }
                    });


                } else {

                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            txtNic.setStyle("-fx-border-color: #4d4d4d");

                        }
                    });
                    txtNic.setStyle("-fx-text-fill: #4059a9");

                }
            }
        });

        tblNotReturn.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("borrowId"));
        tblNotReturn.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("name"));
        tblNotReturn.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("isbn"));
        tblNotReturn.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("book"));
        tblNotReturn.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("borrowDate"));
        tblNotReturn.getColumns().get(5).setCellValueFactory(new PropertyValueFactory<>("returnDate"));
        tblNotReturn.getColumns().get(6).setCellValueFactory(new PropertyValueFactory<>("status"));
        tblNotReturn.getColumns().get(7).setCellValueFactory(new PropertyValueFactory<>("button"));

        tblReturned.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("borrowId"));
        tblReturned.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("name"));
        tblReturned.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("isbn"));
        tblReturned.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("book"));
        tblReturned.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("borrowDate"));
        tblReturned.getColumns().get(5).setCellValueFactory(new PropertyValueFactory<>("returnDate"));
        tblReturned.getColumns().get(6).setCellValueFactory(new PropertyValueFactory<>("returnedDate"));
        tblReturned.getColumns().get(7).setCellValueFactory(new PropertyValueFactory<>("lateFee"));
        tblReturned.getColumns().get(8).setCellValueFactory(new PropertyValueFactory<>("status"));


    }

    public void navigateHome_OnKeyPressed(KeyEvent keyEvent) throws IOException {
        if (keyEvent.getCode() == KeyCode.ESCAPE) {
            Parent root = FXMLLoader.load(this.getClass().getResource("/view/MainWindow.fxml"));
            Scene subScene = new Scene(root);
            Stage primaryStage = (Stage) this.Return.getScene().getWindow();
            primaryStage.setScene(subScene);
            primaryStage.centerOnScreen();

            TranslateTransition tt = new TranslateTransition(Duration.millis(350), subScene.getRoot());
            tt.setFromX(-subScene.getWidth());
            tt.setFromX(0);
            tt.setToY(0);
            tt.play();
        }
    }

    public void txtNic_OnAction(ActionEvent event) throws SQLException {
        if (!(txtNic.getText().trim().matches("^[0-9]{9}[x|X|v|V]|[0-9]{12}$"))) {
            count++;
            if (count == 1) {
                errorAlertSound.play();
                Alert alert = new Alert(Alert.AlertType.WARNING, "Nic Number is Invalid", ButtonType.OK);
                alert.show();
                return;
            }
            errorAlertSound.play();
            return;
        } else {
            ResultSet resultSet = DBConnection.getInstance().getConnection().createStatement().executeQuery("SELECT * FROM `member` WHERE nic='" + txtNic.getText().trim() + "' ");
            if (resultSet.next()) {

                loadReturnNotReturned();
            } else {
                informationAlertSound.play();
                new Alert(Alert.AlertType.INFORMATION, "Member is not registered", ButtonType.OK).show();
            }
        }
    }

    public void loadReturnNotReturned() throws SQLException {
        ObservableList<Return> notReturned = tblNotReturn.getItems();
        ObservableList<Return> returned = tblReturned.getItems();
        ResultSet resultSet = DBConnection.getInstance().getConnection().createStatement().executeQuery("SELECT b.*, m.name, k.title  FROM borrow b  inner join `member` m on b.nic = m.nic inner join book k on b.isbn = k.isbn WHERE b.nic ='" + txtNic.getText() + "';");
        notReturned.clear();
        returned.clear();
        while (resultSet.next()) {
            int borrowId = resultSet.getInt(1);
            String nic = resultSet.getString(2);
            String name = resultSet.getString(7);
            String isbn = resultSet.getString(3);
            String book = resultSet.getString(8);
            Date borrowedDate = resultSet.getDate(4);
            Date toBeReturn = resultSet.getDate(5);
            String status = resultSet.getString(6);

            if (status.equals("not returned")) {
                Button btnReturn = new Button("RETURN");
                btnReturn.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {

                        id = borrowId;
                        LocalDate returnDate = LocalDate.now();
                        if (returnDate.isBefore(toBeReturn.toLocalDate()) || returnDate.isEqual(toBeReturn.toLocalDate())) {
                            try {
                                PreparedStatement statement = DBConnection.getInstance().getConnection().prepareStatement("UPDATE borrow SET status =? where borrowId=?");
                                statement.setObject(1, "returned");
                                statement.setObject(2, borrowId);
                                statement.executeUpdate();

                                PreparedStatement statement2 = DBConnection.getInstance().getConnection().prepareStatement("insert into `return` values (?,?,0.0)");
                                statement2.setObject(1, borrowId);
                                statement2.setObject(2, LocalDate.now());
                                statement2.executeUpdate();

                                PreparedStatement statement1 = DBConnection.getInstance().getConnection().prepareStatement("UPDATE book set status =? where isbn =?");
                                statement1.setObject(1,"not issued");
                                statement1.setObject(2,isbn);
                                statement1.executeUpdate();

                                informationAlertSound.play();
                                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Successfully returned the Book! Thank You!", ButtonType.OK);
                                Optional<ButtonType> buttonType = alert.showAndWait();
                                if (buttonType.get() == ButtonType.OK) {
                                    loadReturnNotReturned();
                                }


                            } catch (SQLException e) {
                                e.printStackTrace();
                            }


                        } else {
                            Stage primaryStage = new Stage();
                            Parent root = null;
                            try {
                                FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("/view/returnConfirmation.fxml"));
                                root = fxmlLoader.load();
                                ReturnConfirmationController controller = fxmlLoader.getController();
                                primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                                    @Override
                                    public void handle(WindowEvent event) {
                                        controller.payment = null;
                                        Return.setDisable(false);
                                    }
                                });
                                controller.isbn = isbn;
                                controller.lblNic.setText(nic);
                                controller.lblName.setText(name);
                                controller.lblBook.setText(book);
                                controller.lblBoroowDate.setText(borrowedDate.toString());
                                controller.lblToBeReturnDate.setText(toBeReturn.toString());
                                controller.lblReturningDate.setText(returnDate.toString());
                                controller.txtLateFee.setText("Rs.50.00");
                                controller.txtLateFee.setStyle("-fx-text-fill: red");
                                controller.btnConfirm.requestFocus();


                                Scene mainScene = new Scene(root);
                                primaryStage.setScene(mainScene);
                                primaryStage.setTitle("Return Confirmation");
                                primaryStage.resizableProperty().setValue(false);
                                Image icon = new Image(getClass().getResourceAsStream("/resource/321934001.png"));
                                primaryStage.getIcons().add(icon);
                                primaryStage.centerOnScreen();
                                informationAlertSound.play();
                                Return.setDisable(true);
                                primaryStage.initOwner(Return.getScene().getWindow());
                                primaryStage.initModality(Modality.APPLICATION_MODAL);
                                primaryStage.showAndWait();

                                if (controller.payment == "paid") {
                                    Return.setDisable(false);
                                    try {
                                        PreparedStatement statement = DBConnection.getInstance().getConnection().prepareStatement("UPDATE borrow SET status =? where borrowId=?");
                                        statement.setObject(1, "returned");
                                        statement.setObject(2, borrowId);
                                        statement.executeUpdate();

                                        PreparedStatement statement2 = DBConnection.getInstance().getConnection().prepareStatement("insert into `return` values (?,?,?)");
                                        statement2.setObject(1, borrowId);
                                        statement2.setObject(2, LocalDate.now());
                                        statement2.setObject(3, 50.00);
                                        statement2.executeUpdate();

                                        PreparedStatement statement1 = DBConnection.getInstance().getConnection().prepareStatement("UPDATE book set status =? where isbn =?");
                                        statement1.setObject(1,"not issued");
                                        statement1.setObject(2,isbn);
                                        statement1.executeUpdate();


                                        informationAlertSound.play();
                                        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Successfully returned the Book! Thank You!", ButtonType.OK);
                                        Optional<ButtonType> buttonType = alert.showAndWait();
                                        if (buttonType.get() == ButtonType.OK) {
                                            loadReturnNotReturned();
                                        }


                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                    }
                                }


                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                });
                notReturned.add(new Return(borrowId, name, isbn, book, borrowedDate, toBeReturn, status, btnReturn));
            } else {
                ResultSet resultSet1 = DBConnection.getInstance().getConnection().createStatement().executeQuery("SELECT * FROM `return` WHERE borrowId = '" + borrowId + "'");
                if (resultSet1.next()) {
                    Date returnedDate = resultSet1.getDate(2);
                    Double fee = resultSet1.getDouble(3);
                    returned.add(new Return(borrowId, name, isbn, book, borrowedDate, toBeReturn, returnedDate, fee, status));

                }

            }

        }

    }


    public void navigateToHome_OnKeyPressed(KeyEvent keyEvent) throws IOException {
        if (keyEvent.getCode() == KeyCode.ESCAPE) {
            Parent root = FXMLLoader.load(this.getClass().getResource("/view/MainWindow.fxml"));
            Scene subScene = new Scene(root);
            Stage primaryStage = (Stage) this.Return.getScene().getWindow();
            primaryStage.setScene(subScene);
            primaryStage.centerOnScreen();

            TranslateTransition tt = new TranslateTransition(Duration.millis(350), subScene.getRoot());
            tt.setFromX(600);
            tt.setToX(0);
            tt.play();
        }

    }
}
