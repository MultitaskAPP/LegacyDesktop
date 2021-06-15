package sample.controllers.dialogs;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconName;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sample.controllers.views.GroupViewController;
import sample.models.Group;
import sample.models.User;
import sample.utils.Data;
import sample.utils.ImageTweakerTool;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

public class GroupDialogController implements Initializable {

    @FXML    private FontAwesomeIcon btnMinimize, btnMaximize, btnClose, btnAddUser;
    @FXML    private AnchorPane anchorPaneGroup;
    @FXML    private Rectangle avatarGroup;
    @FXML    private Label tagName, tagDesc;
    @FXML    private Button btnUpdateAvatar, btnAdd, btnCancel;
    @FXML    private TextField tfName, tfAddUser;
    @FXML    private ColorPicker colourPicker;
    @FXML    private VBox vBoxGroupUsers;
    @FXML    private TextArea textArea;

    private GroupViewController groupViewController;
    private Image avatar = null;
    private File uploadedAvatar;
    private boolean updateMode = false;
    private Group selectedGroup;
    private double x, y;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void preloadData(){

        tagName.setText("");
        tagDesc.setText("");
        colourPicker.setValue(Color.valueOf("#32323E"));

        tfName.textProperty().addListener((observableValue, s, t1) -> updateGroupView());
        textArea.textProperty().addListener((observableValue, s, t1) -> updateGroupView());
        colourPicker.valueProperty().addListener((observableValue, color, t1) -> updateGroupView());
        btnUpdateAvatar.setOnMouseClicked(event -> uploadAvatar());
        btnCancel.setOnMouseClicked(this::close);
        btnAddUser.setOnMouseClicked(event -> sendUserRequest());

        if (selectedGroup != null){
            if (updateMode){
                editMode();
            }else {
                viewMode();
            }
        }

        if (!updateMode)
            btnAdd.setOnMouseClicked(mouseEvent -> addGroup());
        else btnAdd.setOnMouseClicked(event -> updateGroup());

    }

    private void viewMode(){

        editMode();

        tfName.setDisable(true);
        textArea.setDisable(true);
        btnAddUser.setDisable(true);
        colourPicker.setDisable(true);
        btnUpdateAvatar.setDisable(true);
        btnAdd.setDisable(true);
        btnAddUser.setDisable(true);
        tfAddUser.setDisable(true);

    }

    private void editMode(){

        tagName.setText(selectedGroup.toString());
        tagDesc.setText(selectedGroup.getDescriptionGroup());
        tfName.setText(selectedGroup.toString());
        textArea.setText(selectedGroup.getDescriptionGroup());
        colourPicker.setValue(Color.valueOf(selectedGroup.getHexCode()));
        avatarGroup.setFill(new ImagePattern(new Image(selectedGroup.getAvatarGroup().getUrl(), avatarGroup.getWidth(), avatarGroup.getHeight(), true, false)));
        btnAddUser.setOnMouseClicked(event -> sendUserRequest(selectedGroup));
        getUsers(selectedGroup);
        updateGroupView();

    }

    private void updateGroupView() {

        anchorPaneGroup.setStyle("-fx-background-radius: 30; -fx-background-color: " + getHexCode(colourPicker.getValue()));
        tagName.setText(tfName.getText());
        tagDesc.setText(textArea.getText());

        for (int i = 0; i < vBoxGroupUsers.getChildren().size(); i++){
            HBox hBox = (HBox) vBoxGroupUsers.getChildren().get(i);
            if (hBox.getId() != null)
                hBox.setStyle("-fx-background-radius: 30; -fx-background-color: " + getHexCode(colourPicker.getValue()));
        }

    }

    private String getHexCode(Color sc){

        java.awt.Color awtColor = new java.awt.Color((float) sc.getRed(), (float) sc.getGreen(), (float) sc.getBlue(), (float) sc.getOpacity());

        int r = awtColor.getRed();
        int g = awtColor.getGreen();
        int b = awtColor.getBlue();

        return String.format("#%02X%02X%02X", r, g, b);
    }

