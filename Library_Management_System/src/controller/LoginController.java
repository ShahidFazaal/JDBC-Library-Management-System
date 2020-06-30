package controller;

import com.jfoenix.controls.JFXButton;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.AudioClip;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import sun.audio.AudioPlayer;
import util.DBConnection;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;


public class LoginController {
    public TextField txtUserName;
    public AnchorPane Login;
    public JFXButton btnLogin;
    public TextField txtPassword;
    public Label lblForgetPassword;
    public Label lblSignUp;
    public ImageView btnClose;
    public static String un;

    private static final AudioClip errorAlertSound = new AudioClip(AudioPlayer.class.getResource("/resource/windows_error.mp3").toString());
    private static final AudioClip success = new AudioClip(AudioPlayer.class.getResource("/resource/sounds/success.mp3").toString());
    private static final AudioClip informationAlertSound = new AudioClip(AudioPlayer.class.getResource("/resource/sounds/windows_10_notify.mp3").toString());
    public ProgressBar pbLoading;


    public void initialize() throws InterruptedException {
        pbLoading.setVisible(false);



    }


    public void change_OnMouseClick(MouseEvent mouseEvent) {
    }

    public void btnClose_OnMouseClick(MouseEvent mouseEvent) {
        Platform.exit();
    }

    public void lblSignUp_OnMouseClick(MouseEvent mouseEvent) throws IOException {
        Parent root = FXMLLoader.load(this.getClass().getResource("/view/SignUp.fxml"));
        Scene subScene = new Scene(root);
        Stage primaryStage = (Stage) this.Login.getScene().getWindow();

        primaryStage.setScene(subScene);
        primaryStage.centerOnScreen();

        TranslateTransition tt = new TranslateTransition(Duration.millis(350), subScene.getRoot());
        tt.setFromX(-subScene.getWidth());
        tt.setToX(0);
        tt.play();


    }


    public void btnLogin_OnAction(ActionEvent event) throws SQLException, InterruptedException {
        if (txtUserName.getText().trim().equals("") || txtPassword.getText().trim().equals("")){
            informationAlertSound.play();
            new Alert(Alert.AlertType.INFORMATION,"UseName and Password can't be empty!",ButtonType.OK).show();
            return;
        }

        String userName = txtUserName.getText();
        String password = txtPassword.getText();
        PreparedStatement statement = DBConnection.getInstance().getConnection().prepareStatement("SELECT * FROM user WHERE userName = ? and password=?");
        statement.setObject(1,userName);
        statement.setObject(2,password);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()){
            if (resultSet.getString(7).equals("not active")){
                informationAlertSound.play();
                new Alert(Alert.AlertType.INFORMATION,"User is not active contact the admin!",ButtonType.OK).show();
                return;
            }
            if (resultSet.getString(6).equals("admin")){
                un = userName+ " Admin";
            }else{
                un = userName;
            }

            new Splash().start();


        }else{
            errorAlertSound.play();
            new Alert(Alert.AlertType.ERROR,"The userName or password is incorrect", ButtonType.OK).show();
        }


    }

    public void lblForgotPassword_OnMuseClick(MouseEvent mouseEvent) throws IOException {
        Parent root = FXMLLoader.load(this.getClass().getResource("/view/ForgetPassword.fxml"));
        Scene subScene = new Scene(root);
        Stage primaryStage = (Stage) this.Login.getScene().getWindow();

        primaryStage.setScene(subScene);
        primaryStage.centerOnScreen();

        TranslateTransition tt = new TranslateTransition(Duration.millis(350), subScene.getRoot());
        tt.setFromX(-subScene.getWidth());
        tt.setToX(0);
        tt.play();
    }

    class Splash extends Thread {
        public void run() {
            try {
                pbLoading.setVisible(true);
                Thread.sleep(5000);
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        Parent root = null;
                        try {
                            root = FXMLLoader.load(this.getClass().getResource("/view/MainWindow.fxml"));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Scene mainScene = new Scene(root);
                        Stage primaryStage = new Stage();
                        primaryStage.setScene(mainScene);
                        primaryStage.setTitle("Library Management System");
                        primaryStage.resizableProperty().setValue(false);
                        Image icon = new Image(getClass().getResourceAsStream("/resource/321934001.png"));
                        primaryStage.getIcons().add(icon);
                        primaryStage.centerOnScreen();
                        primaryStage.show();
                        Login.getScene().getWindow().hide();


                    }
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }



}
