package sample.controllers.views;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import sample.utils.Data;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

public class ProfileViewController implements Initializable {

    @FXML    private FontAwesomeIcon iconUpload, iconEdit, iconSettings, iconDisconnect, iconBirthday, iconLocation, iconTlf, iconChangeColour, iconChangeTheme;
    @FXML    private Button btnEdit;
    @FXML    private Rectangle rectangleAvatar;
    @FXML    private Label tagUsername, tagEmail, tagContactsText, tagContactsNumber, tagGroupsText, tagGroupsNumber, tagBirthday, tagLocation, tagTlf, tagMessagesText, tagMessagesNumber, tagCloudText, tagCloudNumber;
    @FXML    private AnchorPane paneContacts, paneGroups, paneMessages, paneCloud;
    @FXML    private VBox vBoxLogRegister;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        setData();
        setIconsColour();
        setNumberColour();
        setAvatar();
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

}