    private void getUsers(Group group) {

        List<User> requestUsers = Data.groupManager.getInvitedUsersToGroup(group.getIdGroup());

        for (User u : selectedGroup.getArrayUsersGroup()) {
            vBoxGroupUsers.getChildren().add(userView(group, u));
        }

        if (group.getOwnerUser() == Data.userData.getIdUser()){
            HBox hBoxRequests = new HBox();
            hBoxRequests.setPrefSize(325, 40);
            hBoxRequests.setAlignment(Pos.CENTER);
            hBoxRequests.setStyle("-fx-background-radius: 30; -fx-background-color: #373743");

            Label tagRequestNumber = new Label("Se han invitado a [" + requestUsers.size() + "] a unirse al grupo.");
            tagRequestNumber.setStyle("-fx-font-size: 15; -fx-text-fill: white");
            hBoxRequests.getChildren().add(tagRequestNumber);

            vBoxGroupUsers.getChildren().add(hBoxRequests);

            for (User u : requestUsers) {
                vBoxGroupUsers.getChildren().add(requestView(group, u));
            }
        }

    }

    private HBox userView(Group g, User u) {

        HBox hBoxUser = new HBox();
        hBoxUser.setId("user");
        hBoxUser.setPrefSize(325, 50);
        hBoxUser.setSpacing(15);
        hBoxUser.setPadding(new Insets(10));
        hBoxUser.setAlignment(Pos.CENTER_LEFT);
        hBoxUser.setStyle("-fx-background-radius: 30; -fx-background-color: " + getHexCode(colourPicker.getValue()));

        Rectangle avatarUser = new Rectangle(30, 30);
        avatarUser.setArcHeight(360);
        avatarUser.setArcWidth(360);
        avatarUser.setFill(new ImagePattern(new Image(u.getAvatarUser().getUrl(), avatarUser.getWidth(), avatarUser.getHeight(), true, false)));
        hBoxUser.getChildren().add(avatarUser);

        Label tagUserName = new Label(u.toString());
        tagUserName.setPrefSize(225, 30);
        tagUserName.setStyle("-fx-text-fill: white; -fx-font-size: 15");
        hBoxUser.getChildren().add(tagUserName);

        if (updateMode && g.getOwnerUser() != u.getIdUser()){
            MenuItem miDelete = new MenuItem("ELIMINAR");
            miDelete.setOnAction(actionEvent -> deleteUser(g, u, hBoxUser));
            MenuButton menuButton = new MenuButton(null, null, miDelete);
            menuButton.setStyle("-fx-background-color: transparent");
            FontAwesomeIcon iconMenu = new FontAwesomeIcon();
            iconMenu.setIcon(FontAwesomeIconName.ELLIPSIS_H);
            iconMenu.setFill(Color.WHITE);
            menuButton.setGraphic(iconMenu);
            hBoxUser.getChildren().add(menuButton);
        }

        return hBoxUser;

    }

    private void deleteUser(Group g, User u, HBox hBox) {

        boolean success = Data.groupManager.removeUserToGroup(u.getIdUser(), g);
        if (success){
            vBoxGroupUsers.getChildren().remove(hBox);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("MultitaskAPP");
            alert.setHeaderText("Usuario elimiado correctamente!");
            alert.showAndWait();
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("MultitaskAPP");
            alert.setHeaderText("Error al elimnar al usuario...");
            alert.showAndWait();
        }

    }

    private HBox requestView(User u){

        HBox hBoxRequest = new HBox();
        hBoxRequest.setId("request");
        hBoxRequest.setPrefSize(325, 50);
        hBoxRequest.setPadding(new Insets(10, 10, 10, 20));
        hBoxRequest.setAlignment(Pos.CENTER_LEFT);
        hBoxRequest.setStyle("-fx-background-radius: 30; -fx-background-color: " + getHexCode(colourPicker.getValue()));

        Label tagEmail = new Label(u.getEmail());
        tagEmail.setStyle("-fx-font-size: 15; -fx-text-fill: white");
        tagEmail.setPrefSize(275, 30);

        FontAwesomeIcon iconRemoveRequest = new FontAwesomeIcon();
        iconRemoveRequest.setSize("20px");
        iconRemoveRequest.setIcon(FontAwesomeIconName.CLOSE);
        iconRemoveRequest.setFill(Color.WHITE);
        iconRemoveRequest.setOnMouseClicked(event -> vBoxGroupUsers.getChildren().remove(hBoxRequest));

        hBoxRequest.getChildren().add(tagEmail);
        hBoxRequest.getChildren().add(iconRemoveRequest);

        return hBoxRequest;

    }

