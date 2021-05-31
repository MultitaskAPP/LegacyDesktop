package sample.controllers.dialogs;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import sample.controllers.views.EventViewController;
import sample.models.Event;
import sample.models.Group;
import sample.models.Note;
import sample.utils.Data;

import java.net.URL;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;

public class EventDialogController implements Initializable {

    @FXML    private TextArea textArea;
    @FXML    private ComboBox<Group> cbGroups;
    @FXML    private DatePicker dateStartPicker, dateFinishPicker;
    @FXML    private ComboBox<Integer> startHour, startMinutes, finishHour, finishMinutes;
    @FXML    private CheckBox ckbNoRepeat, ckbWeekly, ckbMonthly, ckbYearly, ckbAllDay;
    @FXML    private Button btnAdd, btnCancel;

    private EventViewController eventViewController;
    private boolean isGroup, updateMode = false;
    private Event selectedEvent;
    private LocalDate selectedDate;
    private double x, y;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void preloadData(){

        dateStartPicker.setValue(selectedDate);

        cbGroups.getItems().add(null);
        cbGroups.getItems().addAll(Data.arrayGroupsUser);
        cbGroups.getSelectionModel().selectFirst();

        ckbNoRepeat.selectedProperty().setValue(true);
        ckbAllDay.setOnAction(actionEvent -> {
            if (ckbAllDay.selectedProperty().getValue()){
                startHour.setDisable(true);
                startMinutes.setDisable(true);
                finishHour.setDisable(true);
                finishMinutes.setDisable(true);
            }else{
                startHour.setDisable(false);
                startMinutes.setDisable(false);
                finishHour.setDisable(false);
                finishMinutes.setDisable(false);
            }
        });

        List<CheckBox> checkBoxList = new ArrayList<>();
        checkBoxList.add(ckbNoRepeat);
        checkBoxList.add(ckbWeekly);
        checkBoxList.add(ckbMonthly);
        checkBoxList.add(ckbYearly);

        for(CheckBox ch: checkBoxList) {
            ch.setOnAction(actionEvent -> {
                for (CheckBox ckb : checkBoxList) {
                    ckb.selectedProperty().setValue(false);
                    ch.selectedProperty().setValue(true);
                }
            });
        }

        for (int i = 0; i < 24; i++){
            startHour.getItems().add(i);
            finishHour.getItems().add(i);
        }

        for (int i = 0; i < 60; i++){
            startMinutes.getItems().add(i);
            finishMinutes.getItems().add(i);
        }

        if (selectedEvent != null){
            if (updateMode){
                editMode();
            }else {
                viewMode();
            }
        }

        if (!updateMode)
            btnAdd.setOnMouseClicked(mouseEvent -> addEvent());

    }

    private void viewMode() {

        textArea.setEditable(false);
        dateFinishPicker.setDisable(true);
        dateStartPicker.setDisable(true);
        cbGroups.setDisable(true);
        startHour.setDisable(true);
        startMinutes.setDisable(true);
        finishHour.setDisable(true);
        finishMinutes.setDisable(true);
        btnAdd.setDisable(true);
        ckbYearly.setDisable(true);
        ckbWeekly.setDisable(true);
        ckbNoRepeat.setDisable(true);
        ckbMonthly.setDisable(true);

        editMode();

    }

    private void editMode() {

        cbGroups.setDisable(true);

        textArea.setText(selectedEvent.getTextEvent());

        dateStartPicker.setValue(selectedEvent.getDateStart().toLocalDate());
        dateFinishPicker.setValue(selectedEvent.getDateFinish().toLocalDate());

        startHour.getSelectionModel().select(selectedEvent.getHourStart().getHours());
        finishHour.getSelectionModel().select(selectedEvent.getHourFinish().getHours());

        startMinutes.getSelectionModel().select(selectedEvent.getHourStart().getMinutes());
        finishMinutes.getSelectionModel().select(selectedEvent.getHourFinish().getMinutes());

        int typeEvent = selectedEvent.getTypeEvent();
        switch (typeEvent){
            case 0:
                ckbNoRepeat.selectedProperty().setValue(true);
                ckbWeekly.selectedProperty().setValue(false);
                ckbMonthly.selectedProperty().setValue(false);
                ckbYearly.selectedProperty().setValue(false);
                break;

            case 1:
                ckbNoRepeat.selectedProperty().setValue(false);
                ckbWeekly.selectedProperty().setValue(true);
                ckbMonthly.selectedProperty().setValue(false);
                ckbYearly.selectedProperty().setValue(false);
                break;

            case 2:
                ckbNoRepeat.selectedProperty().setValue(false);
                ckbWeekly.selectedProperty().setValue(false);
                ckbMonthly.selectedProperty().setValue(true);
                ckbYearly.selectedProperty().setValue(false);
                break;

            case 3:
                ckbNoRepeat.selectedProperty().setValue(false);
                ckbWeekly.selectedProperty().setValue(false);
                ckbMonthly.selectedProperty().setValue(false);
                ckbYearly.selectedProperty().setValue(true);
                break;
        }

        if (selectedEvent.isGroup()){
            for (Group g: cbGroups.getItems()) {
                if (g != null){
                    if (g.getIdGroup() == selectedEvent.getIdGroup()){
                        cbGroups.getSelectionModel().select(g);
                        changeStyleComboBox(null);
                    }
                }
            }
        }else{
            cbGroups.getSelectionModel().selectFirst();
        }

        btnAdd.setOnMouseClicked(mouseEvent -> updateEvent());

    }

