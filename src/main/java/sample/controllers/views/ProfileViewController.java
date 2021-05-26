package sample.controllers.views;

import com.sun.javafx.scene.control.CustomColorDialog;
import de.jensd.fx.glyphs.GlyphIcon;
import de.jensd.fx.glyphs.GlyphIconName;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconName;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.json.JSONObject;
import sample.controllers.MainController;
import sample.models.SocialMedia;
import sample.utils.Data;
import sample.utils.ImageTweakerTool;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

public class ProfileViewController implements Initializable {

    @FXML    private FontAwesomeIcon iconUpload, iconEdit, iconSettings, iconDisconnect, iconBirthday, iconLocation, iconTlf, iconChangeColour, iconChangeTheme;
    @FXML    private Rectangle rectangleAvatar;
    @FXML    private Label tagUsername, tagEmail, tagContactsNumber, tagGroupsNumber, tagBirthday, tagLocation, tagTlf, tagMessagesNumber, tagCloudNumber;
    @FXML    private AnchorPane paneContacts, paneGroups, paneMessages, paneCloud, paneDialog, rootPane, blurPane;
    @FXML    private VBox vBoxLogRegister;
    @FXML    private HBox hBoxSocialMedia;

    private MainController mainController;
    private Color selectedColour;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        setData();
        setIconsColour();
        setNumberColour();
        setAvatar();
        setSocialMedia();
    }

    private void setSocialMedia() {

        if (Data.userData.getSocialMedia() != null){
            JSONObject socialMediaJSON = Data.userData.getSocialMedia();

            ArrayList<SocialMedia> arraySocialMedia = new ArrayList<>(
                    Arrays.asList(
                            new SocialMedia("twitter", "https://twitter.com/#", "TWITTER"),
                            new SocialMedia("github", "https://github.com/#", "GITHUB"),
                            new SocialMedia("facebook", "https://facebook.com/#", "FACEBOOK_SQUARE"),
                            new SocialMedia("url", "#", "CHAIN"),
                            new SocialMedia("instagram", "https://instagram.com/#", "INSTAGRAM"),
                            new SocialMedia("skype", "skype:#?chat", "SKYPE"),
                            new SocialMedia("youtube", "https://youtube.com/c/#", "YOUTUBE_PLAY")
                    )
            );

            for (SocialMedia socialMedia : arraySocialMedia) {
                if (socialMediaJSON.has(socialMedia.getName())){
                    FontAwesomeIcon icon = socialMedia.getIcon();
                    icon.setFill(Color.WHITE);
                    icon.setSize("40px");
                    icon.setOnMouseClicked(mouseEvent -> browseURL(socialMedia.getUrl().concat(socialMediaJSON.getString(socialMedia.getName())).replaceFirst("#", "")));
                    hBoxSocialMedia.getChildren().add(icon);
                }
            }

        }
    }

    private void browseURL(String url){
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            try {
                Desktop.getDesktop().browse(new URI(url));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
    }

    private void setData(){

        tagUsername.setText(Data.userData.getName() + " " + Data.userData.getFirstSurname() + " " + Data.userData.getLastSurname());
        tagEmail.setText(Data.userData.getEmail());
        tagBirthday.setText(Data.userData.getBirthday().toLocalDate().toString());
        tagTlf.setText(Integer.toString(Data.userData.getTlf()));
        tagGroupsNumber.setText(Integer.toString(Data.arrayGroupsUser.size()));

    }

    private void setAvatar(){
        Image avatarImage = new Image(Data.userData.getAvatarUser().getUrl(), rectangleAvatar.getWidth(), rectangleAvatar.getHeight(), true, false);
        ImagePattern imagePattern = new ImagePattern(avatarImage);
        rectangleAvatar.setFill(imagePattern);
    }

    private void setIconsColour(){

        ArrayList<FontAwesomeIcon> arrayIcons = new ArrayList<>(
                Arrays.asList(
                        iconBirthday, iconLocation, iconTlf, iconChangeColour, iconChangeTheme, iconEdit, iconSettings, iconDisconnect, iconUpload
                )
        );

        for (FontAwesomeIcon icon : arrayIcons) {
            icon.setFill(Paint.valueOf(Data.userData.getHexCode()));
        }
    }

    private void setNumberColour(){

        ArrayList<Label> arrayNumber = new ArrayList<>(
                Arrays.asList(
                        tagCloudNumber, tagContactsNumber, tagGroupsNumber, tagMessagesNumber
                )
        );

        for (Label tagNumber : arrayNumber) {
            tagNumber.setStyle("-fx-font-size: 75; -fx-font-family: 'Roboto Medium'; -fx-text-fill: " + Data.userData.getHexCode());
        }
    }

    @FXML
    void changeColour(ActionEvent event) {
        Alert alertDialog = new Alert(Alert.AlertType.INFORMATION);
        ColorPicker colorPicker = new ColorPicker();
        colorPicker.setValue(null);
        alertDialog.setTitle("MultitaskAPP");
        alertDialog.setHeaderText("Cambia tu color temático");
        alertDialog.getDialogPane().setContent(colorPicker);
        Optional<ButtonType> result = alertDialog.showAndWait();
        if (result.get() == ButtonType.OK){
            if (colorPicker.getValue() != null){
                selectedColour = colorPicker.getValue();
                Data.userData.setColourUser(parseColor(selectedColour));
                if (Data.userManager.updateColour()){
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("MultitaskAPP");
                    alert.setHeaderText("Color temático actualizado correctamente, la aplicacion debe ser reiniciada para poder visualizar los cambios");
                    alert.showAndWait();
                }

            }else{
                System.out.println("[DEBUG] - No se ha escogido color, abortando");
            }
        }
    }

    private java.awt.Color parseColor(Color sc) {

        java.awt.Color awtColor = new java.awt.Color((float) sc.getRed(), (float) sc.getGreen(), (float) sc.getBlue(), (float) sc.getOpacity());

        int r = awtColor.getRed();
        int g = awtColor.getGreen();
        int b = awtColor.getBlue();

        return awtColor;
    }

    @FXML
    void changeTheme(ActionEvent event) {

    }

    @FXML
    public void disconnect(ActionEvent event){

        try {
            Data.properties.clear();
            Data.storeProperties(Data.properties);
            System.out.println("[DEBUG] - Datos del usuario internos eliminados correctamente!");
            Stage thisStage = (Stage) vBoxLogRegister.getScene().getWindow();
            thisStage.close();

            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getClassLoader().getResource("windows/login.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("MultitaskAPP | DESKTOP");
            stage.initStyle(StageStyle.UNDECORATED);
            Image icon = new Image("windows/res/icons/multitask_icon.png");
            stage.getIcons().add(icon);
            stage.show();

        }catch (Exception e){
            System.out.println("[DEBUG] - Error al cerrar la sesion...");
        }

    }

    @FXML
    void editProfile(ActionEvent event) {
        try {
           FXMLLoader fxmlLoader = new FXMLLoader();
           fxmlLoader.setLocation(getClass().getClassLoader().getResource("windows/dialogs/profileEditView.fxml"));
           paneDialog.getChildren().add(fxmlLoader.load());
           paneDialog.setDisable(false);
           paneDialog.setVisible(true);
           rootPane.setEffect(new BoxBlur());
           blurPane.setVisible(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void showSettings(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getClassLoader().getResource("windows/dialogs/profileSettingsView.fxml"));
            paneDialog.getChildren().add(fxmlLoader.load());
            paneDialog.setDisable(false);
            paneDialog.setVisible(true);
            rootPane.setEffect(new BoxBlur());
            blurPane.setVisible(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void uploadAvatar(ActionEvent event) {
        ImageTweakerTool imageTweakerTool = new ImageTweakerTool(Data.userData.getIdUser());
        File newAvatar = imageTweakerTool.importImage();
        Data.userData.setAvatarUser(new Image(imageTweakerTool.transformImage(newAvatar)));
        mainController.updateAvatar();
        setAvatar();
    }

    @FXML
    void dismissMenu(MouseEvent event) {
        rootPane.setEffect(null);
        blurPane.setVisible(false);
        paneDialog.setVisible(false);
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

}
