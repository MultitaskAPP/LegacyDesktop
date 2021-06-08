package sample.controllers.dialogs;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconName;
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
import javafx.stage.Stage;
import sample.controllers.views.GroupViewController;
import sample.models.Group;
import sample.models.User;
import sample.utils.Data;
import sample.utils.ImageTweakerTool;

import java.net.URL;
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
    private boolean updateMode = false, userIsAdmin = false;
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
        btnUpdateAvatar.setOnMouseClicked(event -> new ImageTweakerTool());

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

    public void viewMode(){

        tfName.setDisable(true);
        textArea.setDisable(true);
        btnAddUser.setDisable(true);
        colourPicker.setDisable(true);
        btnUpdateAvatar.setDisable(true);

    }

    public void editMode(){

        tagName.setText(selectedGroup.toString());
        tagDesc.setText(selectedGroup.getDescriptionGroup());
        tfName.setText(selectedGroup.toString());
        textArea.setText(selectedGroup.getDescriptionGroup());
        colourPicker.setValue(Color.valueOf(selectedGroup.getHexCode()));

        getUsers();

    }

    private void updateGroupView() {

        anchorPaneGroup.setStyle("-fx-background-radius: 30; -fx-background-color: " + getHexCode(colourPicker.getValue()));
        tagName.setText(tfName.getText());
        tagDesc.setText(textArea.getText());

    }

    private String getHexCode(Color sc){

        java.awt.Color awtColor = new java.awt.Color((float) sc.getRed(), (float) sc.getGreen(), (float) sc.getBlue(), (float) sc.getOpacity());

        int r = awtColor.getRed();
        int g = awtColor.getGreen();
        int b = awtColor.getBlue();

        return String.format("#%02X%02X%02X", r, g, b);
    }

    private void getUsers() {

        for (User u : selectedGroup.getArrayUsersGroup()) {
            vBoxGroupUsers.getChildren().add(userView(u));
        }

    }

    private HBox userView(User u) {

        HBox hBoxUser = new HBox();
        hBoxUser.setPrefSize(325, 50);
        hBoxUser.setSpacing(15);
        hBoxUser.setPadding(new Insets(10));
        hBoxUser.setAlignment(Pos.CENTER_LEFT);

        Rectangle avatarUser = new Rectangle(30, 30);
        avatarUser.setArcHeight(360);
        avatarUser.setArcWidth(360);
        avatarUser.setFill(new ImagePattern(new Image(u.getAvatarUser().getUrl(), avatarUser.getWidth(), avatarUser.getHeight(), true, false)));
        hBoxUser.getChildren().add(avatarUser);

        Label tagUserName = new Label(u.toString());
        tagUserName.setPrefSize(225, 30);
        tagUserName.setStyle("-fx-text-fill: white; -fx-font-size: 15");
        hBoxUser.getChildren().add(tagUserName);

        if (updateMode && userIsAdmin){
            MenuButton menuButton = new MenuButton();
            FontAwesomeIcon iconMenu = new FontAwesomeIcon();
            iconMenu.setIcon(FontAwesomeIconName.ELLIPSIS_H);
            iconMenu.setFill(Color.WHITE);
            menuButton.setGraphic(iconMenu);
            hBoxUser.getChildren().add(menuButton);
        }

        return hBoxUser;

    }

    public void addGroup(){

    }

    public void updateGroup(){

    }

    public void setGroupViewController(GroupViewController groupViewController) {
        this.groupViewController = groupViewController;
    }

    public void setUpdateMode(boolean updateMode) {
        this.updateMode = updateMode;
    }

    public void setUserIsAdmin(boolean userIsAdmin) {
        this.userIsAdmin = userIsAdmin;
    }

    public void setSelectedGroup(Group selectedGroup) {
        this.selectedGroup = selectedGroup;
    }

    @FXML
    void close(MouseEvent event) {
        Stage stage = (Stage) btnAddUser.getScene().getWindow();
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
