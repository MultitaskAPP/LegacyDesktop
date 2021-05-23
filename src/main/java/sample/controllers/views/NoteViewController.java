package sample.controllers.views;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sample.controllers.dialogs.NoteDialogController;
import sample.controllers.dialogs.ScheduleDialogController;
import sample.models.Group;
import sample.models.Note;
import sample.utils.Data;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class NoteViewController implements Initializable {

    @FXML    private FlowPane flNotes;
    @FXML    private VBox btnAdd;

    private List<Note> notesList = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        getNotes();
    }

    public void getNotes(){

        clearView();

        ArrayList<String> arrayGroupIDs = new ArrayList<>();
        for (Group g : Data.arrayGroupsUser) {
            arrayGroupIDs.add(Integer.toString(g.getIdGroup()));
        }

        List<Note> notesUser = Data.noteManager.getAllNotesByUser(Data.userData.getIdUser());
        List<Note> notesGroup = Data.noteManager.getAllNotesByGroup(arrayGroupIDs);

        notesList.addAll(notesUser);
        notesList.addAll(notesGroup);

        Collections.sort(notesList, Comparator.comparing(Note::getCreationDate).reversed());

        viewNotes();

    }

    private void viewNotes() {

        for (Note n : notesList) {

            VBox vBoxNote = new VBox();
            vBoxNote.setPrefWidth(285);
            vBoxNote.setAlignment(Pos.TOP_CENTER);
            vBoxNote.setPadding(new Insets(15));

            Label tagTitle = new Label(n.getTitle().toUpperCase());
            tagTitle.setStyle("-fx-text-fill: white; -fx-font-size: 20; -fx-font-weight: bold");

            TextArea textNote = new TextArea(n.getContent());
            textNote.setStyle("-fx-background-color: transparent; -fx-font-size: 15");
            textNote.setWrapText(true);
            textNote.setEditable(false);

            textNote.setPrefRowCount((textNote.getText().length() / 20) + 1);
            if (textNote.getPrefRowCount() <= 1){
                textNote.setPrefHeight(40);
            }else {
                textNote.setPrefHeight(textNote.getPrefRowCount() * 40);
            }

            if (n.isGroup()){
                Group g = Data.groupManager.getGroupByID(n.getIdGroup());
                vBoxNote.setStyle("-fx-background-radius: 30; -fx-background-color: " + g.getHexCode());
            }else{
                vBoxNote.setStyle("-fx-background-color: #32323E; -fx-background-radius: 30");
            }

            vBoxNote.setPrefHeight(textNote.getPrefHeight());

            MenuItem miView = new MenuItem("Visualizar");
            miView.setOnAction(actionEvent -> viewNote(n));
            MenuItem miEdit = new MenuItem("Editar");
            miEdit.setOnAction(actionEvent -> updateNote(n));
            MenuItem miDelete = new MenuItem("Borrar");
            miDelete.setOnAction(actionEvent -> deleteNote(n));
            ContextMenu contextMenu = new ContextMenu(miView, miEdit, miDelete);
            vBoxNote.setOnContextMenuRequested(contextMenuEvent -> contextMenu.show(vBoxNote, Side.BOTTOM, vBoxNote.getWidth() - 25, 0));

            vBoxNote.getChildren().add(tagTitle);
            vBoxNote.getChildren().add(textNote);
            flNotes.getChildren().add(vBoxNote);

        }

    }

    public void clearView(){
        notesList.clear();
        for (int i = flNotes.getChildren().size() - 1; i > 0; i--){
            flNotes.getChildren().remove(i);
        }

    }

    private void viewNote(Note n){
        try {
            Stage stage = new Stage();
            // URL url = new File("src/main/java/sample/windows/dialogs/noteDialog.fxml").toURI().toURL();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("windows/dialogs/noteDialog.fxml"));
            Parent root = loader.load();
            NoteDialogController noteDialogController = loader.getController();
            noteDialogController.setNoteViewController(this);
            noteDialogController.setUpdateMode(false);
            noteDialogController.setSelectedNote(n);
            noteDialogController.setGroup(n.isGroup());
            noteDialogController.preloadData();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.initStyle(StageStyle.TRANSPARENT);
            scene.setFill(Color.TRANSPARENT);
            stage.show();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateNote(Note n){
        try {
            Stage stage = new Stage();
            // URL url = new File("src/main/java/sample/windows/dialogs/noteDialog.fxml").toURI().toURL();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("windows/dialogs/noteDialog.fxml"));
            Parent root = loader.load();
            NoteDialogController noteDialogController = loader.getController();
            noteDialogController.setNoteViewController(this);
            noteDialogController.setUpdateMode(true);
            noteDialogController.setSelectedNote(n);
            noteDialogController.setGroup(n.isGroup());
            noteDialogController.preloadData();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.initStyle(StageStyle.TRANSPARENT);
            scene.setFill(Color.TRANSPARENT);
            stage.show();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteNote(Note n){
        boolean success;

        if (n.isGroup())
            success = Data.noteManager.deleteGroupNote(n);
        else
            success = Data.noteManager.deleteNote(n);

        if (success){
            System.out.println("[DEBUG] - NOTE eliminada correctamente!");
            getNotes();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("MultitaskAPP | DESKTOP");
            alert.setHeaderText("Nota eliminada correctamente!");
            alert.showAndWait();
        }else {
            System.out.println("[DEBUG] - Error al eliminar la NOTE...");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("MultitaskAPP | DESKTOP");
            alert.setHeaderText("Error al eliminar la nota...");
            alert.showAndWait();
        }

    }

    @FXML
    void addNote(MouseEvent event) {
        try {
            Stage stage = new Stage();
            // URL url = new File("src/main/java/sample/windows/dialogs/noteDialog.fxml").toURI().toURL();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("windows/dialogs/noteDialog.fxml"));
            Parent root = loader.load();
            NoteDialogController noteDialogController = loader.getController();
            noteDialogController.setNoteViewController(this);
            noteDialogController.preloadData();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.initStyle(StageStyle.TRANSPARENT);
            scene.setFill(Color.TRANSPARENT);
            stage.show();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

