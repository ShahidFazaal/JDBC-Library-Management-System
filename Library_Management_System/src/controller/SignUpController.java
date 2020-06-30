package controller;

import com.jfoenix.controls.JFXButton;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import sun.audio.AudioPlayer;
import util.DBConnection;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpController {
    private static final AudioClip errorAlertSound = new AudioClip(AudioPlayer.class.getResource("/resource/windows_error.mp3").toString());
    private static final AudioClip success = new AudioClip(AudioPlayer.class.getResource("/resource/sounds/success.mp3").toString());

    private static final AudioClip informationAlertSound = new AudioClip(AudioPlayer.class.getResource("/resource/sounds/windows_10_notify.mp3").toString());
    public AnchorPane signUp;
    public TextField txtUserName;
    public TextField txtPhone;
    public TextField txtEmail;
    public TextField txtPassword;
    public JFXButton btnSignUp;
    public TextField txtPasswordVisible;
    public Pane up;
    public ImageView ur;
    public Pane ep;
    public ImageView er;
    public Pane pp;
    public ImageView pr;
    public Pane tp;
    public ImageView tr;
    int count = 0;

    String userName,password,email,telephone;

    public void initialize() {
        txtPasswordVisible.setVisible(false);
        up.setVisible(false);
        ur.setVisible(false);
        ep.setVisible(false);
        er.setVisible(false);
        pp.setVisible(false);
        pr.setVisible(false);
        tp.setVisible(false);
        tr.setVisible(false);

        txtUserName.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

                String userNameValidation = "^(?=.{5,8}$)(?![_.])(?!.*[_.]{2})[a-zA-Z0-9._]+(?<![_.])$";
                Pattern pattern = Pattern.compile(userNameValidation);
                if (txtUserName.getText().trim().matches(String.valueOf(pattern))) {
                    up.setVisible(true);
                    ur.setVisible(true);
                    userName = "done";
                } else {
                    up.setVisible(false);
                    ur.setVisible(false);
                    userName = "null";
                }
            }
        });


        txtPassword.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                String emailValidation = "^[a-zA-Z]\\w{3,14}$";
                Pattern pattern = Pattern.compile(emailValidation);
                if (txtPassword.getText().trim().matches(String.valueOf(pattern))) {
                    pp.setVisible(true);
                    pr.setVisible(true);
                    password = "done";
                } else {
                    pp.setVisible(false);
                    pr.setVisible(false);
                    password = "null";

                }
            }
        });


        txtEmail.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                String emailValidation = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{3}$";
                Pattern pattern = Pattern.compile(emailValidation);
                if (txtEmail.getText().trim().matches(String.valueOf(pattern))) {
                    ep.setVisible(true);
                    er.setVisible(true);
                    email = "done";

                } else {
                    ep.setVisible(false);
                    er.setVisible(false);
                    email = "null";

                }
            }
        });

        txtPhone.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    txtPhone.setText(newValue.replaceAll("[^\\d]", ""));
                    txtPhone.setStyle("-fx-border-color:  red");
                }else{
                    txtPhone.setStyle(null);
                }

                String phoneValidation = "^(?:0|94|\\+94|0094)?(?:(11|21|23|24|25|26|27|31|32|33|34|35|36|37|38|41|45|47|51|52|54|55|57|63|65|66|67|81|91)(0|2|3|4|5|7|9)|7(0|1|2|5|6|7|8)\\d)\\d{6}$";
                Pattern pattern = Pattern.compile(phoneValidation);

                if (txtPhone.getText().trim().matches(String.valueOf(pattern))) {
                    tp.setVisible(true);
                    tr.setVisible(true);
                    telephone = "done";
                } else {
                    tp.setVisible(false);
                    tr.setVisible(false);
                    telephone = "null";
                }
            }
        });

    }

    public void navigateLogin_OnMouseClick(MouseEvent mouseEvent) throws IOException {
        Parent root = FXMLLoader.load(this.getClass().getResource("/view/Login.fxml"));
        Scene subScene = new Scene(root);
        Stage primaryStage = (Stage) this.signUp.getScene().getWindow();

        primaryStage.setScene(subScene);
        primaryStage.centerOnScreen();

        TranslateTransition tt = new TranslateTransition(Duration.millis(350), subScene.getRoot());
        tt.setFromX(500);
        tt.setToX(0);
        tt.play();
    }

    public void onMouseEntered(MouseEvent mouseEvent) {
        if (mouseEvent.getSource() instanceof ImageView) {
            ImageView icon = (ImageView) mouseEvent.getSource();
            ScaleTransition scaleT = new ScaleTransition(Duration.millis(200), icon);
            scaleT.setToX(1.2);
            scaleT.setToY(1.2);
            scaleT.play();

            DropShadow glow = new DropShadow();
            glow.setColor(Color.CORNFLOWERBLUE);
            glow.setWidth(20);
            glow.setHeight(20);
            glow.setRadius(20);
            icon.setEffect(glow);
        }
    }

    public void onMouseExit(MouseEvent mouseEvent) {
        if (mouseEvent.getSource() instanceof ImageView) {
            ImageView icon = (ImageView) mouseEvent.getSource();
            ScaleTransition scaleT = new ScaleTransition(Duration.millis(200), icon);
            scaleT.setToX(1);
            scaleT.setToY(1);
            scaleT.play();

            icon.setEffect(null);
        }
    }

    public void btnSignUp_OnAction(ActionEvent event) throws SQLException, IOException {

        if (txtUserName.getText().equals("") || txtPassword.getText().equals("") || txtEmail.getText().equals("") || txtPhone.getText().equals("")){
            errorAlertSound.play();
            Alert alert = new Alert(Alert.AlertType.ERROR,"UserName,Password,Email or telephone number can't be empty",ButtonType.OK);
            alert.setHeaderText("Empty Text Fields");
            alert.show();
            return;
        }

            String userName = txtUserName.getText();
            String password = txtPassword.getText();
            String email = txtEmail.getText();
            int phone = Integer.parseInt(txtPhone.getText());

            if (this.userName == "done" && this.password == "done" && this.email == "done" && telephone == "done") {
                ResultSet resultSet = DBConnection.getInstance().getConnection().createStatement().executeQuery("SELECT * FROM user WHERE userName='" + userName + "' or emailAddress='" + email + "'");
                if (resultSet.next()) {
                    if (resultSet.getString(2).equals(userName)) {
                        informationAlertSound.play();
                        Alert alrt = new Alert(Alert.AlertType.INFORMATION, "The userName " + userName + " already in use Change the userName", ButtonType.OK);
                        alrt.setHeaderText("UserName Already in Use");
                        alrt.show();
                        txtUserName.setStyle("-fx-border-color: red");
                        return;
                    }
                    if (resultSet.getString(4).equals(email)) {
                        informationAlertSound.play();
                        Alert alert = new Alert(Alert.AlertType.INFORMATION, "The Email " + email + " already in use use a different email Address", ButtonType.OK);
                        alert.setHeaderText("Email address Already in use");
                        alert.show();
                        txtEmail.setStyle("-fx-border-color: red");
                        return;
                    }

                } else {

                    PreparedStatement statement = DBConnection.getInstance().getConnection().prepareStatement("INSERT INTO user values (0,?,?,?,?,DEFAULT ,DEFAULT )");
                    statement.setObject(1, userName);
                    statement.setObject(2, password);
                    statement.setObject(3, email);
                    statement.setObject(4, phone);
                    statement.executeUpdate();
                    informationAlertSound.play();

                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Successfully Registered", ButtonType.OK);
                    Optional<ButtonType> buttonType = alert.showAndWait();
                    if (buttonType.get() == ButtonType.OK) {


                        Parent root = FXMLLoader.load(this.getClass().getResource("/view/Login.fxml"));
                        Scene subScene = new Scene(root);
                        Stage primaryStage = (Stage) this.signUp.getScene().getWindow();

                        primaryStage.setScene(subScene);
                        primaryStage.centerOnScreen();

                    }

                }
            } else {
                errorAlertSound.play();
               Alert alert =  new Alert(Alert.AlertType.ERROR, "UserName,Password,Email or telephone is invalid", ButtonType.OK);
               alert.setHeaderText("Invalid");
               alert.show();


            }

        }

    public void showPassword_OnMouseClick(MouseEvent mouseEvent) {
        if (count == 0) {
            txtPasswordVisible.setText(txtPassword.getText());
            txtPasswordVisible.setVisible(true);
            txtPassword.setVisible(false);
            count = 1;
        } else {
            txtPasswordVisible.setVisible(false);
            txtPassword.setVisible(true);
            count = 0;
        }

    }
}
