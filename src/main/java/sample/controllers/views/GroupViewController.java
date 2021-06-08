package sample.controllers.views;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconName;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import sample.models.Group;
import sample.models.User;
import sample.utils.Data;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class GroupViewController implements Initializable {

    @FXML    private TextField tfSearch;
    @FXML    private VBox vBoxGroupsList, vBoxGroupData, vBoxUsersGroup;
    @FXML    private Button btnAddGroup, btnEditGroup, btnDeleteGroup;
    @FXML    private HBox hBoxGroupData;
    @FXML    private Rectangle avatarData;
    @FXML    private Label tagName, tagDesc, tagUsers;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        btnAddGroup.setStyle("-fx-background-radius: 30; -fx-background-color: " + Data.userData.getHexCode());
        getGroups();
    }

    private void getGroups() {

        vBoxGroupsList.getChildren().clear();
        Data.arrayGroupsUser = Data.groupManager.getAllGroups(Data.userData.getIdUser());
        for (Group g : Data.arrayGroupsUser) {
            vBoxGroupsList.getChildren().add(groupView(g));
        }

    }

    private HBox groupView(Group g) {

        HBox hBoxGroup = new HBox();
        hBoxGroup.setAlignment(Pos.CENTER_LEFT);
        hBoxGroup.setPrefSize(200, 65);
        hBoxGroup.setPadding(new Insets(10));
        hBoxGroup.setSpacing(10);

        Rectangle avatarList = new Rectangle(45, 45);
        avatarList.setArcHeight(360);
        avatarList.setArcWidth(360);
        avatarList.setFill(new ImagePattern(new Image(g.getAvatarGroup().getUrl(), avatarList.getWidth(), avatarList.getHeight(), true, false)));

        Label tagName = new Label(g.toString());
        tagName.setStyle("-fx-font-size: 20; -fx-text-fill: white");

        hBoxGroup.getChildren().add(avatarList);
        hBoxGroup.getChildren().add(tagName);
        hBoxGroup.setStyle("-fx-background-color:  #32323E; -fx-background-radius: 30");
        hBoxGroup.setOnMouseClicked(mouseEvent -> getGroupData(hBoxGroup, g));

        return hBoxGroup;

    }

    private HBox userView(Group g, User u){

        HBox hBoxUser = new HBox();
        hBoxUser.setPrefSize(200, 55);
        hBoxUser.setPadding(new Insets(10));
        hBoxUser.setSpacing(10);
        hBoxUser.setAlignment(Pos.CENTER_LEFT);
        hBoxUser.setStyle("-fx-background-color:  #32323E; -fx-background-radius: 30");

        Rectangle avatarUser = new Rectangle(35, 35);
        avatarUser.setArcHeight(360);
        avatarUser.setArcWidth(360);
        avatarUser.setFill(new ImagePattern(new Image(u.getAvatarUser().getUrl(), avatarUser.getWidth(), avatarUser.getHeight(), true, false)));
        hBoxUser.getChildren().add(avatarUser);

        Label tagName = new Label(u.toString());
        tagName.setPrefSize(340, 30);
        tagName.setStyle("-fx-text-fill: white; -fx-font-size: 18");
        hBoxUser.getChildren().add(tagName);

        if (g.getOwnerUser() == u.getIdUser()){
            FontAwesomeIcon adminIcon = new FontAwesomeIcon();
            adminIcon.setIcon(FontAwesomeIconName.WRENCH);
            adminIcon.setFill(Color.WHITE);
            adminIcon.setSize("2em");
            hBoxUser.getChildren().add(adminIcon);
        }

        return hBoxUser;
    }

    private void getGroupData(HBox hBoxGroup, Group g) {

        styleContactList(hBoxGroup, g);

        if (g.getOwnerUser() == Data.userData.getIdUser()){
            btnEditGroup.setDisable(false);
            FontAwesomeIcon icon = new FontAwesomeIcon();
            icon.setIcon(FontAwesomeIconName.TRASH);
            icon.setFill(Color.WHITE);
            icon.setSize("3em");
            btnDeleteGroup.setGraphic(icon);
            btnDeleteGroup.setOnMouseClicked(event -> deleteGroup(hBoxGroup, g));
        }else{
            btnEditGroup.setDisable(true);
            FontAwesomeIcon icon = new FontAwesomeIcon();
            icon.setIcon(FontAwesomeIconName.CLOSE);
            icon.setFill(Color.WHITE);
            icon.setSize("3em");
            btnDeleteGroup.setGraphic(icon);
            btnDeleteGroup.setOnMouseClicked(event -> leaveGroup(hBoxGroup, g));
        }

        hBoxGroupData.setStyle("-fx-background-radius: 30; -fx-background-color: " + g.getHexCode());
        avatarData.setFill(new ImagePattern(new Image(g.getAvatarGroup().getUrl(), avatarData.getWidth(), avatarData.getHeight(), true, false)));
        tagName.setText(g.toString());
        tagDesc.setText(g.getDescriptionGroup());
        tagUsers.setStyle("-fx-text-fill: " + g.getHexCode());

        vBoxUsersGroup.getChildren().clear();
        for (User u : g.getArrayUsersGroup()) {
            vBoxUsersGroup.getChildren().add(userView(g, u));
        }

        vBoxGroupData.setVisible(true);

    }

    private void leaveGroup(HBox hBoxGroup, Group g) {

        Alert verifyAlert = new Alert(Alert.AlertType.WARNING);
        verifyAlert.setTitle("MultitaskAPP");
        verifyAlert.setHeaderText("Estas seguro que quieres abandonar el grupo: [" + g.toString() + "]?");
        verifyAlert.getButtonTypes().add(new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE));

        Data.setBlur();
        Optional<ButtonType> result = verifyAlert.showAndWait();
        if (result.get() == ButtonType.OK){
            boolean success = Data.groupManager.leaveGroup(g);
            if (success){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("MultitaskAPP");
                alert.setHeaderText("Has abandonado el grupo correctamente!");
                vBoxGroupsList.getChildren().remove(hBoxGroup);
                vBoxGroupData.setVisible(false);
                alert.showAndWait();
            }else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("MultitaskAPP");
                alert.setHeaderText("Error al abandonar el grupo...");
                alert.showAndWait();
            }
        }

        Data.removeBlur();

    }

    private void deleteGroup(HBox hBoxGroup, Group g) {

        Alert verifyAlert = new Alert(Alert.AlertType.WARNING);
        verifyAlert.setTitle("MultitaskAPP");
        verifyAlert.setHeaderText("Estas seguro que quieres eliminar el grupo: [" + g.toString() + "]?");
        verifyAlert.getButtonTypes().add(new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE));

        Data.setBlur();
        Optional<ButtonType> result = verifyAlert.showAndWait();
        if (result.get() == ButtonType.OK){
            boolean success = Data.groupManager.deleteGroup(g);
            if (success){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("MultitaskAPP");
                alert.setHeaderText("Has elimnado el grupo correctamente!");
                vBoxGroupsList.getChildren().remove(hBoxGroup);
                vBoxGroupData.setVisible(false);
                alert.showAndWait();
            }else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("MultitaskAPP");
                alert.setHeaderText("Error al eliminar el grupo...");
                alert.showAndWait();
            }
        }

        Data.removeBlur();
    }

    private void styleContactList(HBox hBoxSelected, Group g){
        for (int i = 0; i < vBoxGroupsList.getChildren().size(); i++){
            HBox hBoxContact = (HBox) vBoxGroupsList.getChildren().get(i);
            hBoxContact.setStyle("-fx-background-color:  #32323E; -fx-background-radius: 30");
        }

        hBoxSelected.setStyle("-fx-background-radius: 30; -fx-background-color: " + g.getHexCode());
    }

}
