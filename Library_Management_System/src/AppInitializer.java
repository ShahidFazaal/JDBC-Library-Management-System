import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import util.DBConnection;

import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;

public class AppInitializer extends Application {

    public static void main(String[] args) throws SQLException {
        launch(args);
        DBConnection.getInstance().getConnection().close();
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(this.getClass().getResource("/view/SplashScreen.fxml"));

        Scene mainScene = new Scene(root);
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setScene(mainScene);
        primaryStage.setTitle("Library Management System");

        primaryStage.resizableProperty().setValue(false);
        Image icon = new Image(getClass().getResourceAsStream("/resource/321934001.png"));
        primaryStage.getIcons().add(icon);
        primaryStage.centerOnScreen();
        primaryStage.show();



    }
}
