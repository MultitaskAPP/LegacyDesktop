package sample.controllers.views;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import sample.controllers.dialogs.ProfileSettingsViewController;
import sample.models.Contact;
import sample.utils.Data;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class ContactViewController implements Initializable {

    @FXML    private TextField tfSearch;
    @FXML    private Rectangle avatarData;
    @FXML    private VBox vBoxContactsList, vBoxContactData;
    @FXML    private Label tagName, tagEmail, tagBirthday, tagTlf, tagAddress;
    @FXML    private HBox hBoxSocialMedia;
    @FXML    private Button btnAddContact, btnSendEmail, btnSendMessage,btnShowGroups, btnDeleteContact;
    @FXML    private FontAwesomeIcon iconBirthday, iconTlf, iconAddress, iconGroups, iconEmail, iconChat;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        setIconStyle();
        getContacts();

    }

    private void setIconStyle() {

        btnAddContact.setStyle("-fx-background-radius: 30; -fx-background-color: " + Data.userData.getHexCode());
        ArrayList<FontAwesomeIcon> arrayIcons = new ArrayList<>(
                Arrays.asList(
                        iconBirthday, iconAddress, iconTlf, iconChat, iconEmail, iconGroups
                )
        );

        for (FontAwesomeIcon icon : arrayIcons) {
            icon.setFill(Paint.valueOf(Data.userData.getHexCode()));
        }
    }

    private void getContacts(){

        vBoxContactData.getChildren().removeAll();
        List<Contact> contactList = Data.contactManager.getAllContacts();
        for (Contact c : contactList) {
            vBoxContactsList.getChildren().add(contactView(c));
        }

    }

    private HBox contactView(Contact c){

        HBox hBoxContact = new HBox();
        hBoxContact.setAlignment(Pos.CENTER_LEFT);
        hBoxContact.setPrefSize(200, 65);
        hBoxContact.setPadding(new Insets(10));
        hBoxContact.setSpacing(10);

        Rectangle avatarList = new Rectangle(45, 45);
        avatarList.setArcHeight(360);
        avatarList.setArcWidth(360);

        Image image = new Image(c.getAvatar().getUrl(), avatarList.getWidth(), avatarList.getHeight(), true, false);
        avatarList.setFill(new ImagePattern(image));

        Label tagName = new Label(c.toString());
        tagName.setStyle("-fx-font-size: 20; -fx-text-fill: white");

        hBoxContact.getChildren().add(avatarList);
        hBoxContact.getChildren().add(tagName);
        hBoxContact.setStyle("-fx-background-color:  #32323E; -fx-background-radius: 30");
        hBoxContact.setOnMouseClicked(mouseEvent -> getContactData(hBoxContact, c));

        return hBoxContact;

    }

    private void styleContactList(HBox hBoxSelected){
        for (int i = 0; i < vBoxContactsList.getChildren().size(); i++){
            HBox hBoxContact = (HBox) vBoxContactsList.getChildren().get(i);
            hBoxContact.setStyle("-fx-background-color:  #32323E; -fx-background-radius: 30");
        }

        hBoxSelected.setStyle("-fx-background-radius: 30; -fx-background-color: " + Data.userData.getHexCode());
    }

    private void getContactData(HBox hBoxContact, Contact c) {

        styleContactList(hBoxContact);

        avatarData.setFill(new ImagePattern(new Image(c.getAvatar().getUrl(), avatarData.getWidth(), avatarData.getHeight(), true, false)));
        tagName.setText(c.toString());
        tagEmail.setText(c.getEmail());

        hBoxSocialMedia.getChildren().clear();

        if (c.getSocialMediaJSON() != null){
            new ProfileViewController().setSocialMedia(hBoxSocialMedia, c.getSocialMediaJSON(), true);
        }else{
            Label tagNoSM = new Label("No hay redes sociales disponibles");
            tagNoSM.setStyle("-fx-font-size: 20; -fx-text-fill: white");
            hBoxSocialMedia.getChildren().add(tagNoSM);
        }

        if (c.getBirthday() != null)
            tagBirthday.setText(c.getBirthday().toLocalDate().toString());
        else tagBirthday.setText("No disponible");

        if (c.getTlf() != 0)
            tagTlf.setText(Integer.toString(c.getTlf()));
        else tagTlf.setText("No disponible");

        if (c.getAddress() != null)
            tagAddress.setText(c.getAddress());
        else tagAddress.setText("No disponible");

        btnSendEmail.setOnMouseClicked(event -> sendEmail(c));
        btnSendMessage.setOnMouseClicked(event -> sendMessage(c));
        btnDeleteContact.setOnMouseClicked(event -> deleteContact(hBoxContact, c));

        vBoxContactData.setVisible(true);
    }

    private void deleteContact(HBox hboxContact, Contact c){
        boolean success = Data.contactManager.deleteContact(c.getIdContact());
        if (success){
            vBoxContactsList.getChildren().remove(hboxContact);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("MultitaskAPP");
            alert.setHeaderText("Contacto eliminado correctamente!");
            Data.setBlur();
            alert.showAndWait();
            Data.removeBlur();
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("MultitaskAPP");
            alert.setHeaderText("Error al eliminar el contacto...");
            Data.setBlur();
            alert.showAndWait();
            Data.removeBlur();
        }
    }

    private void sendMessage(Contact c){

    }

    private void sendEmail(Contact c){

    }
}