    private Event getData(){

        if (dateStartPicker.getValue() != null){
            if (!textArea.getText().isBlank()){
                Event e = new Event();
                e.setTextEvent(textArea.getText());

                if (selectedEvent != null){
                    e.setIdEvent(selectedEvent.getIdEvent());
                }

                if (cbGroups.getSelectionModel().getSelectedIndex() == 0){
                    e.setIdUser(Data.userData.getIdUser());
                    e.setGroup(false);
                }else{
                    e.setIdGroup(cbGroups.getSelectionModel().getSelectedItem().getIdGroup());
                    e.setGroup(true);
                }

                e.setDateStart(Date.valueOf(dateStartPicker.getValue()));
                if (dateFinishPicker.getValue() != null){
                    e.setDateFinish(Date.valueOf(dateFinishPicker.getValue()));
                }else{
                    e.setDateFinish(Date.valueOf(dateStartPicker.getValue()));
                }

                if (ckbAllDay.selectedProperty().getValue()){
                    e.setHourStart(new Time(00, 00, 00));
                    e.setHourFinish(new Time(23, 59, 59));
                }else{
                    e.setHourStart(new Time(startHour.getSelectionModel().getSelectedItem(), startMinutes.getSelectionModel().getSelectedItem(), 00));
                    e.setHourFinish(new Time(finishHour.getSelectionModel().getSelectedItem(), finishMinutes.getSelectionModel().getSelectedItem(), 00));
                }

                if (ckbNoRepeat.selectedProperty().getValue())
                    e.setTypeEvent(0);

                if (ckbWeekly.selectedProperty().getValue())
                    e.setTypeEvent(1);

                if (ckbMonthly.selectedProperty().getValue())
                    e.setTypeEvent(2);

                if (ckbYearly.selectedProperty().getValue())
                    e.setTypeEvent(3);

                return e;

            }else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("MultitaskAPP | DESKTOP");
                alert.setHeaderText("El evento debe tener una descripcion...");
                alert.showAndWait();
            }
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("MultitaskAPP | DESKTOP");
            alert.setHeaderText("Debes seleccionar un dia de inicio...");
            alert.showAndWait();
        }

        return null;

    }

    private void addEvent() {

        Event newEvent = getData();
        if (newEvent != null){
            boolean success = false;
            if (newEvent.isGroup()){
                success = Data.eventManager.insertGroupEvent(newEvent);
            }else {
                success = Data.eventManager.insertEvent(newEvent);
            }

            if (success){
                System.out.println("[DEBUG] - EVENT añadido correctamente");
                eventViewController.setSelectedDate(dateStartPicker.getValue());
                close(null);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("MultitaskAPP | DESKTOP");
                alert.setHeaderText("Evento añadido correctamente!");
                alert.showAndWait();
            }else{
                System.out.println("[DEBUG] - Error al añadir el EVENT...");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("MultitaskAPP | DESKTOP");
                alert.setHeaderText("Error al añadir el evento...");
                alert.showAndWait();
            }
        }
    }

    private void updateEvent() {

        Event updatedEvent = getData();
        boolean success = false;

        if (updatedEvent.isGroup()){
            success = Data.eventManager.updateGroupEvent(updatedEvent);
        }else{
            success = Data.eventManager.updateEvent(updatedEvent);
        }

        if (success){
            System.out.println("[DEBUG] - EVENT actualizado correctamente");
            eventViewController.setSelectedDate(dateStartPicker.getValue());
            close(null);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("MultitaskAPP | DESKTOP");
            alert.setHeaderText("Evento actualizado correctamente!");
            alert.showAndWait();
        }else {
            System.out.println("[DEBUG] - Error al actualizar el EVENT...");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("MultitaskAPP | DESKTOP");
            alert.setHeaderText("Error al actualizar el evento...");
            alert.showAndWait();
        }
    }

    private void updateView(){
        eventViewController.getEvents();
    }

    public void setEventViewController(EventViewController eventViewController) {
        this.eventViewController = eventViewController;
    }

    public void setGroup(boolean group) {
        isGroup = group;
    }

    public void setUpdateMode(boolean updateMode) {
        this.updateMode = updateMode;
    }

    public void setSelectedEvent(Event selectedEvent) {
        this.selectedEvent = selectedEvent;
    }

    public void setSelectedDate(LocalDate selectedDate) {
        this.selectedDate = selectedDate;
    }

    @FXML
    void changeStyleComboBox(ActionEvent event) {
        if (cbGroups.getSelectionModel().getSelectedItem() != null){
            cbGroups.setStyle("-fx-background-radius: 30; -fx-background-color: " + cbGroups.getSelectionModel().getSelectedItem().getHexCode());
        }else {
            cbGroups.setStyle("-fx-background-radius: 30; -fx-background-color:  #32323E");
        }
    }

    @FXML
    void close(MouseEvent event) {
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
