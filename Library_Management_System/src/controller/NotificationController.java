package controller;


import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import util.DBConnection;
import util.Notification;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;


public class NotificationController {
    public AnchorPane notification;
    public TableView<Notification> tblNotification;
    public Label lblDate;
    public JFXButton btnNotifyAll;


    public void initialize() {
        tblNotification.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("name"));
        tblNotification.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("isbn"));
        tblNotification.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("book"));
        tblNotification.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("borrowDate"));
        tblNotification.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("returnDate"));
        tblNotification.getColumns().get(5).setCellValueFactory(new PropertyValueFactory<>("button"));


    }

    public void btnNotifyAll_OnAction(ActionEvent event) throws SQLException, MessagingException {
        System.out.println(tblNotification.getItems().size());
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

        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress("shahidfazaal@gmail.com"));//change accordingly
        String name = null;
        String book = null;
        for (Notification notification : tblNotification.getItems()) {
            name = notification.getName();
            book = notification.getBook();
            ResultSet resultSet = DBConnection.getInstance().getConnection().createStatement().executeQuery("SELECT email from `member` where name ='" + name + "'");
            resultSet.next();
            String email = resultSet.getString(1);
            System.out.println(email);
            message.addRecipients(Message.RecipientType.CC, InternetAddress.parse(email));
        }
        //compose message
        String l1 = "Hi!,";
        String l2 = "This email is to inform that, the book which is borrowed from IJSE Library still not returned. You will be charge Rs.50.00 for your delay. Please return the book quickly ";
        String l3 = "IJSE Library \n #223A 1/2, Galle Road, Panadura. \n +94 38 2222 800 / +94 38 2244 855";

        message.setSubject("Return Notification");
        message.setText(l1 + "\r\n" + "\r\n" + l2 + "\r\n" + "\r\n" + l3);

        //send message
        Transport.send(message);
        System.out.println("message sent successfully");
        System.out.println("Notification send to your email id successfully !");

        tblNotification.getItems().clear();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Calendar c = Calendar.getInstance();
        c.setTime(new Date()); // Now use today date.
        c.add(Calendar.DATE, 7); // Adding 5 days
        String output = sdf.format(c.getTime());
        System.out.println(output);

        PreparedStatement statement = DBConnection.getInstance().getConnection().prepareStatement("UPDATE notification SET notifyDate=? ");
        statement.setObject(1, output);
        statement.executeUpdate();


//        ObservableList<Notification> notifications = tblNotification.getItems();
//        ObservableList<Notification> a = notifications;
//        int size = tblNotification.getItems().size();
//
//        for (int i = 0; i < size ; i++) {
//          a.get(0).getButton().fire();
//        }

    }
}