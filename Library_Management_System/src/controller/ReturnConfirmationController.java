package controller;

import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.AudioClip;
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

public class ReturnConfirmationController {
    public AnchorPane returnConfirmation;
    public TextField txtLateFee;
    public JFXButton btnConfirm;
    public Label lblToBeReturnDate;
    public Label lblReturningDate;
    public Label lblBoroowDate;
    public Label lblName;
    public Label lblNic;
    public Label lblBook;
    public ImageView imgCheck;
    public JFXButton btnOk;

    String payment = null;
    private static final AudioClip informationAlertSound = new AudioClip(AudioPlayer.class.getResource("/resource/sounds/windows_10_notify.mp3").toString());
    private static final AudioClip errorAlertSound = new AudioClip(AudioPlayer.class.getResource("/resource/windows_error.mp3").toString());
    private static final AudioClip success = new AudioClip(AudioPlayer.class.getResource("/resource/sounds/success.mp3").toString());

    int borrowId;
    public String nic;
    public String name;
    public String isbn = null;
    public String book;
    public Date borrowedDate;
    public Date toBeReturn;
    public String status;

    public void initialize() {

        imgCheck.setVisible(false);
        System.out.println(payment);
        btnOk.setDisable(true);
    }


    public void btnConfirm_OnAction(ActionEvent event) {
        payment = "paid";
        imgCheck.setVisible(true);
        System.out.println(payment);
        btnOk.setDisable(false);
    }

    public void btnOk_OnAction(ActionEvent event) throws IOException, SQLException {
        if (payment == "paid") {
//            System.out.println("hoooo");
//            FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("/view/Return.fxml"));
//            Parent root = fxmlLoader.load();
//            ReturnController controller = (ReturnController) fxmlLoader.getController();

                returnConfirmation.getScene().getWindow().hide();


            }else{
                System.out.println("need to pay");
            }
        }
    }
