package sample.controllers.views;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import sample.models.Task;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;

public class TaskListView implements Initializable {

    @FXML    private VBox vBoxTasks;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        getAllTasks();
    }

    private void getAllTasks(){

        for (Task t : TaskViewController.taskList) {
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

            textArea.setStyle("-fx-background-radius: 15; -fx-background-color:  #0392FF; -fx-text-fill: white; -fx-font-size: 20; -fx-font-family: Arial");

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

            vBoxTasks.getChildren().add(hBox);

        }
    }

    private void showTaskOptions(int idTask){
        System.out.println("[DEBUG] - Task: " + idTask);
    }

}
