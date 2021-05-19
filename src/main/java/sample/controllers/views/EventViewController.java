package sample.controllers.views;

import de.jensd.fx.glyphs.GlyphIcon;
import de.jensd.fx.glyphs.GlyphIconName;
import de.jensd.fx.glyphs.GlyphsStyle;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconName;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import sample.models.Event;
import sample.models.Group;
import sample.utils.Data;
import java.io.File;
import java.net.URL;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;

public class EventViewController implements Initializable {

    @FXML    private Label tagDayNumber, tagDayText;
    @FXML    private VBox vBoxEvents;
    @FXML    private HBox hBoxPages;
    @FXML    private AnchorPane paneCalendar;

    private List<Event> eventList = new ArrayList<>();
    private DatePicker datePicker = new DatePicker(LocalDate.now());
    private int totalPages;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        preloadCalendar();
    }

    private void preloadCalendar(){

        tagDayNumber.setText(Integer.toString(datePicker.getValue().getDayOfMonth()));
        tagDayText.setText(datePicker.getValue().getDayOfWeek().getDisplayName(TextStyle.FULL, new Locale("es", "ES")).toUpperCase());

        getEvents();
        getThisDayEvents(LocalDate.of(2021, 5, 19));

    }

    private void getEvents(){

        eventList.clear();

        ArrayList<String> arrayGroupIDs = new ArrayList<>();
        for (Group g : Data.arrayGroupsUser) {
            arrayGroupIDs.add(Integer.toString(g.getIdGroup()));
        }

        List<Event> eventsUser = Data.eventManager.getAllEventsByUser(Data.userData.getIdUser());
        // List<Event> eventsGroup = Data.eventManager.getAllEventsByGroup(arrayGroupIDs);

        eventList.addAll(eventsUser);
        // eventList.addAll(eventsGroup);

        Collections.sort(eventList, Comparator.comparing(Event::getDateEvent).reversed());

    }

    private void getThisDayEvents(LocalDate selectedDate){

        List<Event> thisDayEvents = new ArrayList<>();
        for (Event e : eventList) {
            if (e.getDateEvent().toLocalDate().equals(selectedDate))
                thisDayEvents.add(e);
        }

        totalPages = thisDayEvents.size() / 2;


        if (totalPages != 0){
            for (int i = 0; i <= totalPages; i++){
                int finalI = i;
                FontAwesomeIcon icon = new FontAwesomeIcon();
                icon.setIcon(FontAwesomeIconName.CIRCLE);
                icon.setFill(Color.WHITE);
                icon.setSize("20px");
                icon.setOnMouseClicked(mouseEvent -> viewEvents(finalI, thisDayEvents));
                hBoxPages.getChildren().add(icon);
            }
        }

        if (thisDayEvents.size() == 0){
            vBoxEvents.getChildren().add(createAddEventButton());
        }else{
            vBoxEvents.getChildren().add(createAddEventButton());
            vBoxEvents.getChildren().add(createEventView(thisDayEvents.get(0)));
        }

    }

    private void viewEvents(int page, List<Event> thisDayEvents){

        vBoxEvents.getChildren().clear();

        if (page == 0){
            vBoxEvents.getChildren().add(createAddEventButton());
            vBoxEvents.getChildren().add(createEventView(thisDayEvents.get(0)));
        }else{
            for (int i = (page * 2) - 1; i < (page * 2) + 1; i++){
                System.out.println(i);
                vBoxEvents.getChildren().add(createEventView(thisDayEvents.get(i)));
            }
        }


    }

    private VBox createEventView(Event e){

        VBox vBoxEvent = new VBox();
        vBoxEvent.setPrefSize(275, 200);
        vBoxEvent.setPadding(new Insets(15));
        vBoxEvent.setSpacing(5);

        HBox hBoxTitle = new HBox();
        hBoxTitle.setPrefSize(200, 50);
        hBoxTitle.setAlignment(Pos.CENTER_LEFT);
        hBoxTitle.setSpacing(15);

        Rectangle rectangle = new Rectangle(50, 50);
        rectangle.setStrokeWidth(0);
        rectangle.setArcWidth(30);
        rectangle.setArcHeight(30);
        Image avatarImage = new Image(Data.userData.getAvatarUser().getUrl(), rectangle.getWidth(), rectangle.getHeight(), false, true);
        ImagePattern imagePattern = new ImagePattern(avatarImage);
        rectangle.setFill(imagePattern);

        Label tagTitle = new Label(e.getDateEvent().toLocalDate().getDayOfMonth() + " " + e.getDateEvent().toLocalDate().getMonth().getDisplayName(TextStyle.SHORT, new Locale("es", "ES")).toUpperCase());
        tagTitle.setStyle("-fx-font-size: 30; -fx-text-fill: white; -fx-font-weight: bold");

        hBoxTitle.getChildren().add(rectangle);
        hBoxTitle.getChildren().add(tagTitle);
        vBoxEvent.getChildren().add(hBoxTitle);

        TextArea textArea = new TextArea(e.getTextEvent());
        textArea.setStyle("-fx-background-color: transparent; -fx-font-size: 15");
        textArea.setPrefSize(275, 70);
        textArea.setWrapText(true);
        textArea.setEditable(false);

        vBoxEvent.getChildren().add(textArea);

        if (e.isGroup()){
            Group g = Data.groupManager.getGroupByID(e.getIdGroup());
            vBoxEvent.setStyle("-fx-background-radius: 30; -fx-background-color: " + g.getHexCode());
        }else{
            vBoxEvent.setStyle("-fx-background-radius: 30; -fx-background-color: " + Data.userData.getHexCode());
        }

        HBox hBoxTime = new HBox();
        hBoxTime.setAlignment(Pos.CENTER);
        hBoxTime.setPrefSize(200, 40);
        hBoxTime.setSpacing(5);

        ImageView ivTime = new ImageView(new Image(new File("src/main/java/sample/windows/res/mt_time_icon.png").toURI().toString()));
        ivTime.setFitHeight(40);
        ivTime.setFitWidth(40);

        Label tagTime = null;

        if (e.getHourStart().toString().equals("00:00:00") && e.getHourFinish().toString().equals("23:59:59")){
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            tagTime = new Label(sdf.format(e.getHourStart()) + " - " +sdf.format(e.getHourFinish()));
        }else{
            tagTime = new Label("TODO EL DIA");

        }

        tagTime.setStyle("-fx-text-fill: white; -fx-font-size: 20; -fx-font-weight: bold");

        hBoxTime.getChildren().add(ivTime);
        hBoxTime.getChildren().add(tagTime);
        vBoxEvent.getChildren().add(hBoxTime);

        return vBoxEvent;

    }

    private VBox createAddEventButton(){

        VBox hBoxAddEvent = new VBox();
        hBoxAddEvent.setAlignment(Pos.CENTER);
        hBoxAddEvent.setStyle("-fx-background-color: #272730; -fx-background-radius: 30");
        hBoxAddEvent.setPrefSize(275, 200);

        ImageView imageView = new ImageView(new Image(new File("src/main/java/sample/windows/res/mt_add1_icon.png").toURI().toString()));
        imageView.setFitWidth(50);
        imageView.setFitHeight(50);

        Label tagAddEvent = new Label("Nuevo evento");
        tagAddEvent.setStyle("-fx-text-fill: white; -fx-font-size: 20");

        hBoxAddEvent.getChildren().add(imageView);
        hBoxAddEvent.getChildren().add(tagAddEvent);
        hBoxAddEvent.setOnMouseClicked(mouseEvent -> addEvent());

        return hBoxAddEvent;
    }

    private void addEvent(){

    }

    private void deleteEvent(){

    }
}
