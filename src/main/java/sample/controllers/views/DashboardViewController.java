package sample.controllers.views;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.skin.DatePickerSkin;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import sample.controllers.MainController;
import sample.models.*;
import sample.utils.Data;
import sample.utils.ImageTweakerTool;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.*;

public class DashboardViewController implements Initializable {

    @FXML    private Label tvDay, tvMonth, tagSummary;
    @FXML    private VBox paneCalendar, vBoxEvents, vBoxTasks, vBoxNotifications;

    private List<Event> eventList = new ArrayList<>();
    private List<Task> taskList = new ArrayList<>();
    private List<Schedule> scheduleList;
    private DatePicker datePicker = new DatePicker(LocalDate.now());
    private MainController mainController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void preloadData(){

        loadCalendar();

        DatePickerSkin datePickerSkin = new DatePickerSkin(datePicker);
        Node popupContent = datePickerSkin.getPopupContent();
        paneCalendar.getChildren().add(popupContent);

        tagSummary.setText("¡Hola, " + Data.userData.getName() + "!");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd");
        Date date = new Date();
        tvDay.setText(dateFormat.format(date));
        dateFormat = new SimpleDateFormat("MMM");
        tvMonth.setText(dateFormat.format(date).toUpperCase());

        loadTasks();

        loadNotifications();

    }

    private void loadCalendar(){

        datePicker.setShowWeekNumbers(false);

        ArrayList<String> arrayGroupIDs = new ArrayList<>();
        for (Group g : Data.arrayGroupsUser) {
            arrayGroupIDs.add(Integer.toString(g.getIdGroup()));
        }

        List<Event> eventsUser = Data.eventManager.getAllEventsByUser(Data.userData.getIdUser());
        List<Event> eventsGroup = Data.eventManager.getAllEventsByGroup(arrayGroupIDs);

        eventList.addAll(eventsUser);
        eventList.addAll(eventsGroup);

        datePicker.setDayCellFactory(new EventViewController().getMarkedEvents(eventList));

        loadEvents(eventList);
    }

    private void loadEvents(List<Event> allEvents){

        List<Event> listEvents = new ArrayList<>();
        listEvents.addAll(allEvents);

        for (int i = vBoxEvents.getChildren().size() - 1; i > 1; i--){
            vBoxEvents.getChildren().remove(i);
        }

        listEvents.removeIf(e -> LocalDate.now().isAfter(e.getDateStart().toLocalDate()));
        listEvents.removeIf(e -> !e.getDateStart().toLocalDate().isBefore(LocalDate.now().plusDays(30)));
        listEvents.sort(Comparator.comparing(Event::getDateStart));

        if (listEvents.size() > 0){
            if (listEvents.size() > 5){
                for (int i = 0; i < 5; i++){
                    Event e = listEvents.get(i);
                    vBoxEvents.getChildren().add(eventView(e));
                }

                HBox hBoxMoreEvents = new HBox();
                hBoxMoreEvents.setPrefSize(330, 50);
                hBoxMoreEvents.setPadding(new Insets(10));
                hBoxMoreEvents.setAlignment(Pos.CENTER);
                hBoxMoreEvents.setStyle("-fx-background-color:  #32323E; -fx-background-radius: 60");
                hBoxMoreEvents.setOnMouseClicked(mouseEvent -> mainController.gotoEvents(null));

                Label tagMoreEvents = new Label("Y " + (listEvents.size() - 5) + " eventos más");
                tagMoreEvents.setStyle("-fx-text-fill: white; -fx-font-size: 20; -fx-font-family: 'Roboto Light'");

                hBoxMoreEvents.getChildren().add(tagMoreEvents);
                vBoxEvents.getChildren().add(hBoxMoreEvents);

            }else{
                for (int i = 0; i < listEvents.size(); i++){
                    Event e = listEvents.get(i);
                    vBoxEvents.getChildren().add(eventView(e));
                }
            }
        }else{

            HBox hBoxNoEvents = new HBox();
            hBoxNoEvents.setPrefSize(330, 50);
            hBoxNoEvents.setPadding(new Insets(10));
            hBoxNoEvents.setAlignment(Pos.CENTER);
            hBoxNoEvents.setStyle("-fx-background-color:  #272730; -fx-background-radius: 30");

            Label tagNoEvents = new Label("No hay eventos para los proximos 30 dias");
            tagNoEvents.setStyle("-fx-text-fill: white; -fx-font-size: 15; -fx-font-family: 'Roboto Light'");

            hBoxNoEvents.setOnMouseClicked(mouseEvent -> mainController.gotoEvents(null));

            hBoxNoEvents.getChildren().add(tagNoEvents);
            vBoxEvents.getChildren().add(hBoxNoEvents);

        }
    }

