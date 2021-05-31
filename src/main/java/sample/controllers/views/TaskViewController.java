package sample.controllers.views;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.json.JSONArray;
import sample.controllers.dialogs.ScheduleDialogController;
import sample.controllers.dialogs.TaskDialogController;
import sample.models.Group;
import sample.models.Schedule;
import sample.models.Task;
import sample.utils.Data;
import sample.utils.ImageTweakerTool;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class TaskViewController implements Initializable {

    @FXML    private HBox hBoxSchedules, addTaskList, hBoxScheduleView;
    @FXML    private ScrollPane listView, scheduleView;
    @FXML    private Button btnScheduleView, btnListView;
    @FXML    private VBox vBoxList;
    @FXML    private FontAwesomeIcon fxList, fxSchedule;

    public List<Task> taskList = new ArrayList<>();
    private static boolean selectedView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        getAllSchedules();
        selectedView = getSelectedView();
        if (selectedView)
            setListView(null);          // 0
        else
            setScheduleView(null);      // 1

    }

    public void getAllSchedules(){

        if (hBoxSchedules.getChildren().size() > 1){
            for (int i = hBoxSchedules.getChildren().size() - 1; i > 0; i--){
                hBoxSchedules.getChildren().remove(i);
            }
        }

        List<Schedule> scheduleList = Data.scheduleManager.getAllSchedulesByUser(Data.userData.getIdUser());

        ArrayList<String> arrayGroupIDs = new ArrayList<>();
        for (Group g : Data.arrayGroupsUser) {
            arrayGroupIDs.add(Integer.toString(g.getIdGroup()));
        }

        scheduleList.addAll(Data.scheduleManager.getAllSchedulesByGroup(arrayGroupIDs));

        for (Schedule s : scheduleList) {
            VBox vBox = new VBox();
            vBox.setPrefSize(200, 100);
            vBox.setSpacing(5);
            vBox.setPadding(new Insets(10));
            vBox.setAlignment(Pos.TOP_CENTER);
            vBox.setStyle("-fx-background-color: " + s.getHexCode() + "; -fx-background-radius: 20px");

            MenuItem miView = new MenuItem("Visualizar");
            miView.setOnAction(actionEvent -> viewSchedule(s));
            MenuItem miEdit = new MenuItem("Editar");
            miEdit.setOnAction(actionEvent -> updateSchedule(s));
            MenuItem miDelete = new MenuItem("Borrar");
            miDelete.setOnAction(actionEvent -> deleteSchedule(s));
            ContextMenu contextMenu = new ContextMenu(miView, miEdit, miDelete);
            vBox.setOnContextMenuRequested(contextMenuEvent -> contextMenu.show(vBox, Side.BOTTOM, 0, 0));

            Rectangle rectangle = new Rectangle(50, 50);

            ImagePattern imagePattern;
            if (s.isGroup()){
                Group sGroup = Data.groupManager.getGroupByID(s.getIdGroup());
                imagePattern = new ImagePattern(sGroup.getAvatarGroup());
            }else{
                Image image = new Image(Data.userData.getAvatarUser().getUrl(), rectangle.getWidth(), rectangle.getHeight(), false, true);
                imagePattern = new ImagePattern(image);
            }

            rectangle.setArcHeight(360);
            rectangle.setArcWidth(360);
            rectangle.setFill(imagePattern);
            vBox.getChildren().add(rectangle);

            Label label = new Label(s.getNameSchedule());
            label.setStyle("-fx-text-fill: white; -fx-font-size: 20px; -fx-font-family: 'Roboto'");
            vBox.getChildren().add(label);

            vBox.setOnMouseClicked(mouseEvent -> {
                if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                    updateStyleSchedule(vBox);
                    getAllTasks(s);
                }
            });

            hBoxSchedules.getChildren().add(vBox);
        }

    }

    private void updateStyleSchedule(VBox selectedSchedule){
        resetStyle();
        selectedSchedule.setStyle(selectedSchedule.getStyle() + "; -fx-border-width: 2; -fx-border-radius: 20; -fx-border-color: white");

    }

    private void resetStyle(){
        for (int i = 1; i < hBoxSchedules.getChildren().size(); i++){
            VBox vBox = (VBox) hBoxSchedules.getChildren().get(i);
            vBox.setStyle(vBox.getStyle() + "; -fx-border-width: 0");
        }
    }

    public void getAllTasks(Schedule s){

        if (taskList.size() >= 1)
            taskList.clear();
        taskList = Data.taskManager.getAllTaksBySchedule(s.getIdSchedule(), s.isGroup());

        clearListView();
        listView(s);
        scheduleView(s);

        addTaskList.setOnMouseClicked(mouseEvent ->    addTask(s, 0));

    }

    private void listView(Schedule s){

        for (Task t : taskList) {
            HBox hBox = new HBox();
            hBox.setSpacing(3);
            hBox.setPrefWidth(930);

            TextArea textArea = new TextArea();
            textArea.setPrefWidth(880);
            textArea.setWrapText(true);
            textArea.setText(t.getTextTask());
            textArea.setEditable(false);

            textArea.setPrefRowCount((textArea.getText().length() / 100) + 1);
            if (textArea.getPrefRowCount() <= 1){
                textArea.setPrefHeight(50);
            }else {
                textArea.setPrefHeight(textArea.getPrefRowCount() * 35);
            }
            textArea.setStyle("-fx-background-radius: 15; -fx-background-color: " + s.getHexCode() + "; -fx-text-fill: white; -fx-font-size: 20; -fx-font-family: 'Roboto Light'");

            hBox.getChildren().add(textArea);
            hBox.setPrefHeight(textArea.getPrefHeight());

            VBox vBox = new VBox();
            vBox.setPadding(new Insets(5));
            vBox.setSpacing(10);
            vBox.setAlignment(Pos.TOP_CENTER);

            MenuItem miView = new MenuItem("Visualizar");
            miView.setOnAction(actionEvent -> viewTask(t, s));

            MenuItem miEdit = new MenuItem("Editar");
            miEdit.setOnAction(actionEvent -> updateTask(t, s));

            MenuItem miDelete = new MenuItem("Borrar");
            miDelete.setOnAction(actionEvent -> deleteTask(t, s));

            MenuButton mbOptions = new MenuButton(null, null, miView, miEdit, miDelete);
            mbOptions.setPrefSize(40, 40);
            mbOptions.setStyle("-fx-background-radius: 15; -fx-background-color:  #272730");

            ImageView imageView = new ImageView();
            // URL url = new File("src/main/java/sample/windows/res/icons/mt_options_icon.png").toURI().toURL();
            imageView.setImage(new Image("windows/res/icons/mt_options_icon.png"));
            imageView.setSmooth(true);
            imageView.setFitHeight(25);
            imageView.setFitWidth(25);
            mbOptions.setGraphic(imageView);

            vBox.getChildren().add(mbOptions);
            vBox.setPrefSize(52, 44);
            hBox.getChildren().add(vBox);

            vBoxList.getChildren().add(hBox);
        }
    }

    private void scheduleView(Schedule s){

        JSONArray listsSchedule = s.getListsSchedules();
        for (int i = 0; i < listsSchedule.length(); i++){

            int listSize = 50;

            VBox vBoxScheduleList = new VBox();
            vBoxScheduleList.setSpacing(10);
            vBoxScheduleList.setPadding(new Insets(10));
            vBoxScheduleList.setStyle("-fx-background-color:  #272730; -fx-background-radius: 20");

            HBox hBoxScheduleHeader = new HBox();
            hBoxScheduleHeader.setAlignment(Pos.CENTER);
            hBoxScheduleHeader.setSpacing(50);
            hBoxScheduleHeader.setPrefSize(200, 25);

            // Label - SCHEDULE

            Label tagListName = new Label(listsSchedule.getString(i));
            tagListName.setStyle("-fx-text-fill: " + s.getHexCode() +"; -fx-font-size: 16; -fx-font-weight: bold; -fx-font-family: Roboto");
            hBoxScheduleHeader.getChildren().add(tagListName);

            // MenuButton - SCHEDULE

            MenuItem miView = new MenuItem("Visualizar");
            miView.setOnAction(actionEvent -> viewSchedule(s));

            MenuItem miEdit = new MenuItem("Editar");
            miEdit.setOnAction(actionEvent -> updateSchedule(s));

            MenuItem miDelete = new MenuItem("Borrar");
            miDelete.setOnAction(actionEvent -> deleteSchedule(s));

            MenuButton mbOptions = new MenuButton(null, null, miView, miEdit, miDelete);
            mbOptions.setPrefSize(20, 20);
            mbOptions.setStyle("-fx-background-color:  transparent");

            ImageView imageView = new ImageView();
            // URL url = new File("src/main/java/sample/windows/res/icons/mt_options_icon.png").toURI().toURL();
            imageView.setImage(new Image("windows/res/icons/mt_options_icon.png"));
            imageView.setSmooth(true);
            imageView.setFitHeight(20);
            imageView.setFitWidth(20);
            mbOptions.setGraphic(imageView);

            hBoxScheduleHeader.getChildren().add(mbOptions);
            vBoxScheduleList.getChildren().add(hBoxScheduleHeader);

            for (Task t : taskList) {
                if (t.getListTask().equals(listsSchedule.getString(i))){
                    TextArea textArea = new TextArea(t.getTextTask());
                    textArea.setEditable(false);
                    textArea.setWrapText(true);
                    textArea.setPadding(new Insets(5));
                    textArea.setStyle("-fx-background-color:  #32323E; -fx-background-radius: 18; -fx-font-family: 'Roboto Light'");
                    textArea.setPrefWidth(200);
                    textArea.setPrefRowCount((textArea.getText().length() / 25) + 1);
                    if (textArea.getPrefRowCount() <= 1){
                        textArea.setPrefHeight(25);
                    }else {
                        textArea.setPrefHeight(textArea.getPrefRowCount() * 20);
                    }

                    textArea.setOnMouseClicked(mouseEvent -> updateTask(t, s));

                    listSize += textArea.getPrefHeight() + 5;
                    vBoxScheduleList.getChildren().add(textArea);

                }
            }

            HBox hBoxAdd = new HBox();
            hBoxAdd.setSpacing(5);
            hBoxAdd.setAlignment(Pos.CENTER);
            hBoxAdd.setPrefSize(200, 30);

            int selectedList = i;
            hBoxAdd.setOnMouseClicked(mouseEvent -> addTask(s, selectedList));

            try {
                ImageView igAdd = new ImageView();
                URL url = new File("src/main/java/sample/windows/res/icons/mt_add1_icon.png").toURI().toURL();
                igAdd.setImage(new Image(String.valueOf(url)));
                igAdd.setSmooth(true);
                igAdd.setFitHeight(20);
                igAdd.setFitWidth(20);

                igAdd.setOpacity(0.5);
                hBoxAdd.getChildren().add(igAdd);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            Label tagAdd = new Label("Añadir una nueva tarea");
            tagAdd.setStyle("-fx-text-fill: white; -fx-font-size: 12");
            tagAdd.setOpacity(0.5);
            hBoxAdd.getChildren().add(tagAdd);

            vBoxScheduleList.getChildren().add(hBoxAdd);
            vBoxScheduleList.setMaxSize(200, listSize + 40);
            hBoxScheduleView.getChildren().add(vBoxScheduleList);

        }



    }

    private void clearListView(){

        hBoxScheduleView.getChildren().clear();

        for (int i = vBoxList.getChildren().size() - 1; i > 0; i--){
            vBoxList.getChildren().remove(i);
        }
    }

    private void addTask(Schedule s, int selectedList){

        try {
            Stage stage = new Stage();
            // URL url = new File("src/main/java/sample/windows/dialogs/taskDialog.fxml").toURI().toURL();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("windows/dialogs/taskDialog.fxml"));
            Parent root = loader.load();
            TaskDialogController taskDialogController = loader.getController();
            taskDialogController.setTaskViewController(this);
            taskDialogController.setSelectedSchedule(s);
            taskDialogController.setGroup(s.isGroup());
            taskDialogController.setSelectedList(selectedList);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.initStyle(StageStyle.TRANSPARENT);
            scene.setFill(Color.TRANSPARENT);
            Data.setBlur();
            stage.show();
            taskDialogController.preloadData();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void deleteTask(Task task, Schedule schedule){
        boolean success = false;

        if (task.isGroup())
            success = Data.taskManager.deleteGroupTask(task);
        else
            success = Data.taskManager.deleteTask(task);

        if (success){
            System.out.println("[DEBUG] - TASK eliminada correctamente!");
            taskList.remove(task);
            clearListView();
            listView(schedule);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("MultitaskAPP | DESKTOP");
            alert.setHeaderText("Tarea eliminada correctamente!");
            Data.setBlur();
            alert.showAndWait();
            Data.removeBlur();
        }else {
            System.out.println("[DEBUG] - Error al eliminar la TASK...");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("MultitaskAPP | DESKTOP");
            alert.setHeaderText("Error al eliminar la tarea...");
            Data.setBlur();
            alert.showAndWait();
            Data.removeBlur();
        }
    }

    private void updateTask(Task task, Schedule schedule){

        try {
            Stage stage = new Stage();
            // URL url = new File("src/main/java/sample/windows/dialogs/taskDialog.fxml").toURI().toURL();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("windows/dialogs/taskDialog.fxml"));
            Parent root = loader.load();
            TaskDialogController taskDialogController = loader.getController();
            taskDialogController.setTaskViewController(this);
            taskDialogController.setSelectedSchedule(schedule);
            taskDialogController.setSelectedTask(task);
            taskDialogController.setGroup(task.isGroup());
            taskDialogController.setUpdateMode(true);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.initStyle(StageStyle.TRANSPARENT);
            scene.setFill(Color.TRANSPARENT);
            Data.setBlur();
            stage.show();
            taskDialogController.showTaskData(task);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void viewTask(Task task, Schedule schedule){
        try {
            Stage stage = new Stage();
            // URL url = new File("src/main/java/sample/windows/dialogs/taskDialog.fxml").toURI().toURL();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("windows/dialogs/taskDialog.fxml"));
            Parent root = loader.load();
            TaskDialogController taskDialogController = loader.getController();
            taskDialogController.setTaskViewController(this);
            taskDialogController.setSelectedSchedule(schedule);
            taskDialogController.setGroup(task.isGroup());
            taskDialogController.setUpdateMode(false);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.initStyle(StageStyle.TRANSPARENT);
            scene.setFill(Color.TRANSPARENT);
            Data.setBlur();
            stage.show();
            taskDialogController.showTaskData(task);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void deleteSchedule(Schedule schedule){

        boolean success = false;
        if (schedule.isGroup()){
            success = Data.scheduleManager.deleteGroupSchedule(schedule);
        }else{
            success = Data.scheduleManager.deleteSchedule(schedule);
        }

        if (success){
            System.out.println("[DEBUG] - SCHEDULE eliminado correctamente!");
            clearListView();
            getAllSchedules();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("MultitaskAPP | DESKTOP");
            alert.setHeaderText("Tablero eliminado correctamente!");
            Data.setBlur();
            alert.showAndWait();
            Data.removeBlur();
        }else {
            System.out.println("[DEBUG] - Error al eliminar el SCHEDULE...");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("MultitaskAPP | DESKTOP");
            alert.setHeaderText("Error al eliminar el tablero...");
            Data.setBlur();
            alert.showAndWait();
            Data.removeBlur();
        }

    }

    private void updateSchedule(Schedule schedule){
        try {
            Stage stage = new Stage();
            // URL url = new File("src/main/java/sample/windows/dialogs/scheduleDialog.fxml").toURI().toURL();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("windows/dialogs/scheduleDialog.fxml"));
            Parent root = loader.load();
            ScheduleDialogController scheduleDialogController = loader.getController();
            scheduleDialogController.setTaskViewController(this);
            scheduleDialogController.setSelectedSchedule(schedule);
            scheduleDialogController.setGroup(schedule.isGroup());
            scheduleDialogController.setUpdateMode(true);
            scheduleDialogController.preloadData();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.initStyle(StageStyle.TRANSPARENT);
            scene.setFill(Color.TRANSPARENT);
            Data.setBlur();
            stage.show();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void viewSchedule(Schedule schedule){
        try {
            Stage stage = new Stage();
            // URL url = new File("src/main/java/sample/windows/dialogs/scheduleDialog.fxml").toURI().toURL();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("windows/dialogs/scheduleDialog.fxml"));
            Parent root = loader.load();
            ScheduleDialogController scheduleDialogController = loader.getController();
            scheduleDialogController.setTaskViewController(this);
            scheduleDialogController.setSelectedSchedule(schedule);
            scheduleDialogController.setGroup(schedule.isGroup());
            scheduleDialogController.setUpdateMode(false);
            scheduleDialogController.preloadData();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.initStyle(StageStyle.TRANSPARENT);
            scene.setFill(Color.TRANSPARENT);
            Data.setBlur();
            stage.show();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void storeSelectedView(int nView){
        Data.properties.setProperty("taskView", Integer.toString(nView));
        Data.storeProperties(Data.properties);
    }

    private boolean getSelectedView(){
        try {
            int selectedView = Integer.parseInt(Data.properties.getProperty("taskView"));
            return selectedView == 0;
        }catch (Exception e){
            return true;
        }
    }

    @FXML
    void addSchedule(MouseEvent event) {
        try {
            Stage stage = new Stage();
            // URL url = new File("src/main/java/sample/windows/dialogs/scheduleDialog.fxml").toURI().toURL();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("windows/dialogs/scheduleDialog.fxml"));
            Parent root = loader.load();
            ScheduleDialogController scheduleDialogController = loader.getController();
            scheduleDialogController.setTaskViewController(this);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.initStyle(StageStyle.TRANSPARENT);
            scene.setFill(Color.TRANSPARENT);
            Data.setBlur();
            stage.show();
            scheduleDialogController.preloadData();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void setListView(ActionEvent event) {
        storeSelectedView(0);
        selectedView = true;
        listView.setVisible(true);
        scheduleView.setVisible(false);
        fxList.setFill(Paint.valueOf(Data.userData.getHexCode()));
        fxSchedule.setFill(Paint.valueOf("#FFFFFF"));
    }

    @FXML
    void setScheduleView(ActionEvent event) {
        storeSelectedView(1);
        selectedView = false;
        listView.setVisible(false);
        scheduleView.setVisible(true);
        fxSchedule.setFill(Paint.valueOf(Data.userData.getHexCode()));
        fxList.setFill(Paint.valueOf("#FFFFFF"));
    }

}

