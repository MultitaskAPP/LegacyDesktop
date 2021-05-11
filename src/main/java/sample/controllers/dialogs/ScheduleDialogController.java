package sample.controllers.dialogs;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import sample.models.Group;
import sample.models.Schedule;
import sample.utils.Data;
import java.net.URL;
import java.util.ResourceBundle;

public class ScheduleDialogController implements Initializable {

    @FXML    private TextField tfName, tfList;
    @FXML    private ColorPicker colourPicker;
    @FXML    private ComboBox<Group> cbGroups;
    @FXML    private VBox hBoxPreview;
    @FXML    private Rectangle rectanglePreview;
    @FXML    private Label tagNamePreview;
    @FXML    private ListView<String> taskList;
    @FXML    private Button btnAdd, btnCancel, btnAddList, btnDeleteList;

    private boolean isGroup, updateMode = false;
    private Schedule selectedSchedule = null;
    private double x, y;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void preloadData(){

        tfName.textProperty().addListener((observableValue, s, t1) -> {
            tagNamePreview.setText(tfName.getText());
        });

        cbGroups.getItems().add(null);
        cbGroups.getItems().addAll(Data.arrayGroupsUser);
        cbGroups.getSelectionModel().selectFirst();

        colourPicker.valueProperty().addListener((observableValue, color, t1) -> updatePreview(null));

        if (selectedSchedule == null){
            createMode();
        }else {
            if (updateMode){
                editMode();
            }else {
                viewMode();
            }
        }

    }

    private void createMode(){

        btnAddList.setOnMouseClicked(mouseEvent -> {
            if (!tfList.getText().isBlank())
                taskList.getItems().add(tfList.getText());
        });



    }

    private void editMode(){}

    private void viewMode(){}

    private void addSchedule(){}

    private void updateSchedule(){}

    @FXML
    void checkSchedule(ActionEvent event) {

    }

    @FXML
    void exit(ActionEvent event) {
        Stage stage = (Stage) btnAdd.getScene().getWindow();
        stage.close();
    }

    @FXML
    void close(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
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

    @FXML
    void updatePreview(ActionEvent event) {
        if (cbGroups.getSelectionModel().getSelectedIndex() != 0){
            colourPicker.setDisable(true);
            Group selectedGroup = cbGroups.getValue();
            hBoxPreview.setStyle("-fx-background-radius: 20; -fx-background-color: " + selectedGroup.getHexCode());
            ImagePattern imagePattern = new ImagePattern(selectedGroup.getAvatarGroup());
            rectanglePreview.setFill(imagePattern);
        }else {
            colourPicker.setDisable(false);
            Color selectedColour = colourPicker.getValue();
            hBoxPreview.setStyle("-fx-background-radius: 20; -fx-background-color: " + getHexCode(selectedColour));
            ImagePattern imagePattern = new ImagePattern(Data.userData.getAvatarUser());
            rectanglePreview.setFill(imagePattern);
        }
    }

    private String getHexCode(Color sc){

        java.awt.Color awtColor = new java.awt.Color((float) sc.getRed(), (float) sc.getGreen(), (float) sc.getBlue(), (float) sc.getOpacity());

        int r = awtColor.getRed();
        int g = awtColor.getGreen();
        int b = awtColor.getBlue();

        return String.format("#%02X%02X%02X", r, g, b);
    }

    public void setGroup(boolean group) {
        isGroup = group;
    }

    public void setUpdateMode(boolean updateMode) {
        this.updateMode = updateMode;
    }

    public void setSelectedSchedule(Schedule selectedSchedule) {
        this.selectedSchedule = selectedSchedule;
    }
}