    private HBox requestView(Group g, User u){

        HBox hBoxRequest = new HBox();
        hBoxRequest.setId("request");
        hBoxRequest.setPrefSize(325, 50);
        hBoxRequest.setPadding(new Insets(10, 10, 10, 20));
        hBoxRequest.setAlignment(Pos.CENTER_LEFT);
        hBoxRequest.setStyle("-fx-background-radius: 30; -fx-background-color: " + getHexCode(colourPicker.getValue()));

        Label tagEmail = new Label(u.getEmail());
        tagEmail.setStyle("-fx-font-size: 15; -fx-text-fill: white");
        tagEmail.setPrefSize(275, 30);

        FontAwesomeIcon iconRemoveRequest = new FontAwesomeIcon();
        iconRemoveRequest.setSize("20px");
        iconRemoveRequest.setIcon(FontAwesomeIconName.CLOSE);
        iconRemoveRequest.setFill(Color.WHITE);
        iconRemoveRequest.setOnMouseClicked(event -> deleteUserRequest(g, u, hBoxRequest));

        hBoxRequest.getChildren().add(tagEmail);
        hBoxRequest.getChildren().add(iconRemoveRequest);

        return hBoxRequest;


    }

    private void deleteUserRequest(Group g, User u, HBox hBox) {

        boolean success = Data.groupManager.removeUserToGroup(u.getIdUser(), g);
        if (success){
            vBoxGroupUsers.getChildren().remove(hBox);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("MultitaskAPP");
            alert.setHeaderText("Invitacion retirada correctamente!");
            alert.showAndWait();
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("MultitaskAPP");
            alert.setHeaderText("Error al retirar la invitacion...");
            alert.showAndWait();
        }

    }

    private void sendUserRequest(){

        if (!(tfAddUser.getText().isBlank())){
            if (!(tfAddUser.getText().equals(Data.userData.getEmail()))){
                User user = new User();
                user.setEmail(tfAddUser.getText());
                vBoxGroupUsers.getChildren().add(requestView(user));
            }else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("MultitaskAPP");
                alert.setHeaderText("No puedes invitarte a ti mismo, al crear tu el grupo ya eres miembro automaticamente...");
                alert.showAndWait();
            }
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("MultitaskAPP");
            alert.setHeaderText("El correo introducido esta en blanco o no es valido...");
            alert.showAndWait();
        }

