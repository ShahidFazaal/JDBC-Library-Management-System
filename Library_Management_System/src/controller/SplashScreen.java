package controller;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sun.audio.AudioPlayer;

import java.io.IOException;

public class SplashScreen {
    public AnchorPane splash;
    private static final AudioClip start = new AudioClip(AudioPlayer.class.getResource("/resource/sounds/windows_starts.mp3").toString());


    public void initialize() {
        new Splash().start();
    }

    class Splash extends Thread {
        public void run() {
            try {
                start.play();
                Thread.sleep(5000);
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        Parent root = null;
                        try {
                            root = FXMLLoader.load(this.getClass().getResource("/view/Login.fxml"));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Scene mainScene = new Scene(root);
                        Stage primaryStage = new Stage();
                        primaryStage.initStyle(StageStyle.UNDECORATED);
                        primaryStage.setScene(mainScene);
                        primaryStage.setTitle("Login");
                        primaryStage.resizableProperty().setValue(false);
                        Image icon = new Image(getClass().getResourceAsStream("/resource/321934001.png"));
                        primaryStage.getIcons().add(icon);
                        primaryStage.show();
                        splash.getScene().getWindow().hide();


                    }
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


}
