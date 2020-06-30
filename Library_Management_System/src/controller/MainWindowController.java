package controller;

import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import sun.audio.AudioPlayer;
import util.DBConnection;
import util.Notification;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;
import java.util.Properties;

public class MainWindowController {


    public Rectangle view;
    public Rectangle view1;
    public Rectangle view2;

    public Label txtIssue;
    public Label txtAdd;
    public Label txtMemberAdd;
    public Label txtReturn;
    public Rectangle view3;
    public Rectangle view0;
    public AnchorPane Main;
    public Label lblBookCount;
    public Label tblMemberCount;
    public Label lblAvailableBook;
    public Label lblTotalFine;
    public  Label lblUser;
    private int bookCount = 0;
    private int memberCount = 0;
    private int availableBook = 0;
    LocalDate date = LocalDate.now();
    int stage = 0;
    static Boolean notificationMethod = false;
    private static final AudioClip success = new AudioClip(AudioPlayer.class.getResource("/resource/sounds/success.mp3").toString());



    public void initialize() throws IOException, SQLException {


        ResultSet resultSet4 = DBConnection.getInstance().getConnection().createStatement().executeQuery("SELECT notifyDate from notification");
        resultSet4.next();
            LocalDate notiDate = resultSet4.getDate(1).toLocalDate();
            if (LocalDate.now().isAfter(notiDate)){




        if (notificationMethod == false){

        NotificationController controller = null;
        ObservableList<Notification> notify = null;

        ResultSet resultSet1 = DBConnection.getInstance().getConnection().createStatement().executeQuery("SELECT b.*, m.name,email, k.title FROM borrow b inner join `member` m on b.nic = m.nic inner join book k on b.isbn = k.isbn where b.status = 'not returned';");
        while (resultSet1.next()){

            LocalDate bDate = resultSet1.getDate(4).toLocalDate();

            LocalDate rDate = resultSet1.getDate(5).toLocalDate();

            if (!date.isBefore(rDate)) {
                if (stage == 0) {
                    Parent root = null;
                    FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("/view/Notification.fxml"));
                    root = fxmlLoader.load();
                    controller = fxmlLoader.getController();
                    notify = controller.tblNotification.getItems();
                    controller.lblDate.setText(String.valueOf(LocalDate.now()));
                    Stage stage1 = new Stage();

                    Scene subScene = new Scene(root);
                    stage1.setScene(subScene);
                    stage1.centerOnScreen();
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            stage1.initOwner(Main.getScene().getWindow());
                            stage1.initModality(Modality.APPLICATION_MODAL);
                            stage1.showAndWait();
                        }
                    });

                    stage = 1;
                }
                if (stage == 1) {
                    int bId = resultSet1.getInt(1);

                    String name = resultSet1.getString(7);
                    String book = resultSet1.getString(9);
                    String isbn = resultSet1.getString(3);
                    Date borrowDate = resultSet1.getDate(4);
                    Date returnDate = resultSet1.getDate(5);
                    String email = resultSet1.getString(8);
                    Button btnNotify = new Button("Notify");
                    ObservableList<Notification> finalNotify = notify;
                    ObservableList<Notification> finalNotify1 = notify;
                    btnNotify.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
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
                                    return new PasswordAuthentication("shahidfazaal@gmail.com", "fazaal95143");//Put your email id and password here
                                }
                            });
                            //compose message
                            String l1 = "Hi " + name + "!,";
                            String l2 = "The book " + book + " which is borrowed from IJSE Library still not returned. You will be charge Rs.50.00 for your delay. Please return the book quickly ";
                            String l3 = "IJSE Library \n #223A 1/2, Galle Road, Panadura. \n +94 38 2222 800 / +94 38 2244 855";
                            MimeMessage message = new MimeMessage(session);

                            try {
                                message.setFrom(new InternetAddress("shahidfazaal@gmail.com"));//change accordingly
                                //message.addRecipients(Message.RecipientType.CC,
                                        //InternetAddress.parse("mzfazaal@gmail.com,zaidfazaal@gmail.com"));
                                message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
                                message.setSubject("Return Notification");
                                message.setText(l1 + "\r\n" + "\r\n" + l2 + "\r\n" + "\r\n" + l3);

                                //send message
                                Transport.send(message);
                                System.out.println("message sent successfully");
                                System.out.println("Notification send to your email id successfully !");
                            } catch (MessagingException e) {
                                e.printStackTrace();
                            }


                            //success.play();
                           // new Alert(Alert.AlertType.INFORMATION, "The delay notification sent successfully to " + email, ButtonType.OK).show();
                            PreparedStatement statement = null;
                            try {
                                statement = DBConnection.getInstance().getConnection().prepareStatement("INSERT into notification values (?,?)");
                                statement.setObject(1,bId);
                                statement.setObject(2,LocalDate.now());
                                statement.executeUpdate();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }




                           finalNotify.remove(0);



                        }
                    });

                    notify.add(new Notification(name, isbn, book, borrowDate, returnDate, btnNotify));



                }
                notificationMethod = true;
            }

            }

        }
        }


        lblUser.setText(LoginController.un);
        DropShadow glow = new DropShadow();
        glow.setColor(Color.SILVER);
        glow.setWidth(20);
        glow.setHeight(20);
        glow.setRadius(20);
        view.setEffect(glow);
        view1.setEffect(glow);
        view2.setEffect(glow);
        view3.setEffect(glow);
        view0.setEffect(glow);


        ResultSet sumSalary = DBConnection.getInstance().getConnection().createStatement().executeQuery("SELECT SUM(lateFee) from `return`");
        sumSalary.next();
        lblTotalFine.setText(sumSalary.getInt(1)+"");

        ResultSet resultSet = DBConnection.getInstance().getConnection().createStatement().executeQuery("SELECT isbn FROM book");
        while (resultSet.next()){
            bookCount++;
        }
        lblBookCount.setText(String.valueOf(bookCount));

        ResultSet resultSet2 = DBConnection.getInstance().getConnection().createStatement().executeQuery("SELECT nic FROM `member`");
        while (resultSet2.next()){
            memberCount++;
        }
        tblMemberCount.setText(String.valueOf(memberCount));

        ResultSet resultSet3 = DBConnection.getInstance().getConnection().createStatement().executeQuery("SELECT isbn FROM book WHERE status='not issued'");
        while (resultSet3.next()){
            availableBook++;
        }
        lblAvailableBook.setText(String.valueOf(availableBook));


    }


    public void PlayOnMouseEnter(MouseEvent mouseEvent) throws IOException {
        if (mouseEvent.getSource() instanceof ImageView) {
            ImageView icon = (ImageView) mouseEvent.getSource();

            switch (icon.getId()) {
                case "imgBookIssue":

                    txtIssue.setTextFill(Paint.valueOf("Blue"));
                    break;
                case "imgBookReturn":
                    txtReturn.setTextFill(Paint.valueOf("Blue"));
                    break;
                case "imgBookEntry":
                    txtAdd.setTextFill(Paint.valueOf("Blue"));
                    break;
                case "imgMemberEntry":
                    txtMemberAdd.setTextFill(Paint.valueOf("Blue"));
                    break;
            }



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

    public void PlayOnMouseExit(MouseEvent mouseEvent) {
        if (mouseEvent.getSource() instanceof ImageView) {
            ImageView icon = (ImageView) mouseEvent.getSource();
            ScaleTransition scaleT = new ScaleTransition(Duration.millis(200), icon);
            scaleT.setToX(1);
            scaleT.setToY(1);
            scaleT.play();
            icon.setEffect(null);
            txtIssue.setTextFill(Paint.valueOf("Black"));
            txtMemberAdd.setTextFill(Paint.valueOf("Black"));
            txtReturn.setTextFill(Paint.valueOf("Black"));
            txtAdd.setTextFill(Paint.valueOf("Black"));
        }
    }

    public void navigate(MouseEvent mouseEvent) throws IOException {
        if (mouseEvent.getSource() instanceof ImageView) {
            ImageView icon = (ImageView) mouseEvent.getSource();

            Parent root = null;

            switch (icon.getId()) {
                case "imgBookIssue":
                    root = FXMLLoader.load(this.getClass().getResource("/view/IssueBook.fxml"));

                    break;
                case "imgBookReturn":
                    root = FXMLLoader.load(this.getClass().getResource("/view/Return.fxml"));
                    break;
                case "imgBookEntry":
                    root = FXMLLoader.load(this.getClass().getResource("/view/AddBook.fxml"));
                    break;
                case "imgMemberEntry":
                    root = FXMLLoader.load(this.getClass().getResource("/view/AddMembers.fxml"));

                    break;

            }
            if (root != null) {
                Scene subScene = new Scene(root);
                Stage primaryStage = (Stage) this.Main.getScene().getWindow();
                primaryStage.setScene(subScene);
                primaryStage.centerOnScreen();

                TranslateTransition tt = new TranslateTransition(Duration.millis(350), subScene.getRoot());
                tt.setFromX(-subScene.getWidth());
                tt.setToX(0);
                tt.play();
            }
        }
    }


    //=================================== Notification Window ===================================================//

    public AnchorPane a;

    private Rectangle rectangle () {
        Rectangle rectangle = new Rectangle();
        rectangle.setLayoutX(10);
        rectangle.setLayoutY(20);
        rectangle.setHeight(98);
        rectangle.setWidth(475);
        return rectangle;

    }

    private TextField textField () {
        TextField textField = new TextField();
        textField.setLayoutX(20);
        textField.setLayoutY(410);
        textField.setMinWidth(450);
        textField.setMinHeight(25);
        return textField;
    }

    private Button button() {
        Button button = new Button("Notify");
        button.setLayoutX(371);
        button.setLayoutY(58);
        button.setPrefHeight(25);
        button.setPrefWidth(91);
        return button;
    }


    private void addControls    () {
        a.getChildren().add(0,rectangle());
        a.getChildren().add(1,textField());
        a.getChildren().add(2,button());
    }

    public void startForm () {
        a = new AnchorPane();
        Scene scene = new Scene(a, 500, 500);
        Stage stage = new Stage();
        stage.setScene(scene);
        addControls();
        stage.show();
        stage.toFront();



    }


}
