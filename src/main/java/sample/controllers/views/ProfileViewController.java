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
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
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
import sample.controllers.dialogs.ProfileEditViewController;
import sample.controllers.dialogs.ProfileSettingsViewController;
import sample.models.Contact;
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
import java.util.List;

public class ProfileViewController implements Initializable {

    @FXML    private FontAwesomeIcon iconUpload, iconEdit, iconSettings, iconDisconnect, iconBirthday, iconLocation, iconTlf, iconChangeColour, iconChangeTheme;
    @FXML    private Rectangle rectangleAvatar;
    @FXML    private Label tagUsername, tagEmail, tagContactsNumber, tagGroupsNumber, tagBirthday, tagLocation, tagTlf, tagMessagesNumber, tagCloudNumber;
    @FXML    private AnchorPane paneDialog, rootPane, blurPane;
    @FXML    private VBox vBoxLogRegister;
    @FXML    private HBox hBoxSocialMedia;

    private ArrayList<SocialMedia> arraySocialMedia;
    private MainController mainController;
    private Color selectedColour;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        setData();
        setIconsColour();
        setNumberColour();
        setAvatar();
        setSocialMedia(hBoxSocialMedia, Data.userData.getSocialMedia(), false);
        getFriendshipRequests();

    }

    public void setSocialMedia(HBox hBoxSocialMedia, JSONObject socialMediaJSON, boolean useColor) {

        hBoxSocialMedia.getChildren().removeAll();

        if (socialMediaJSON != null){
            arraySocialMedia = new ArrayList<>(
                    Arrays.asList(
                            new SocialMedia("twitter", "https://twitter.com/#", "TWITTER", "#5CAAF8"),
                            new SocialMedia("github", "https://github.com/#", "GITHUB", "#1D1D1D"),
                            new SocialMedia("facebook", "https://facebook.com/#", "FACEBOOK_SQUARE", "#0335FF"),
                            new SocialMedia("url", "#", "CHAIN", "#7FFE61"),
                            new SocialMedia("instagram", "https://instagram.com/#", "INSTAGRAM", "#FF3EF2"),
                            new SocialMedia("skype", "skype:#?chat", "SKYPE", "#0392FF"),
                            new SocialMedia("youtube", "https://youtube.com/c/#", "YOUTUBE_PLAY", "#FF3E3E")
                    )
            );

            for (SocialMedia socialMedia : arraySocialMedia) {
                if (socialMediaJSON.has(socialMedia.getName())){
                    FontAwesomeIcon icon = socialMedia.getIcon();

                    if (useColor)
                        icon.setFill(Color.valueOf(Data.userData.getHexCode()));
                    else icon.setFill(Color.WHITE);

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
        tagLocation.setText(Data.userData.getAddress());

        tagContactsNumber.setText(Integer.toString(Data.contactList.size()));

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

    private void getFriendshipRequests(){

        List<Contact> friendshipRequestsList = Data.contactManager.getFriendshipRequests();
        if (friendshipRequestsList.size() != 0){
            Label tagFriendships = new Label("SOLCITUDES DE AMISTAD:");
            tagFriendships.setPrefSize(430, 35);
            tagFriendships.setAlignment(Pos.CENTER);
            tagFriendships.setStyle("-fx-text-fill: white; -fx-font-size: 15");
            vBoxLogRegister.getChildren().add(tagFriendships);
            for (Contact c : friendshipRequestsList) {
                vBoxLogRegister.getChildren().add(friendshipRequestView(c));
            }
        }else{
            Label tagFriendships = new Label("NO HAY SOLICITUDES DE AMISTAD PENDIENTES");
            tagFriendships.setStyle("-fx-text-fill: white; -fx-font-size: 15");
            tagFriendships.setAlignment(Pos.CENTER);
            tagFriendships.setPrefSize(430, 35);
            vBoxLogRegister.getChildren().add(tagFriendships);
        }


    }

    private HBox friendshipRequestView(Contact c) {

        HBox hBoxFriendship = new HBox();
        hBoxFriendship.setAlignment(Pos.CENTER_LEFT);
        hBoxFriendship.setPrefSize(430, 55);
        hBoxFriendship.setPadding(new Insets(10));
        hBoxFriendship.setSpacing(10);
        hBoxFriendship.setStyle("-fx-background-color: #32323E; -fx-background-radius: 30");

        Rectangle rectangleAvatar = new Rectangle(35, 35);
        rectangleAvatar.setArcWidth(360);
        rectangleAvatar.setArcHeight(360);
        rectangleAvatar.setFill(new ImagePattern(new Image(c.getAvatar().getUrl(), rectangleAvatar.getWidth(), rectangleAvatar.getHeight(), true, false)));
        hBoxFriendship.getChildren().add(rectangleAvatar);

        Label tagName = new Label(c.toString());
        tagName.setPrefSize(250, 35);
        tagName.setStyle("-fx-text-fill: white; -fx-font-size: 15");
        hBoxFriendship.getChildren().add(tagName);

        HBox hBoxOptions = new HBox();
        hBoxOptions.setAlignment(Pos.CENTER_RIGHT);
        hBoxOptions.setSpacing(10);

        Button btnAcceptFriendship = new Button();
        btnAcceptFriendship.setPrefSize(50, 35);
        FontAwesomeIcon iconAdd = new FontAwesomeIcon();
        iconAdd.setIcon(FontAwesomeIconName.PLUS);
        iconAdd.setSize("25px");
        iconAdd.setFill(Color.WHITE);
        btnAcceptFriendship.setGraphic(iconAdd);
        btnAcceptFriendship.setStyle("-fx-background-radius: 30; -fx-background-color:  #39CE39");
        btnAcceptFriendship.setOnMouseClicked(event -> acceptFriendshipRequest(c, hBoxFriendship));

        Button btnRejectFriendship = new Button();
        btnRejectFriendship.setPrefSize(50, 35);
        FontAwesomeIcon iconReject = new FontAwesomeIcon();
        iconReject.setIcon(FontAwesomeIconName.CLOSE);
        iconReject.setSize("25px");
        iconReject.setFill(Color.WHITE);
        btnRejectFriendship.setGraphic(iconReject);
        btnRejectFriendship.setStyle("-fx-background-color: #CE3939; -fx-background-radius: 30");
        btnRejectFriendship.setOnMouseClicked(event -> rejectFriendshipRequest(c, hBoxFriendship));

        hBoxOptions.getChildren().add(btnAcceptFriendship);
        hBoxOptions.getChildren().add(btnRejectFriendship);

        hBoxFriendship.getChildren().add(hBoxOptions);

        return hBoxFriendship;

    }

    private void acceptFriendshipRequest(Contact c, HBox hBox){

        boolean success = Data.contactManager.acceptFriendRequest(c.getIdContact());
        if (success){
            vBoxLogRegister.getChildren().remove(hBox);
            tagContactsNumber.setText(Integer.toString(Data.contactList.size() + 1));
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("MultitaskAPP");
            alert.setHeaderText("Solicitud de amistad aceptada correctamente!");
            Data.setBlur();
            alert.showAndWait();
            Data.removeBlur();
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("MultitaskAPP");
            alert.setHeaderText("Error al aceptar la solicitud de amistad...");
            Data.setBlur();
            alert.showAndWait();
            Data.removeBlur();
        }

    }

    private void rejectFriendshipRequest(Contact c, HBox hBox){

        boolean success = Data.contactManager.rejectFriendRequest(c.getIdContact());
        if (success){
            vBoxLogRegister.getChildren().remove(hBox);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("MultitaskAPP");
            alert.setHeaderText("Solicitud de amistad rechazada correctamente!");
            Data.setBlur();
            alert.showAndWait();
            Data.removeBlur();
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("MultitaskAPP");
            alert.setHeaderText("Error al rechazar la solicitud de amistad...");
            Data.setBlur();
            alert.showAndWait();
            Data.removeBlur();
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
        Data.setBlur();
        Optional<ButtonType> result = alertDialog.showAndWait();
        Data.removeBlur();
        if (result.get() == ButtonType.OK){
            if (colorPicker.getValue() != null){
                selectedColour = colorPicker.getValue();
                Data.userData.setColourUser(parseColor(selectedColour));
                if (Data.userManager.updateColour()){
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("MultitaskAPP");
                    alert.setHeaderText("Color temático actualizado correctamente, la aplicacion debe ser reiniciada para poder visualizar los cambios");
                    Data.setBlur();
                    alert.showAndWait();
                    Data.removeBlur();
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

        Alert verifyAlert = new Alert(Alert.AlertType.WARNING);
        verifyAlert.setTitle("MultitaskAPP");
        verifyAlert.setHeaderText("Estas seguro de que quieres cerrar tu sesión?");
        verifyAlert.getButtonTypes().add(new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE));

        Optional<ButtonType> result = verifyAlert.showAndWait();
        if (result.get() == ButtonType.OK){
            new ProfileSettingsViewController().disconnect((Stage) vBoxLogRegister.getScene().getWindow());
        }
    }

    @FXML
    void editProfile(ActionEvent event) {
        try {
           FXMLLoader fxmlLoader = new FXMLLoader();
           fxmlLoader.setLocation(getClass().getClassLoader().getResource("windows/dialogs/profileEditView.fxml"));
           paneDialog.getChildren().add(fxmlLoader.load());
           ProfileEditViewController profileEditViewController = fxmlLoader.getController();
           profileEditViewController.setArraySocialMedia(arraySocialMedia);
           profileEditViewController.preloadData();
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
        setSocialMedia(hBoxSocialMedia, Data.userData.getSocialMedia(), false);

    }

    @FXML
    void gotoCloud(MouseEvent event) {

    }

    @FXML
    void gotoContacts(MouseEvent event) {
        mainController.gotoContacts(event);
    }

    @FXML
    void gotoGroups(MouseEvent event) {

    }

    @FXML
    void gotoMessages(MouseEvent event) {

    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

}
