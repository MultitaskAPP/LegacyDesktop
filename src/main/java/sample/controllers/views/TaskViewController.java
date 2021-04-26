package sample.controllers.views;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Pair;
import org.json.JSONArray;
import sample.controllers.dialogs.TaskDialogController;
import sample.models.Schedule;
import sample.models.Task;
import sample.utils.Data;
import sample.utils.ImageTweakerTool;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Date;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;

public class TaskViewController implements Initializable {

    @FXML    private HBox hBoxSchedules, addTaskList;
    @FXML    private ScrollPane listView, scheduleView;
    @FXML    private Button btnScheduleView, btnListView;
    @FXML    private VBox vBoxList;

    public List<Task> taskList = new ArrayList<>();
    private static boolean selectedView;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        getAllSchedules();
        selectedView = true;

    }

    private void getAllSchedules(){

        List<Schedule> scheduleList = Data.scheduleManager.getAllSchedulesByUser(Data.userData.getIdUser());

        for (Schedule s : scheduleList) {
            VBox vBox = new VBox();
            vBox.setPrefSize(200, 100);
            vBox.setSpacing(5);
            vBox.setPadding(new Insets(10));
            vBox.setAlignment(Pos.TOP_CENTER);
            vBox.setStyle("-fx-background-color: " + s.getHexCode() + "; -fx-background-radius: 20px");

            Rectangle rectangle = new Rectangle(50, 50);
            Image image = new Image(new ImageTweakerTool(Data.userData.getIdUser()).getProfilePic(), rectangle.getWidth(), rectangle.getHeight(), false, true);
            ImagePattern imagePattern = new ImagePattern(image);
            rectangle.setArcHeight(360);
            rectangle.setArcWidth(360);
            rectangle.setFill(imagePattern);
            vBox.getChildren().add(rectangle);

            Label label = new Label(s.getNameSchedule());
            label.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-family: Arial");
            vBox.getChildren().add(label);

            vBox.setOnMouseClicked(mouseEvent -> getAllTasks(s));

            hBoxSchedules.getChildren().add(vBox);
        }

    }

    private void getAllTasks(Schedule s){

        if (taskList.size() >= 1)
            taskList.clear();
        taskList = Data.taskManager.getAllTaksBySchedule(s.getIdSchedule());
        if (selectedView){
            listView(s);
            addTaskList.setOnMouseClicked(mouseEvent -> addTask(s));
        }else {
            scheduleView(s);
        }
    }

    private void listView(Schedule s){

        clearListView();

        for (Task t : taskList) {
            HBox hBox = new HBox();
            hBox.setSpacing(3);
            hBox.setPrefWidth(930);

            TextArea textArea = new TextArea();
            textArea.setPrefWidth(880);
            textArea.setWrapText(true);
            textArea.setText(t.getTextTask());

            textArea.setPrefRowCount((textArea.getText().length() / 100) + 1);
            if (textArea.getPrefRowCount() <= 1){
                textArea.setPrefHeight(50);
            }else {
                textArea.setPrefHeight(textArea.getPrefRowCount() * 30);
            }
            textArea.setStyle("-fx-background-radius: 15; -fx-background-color: " + s.getHexCode() + "; -fx-text-fill: white; -fx-font-size: 20; -fx-font-family: Arial");

            hBox.getChildren().add(textArea);
            hBox.setPrefHeight(textArea.getPrefHeight());

            VBox vBox = new VBox();
            vBox.setPadding(new Insets(5));
            vBox.setSpacing(10);
            vBox.setAlignment(Pos.TOP_CENTER);

            Button btnOptions = new Button();
            btnOptions.setPrefSize(40, 40);
            btnOptions.setStyle("-fx-background-radius: 15; -fx-background-color:  #272730");
            btnOptions.setOnMouseClicked(mouseEvent -> showTaskOptions(t.getIdTask()));

            try{
                ImageView imageView = new ImageView();
                URL url = new File("src/main/java/sample/windows/res/mt_options_icon.png").toURI().toURL();
                imageView.setImage(new Image(String.valueOf(url)));
                imageView.setSmooth(true);
                imageView.setFitHeight(25);
                imageView.setFitWidth(25);
                btnOptions.setGraphic(imageView);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            vBox.getChildren().add(btnOptions);
            vBox.setPrefSize(52, 44);
            hBox.getChildren().add(vBox);

            vBoxList.getChildren().add(hBox);
        }
    }

    private void showTaskOptions(int idTask){
        System.out.println("[DEBUG] - Task: " + idTask);
    }

    private void scheduleView(Schedule s){

    }

    @FXML
    void setListView(ActionEvent event) {
        selectedView = true;
        listView.setVisible(true);
        scheduleView.setVisible(false);
    }

    @FXML
    void setScheduleView(ActionEvent event) {
        selectedView = false;
        listView.setVisible(false);
        scheduleView.setVisible(true);
    }

    /**
     * Elimina todos los items dentro del vBox menos el primero.
     */
    private void clearListView(){
        System.out.println(vBoxList.getChildren().size());
        for (int i = 1; i < vBoxList.getChildren().size(); i++){
            vBoxList.getChildren().remove(i);
        }

    }

    private void addTask(Schedule s){

        try {
            Stage stage = new Stage();
            URL url = new File("src/main/java/sample/windows/dialogs/taskDialog.fxml").toURI().toURL();
            FXMLLoader loader = new FXMLLoader(url);
            Parent root = loader.load();
            TaskDialogController taskDialogController = loader.getController();
            taskDialogController.setSelectedSchedule(s);
            taskDialogController.setGroup(false);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.initStyle(StageStyle.TRANSPARENT);
            scene.setFill(Color.TRANSPARENT);
            stage.show();
            taskDialogController.preloadData();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}

