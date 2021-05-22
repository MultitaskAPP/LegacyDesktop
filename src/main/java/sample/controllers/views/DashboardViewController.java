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
import sample.models.Event;
import sample.models.Group;
import sample.utils.Data;
import sample.utils.ImageTweakerTool;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.*;

public class DashboardViewController implements Initializable {

    @FXML    private Label tvDay, tvMonth, tagSummary;
    @FXML    private VBox paneCalendar, vBoxEvents;

    private List<Event> eventList = new ArrayList<>();

    private DatePicker datePicker = new DatePicker(LocalDate.now());

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        preloadData();

    }

    private void preloadData(){

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

    }

    public void loadCalendar(){

        datePicker.setShowWeekNumbers(false);

        ArrayList<String> arrayGroupIDs = new ArrayList<>();
        for (Group g : Data.arrayGroupsUser) {
            arrayGroupIDs.add(Integer.toString(g.getIdGroup()));
        }

        List<Event> eventsUser = Data.eventManager.getAllEventsByUser(Data.userData.getIdUser());
        List<Event> eventsGroup = Data.eventManager.getAllEventsByGroup(arrayGroupIDs);

        eventList.addAll(eventsUser);
        eventList.addAll(eventsGroup);

        Collections.sort(eventList, Comparator.comparing(Event::getDateStart));
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
                hBoxMoreEvents.setStyle("-fx-background-color:  #32323E; -fx-background-radius: 30");

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
            hBoxNoEvents.setStyle("-fx-background-color:  #32323E; -fx-background-radius: 30");

            Label tagNoEvents = new Label("No hay eventos para los proximos 30 dias");
            tagNoEvents.setStyle("-fx-text-fill: white; -fx-font-size: 15; -fx-font-family: 'Roboto Light'");

            hBoxNoEvents.getChildren().add(tagNoEvents);
            vBoxEvents.getChildren().add(hBoxNoEvents);

        }
    }

    private HBox eventView(Event e){

        HBox hBoxEvent = new HBox();
        hBoxEvent.setPrefSize(330, 75);
        hBoxEvent.setPadding(new Insets(10));
        hBoxEvent.setSpacing(10);
        hBoxEvent.setStyle("-fx-background-color:  #272730; -fx-background-radius: 30");

        VBox vBoxDate = new VBox();
        vBoxDate.setPrefSize(75, 55);
        vBoxDate.setAlignment(Pos.CENTER);
        vBoxDate.setStyle("-fx-background-color:  #32323E; -fx-background-radius: 20");

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

}
