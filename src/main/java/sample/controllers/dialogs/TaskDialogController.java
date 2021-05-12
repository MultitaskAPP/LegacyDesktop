package sample.controllers.dialogs;

import com.jfoenix.controls.JFXDatePicker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.skin.DatePickerSkin;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.json.JSONArray;
import sample.models.Schedule;
import sample.models.Task;
import sample.utils.Data;
import java.net.URL;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class TaskDialogController implements Initializable {

    @FXML    private TextArea textArea;
    @FXML    private ChoiceBox<String> cbLists, cbPriority;
    @FXML    private TextField tfDuration, tfSchedule;
    @FXML    private AnchorPane calendarPane;
    @FXML    private Button btnCancel, btnAdd;

    private DatePicker datePicker = new DatePicker(LocalDate.now());
    private Schedule selectedSchedule;
    private boolean isGroup, updateMode = false;
    private double x, y;
    private int idTask = 0, selectedList = 0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    @FXML
    void checkTask(ActionEvent event) {
        if (!textArea.getText().isBlank()) {
            if(!cbLists.getSelectionModel().isEmpty()){
                Task taskObj = new Task();
                taskObj.setIdTask(idTask);
                taskObj.setTextTask(textArea.getText().replaceAll("\"", "'"));
                taskObj.setListTask(cbLists.getSelectionModel().getSelectedItem());
                taskObj.setIdSchedule(selectedSchedule.getIdSchedule());
                if (!tfDuration.getText().isBlank())
                    taskObj.setDurationTask(Integer.parseInt(tfDuration.getText()));
                if (!cbPriority.getSelectionModel().isEmpty())
                    taskObj.setPriorityTask(cbPriority.getSelectionModel().getSelectedIndex() + 1);
                if (datePicker.valueProperty().get() != null){
                    LocalDate localDate = datePicker.getValue();
                    Date date = Date.valueOf(localDate);
                    taskObj.setLimitDateTask(date);
                    System.out.println(taskObj.getLimitDateTask());
                }

                if(updateMode){
                    if (isGroup)
                        updateGroupTask(taskObj);
                    else
                        updateTask(taskObj);
                }else {
                    if (isGroup)
                        insertGroupTask(taskObj);
                    else
                        insertTask(taskObj);
                }

            }else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("MultitaskAPP | DESKTOP");
                alert.setHeaderText("Debes seleccionar una lista...");
                alert.showAndWait();
            }
        }else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("MultitaskAPP | DESKTOP");
            alert.setHeaderText("La tarea no puede estar en blanco...");
            alert.showAndWait();
        }
    }

    public void preloadData(){

        // Añade las opciones al ChoiceBox cbPriority
        String[] priorityOptions = new String[]{"BAJA", "MEDIA", "ALTA"};
        cbPriority.getItems().addAll(priorityOptions);

        // Obtiene todas las LISTAS del SCHEDULE
        JSONArray scheduleListsJSON = selectedSchedule.getListsSchedules();
        ArrayList<String> scheduleLists = new ArrayList<>();
        for (int i = 0; i < scheduleListsJSON.length(); i++){
            scheduleLists.add(scheduleListsJSON.getString(i));
        }
        cbLists.getItems().addAll(scheduleLists);
        cbLists.getSelectionModel().select(selectedList);


        // tfDuration - SOLO NUMEROS
        tfDuration.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                tfDuration.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        tfSchedule.setText(selectedSchedule.getNameSchedule());

        DatePickerSkin datePickerSkin = new DatePickerSkin(datePicker);
        Node popupContent = datePickerSkin.getPopupContent();
        calendarPane.getChildren().add(popupContent);

    }

    public void showTaskData(Task task){

        if (!updateMode){
            btnAdd.setDisable(true);
            textArea.setEditable(false);
            tfDuration.setEditable(false);
            tfSchedule.setEditable(false);
            cbLists.setDisable(true);
            cbPriority.setDisable(true);
        }

        idTask = task.getIdTask();

        btnAdd.setText("EDITAR");
        textArea.setText(task.getTextTask());

        String[] priorityOptions = new String[]{"BAJA", "MEDIA", "ALTA"};
        cbPriority.getItems().addAll(priorityOptions);
        if (task.getPriorityTask() != 0)
            cbPriority.getSelectionModel().select(task.getPriorityTask());

        JSONArray scheduleListsJSON = selectedSchedule.getListsSchedules();
        ArrayList<String> scheduleLists = new ArrayList<>();
        for (int i = 0; i < scheduleListsJSON.length(); i++){
            scheduleLists.add(scheduleListsJSON.getString(i));
        }
        cbLists.getItems().addAll(scheduleLists);
        cbLists.getSelectionModel().select(scheduleLists.indexOf(task.getListTask()));

        tfDuration.setText(Integer.toString(task.getDurationTask()));
        tfDuration.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                tfDuration.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        tfSchedule.setText(selectedSchedule.getNameSchedule());

        try {
            Date date = (Date) task.getLimitDateTask();
            datePicker.valueProperty().setValue(date.toLocalDate());
        }catch (Exception e){
            System.out.println("[DEBUG] - Esta TASK no tiene fecha límite");
        }
        DatePickerSkin datePickerSkin = new DatePickerSkin(datePicker);
        Node popupContent = datePickerSkin.getPopupContent();
        calendarPane.getChildren().add(popupContent);

    }

    private void insertTask(Task task){
        task = Data.taskManager.insertTask(task);
        if (task.getIdTask() != 0){
            System.out.println("[DEBUG] - TASK añadida correctamente");
            exit(null);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("MultitaskAPP | DESKTOP");
            alert.setHeaderText("Tarea añadida correctamente!");
            alert.showAndWait();
        }else {
            System.out.println("[DEBUG] - Error al añadir la TASK...");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("MultitaskAPP | DESKTOP");
            alert.setHeaderText("Error al añadir la tarea...");
            alert.showAndWait();
        }
    }

    private void insertGroupTask(Task task){

        task = Data.taskManager.insertGroupTask(task);
        if (task.getIdTask() != 0){
            System.out.println("[DEBUG] - TASK añadida correctamente");
            exit(null);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("MultitaskAPP | DESKTOP");
            alert.setHeaderText("Tarea añadida correctamente!");
            alert.showAndWait();
        }else {
            System.out.println("[DEBUG] - Error al añadir la TASK...");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("MultitaskAPP | DESKTOP");
            alert.setHeaderText("Error al añadir la tarea...");
            alert.showAndWait();
        }

    }

    private void updateTask(Task task){

        boolean success = Data.taskManager.updateTask(task);
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

    private void updateGroupTask(Task task){
        boolean success = Data.taskManager.updateGroupTask(task);
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

    public void setSelectedSchedule(Schedule selectedSchedule) {
        this.selectedSchedule = selectedSchedule;
    }

    public void setGroup(boolean group) {
        isGroup = group;
    }

    public void setUpdateMode(boolean updateMode) {
        this.updateMode = updateMode;
    }

    public void setSelectedList(int selectedList) {
        this.selectedList = selectedList;
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

}
