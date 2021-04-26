package sample.controllers.dialogs;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.skin.DatePickerSkin;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.json.JSONArray;
import sample.models.Schedule;
import sample.models.Task;
import sample.utils.Data;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class TaskDialogController implements Initializable {

    @FXML    private TextArea textArea;
    @FXML    private ChoiceBox<String> cbLists, cbPriority;
    @FXML    private TextField tfDuration, tfSchedule;
    @FXML    private AnchorPane calendarPane;
    @FXML    private Button btnCancel, btnAdd;

    private DatePickerSkin datePickerSkin = new DatePickerSkin(new DatePicker(LocalDate.now()));
    private Schedule selectedSchedule;
    private boolean isGroup;
    private double x, y;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


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

        // Calendario
        Node popupContent = datePickerSkin.getPopupContent();
        calendarPane.getChildren().add(popupContent);

        // tfDuration - SOLO NUMEROS
        tfDuration.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                tfDuration.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        tfSchedule.setText(selectedSchedule.getNameSchedule());

    }

    @FXML
    void checkTask(ActionEvent event) {

        if (!textArea.getText().isBlank()) {
            if(!cbLists.getSelectionModel().isEmpty()){
                Task taskObj = new Task();
                taskObj.setTextTask(textArea.getText().replaceAll("\"", "'"));
                taskObj.setListTask(cbLists.getSelectionModel().getSelectedItem());
                taskObj.setIdSchedule(selectedSchedule.getIdSchedule());
                if (!tfDuration.getText().isBlank())
                    taskObj.setDurationTask(Integer.parseInt(tfDuration.getText()));
                if (!cbPriority.getSelectionModel().isEmpty())
                    taskObj.setPriorityTask(cbPriority.getSelectionModel().getSelectedIndex() + 1);
                // if (limitDatePicker.getValue() != null)
                //  taskObj.setLimitDateTask(Date.from(limitDatePicker.getValue().atStartOfDay().toInstant(ZoneOffset.UTC)));

                if (isGroup)
                    insertGroupTask(taskObj);
                else
                    insertTask(taskObj);

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

    }



    @FXML
    void exit(ActionEvent event) {
        Stage stage = (Stage) btnAdd.getScene().getWindow();
        stage.close();
    }

    public void setSelectedSchedule(Schedule selectedSchedule) {
        this.selectedSchedule = selectedSchedule;
    }

    public void setGroup(boolean group) {
        isGroup = group;
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