    private void loadTasks(){

        ArrayList<String> arrayGroupIDs = new ArrayList<>();
        for (Group g : Data.arrayGroupsUser) {
            arrayGroupIDs.add(Integer.toString(g.getIdGroup()));
        }

        List<Task> tasksUser = Data.taskManager.getAllTasksByUser(Data.userData.getIdUser());
        List<Task> tasksGroups = Data.taskManager.getAllTasksByGroup(arrayGroupIDs);

        taskList.addAll(tasksUser);
        taskList.addAll(tasksGroups);
        taskList.removeIf(Task::isFinished);
        taskList.sort(Comparator.comparing(Task::getPriorityTask).reversed());
        taskList.sort(Comparator.comparing(Task::getCreationDate));

        if (taskList.size() > 0){
            if (taskList.size() > 10){
                for (int i = 0; i < 10; i++){
                    Task t = taskList.get(i);
                    vBoxTasks.getChildren().add(taskView(t));
                }

                HBox hBoxMoreTasks = new HBox();
                hBoxMoreTasks.setPrefSize(578, 55);
                hBoxMoreTasks.setPadding(new Insets(10));
                hBoxMoreTasks.setAlignment(Pos.CENTER);
                hBoxMoreTasks.setStyle("-fx-background-color:  #32323E; -fx-background-radius: 60");

                Label tagMoreEvents = new Label("Y " + (taskList.size() - 5) + " tareas más!");
                tagMoreEvents.setStyle("-fx-text-fill: white; -fx-font-size: 20; -fx-font-family: 'Roboto Light'");

                hBoxMoreTasks.getChildren().add(tagMoreEvents);
                vBoxTasks.getChildren().add(hBoxMoreTasks);

            }else{
                for (int i = 0; i < taskList.size(); i++){
                    Task t = taskList.get(i);
                    vBoxTasks.getChildren().add(taskView(t));
                }
            }
        }else{

            HBox hBoxNoTasks = new HBox();
            hBoxNoTasks.setPrefSize(578, 55);
            hBoxNoTasks.setPadding(new Insets(10));
            hBoxNoTasks.setAlignment(Pos.CENTER);
            hBoxNoTasks.setStyle("-fx-background-color:  #272730; -fx-background-radius: 60");

            Label tagNoEvents = new Label("No tienes tareas sin finalizar. ¡Añade más desde Tareas!");
            tagNoEvents.setStyle("-fx-text-fill: white; -fx-font-size: 15; -fx-font-family: 'Roboto Light'");

            hBoxNoTasks.setOnMouseClicked(mouseEvent -> mainController.gotoTasks(null));

            hBoxNoTasks.getChildren().add(tagNoEvents);
            vBoxTasks.getChildren().add(hBoxNoTasks);

        }
    }

    private void loadNotifications(){

        vBoxNotifications.getChildren().clear();

        List<Notification> notificationsList = Data.notificationManager.getAllNotifications();
        if (notificationsList.size() != 0){
            for (Notification n : notificationsList) {
                vBoxNotifications.getChildren().add(notificationView(n));
            }
        }else{
            HBox hBoxNoNotif = new HBox();
            hBoxNoNotif.setPrefSize(300, 35);
            hBoxNoNotif.setAlignment(Pos.CENTER);
            hBoxNoNotif.setStyle("-fx-background-radius: 30; -fx-background-color: #32323E");

            Label tagNoNotif = new Label("No tienes notificaciones nuevas.");
            tagNoNotif.setStyle("-fx-text-fill: white; -fx-font-size: 13");
            hBoxNoNotif.getChildren().add(tagNoNotif);

            vBoxNotifications.getChildren().add(hBoxNoNotif);
        }
    }

    private HBox notificationView(Notification n) {

        HBox hBoxNotification = new HBox();
        hBoxNotification.setPrefSize(300, 35);
        hBoxNotification.setAlignment(Pos.CENTER_LEFT);
        hBoxNotification.setSpacing(10);
        hBoxNotification.setPadding(new Insets(5));

        Rectangle avatar = new Rectangle(25, 25);
        avatar.setArcWidth(360);
        avatar.setArcHeight(360);
        if (n.isGroup()){
            Group group = Data.groupManager.getGroupByID(n.getIdGroup());
            avatar.setFill(new ImagePattern(new Image(group.getAvatarGroup().getUrl(), avatar.getWidth(), avatar.getHeight(), true, false)));
            hBoxNotification.setStyle("-fx-background-radius: 30; -fx-background-color: " + group.getHexCode());
        }else{
            avatar.setFill(new ImagePattern(new Image(Data.userData.getAvatarUser().getUrl(), avatar.getWidth(), avatar.getHeight(), true, false)));
            hBoxNotification.setStyle("-fx-background-radius: 30; -fx-background-color: " + Data.userData.getHexCode());
        }

        hBoxNotification.getChildren().add(avatar);

        Label tagNotification = new Label(n.getTextNotification());
        tagNotification.setStyle("-fx-text-fill: white; -fx-font-size: 13");

        hBoxNotification.getChildren().add(tagNotification);

        return hBoxNotification;
    }

