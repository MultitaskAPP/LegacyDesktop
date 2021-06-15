package sample.controllers.dialogs;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconName;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.effect.BoxBlur;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.json.JSONObject;
import sample.controllers.views.ProfileViewController;
import sample.models.SocialMedia;
import sample.models.User;
import sample.utils.Data;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ProfileEditViewController implements Initializable {

    @FXML    private TextField tfFirstSurname, tfName, tfLastSurname, tfTelephone, tfAddress, tfSocialMedia;
    @FXML    private DatePicker datePicker;
    @FXML    private VBox vBoxSocialMedia;
    @FXML    private Button btnUpdateProfile, btnAddSocialMedia;
    @FXML    private ComboBox<SocialMedia> cmbSocialMedia;

    private boolean hasChanges = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void preloadData(){

        setData();
        getSocialMedia();

        tfTelephone.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                tfTelephone.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

    }

    private void setData() {

        tfName.setText(Data.userData.getName());
        tfFirstSurname.setText(Data.userData.getFirstSurname());
        tfLastSurname.setText(Data.userData.getLastSurname());
        tfTelephone.setText(Integer.toString(Data.userData.getTlf()));
        tfAddress.setText(Data.userData.getAddress());
        datePicker.setValue(Data.userData.getBirthday().toLocalDate());

        if(ProfileViewController.arraySocialMedia != null){
            cmbSocialMedia.getItems().addAll(ProfileViewController.arraySocialMedia);

        }

    }

    private void getSocialMedia(){

        if (Data.userData.getSocialMedia() != null){
            JSONObject socialMediaJSON = Data.userData.getSocialMedia();
            for (SocialMedia socialMedia : ProfileViewController.arraySocialMedia) {
                if (socialMediaJSON.has(socialMedia.getName())){
                    cmbSocialMedia.getItems().remove(socialMedia);
                    vBoxSocialMedia.getChildren().add(socialMediaView(socialMedia, socialMediaJSON.getString(socialMedia.getName())));
                }
            }
        }
    }

    private HBox socialMediaView(SocialMedia sm, String nameSocialMedia){

        HBox hBoxSocialMedia = new HBox();
        hBoxSocialMedia.setId(sm.getName());
        hBoxSocialMedia.setPrefSize(200, 50);
        hBoxSocialMedia.setSpacing(15);

        HBox hBoxIcon = new HBox();
        hBoxIcon.setPrefSize(100, 50);
        hBoxIcon.setAlignment(Pos.CENTER);
        hBoxIcon.setStyle("-fx-background-radius: 30; -fx-background-color: " + sm.getHexCode());

        FontAwesomeIcon icon = sm.getIcon();
        icon.setFill(Color.WHITE);
        icon.setSize("35px");
        hBoxIcon.getChildren().add(icon);
        hBoxSocialMedia.getChildren().add(hBoxIcon);

        TextField textField = new TextField(nameSocialMedia);
        textField.setPrefSize(166, 50);
        textField.setPadding(new Insets(10));
        textField.setStyle("-fx-background-color:  #32323E; -fx-text-fill: white; -fx-background-radius: 30");
        hBoxSocialMedia.getChildren().add(textField);

        Button btnDelete = new Button();
        btnDelete.setPrefSize(50, 50);
        btnDelete.setStyle("-fx-background-radius: 360; -fx-background-color: #32323E");
        btnDelete.setOnMouseClicked(mouseEvent -> deleteSocialMedia(hBoxSocialMedia, sm));

        FontAwesomeIcon iconClose = new FontAwesomeIcon();
        iconClose.setIcon(FontAwesomeIconName.REMOVE);
        iconClose.setFill(Color.WHITE);
        iconClose.setSize("30px");

        btnDelete.setGraphic(iconClose);
        hBoxSocialMedia.getChildren().add(btnDelete);

        return hBoxSocialMedia;
    }

    private void deleteSocialMedia(HBox hBoxSocialMedia, SocialMedia sm) {
        vBoxSocialMedia.getChildren().remove(hBoxSocialMedia);
        cmbSocialMedia.getItems().add(sm);
        hasChanges = true;
    }

    @FXML
    void addSocialMedia(ActionEvent event) {
        if (cmbSocialMedia.getSelectionModel().getSelectedItem() != null && !(tfSocialMedia.getText().isBlank())){
            vBoxSocialMedia.getChildren().add(socialMediaView(cmbSocialMedia.getSelectionModel().getSelectedItem(), tfSocialMedia.getText()));
            cmbSocialMedia.getItems().remove(cmbSocialMedia.getSelectionModel().getSelectedItem());
            tfSocialMedia.clear();
            hasChanges = true;
        }
    }

    @FXML
    void checkData(MouseEvent event) {
        User userData = setUserData();
        if (hasChanges)
            userData.setSocialMedia(parseSocialMedia());
        else userData.setSocialMedia(Data.userData.getSocialMedia());

        boolean success = Data.userManager.updateUser(userData);
        if (success){
            hasChanges = false;
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("MultitaskAPP");
            alert.setHeaderText("Datos actualizados correctamente!");
            Data.setBlur();
            alert.showAndWait();
            Data.removeBlur();
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("MultitaskAPP");
            alert.setHeaderText("Ha ocurrido un error al actualizar los datos...");
            Data.setBlur();
            alert.showAndWait();
            Data.removeBlur();
        }

    }

    private JSONObject parseSocialMedia(){
        JSONObject socialMediaJSON = new JSONObject();
        for (int i = 2; i < vBoxSocialMedia.getChildren().size(); i++){
            HBox hBoxSocialMedia = (HBox) vBoxSocialMedia.getChildren().get(i);
            TextField textField = (TextField) hBoxSocialMedia.getChildren().get(1);
            socialMediaJSON.put(hBoxSocialMedia.getId(), textField.getText());
        }

        return socialMediaJSON;
    }

    private User setUserData(){

        User userObj = new User();
        userObj.setName(tfName.getText());
        userObj.setFirstSurname(tfFirstSurname.getText());
        userObj.setLastSurname(tfLastSurname.getText());
        userObj.setAddress(tfAddress.getText());
        userObj.setBirthday(Date.valueOf(datePicker.getValue()));
        userObj.setTlf(Integer.parseInt(tfTelephone.getText()));

        return userObj;

    }
}
