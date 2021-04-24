package sample.controllers.views;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import sample.models.Schedule;
import sample.models.Task;
import sample.utils.Data;
import sample.utils.ImageTweakerTool;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class TaskViewController implements Initializable {

    @FXML    private HBox hBoxSchedules;
    @FXML    private AnchorPane viewPane;
    @FXML    private Button btnScheduleView, btnListView;

    public static List<Task> taskList = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        getAllSchedules();


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

            vBox.setOnMouseClicked(mouseEvent -> getAllTasks(s.getIdSchedule()));

            hBoxSchedules.getChildren().add(vBox);
        }

    }

    private void getAllTasks(int scheduleID){

        taskList = Data.taskManager.getAllTaksBySchedule(scheduleID);
    }

    @FXML
    void listView(ActionEvent event) {
        try {
            URL url = new File("src/main/java/sample/windows/views/listView.fxml").toURI().toURL();
            viewPane.getChildren().add(FXMLLoader.load(url));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    void scheduleView(ActionEvent event) {
        try {
            URL url = new File("src/main/java/sample/windows/views/scheduleView.fxml").toURI().toURL();
            viewPane.getChildren().add(FXMLLoader.load(url));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}