    private HBox eventView(Event e){

        HBox hBoxEvent = new HBox();
        hBoxEvent.setPrefSize(330, 75);
        hBoxEvent.setPadding(new Insets(10));
        hBoxEvent.setSpacing(10);
        hBoxEvent.setStyle("-fx-background-color:  #272730; -fx-background-radius: 60");
        hBoxEvent.setOnMouseClicked(mouseEvent -> mainController.gotoEvents(null));

        VBox vBoxDate = new VBox();
        vBoxDate.setPrefSize(75, 55);
        vBoxDate.setAlignment(Pos.CENTER);
        vBoxDate.setStyle("-fx-background-color:  #32323E; -fx-background-radius: 60");

        Label tagDate = new Label(e.getDateStart().toLocalDate().getDayOfMonth() + e.getDateStart().toLocalDate().getMonth().getDisplayName(TextStyle.NARROW, new Locale("es", "ES")));

        if (e.isGroup()){
            Group g = Data.groupManager.getGroupByID(e.getIdGroup());
            tagDate.setStyle("-fx-font-size: 25; -fx-font-family: 'Roboto Bold'; -fx-text-fill: " + g.getHexCode());
        }else{
            tagDate.setStyle("-fx-font-size: 25; -fx-font-family: 'Roboto Bold'; -fx-text-fill: " + Data.userData.getHexCode());
        }

        vBoxDate.getChildren().add(tagDate);
        hBoxEvent.getChildren().add(vBoxDate);

        VBox vBoxContent = new VBox();
        vBoxContent.setPrefSize(250, 30);
        vBoxContent.setAlignment(Pos.CENTER);

        Label tagContext = new Label(e.getTextEvent());
        tagContext.setStyle("-fx-text-fill: white; -fx-font-size: 20; -fx-font-family: 'Roboto'");

        Label tagTime = new Label();
        if (e.getHourStart().toString().equals("00:00:00") && e.getHourFinish().toString().equals("23:59:59")){
            tagTime = new Label("TODO EL DIA");
        }else{
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            tagTime = new Label(sdf.format(e.getHourStart()) + " - " +sdf.format(e.getHourFinish()));
        }
        tagTime.setStyle("-fx-font-size: 15; -fx-text-fill: white; -fx-font-family: 'Roboto Light'");

        vBoxContent.getChildren().add(tagContext);
        vBoxContent.getChildren().add(tagTime);
        hBoxEvent.getChildren().add(vBoxContent);

        return hBoxEvent;
    }

    private HBox taskView(Task t){

        HBox hBoxTask = new HBox();
        hBoxTask.setAlignment(Pos.CENTER_LEFT);
        hBoxTask.setPrefSize(578, 55);
        hBoxTask.setPadding(new Insets(10));
        hBoxTask.setSpacing(10);
        hBoxTask.setOnMouseClicked(mouseEvent -> mainController.gotoTasks(null));


        if (t.isGroup()){
            Schedule s = getGroupSchedule(t.getIdSchedule());
            Group g = Data.groupManager.getGroupByID(s.getIdGroup());
            hBoxTask.setStyle("-fx-background-radius: 60; -fx-background-color: " + g.getHexCode());
        }else{
            Schedule s = getSchedule(t.getIdSchedule());
            hBoxTask.setStyle("-fx-background-radius: 60; -fx-background-color: " + s.getHexCode());
        }

        Rectangle rectangle = new Rectangle(40, 40);
        rectangle.setStrokeWidth(0);
        rectangle.setArcWidth(360);
        rectangle.setArcHeight(360);

        Image image = null;
        if (t.isGroup()){
            Schedule s = getGroupSchedule(t.getIdSchedule());
            Group g = Data.groupManager.getGroupByID(s.getIdGroup());
            image = new Image(g.getAvatarGroup().getUrl(), 40, 40, true, false);
        }else{
            image = new Image(Data.userData.getAvatarUser().getUrl(), 40, 40, true, false);
        }

        ImagePattern imagePattern = new ImagePattern(image);
        rectangle.setFill(imagePattern);

        Label tagTask = new Label(t.getTextTask());
        tagTask.setStyle("-fx-text-fill: white; -fx-font-size: 18; -fx-font-family: 'Roboto Light'");

        hBoxTask.getChildren().add(rectangle);
        hBoxTask.getChildren().add(tagTask);

        return hBoxTask;

    }

    private Schedule getSchedule(int idSchedule){
        scheduleList = Data.scheduleManager.getAllSchedulesByUser(Data.userData.getIdUser());

        for (Schedule s : scheduleList) {
            if (s.getIdSchedule() == idSchedule)
                return s;
        }

        return null;

    }

    private Schedule getGroupSchedule(int idSchedule){
        scheduleList = Data.scheduleManager.getAllSchedulesByGroup(Data.groupManager.getAllGroupsForSQLQuery());

        for (Schedule s : scheduleList) {
            if (s.getIdSchedule() == idSchedule)
                return s;
        }

        return null;

    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }
}
