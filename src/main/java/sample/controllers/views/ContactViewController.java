package sample.controllers.views;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import sample.models.Contact;

import java.net.URL;
import java.util.ResourceBundle;

public class ContactViewController implements Initializable {

    @FXML    private TextField tfSearch;
    @FXML    private Rectangle avatarData;
    @FXML    private VBox vBoxContactsList, vBoxContactData, vBoxExtraData;
    @FXML    private Label tagName, tagEmail;
    @FXML    private HBox hBoxSocialMedia;
    @FXML    private Button btnAddContact, btnSendEmail, btnSendMessage,btnShowGroups, btnDeleteContact;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    private void getContacts(){

        List<Contact> contactList = Data.contactManager.getAll

    }

    private HBox contactView(Contact c){

        HBox hBoxContact = new HBox();
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
        hBoxContact.setOnMouseClicked(mouseEvent -> getContactData(c));

        return hBoxContact;

    }

    private void getContactData(Contact c) {
    }
}
