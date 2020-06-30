package controller;

import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;
import javafx.util.Duration;
import sun.audio.AudioPlayer;
import util.DBConnection;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.xml.transform.Result;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.regex.Pattern;

public class ForgetPassword {
    private static final AudioClip errorAlertSound = new AudioClip(AudioPlayer.class.getResource("/resource/windows_error.mp3").toString());
    private static final AudioClip success = new AudioClip(AudioPlayer.class.getResource("/resource/sounds/success.mp3").toString());
    private static final AudioClip informationAlertSound = new AudioClip(AudioPlayer.class.getResource("/resource/sounds/windows_10_notify.mp3").toString());
    public AnchorPane forgetPassword;
    public TextField txtUserEmail;
    public Button btnSubmit;
    String email;

    public void initialize() {
        txtUserEmail.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                String emailValidation = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{3}$";
                Pattern pattern = Pattern.compile(emailValidation);

                if (txtUserEmail.getText().trim().matches(String.valueOf(pattern))) {
                    txtUserEmail.setStyle(null);
                    btnSubmit.setDisable(false);
                    email = "done";

                } else {
                    txtUserEmail.setStyle("-fx-border-color: red");
                    btnSubmit.setDisable(true);
                    email = "null";

                }
            }
        });
    }

    public void btnSubmit_OnAction(ActionEvent event) throws SQLException, MessagingException {
        if (txtUserEmail.getText().trim().equals("")) {
            informationAlertSound.play();
            new Alert(Alert.AlertType.INFORMATION, "Enter the Email Address", ButtonType.OK).show();
            return;
        }

        String email = txtUserEmail.getText();
        PreparedStatement statement = DBConnection.getInstance().getConnection().prepareStatement("SELECT * FROM user WHERE emailAddress = ?");
        statement.setObject(1, email);
        ResultSet rs = statement.executeQuery();
        System.out.println("done");
        if (rs.next()) {
            String userName = rs.getString(2);
            String password = rs.getString(3);

            String to = email;//change accordingly
            //Get the session object
            Properties props = new Properties();
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.socketFactory.port", "465");
            props.put("mail.smtp.socketFactory.class",
                    "javax.net.ssl.SSLSocketFactory");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.port", "465");
            Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("shahidfazaal@gmail.com", "email password must come here");//Put your email id and password here
                }
            });
            //compose message
            String l1 = "Hi " + userName + "!,";
            String l2 = "Your password for the Email ID " + email + " is " + password;
            String l3 = "IJSE Library \n #223A 1/2, Galle Road, Panadura. \n +94 38 2222 800 / +94 38 2244 855";

            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress("shahidfazaal@gmail.com"));//change accordingly
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject("Your Forgot Password is");
            message.setText(l1 + "\r\n" + "\r\n" + l2 + "\r\n" + "\r\n" + l3);

            //send message
            Transport.send(message);
            System.out.println("message sent successfully");


            System.out.println("Password send to your email id successfully !");
            success.play();
            new Alert(Alert.AlertType.INFORMATION, "The password hass successfully sent to the email address " + email, ButtonType.OK).show();

        } else {
            errorAlertSound.play();
            new Alert(Alert.AlertType.ERROR, "there is no user with the email address " + email, ButtonType.OK).show();

        }
    }

    public void navigate_onKeyPressed(KeyEvent keyEvent) throws IOException {
        if (keyEvent.getCode() == KeyCode.ESCAPE) {
            Parent root = FXMLLoader.load(this.getClass().getResource("/view/Login.fxml"));
            Scene subScene = new Scene(root);
            Stage primaryStage = (Stage) this.forgetPassword.getScene().getWindow();
            primaryStage.setScene(subScene);
            primaryStage.centerOnScreen();

            TranslateTransition tt = new TranslateTransition(Duration.millis(350), subScene.getRoot());
            tt.setFromX(500);
            tt.setToX(0);
            tt.play();
        }
    }
}