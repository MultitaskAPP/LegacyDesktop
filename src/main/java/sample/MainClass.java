package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.net.URL;

public class MainClass extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        URL url = new File("src/main/java/sample/windows/login.fxml").toURI().toURL();
        Parent root = FXMLLoader.load(url);
        root.setEffect(new DropShadow());
        primaryStage.setTitle("MultitaskAPP | DESKTOP");
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setScene(new Scene(root, 1280, 720));

        url = new File("src/main/java/sample/windows/res/multitask_icon.png").toURI().toURL();
        Image icon = new Image(String.valueOf(url));
        primaryStage.getIcons().add(icon);

        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
