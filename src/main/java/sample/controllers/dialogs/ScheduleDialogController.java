package sample.controllers.dialogs;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import sample.models.Group;

import java.net.URL;
import java.util.ResourceBundle;

public class ScheduleDialogController implements Initializable {

    @FXML    private TextField tfName, tfTask;
    @FXML    private ColorPicker colourPicker;
    @FXML    private ChoiceBox<Group> cbGroups;
    @FXML    private VBox hBoxPreview;
    @FXML    private Rectangle rectanglePreview;
    @FXML    private Label tagNamePreview;
    @FXML    private ListView<String> taskList;
    @FXML    private Button btnAdd, btnCancel;

    private boolean isGroup, updateMode = false;
    private double x, y;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    private void preloadData(){}

    private void createMode(){}

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


}
