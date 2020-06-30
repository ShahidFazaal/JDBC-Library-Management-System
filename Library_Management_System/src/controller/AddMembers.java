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
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;
import javafx.util.Duration;
import sun.audio.AudioPlayer;
import util.DBConnection;
import util.Member;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.regex.Pattern;

public class AddMembers {
    private static final AudioClip informationAlertSound = new AudioClip(AudioPlayer.class.getResource("/resource/sounds/windows_10_notify.mp3").toString());

    public AnchorPane addMember;
    public JFXButton btnAddNewMember;
    public JFXTextField txtNic;
    public JFXTextField txtName;
    public JFXTextField txtPhone;
    public JFXButton btnAddMember;
    public JFXButton btnDeleteMember;
    public TableView <Member> tblMember;
    public JFXTextField txtEmail;
    String emailVerification;

    public void initialize() throws SQLException {
        txtNic.setDisable(true);
        txtName.setDisable(true);
        txtPhone.setDisable(true);
        txtEmail.setDisable(true);
        btnAddMember.setDisable(true);
        btnDeleteMember.setDisable(true);
        loadMembers();

        tblMember.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("nic"));
        tblMember.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("name"));
        tblMember.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("phone"));
        tblMember.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("email"));

        tblMember.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Member>() {
            @Override
            public void changed(ObservableValue<? extends Member> observable, Member oldValue, Member newValue) {
                Member selectedItem = tblMember.getSelectionModel().getSelectedItem();
                if (selectedItem == null){
                    btnAddMember.setText("Add Member");
                    btnDeleteMember.setDisable(true);
                    txtNic.clear();
                    txtName.clear();
                    txtPhone.clear();
                    txtEmail.clear();
                    return;
                }
                txtNic.setEditable(false);
                txtNic.setDisable(false);
                txtName.setDisable(false);
                txtPhone.setDisable(false);
                txtEmail.setDisable(false);
                btnAddMember.setDisable(false);
                btnDeleteMember.setDisable(false);
                txtNic.setText(newValue.getNic());
                txtName.setText(newValue.getName());
                txtPhone.setText(newValue.getPhone()+"");
                txtEmail.setText(newValue.getEmail() +"");
                btnAddMember.setText("Update");
            }
        });


        txtEmail.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                String emailValidation = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{3}$";
                Pattern pattern = Pattern.compile(emailValidation);
                if (txtEmail.getText().trim().matches(String.valueOf(pattern)) || txtEmail.getText().trim().equals("")) {
                    txtEmail.setStyle(null);
                   emailVerification = "done";

                } else {
                    txtEmail.setStyle("-fx-border-color: red");
                    emailVerification = "null";
                }
            }
        });



    }

    public void btnAddNewMember_OnAction(ActionEvent event) {
        txtNic.setEditable(true);
        tblMember.getSelectionModel().clearSelection();
        txtNic.setDisable(false);
        txtName.setDisable(false);
        txtPhone.setDisable(false);
        txtEmail.setDisable(false);
        btnAddMember.setDisable(false);
        btnDeleteMember.setDisable(true);
        txtNic.clear();
        txtName.clear();
        txtPhone.clear();
        btnAddMember.setText("Add Member");
        txtNic.requestFocus();

    }

    public void txtTelephone_OnAction(ActionEvent event) {
    }

    public void btnAddMember_OnAction(ActionEvent event) throws SQLException {
        ResultSet resultSet = DBConnection.getInstance().getConnection().createStatement().executeQuery("select * from `member` where email = '" + txtEmail.getText() + "' or nic ='"+txtNic.getText()+"' or phone = '"+txtPhone.getText()+"' ");
        if (resultSet.next()){
            new Alert(Alert.AlertType.INFORMATION,"The email, mobile or nic already in use",ButtonType.OK).show();
            return;
        }

        if (emailVerification == "done") {
            String nic = txtNic.getText();
            String name = txtName.getText();
            String email = txtEmail.getText();
            int phone = Integer.parseInt(txtPhone.getText());
            if (btnAddMember.getText().equals("Add Member")) {

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, " Press ok for the confirmation", ButtonType.OK, ButtonType.CANCEL);
                informationAlertSound.play();
                Optional<ButtonType> buttonType = alert.showAndWait();
                if (buttonType.get() == ButtonType.OK) {
                    PreparedStatement statement = DBConnection.getInstance().getConnection().prepareStatement("INSERT INTO `member` VALUES (?,?,?,?)");
                    statement.setObject(1, nic);
                    statement.setObject(2, name);
                    statement.setObject(3, phone);
                    statement.setObject(4, email);
                    statement.executeUpdate();
                    loadMembers();
                }
            } else {
                PreparedStatement statement = DBConnection.getInstance().getConnection().prepareStatement("UPDATE `member` SET nic=?,name=?,phone=?,email=?  WHERE nic=?");
                statement.setObject(1, nic);
                statement.setObject(2, name);
                statement.setObject(3, phone);
                statement.setObject(4, nic);
                statement.setObject(5, email);
                statement.executeUpdate();
                loadMembers();
                txtNic.requestFocus();
            }
            txtNic.clear();
            txtName.clear();
            txtPhone.clear();
            txtEmail.clear();

        }else{
            informationAlertSound.play();
            new Alert(Alert.AlertType.INFORMATION,"Email Address is invalid",ButtonType.OK).show();
        }
    }

    public void btnDeleteMember_OnAction(ActionEvent event) throws SQLException {
        String nic = tblMember.getSelectionModel().getSelectedItem().getNic();
        PreparedStatement statement = DBConnection.getInstance().getConnection().prepareStatement("DELETE FROM `member` WHERE nic=?");
        statement.setObject(1,nic);
        statement.executeUpdate();
        loadMembers();
        tblMember.getSelectionModel().clearSelection();
        txtNic.setDisable(false);
        txtName.setDisable(false);
        txtPhone.setDisable(false);
        txtEmail.setDisable(false);
        btnAddMember.setDisable(false);
        btnDeleteMember.setDisable(false);
        txtNic.requestFocus();
        if (tblMember.getItems().size() ==0){
            btnDeleteMember.setDisable(true);
        }

    }

    public void navigateHome_OnKeyPressed(KeyEvent keyEvent) throws IOException {
            if (keyEvent.getCode() == KeyCode.ESCAPE) {
                Parent root = FXMLLoader.load(this.getClass().getResource("/view/MainWindow.fxml"));
                Scene subScene = new Scene(root);
                Stage primaryStage = (Stage) this.addMember.getScene().getWindow();
                primaryStage.setScene(subScene);
                primaryStage.centerOnScreen();

                TranslateTransition tt = new TranslateTransition(Duration.millis(350), subScene.getRoot());
                tt.setFromX(600);
                tt.setToX(0);
                tt.play();
            }

        if (keyEvent.getCode() == KeyCode.ENTER){
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    txtNic.setEditable(true);
                    btnAddMember.setText("Add Member");
                    txtNic.setDisable(false);
                    txtName.setDisable(false);
                    txtPhone.setDisable(false);
                    txtEmail.setDisable(false);
                    btnAddMember.setDisable(false);
                    txtNic.clear();
                    txtName.clear();
                    txtPhone.clear();
                    tblMember.getSelectionModel().clearSelection();
                    txtNic.requestFocus();
                }
            });


        }

        }

    private void loadMembers() throws SQLException {
        ObservableList<Member> member = tblMember.getItems();
        ResultSet resultSet = DBConnection.getInstance().getConnection().createStatement().executeQuery("SELECT  * FROM `member`");
        member.clear();
        while (resultSet.next()){
            member.add(new Member(resultSet.getString(1),resultSet.getString(2),resultSet.getInt(3),resultSet.getString(4)));
        }
    }

}
