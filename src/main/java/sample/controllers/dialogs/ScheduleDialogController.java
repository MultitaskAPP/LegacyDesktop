package sample.controllers.dialogs;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import org.json.JSONArray;
import sample.controllers.views.TaskViewController;
import sample.models.Group;
import sample.models.Schedule;
import sample.utils.Data;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Date;
import java.util.Calendar;
import java.util.ResourceBundle;

public class ScheduleDialogController implements Initializable {

    @FXML    private TextField tfName, tfList;
    @FXML    private ColorPicker colourPicker;
    @FXML    private ComboBox<Group> cbGroups;
    @FXML    private VBox hBoxPreview, vBoxLeft;
    @FXML    private Rectangle rectanglePreview;
    @FXML    private Label tagNamePreview;
    @FXML    private ListView<String> taskList;
    @FXML    private Button btnAdd, btnCancel, btnAddList, btnDeleteList;

    private TaskViewController taskViewController;
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

        colourPicker.setValue(Color.valueOf("#32323E"));
        colourPicker.valueProperty().addListener((observableValue, color, t1) -> updatePreview(null));

        if (selectedSchedule != null){
            if (updateMode){
                editMode();
            }else {
                viewMode();
            }
        }

        btnAddList.setOnMouseClicked(mouseEvent -> {
            if (!tfList.getText().isBlank()){
                taskList.getItems().add(tfList.getText());
                tfList.clear();
            }
        });

        btnDeleteList.setOnMouseClicked(mouseEvent -> {
            if (taskList.getSelectionModel().getSelectedItem() != null){
                taskList.getItems().remove(taskList.getSelectionModel().getSelectedIndex());
            }
        });

        if (!updateMode)
            btnAdd.setOnMouseClicked(mouseEvent -> addSchedule());


    }

    private void editMode(){

        tfName.setText(selectedSchedule.getNameSchedule());
        colourPicker.valueProperty().setValue(Color.valueOf(selectedSchedule.getHexCode()));
        hBoxPreview.setStyle("-fx-background-radius: 20; -fx-background-color: " + selectedSchedule.getHexCode());
        tagNamePreview.setText(selectedSchedule.getNameSchedule());

        JSONArray listSchedules = selectedSchedule.getListsSchedules();
        for (int i = 0; i < listSchedules.length(); i++){
            taskList.getItems().add(listSchedules.getString(i));
        }

        if (selectedSchedule.isGroup()){
            for (Group g: cbGroups.getItems()) {
                if (g != null){
                    if (g.getIdGroup() == selectedSchedule.getIdGroup()){
                        ImagePattern imagePattern = new ImagePattern(g.getAvatarGroup());
                        rectanglePreview.setFill(imagePattern);
                        cbGroups.getSelectionModel().select(g);
                    }
                }
            }
        }else{
            cbGroups.getSelectionModel().selectFirst();
            ImagePattern imagePattern = new ImagePattern(Data.userData.getAvatarUser());
            rectanglePreview.setFill(imagePattern);
        }

        btnAdd.setText("EDITAR");
        btnAdd.setOnMouseClicked(mouseEvent -> updateSchedule());

    }

    private void viewMode(){

        btnAddList.setDisable(true);
        btnDeleteList.setDisable(true);
        tfList.setEditable(false);
        tfName.setEditable(false);
        cbGroups.setDisable(true);
        vBoxLeft.getChildren().remove(colourPicker);
        btnAdd.setDisable(true);

        editMode();

    }

    private Schedule getData(){

        if (!tfName.getText().isBlank()){
            Schedule s = new Schedule();
            s.setNameSchedule(tfName.getText());

            if (selectedSchedule != null){
                s.setIdSchedule(selectedSchedule.getIdSchedule());
                s.setCreationDate(selectedSchedule.getCreationDate());
            }else {
                s.setCreationDate(new Date(Calendar.getInstance().getTime().getTime()));
            }

            JSONArray listsSchedules = new JSONArray();
            for (int i = 0; i < taskList.getItems().size(); i++){
                listsSchedules.put(taskList.getItems().get(i));
            }

            s.setListsSchedules(listsSchedules);

            if (cbGroups.getSelectionModel().getSelectedIndex() != 0){
                s.setColourSchedule(cbGroups.getSelectionModel().getSelectedItem().getColourGroup());
                s.setIdGroup(cbGroups.getSelectionModel().getSelectedItem().getIdGroup());
                s.setGroup(true);
            }else{
                s.setIdUser(Data.userData.getIdUser());
                s.setGroup(false);
                s.setColourSchedule(getSelectedColour(colourPicker.getValue()));
            }

            return s;
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("MultitaskAPP | DESKTOP");
            alert.setHeaderText("El tablero debe tener nombre...");
            alert.showAndWait();
        }

        return null;

    }

    private void addSchedule(){

        Schedule newSchedule = getData();
        if (newSchedule != null){
            boolean success = false;
            if (newSchedule.isGroup()){
                success = Data.scheduleManager.insertGroupSchedule(newSchedule);

            }else{
                success = Data.scheduleManager.insertSchedule(newSchedule);
            }

            if (success){
                System.out.println("[DEBUG] - SCHEDULE añadido correctamente");
                exit(null);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("MultitaskAPP | DESKTOP");
                alert.setHeaderText("Tablero añadido correctamente!");
                alert.showAndWait();
            }else {
                System.out.println("[DEBUG] - Error al añadir la SCHEDULE...");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("MultitaskAPP | DESKTOP");
                alert.setHeaderText("Error al añadir la tablero...");
                alert.showAndWait();
            }
        }
    }

    private void updateSchedule(){

        Schedule updateSchedule = getData();
        boolean success = false;

        if (updateSchedule.isGroup())
            success = Data.scheduleManager.updateGroupSchedule(updateSchedule);
        else
            success = Data.scheduleManager.updateSchedule(updateSchedule);

        if (success){
            System.out.println("[DEBUG] - TASK actualizada correctamente");
            exit(null);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("MultitaskAPP | DESKTOP");
            alert.setHeaderText("Tarea actualizada correctamente!");
            alert.showAndWait();
        }else {
            System.out.println("[DEBUG] - Error al actualizar la TASK...");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("MultitaskAPP | DESKTOP");
            alert.setHeaderText("Error al actualizar la tarea...");
            alert.showAndWait();
        }

    }

    @FXML
    void exit(ActionEvent event) {
        updateView();
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

    private java.awt.Color getSelectedColour(Color sc){

        java.awt.Color awtColor = new java.awt.Color((float) sc.getRed(), (float) sc.getGreen(), (float) sc.getBlue(), (float) sc.getOpacity());

        int r = awtColor.getRed();
        int g = awtColor.getGreen();
        int b = awtColor.getBlue();

        return awtColor;
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

    public TaskViewController getTaskViewController() {
        return taskViewController;
    }

    public void setTaskViewController(TaskViewController taskViewController) {
        this.taskViewController = taskViewController;
    }

    private void updateView(){
        taskViewController.getAllSchedules();
    }
}