        tfAddUser.clear();

    }

    private void sendUserRequest(Group g){

        if (!(tfAddUser.getText().isBlank())){
            String email = tfAddUser.getText();
            HashMap<Integer, Integer> hashMap = Data.groupManager.addUserToGroup(email, g);
            Alert alert = null;
            if (hashMap.containsKey(200)){
                User user = new User();
                user.setIdUser(hashMap.get(200));
                user.setEmail(email);
                vBoxGroupUsers.getChildren().add(requestView(g, user));
                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("MultitaskAPP");
                alert.setHeaderText("Invitacion al grupo mandada correctamente!");
                alert.showAndWait();
            } else if (hashMap.containsKey(401)) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("MultitaskAPP");
                alert.setHeaderText("No se ha encontrado al usuario [" + email + "]...");
                alert.showAndWait();
            } else if (hashMap.containsKey(402)) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("MultitaskAPP");
                alert.setHeaderText("Ya se ha mandado una invitacion a este usuario...");
                alert.showAndWait();
            } else if (hashMap.containsKey(403)) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("MultitaskAPP");
                alert.setHeaderText("Este usuario ya es miembro del grupo...");
                alert.showAndWait();
            } else if (hashMap.containsKey(404)) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("MultitaskAPP");
                alert.setHeaderText("No puedes invitarte al grupo a ti mismo...");
                alert.showAndWait();
            } else if (hashMap.containsKey(500)) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("MultitaskAPP");
                alert.setHeaderText("Error al mandar la invitacion al usuario...");
                alert.showAndWait();
            }
        }

    }

    private Group getData(){

       if (!(tfName.getText().isBlank())){
           Group group = new Group();
           group.setNameGroup(tfName.getText());
           group.setDescriptionGroup(textArea.getText());
           group.setColourGroup(java.awt.Color.decode(getHexCode(colourPicker.getValue())));

           if (selectedGroup != null){
               group.setIdGroup(selectedGroup.getIdGroup());
               group.setOwnerUser(selectedGroup.getOwnerUser());
               group.setAvatarGroup(selectedGroup.getAvatarGroup());
           }else{
               group.setOwnerUser(Data.userData.getIdUser());
               group.setAvatarGroup(avatar);

           }

           return group;

       }else{
           Alert alert = new Alert(Alert.AlertType.ERROR);
           alert.setTitle("MultitaskAPP");
           alert.setHeaderText("El grupo debe tener nombre...");
           alert.showAndWait();
       }

       return null;

    }

    private void addGroup(){

        Group newGroup = getData();
        int requestsSended = 0;
        int totalRequests = 0;

        if (newGroup != null){
            newGroup = Data.groupManager.createGroup(newGroup);
            if (newGroup != null){
                for (int i = 0; i < vBoxGroupUsers.getChildren().size(); i++){
                    HBox hBox = (HBox) vBoxGroupUsers.getChildren().get(i);
                    if (hBox.getId().equals("request")){
                        totalRequests++;
                        Label tagEmail = (Label) hBox.getChildren().get(0);
                        HashMap<Integer, Integer> hashMap = Data.groupManager.addUserToGroup(tagEmail.getText(), newGroup);
                        if (hashMap.containsKey(200))
                            requestsSended++;
                    }
                }

                if (uploadedAvatar != null){
                    ImageTweakerTool imageTweakerTool = new ImageTweakerTool(newGroup.getIdGroup(), 0);
                    imageTweakerTool.uploadImageGroup(imageTweakerTool.transformImage(uploadedAvatar), Integer.toString(newGroup.getIdGroup()));
                }
                close(null);
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("MultitaskAPP");
                if (totalRequests != 0){
                    alert.setHeaderText("Grupo creado correctamente!. Se han invitado a ["+requestsSended + " de " + totalRequests + "] al grupo correctamente!");
                }else{
                    alert.setHeaderText("Grupo creado correctamente!");
                }
                alert.showAndWait();
            }
        }
    }

    private void updateGroup(){

        Group newGroup = getData();

        if (newGroup != null){
            newGroup = Data.groupManager.updateGroup(newGroup);
            if (newGroup != null){
                if (uploadedAvatar != null){
                    ImageTweakerTool imageTweakerTool = new ImageTweakerTool(newGroup.getIdGroup(), 0);
                    imageTweakerTool.uploadImageGroup(imageTweakerTool.transformImage(uploadedAvatar), Integer.toString(newGroup.getIdGroup()));
                }
                close(null);
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("MultitaskAPP");
                alert.setHeaderText("Grupo actualizado correctamente!");
                alert.showAndWait();
            }
        }

    }

    private void uploadAvatar(){

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Imagenes", "*.jpeg", "*.png", "*.jpg"));
        uploadedAvatar = fileChooser.showOpenDialog(btnCancel.getScene().getWindow());
        if (uploadedAvatar != null){
            avatar = SwingFXUtils.toFXImage(new ImageTweakerTool(0).transformImage(uploadedAvatar), null);
            System.out.println(avatar.getUrl());
            avatarGroup.setFill(new ImagePattern(avatar));
        }

    }

    private void updateView() {
        groupViewController.getGroups();
    }

    public void setGroupViewController(GroupViewController groupViewController) {
        this.groupViewController = groupViewController;
    }

    public void setUpdateMode(boolean updateMode) {
        this.updateMode = updateMode;
    }

    public void setSelectedGroup(Group selectedGroup) {
        this.selectedGroup = selectedGroup;
    }

    @FXML
    void close(MouseEvent event) {
        if (event == null)
            updateView();
        Stage stage = (Stage) btnAdd.getScene().getWindow();
        stage.close();
        Data.removeBlur();
    }

    @FXML
    void minimize(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setIconified(true);
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
}
