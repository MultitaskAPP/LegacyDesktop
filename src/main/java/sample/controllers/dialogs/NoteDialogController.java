package sample.controllers.dialogs;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.ImagePattern;
import javafx.stage.Stage;
import sample.controllers.views.NoteViewController;
import sample.models.Group;
import sample.models.Note;
import sample.utils.Data;

import java.net.URL;
import java.sql.Date;
import java.util.Calendar;
import java.util.ResourceBundle;

public class NoteDialogController implements Initializable {

    @FXML    private TextField tfTitle;
    @FXML    private Button btnAdd, btnCancel;
    @FXML    private TextArea taContent;
    @FXML    private ComboBox<Group> cbGroups;

    private NoteViewController noteViewController;
    private boolean isGroup, updateMode = false;
    private Note selectedNote;
    private double x, y;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void preloadData(){

        cbGroups.getItems().add(null);
        cbGroups.getItems().addAll(Data.arrayGroupsUser);
        cbGroups.getSelectionModel().selectFirst();

        if (selectedNote != null){
            if (updateMode){
                editMode();
            }else {
                viewMode();
            }
        }

        if (!updateMode)
            btnAdd.setOnMouseClicked(mouseEvent -> addNote());

    }

    private void editMode(){

        cbGroups.setDisable(true);

        tfTitle.setText(selectedNote.getTitle());
        taContent.setText(selectedNote.getContent());

        if (selectedNote.isGroup()){
            for (Group g: cbGroups.getItems()) {
                if (g != null){
                    if (g.getIdGroup() == selectedNote.getIdGroup()){
                        cbGroups.getSelectionModel().select(g);
                        changeStyleComboBox(null);
                    }
                }
            }
        }else{
            cbGroups.getSelectionModel().selectFirst();
        }

        btnAdd.setOnMouseClicked(mouseEvent -> updateNote());

    }

    private void viewMode(){

        tfTitle.setEditable(false);
        taContent.setEditable(false);
        cbGroups.setDisable(true);
        btnAdd.setDisable(true);

        editMode();

    }

    private Note getData(){

        if (!tfTitle.getText().isBlank()){
            Note n = new Note();
            n.setTitle(tfTitle.getText());
            if (!taContent.getText().isBlank())
                n.setContent(taContent.getText());

            if (selectedNote != null){
                n.setIdNote(selectedNote.getIdNote());
                n.setCreationDate(selectedNote.getCreationDate());
            }else{
                n.setCreationDate(new Date(Calendar.getInstance().getTime().getTime()));
            }

            if (cbGroups.getSelectionModel().getSelectedIndex() == 0){
                n.setIdUser(Data.userData.getIdUser());
                n.setGroup(false);
            }else{
                n.setIdGroup(cbGroups.getSelectionModel().getSelectedItem().getIdGroup());
                n.setGroup(true);
            }

            return n;

        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("MultitaskAPP | DESKTOP");
            alert.setHeaderText("La nota debe tener titulo...");
            alert.showAndWait();
        }

        return null;
    }

    private void addNote(){

        Note newNote = getData();
        if (newNote != null){
            boolean success = false;
            if (newNote.isGroup()){
                success = Data.noteManager.insertGroupNote(newNote);
            }else {
                success = Data.noteManager.insertNote(newNote);
            }

            if (success){
                System.out.println("[DEBUG] - NOTE añadido correctamente");
                close(null);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("MultitaskAPP | DESKTOP");
                alert.setHeaderText("Nota añadida correctamente!");
                alert.showAndWait();
            }else{
                System.out.println("[DEBUG] - Error al añadir la NOTE...");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("MultitaskAPP | DESKTOP");
                alert.setHeaderText("Error al añadir la nota...");
                alert.showAndWait();
            }
        }

    }

    private void updateNote(){

        Note updatedNote = getData();
        boolean success = false;

        if (updatedNote.isGroup()){
            success = Data.noteManager.updateGroupNote(updatedNote);
        }else{
            success = Data.noteManager.updateNote(updatedNote);
        }

        if (success){
            System.out.println("[DEBUG] - NOTE actualizada correctamente");
            close(null);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("MultitaskAPP | DESKTOP");
            alert.setHeaderText("Nota actualizada correctamente!");
            alert.showAndWait();
        }else {
            System.out.println("[DEBUG] - Error al actualizar la NOTE...");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("MultitaskAPP | DESKTOP");
            alert.setHeaderText("Error al actualizar la nota...");
            alert.showAndWait();
        }

    }

    public void setNoteViewController(NoteViewController noteViewController) {
        this.noteViewController = noteViewController;
    }

    public void setGroup(boolean group) {
        isGroup = group;
    }

    public void setUpdateMode(boolean updateMode) {
        this.updateMode = updateMode;
    }

    public void setSelectedNote(Note selectedNote) {
        this.selectedNote = selectedNote;
    }

    private void updateView(){
        noteViewController.getNotes();
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
