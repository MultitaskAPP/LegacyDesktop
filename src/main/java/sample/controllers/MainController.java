package sample.controllers;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
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
import sample.controllers.views.DashboardViewController;
import sample.controllers.views.ProfileViewController;
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
        Image image = new Image(Data.userData.getAvatarUser().getUrl(), rectAvatar.getWidth(), rectAvatar.getHeight(), false, true);
        ImagePattern imagePattern = new ImagePattern(image);
        rectAvatar.setFill(imagePattern);
    }

    private void resetButtonStyles(){
        for (int i = 0; i < vBoxButtons.getChildren().size(); i++){
            HBox hBox = (HBox) vBoxButtons.getChildren().get(i);
            Label tagButton = (Label) hBox.getChildren().get(1);
            tagButton.setStyle("-fx-font-family: 'Roboto Light'");
            hBox.setStyle("-fx-background-color: transparent; -fx-background-radius: 0");
            FontAwesomeIcon icon = (FontAwesomeIcon) hBox.getChildren().get(0);
            icon.setFill(Paint.valueOf("#FFFFFF"));
        }

        btnProfile.setStyle("-fx-background-color:  #272730; -fx-background-radius: 30");
        btnOptions.setStyle("-fx-background-color:  #272730; -fx-background-radius: 30");

    }

    private void setStyleButton(HBox selectedButton){

        if (selectedButton.equals(btnProfile) ||selectedButton.equals(btnOptions)){
            selectedButton.setStyle("-fx-background-radius: 30; -fx-background-color: " + Data.userData.getHexCode());
            if (selectedButton.equals(btnProfile)){
                Label tagButton = (Label) selectedButton.getChildren().get(1);
                tagButton.setStyle("-fx-font-family: 'Roboto Medium'");
            }
        }else{
            selectedButton.setStyle("-fx-background-color: #202027; -fx-background-radius: 30");
            FontAwesomeIcon selectedIcon = (FontAwesomeIcon) selectedButton.getChildren().get(0);
            selectedIcon.setFill(Paint.valueOf(Data.userData.getHexCode()));
            Label tagButton = (Label) selectedButton.getChildren().get(1);
            tagButton.setStyle("-fx-font-family: 'Roboto Medium'");
        }
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
    public void gotoDashboard(MouseEvent event) {
        try {
            // URL url = new File("src/main/java/sample/windows/views/dashboardView.fxml").toURI().toURL();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("windows/views/dashboardView.fxml"));
            scenePane.getChildren().add(loader.load());
            DashboardViewController dashboardViewController = loader.getController();
            dashboardViewController.preloadData();
            dashboardViewController.setMainController(this);
            resetButtonStyles();
            setStyleButton(btnDashboard);
            transitionEffect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void gotoTasks(MouseEvent event) {
        try {
            // URL url = new File("src/main/java/sample/windows/views/taskView.fxml").toURI().toURL();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("windows/views/taskView.fxml"));
            scenePane.getChildren().add(loader.load());
            resetButtonStyles();
            setStyleButton(btnTasks);
            transitionEffect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void gotoEvents(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("windows/views/eventView.fxml"));
            scenePane.getChildren().add(loader.load());
            resetButtonStyles();
            setStyleButton(btnEvents);
            transitionEffect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void gotoNotes(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("windows/views/noteView.fxml"));
            scenePane.getChildren().add(loader.load());
            resetButtonStyles();
            setStyleButton(btnNotes);
            transitionEffect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void gotoChats(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("windows/views/chatView.fxml"));
            scenePane.getChildren().add(loader.load());
            resetButtonStyles();
            setStyleButton(btnChats);
            transitionEffect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void gotoProfile(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("windows/views/profileView.fxml"));
            scenePane.getChildren().add(loader.load());
            ProfileViewController profileViewController = loader.getController();
            profileViewController.setMainController(this);
            resetButtonStyles();
            setStyleButton(btnProfile);
            transitionEffect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void gotoContacts(MouseEvent event){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("windows/views/contactView.fxml"));
            scenePane.getChildren().add(loader.load());
            resetButtonStyles();
            setStyleButton(btnProfile);
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

    public void updateAvatar() {
        rectAvatar.setFill(new ImagePattern(new Image(Data.userData.getAvatarUser().getUrl(), rectAvatar.getWidth(), rectAvatar.getHeight(), true, false)));
    }
}
