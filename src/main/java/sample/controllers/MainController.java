package sample.controllers;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import sample.utils.Data;
import sample.utils.ImageTweakerTool;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML    private AnchorPane mainPane;
    @FXML    private AnchorPane scenePane;

    @FXML    private HBox btnDashboard, btnTasks, btnEvents, btnNotes, btnChats;
    @FXML    private HBox btnProfile, btnOptions;
    @FXML    private VBox vBoxButtons;
    @FXML    private Rectangle rectAvatar;

    public static double x, y;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        preloadMenu();
        gotoDashboard(null);

    }

    private void preloadMenu(){

        Image image = new Image(new ImageTweakerTool(Data.userData.getIdUser()).getProfilePicUser(), rectAvatar.getWidth(), rectAvatar.getHeight(), false, true);
        ImagePattern imagePattern = new ImagePattern(image);
        rectAvatar.setFill(imagePattern);

    }

    private void resetButtonStyles(){
        for (int i = 0; i < vBoxButtons.getChildren().size(); i++){
            HBox hBox = (HBox) vBoxButtons.getChildren().get(i);
            hBox.setStyle("-fx-background-color: transparent; -fx-background-radius: 0");
            FontAwesomeIcon icon = (FontAwesomeIcon) hBox.getChildren().get(0);
            icon.setFill(Paint.valueOf("#FFFFFF"));
        }
    }

    private void setStyleButton(HBox selectedButton){
        selectedButton.setStyle("-fx-background-color: #202027; -fx-background-radius: 30");
        FontAwesomeIcon selectedIcon = (FontAwesomeIcon) selectedButton.getChildren().get(0);
        selectedIcon.setFill(Paint.valueOf(Data.userData.getHexCode()));
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
    void gotoDashboard(MouseEvent event) {
        try {
            URL url = new File("src/main/java/sample/windows/views/dashboardView.fxml").toURI().toURL();
            scenePane.getChildren().add(FXMLLoader.load(url));
            resetButtonStyles();
            setStyleButton(btnDashboard);
            transitionEffect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void gotoTasks(MouseEvent event) {
        try {
            URL url = new File("src/main/java/sample/windows/views/taskView.fxml").toURI().toURL();
            scenePane.getChildren().add(FXMLLoader.load(url));
            resetButtonStyles();
            setStyleButton(btnTasks);
            transitionEffect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void gotoEvents(MouseEvent event) {
        try {
            URL url = new File("src/main/java/sample/windows/views/eventView.fxml").toURI().toURL();
            scenePane.getChildren().add(FXMLLoader.load(url));
            resetButtonStyles();
            setStyleButton(btnEvents);
            transitionEffect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void gotoNotes(MouseEvent event) {
        try {
            URL url = new File("src/main/java/sample/windows/views/noteView.fxml").toURI().toURL();
            scenePane.getChildren().add(FXMLLoader.load(url));
            resetButtonStyles();
            setStyleButton(btnNotes);
            transitionEffect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void gotoChats(MouseEvent event) {
        try {
            URL url = new File("src/main/java/sample/windows/views/chatView.fxml").toURI().toURL();
            scenePane.getChildren().add(FXMLLoader.load(url));
            resetButtonStyles();
            setStyleButton(btnChats);
            transitionEffect();
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
