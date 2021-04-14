package sample.controllers;

import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML    private AnchorPane mainPane;
    @FXML    private AnchorPane scenePane;

    public static double x, y;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        gotoDashboard(null);

    }

    @FXML
    void windowDrag(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setX(event.getScreenX() - x);
        stage.setY(event.getScreenY() - y);

    }

    @FXML
    void windowPressed(MouseEvent event) {
        x = event.getSceneX();
        y = event.getSceneY();
    }

    @FXML
    void close(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
        System.exit(0);
    }

    @FXML
    void minimize(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    void gotoDashboard(ActionEvent event) {
        try {
            transitionEffect();
            URL url = new File("src/main/java/sample/windows/views/dashboardView.fxml").toURI().toURL();
            scenePane.getChildren().add(FXMLLoader.load(url));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void gotoTasks(ActionEvent event) {
        try {
            transitionEffect();
            URL url = new File("src/main/java/sample/windows/views/taskView.fxml").toURI().toURL();
            scenePane.getChildren().add(FXMLLoader.load(url));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void transitionEffect(){

        // transitionEffect()
        // Transición al cambiar de Scene, efecto de desvanecido.

        FadeTransition fadeTransition = new FadeTransition(new Duration(250), scenePane);
        fadeTransition.setFromValue(0.5);
        fadeTransition.setToValue(1);
        fadeTransition.play();

    }
}
