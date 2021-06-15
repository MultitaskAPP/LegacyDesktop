package sample.controllers.views;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconName;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.skin.DatePickerSkin;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import sample.controllers.dialogs.EventDialogController;
import sample.models.Event;
import sample.models.Group;
import sample.utils.Data;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.MonthDay;
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
    private LocalDate selectedDate;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        datePicker.setShowWeekNumbers(false);
        DatePickerSkin datePickerSkin = new DatePickerSkin(datePicker);
        Node popupContent = datePickerSkin.getPopupContent();
        paneCalendar.getChildren().add(popupContent);
        datePickerSkin.getPopupContent().setOnMouseClicked(mouseEvent -> getThisDayEvents(datePickerSkin.getSkinnable().getValue()));

        getEvents();
        getThisDayEvents(LocalDate.now());
    }

    public void getEvents(){

        eventList.clear();
        paneCalendar.getChildren().clear();

        ArrayList<String> arrayGroupIDs = new ArrayList<>();
        for (Group g : Data.arrayGroupsUser) {
            arrayGroupIDs.add(Integer.toString(g.getIdGroup()));
        }

        List<Event> eventsUser = Data.eventManager.getAllEventsByUser(Data.userData.getIdUser());
        List<Event> eventsGroup = Data.eventManager.getAllEventsByGroup(arrayGroupIDs);

        eventList.addAll(eventsUser);
        eventList.addAll(eventsGroup);

        Collections.sort(eventList, Comparator.comparing(Event::isGroup));

        datePicker.setDayCellFactory(getMarkedEvents(eventList));

        DatePickerSkin datePickerSkin = new DatePickerSkin(datePicker);
        Node popupContent = datePickerSkin.getPopupContent();
        paneCalendar.getChildren().add(popupContent);
        datePickerSkin.getPopupContent().setOnMouseClicked(mouseEvent -> {
            selectedDate = datePickerSkin.getSkinnable().getValue();;
            getThisDayEvents(selectedDate);
        });

        if (selectedDate != null)
            getThisDayEvents(selectedDate);

    }

    private void getThisDayEvents(LocalDate selectedDate){

        vBoxEvents.getChildren().clear();
        hBoxPages.getChildren().clear();

        tagDayNumber.setText(Integer.toString(selectedDate.getDayOfMonth()));
        tagDayText.setText(selectedDate.getDayOfWeek().getDisplayName(TextStyle.FULL, new Locale("es", "ES")).toUpperCase());

        List<Event> thisDayEvents = new ArrayList<>();
        for (Event e : eventList) {
            if (e.getDateStart().toLocalDate().equals(selectedDate))
                thisDayEvents.add(e);
            else if(selectedDate.isAfter(e.getDateStart().toLocalDate()) && selectedDate.isBefore(e.getDateFinish().toLocalDate().plusDays(1)))
                thisDayEvents.add(e);
        }

        totalPages = thisDayEvents.size() / 2;


        if (totalPages != 0){
            for (int i = 0; i <= totalPages; i++){
                int finalI = i;
                FontAwesomeIcon icon = new FontAwesomeIcon();
                icon.setIcon(FontAwesomeIconName.CIRCLE);
                icon.setFill(Color.GRAY);
                icon.setSize("20px");
                icon.setOnMouseClicked(mouseEvent -> viewEvents(icon, finalI, thisDayEvents));
                hBoxPages.getChildren().add(icon);
            }
            FontAwesomeIcon icon = (FontAwesomeIcon) hBoxPages.getChildren().get(0);
            icon.setFill(Color.WHITE);
        }

        if (thisDayEvents.size() == 0){
            vBoxEvents.getChildren().add(createAddEventButton());
        }else{
            vBoxEvents.getChildren().add(createAddEventButton());
            vBoxEvents.getChildren().add(createEventView(thisDayEvents.get(0)));
        }

    }

    private void viewEvents(FontAwesomeIcon selectedPage, int page, List<Event> thisDayEvents){

        updatePages(selectedPage);
        vBoxEvents.getChildren().clear();

        if (page == 0){
            vBoxEvents.getChildren().add(createAddEventButton());
            vBoxEvents.getChildren().add(createEventView(thisDayEvents.get(0)));
        }else{
            for (int i = (page * 2) - 1; i < (page * 2) + 1; i++){
                if (thisDayEvents.size() > i){
                    vBoxEvents.getChildren().add(createEventView(thisDayEvents.get(i)));
                }
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
        Image avatarImage;

        if (e.isGroup()){
            Group g = Data.groupManager.getGroupByID(e.getIdGroup());
            avatarImage = new Image(g.getAvatarGroup().getUrl(), rectangle.getWidth(), rectangle.getHeight(), false, true);
        }else{
            avatarImage = new Image(Data.userData.getAvatarUser().getUrl(), rectangle.getWidth(), rectangle.getHeight(), false, true);
        }

        ImagePattern imagePattern = new ImagePattern(avatarImage);
        rectangle.setFill(imagePattern);

        Label tagTitle = new Label();

        if (e.getDateStart().equals(e.getDateFinish()))
            tagTitle.setText(e.getDateStart().toLocalDate().getDayOfMonth() + " " + e.getDateStart().toLocalDate().getMonth().getDisplayName(TextStyle.SHORT, new Locale("es", "ES")).toUpperCase());
        else{
            if (e.getDateStart().toLocalDate().getMonth().equals(e.getDateFinish().toLocalDate().getMonth()))
                tagTitle.setText(e.getDateStart().toLocalDate().getDayOfMonth() + "-" + e.getDateFinish().toLocalDate().getDayOfMonth() + " " + e.getDateStart().toLocalDate().getMonth().getDisplayName(TextStyle.SHORT, new Locale("es", "ES")).toUpperCase());
            else
                tagTitle.setText(e.getDateStart().toLocalDate().getDayOfMonth() + "-" + e.getDateFinish().toLocalDate().getDayOfMonth() + " "
                        + e.getDateStart().toLocalDate().getMonth().getDisplayName(TextStyle.NARROW, new Locale("es", "ES")).toUpperCase() + "-"
                        + e.getDateFinish().toLocalDate().getMonth().getDisplayName(TextStyle.NARROW, new Locale("es", "ES")).toUpperCase());
        }

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

        ImageView ivTime = new ImageView(new Image("windows/res/icons/mt_time_icon.png"));
        ivTime.setFitHeight(40);
        ivTime.setFitWidth(40);

        Label tagTime = null;

        if (e.getHourStart().toString().equals("00:00:00") && e.getHourFinish().toString().equals("23:59:59")){
            tagTime = new Label("TODO EL DIA");
        }else{
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            tagTime = new Label(sdf.format(e.getHourStart()) + " - " +sdf.format(e.getHourFinish()));
        }

        tagTime.setStyle("-fx-text-fill: white; -fx-font-size: 20; -fx-font-weight: bold");

        hBoxTime.getChildren().add(ivTime);
        hBoxTime.getChildren().add(tagTime);
        vBoxEvent.getChildren().add(hBoxTime);

        MenuItem miView = new MenuItem("Visualizar");
        miView.setOnAction(actionEvent -> viewEvent(e));
        MenuItem miEdit = new MenuItem("Editar");
        miEdit.setOnAction(actionEvent -> updateEvent(e));
        MenuItem miDelete = new MenuItem("Borrar");
        miDelete.setOnAction(actionEvent -> deleteEvent(e));
        ContextMenu contextMenu = new ContextMenu(miView, miEdit, miDelete);
        vBoxEvent.setOnContextMenuRequested(contextMenuEvent -> contextMenu.show(vBoxEvent, Side.BOTTOM, vBoxEvent.getWidth() - 25, 0));

        return vBoxEvent;

    }

    private VBox createAddEventButton(){

        VBox hBoxAddEvent = new VBox();
        hBoxAddEvent.setAlignment(Pos.CENTER);
        hBoxAddEvent.setStyle("-fx-background-color: #272730; -fx-background-radius: 30");
        hBoxAddEvent.setPrefSize(275, 200);

        ImageView imageView = new ImageView(new Image("windows/res/icons/mt_add1_icon.png"));
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
        try {
            Stage stage = new Stage();
            // URL url = new File("src/main/java/sample/windows/dialogs/eventDialog.fxml").toURI().toURL();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("windows/dialogs/eventDialog.fxml"));
            Parent root = loader.load();
            EventDialogController eventDialogController = loader.getController();
            eventDialogController.setSelectedDate(selectedDate);
            eventDialogController.setEventViewController(this);
            eventDialogController.preloadData();
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

    private void viewEvent(Event e){
        try {
            Stage stage = new Stage();
            // URL url = new File("src/main/java/sample/windows/dialogs/eventDialog.fxml").toURI().toURL();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("windows/dialogs/eventDialog.fxml"));
            Parent root = loader.load();
            EventDialogController eventDialogController = loader.getController();
            eventDialogController.setEventViewController(this);
            eventDialogController.setUpdateMode(false);
            eventDialogController.setSelectedEvent(e);
            eventDialogController.setGroup(e.isGroup());
            eventDialogController.preloadData();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.initStyle(StageStyle.TRANSPARENT);
            scene.setFill(Color.TRANSPARENT);
            Data.setBlur();
            stage.show();
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void updateEvent(Event e){
        try {
            Stage stage = new Stage();
            // URL url = new File("src/main/java/sample/windows/dialogs/eventDialog.fxml").toURI().toURL();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("windows/dialogs/eventDialog.fxml"));
            Parent root = loader.load();
            EventDialogController eventDialogController = loader.getController();
            eventDialogController.setEventViewController(this);
            eventDialogController.setUpdateMode(true);
            eventDialogController.setSelectedEvent(e);
            eventDialogController.setGroup(e.isGroup());
            eventDialogController.preloadData();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.initStyle(StageStyle.TRANSPARENT);
            scene.setFill(Color.TRANSPARENT);
            Data.setBlur();
            stage.show();
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void deleteEvent(Event e){

        boolean success = false;
        if (e.isGroup())
            success = Data.eventManager.deleteGroupEvent(e);
        else
            success = Data.eventManager.deleteEvent(e);

        if (success){
            System.out.println("[DEBUG] - EVENT eliminado correctamente!");
            getEvents();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("MultitaskAPP | DESKTOP");
            alert.setHeaderText("Evento eliminado correctamente!");
            Data.setBlur();
            alert.showAndWait();
            Data.removeBlur();
        }else {
            System.out.println("[DEBUG] - Error al eliminar el EVENT...");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("MultitaskAPP | DESKTOP");
            alert.setHeaderText("Error al eliminar el evento...");
            Data.setBlur();
            alert.showAndWait();
            Data.removeBlur();
        }
    }

    private void updatePages(FontAwesomeIcon selectedPage){
        for (int i = 0; i < hBoxPages.getChildren().size(); i++){
            FontAwesomeIcon icon = (FontAwesomeIcon) hBoxPages.getChildren().get(i);
            icon.setFill(Color.GRAY);
            selectedPage.setFill(Color.WHITE);
        }
    }

    public void setSelectedDate(LocalDate selectedDate) {
        this.selectedDate = selectedDate;
    }

    public Callback<DatePicker, DateCell> getMarkedEvents(List<Event> listEvents){

        final Callback<DatePicker, DateCell> dayCellFactory = new Callback<DatePicker, DateCell>() {
            public DateCell call(final DatePicker datePicker) {
                return new DateCell() {
                    @Override public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                        for (Event e : listEvents) {
                            if (e.getTypeEvent() == 0){
                                if (item.equals(e.getDateStart().toLocalDate())) {
                                    if (e.isGroup()) {
                                        Group g = Data.groupManager.getGroupByID(e.getIdGroup());
                                        setStyle("-fx-background-color: " + g.getHexCode());
                                    } else {
                                        setStyle("-fx-background-color: " + Data.userData.getHexCode());
                                    }
                                } else if(item.isAfter(e.getDateStart().toLocalDate()) && item.isBefore(e.getDateFinish().toLocalDate().plusDays(1))){
                                    if (e.isGroup()) {
                                        Group g = Data.groupManager.getGroupByID(e.getIdGroup());
                                        setStyle("-fx-background-color: " + g.getHexCode());
                                    } else {
                                        setStyle("-fx-background-color: " + Data.userData.getHexCode());
                                    }
                                }
                            }else if (e.getTypeEvent() == 1){
                                if (item.getDayOfWeek().equals(e.getDateStart().toLocalDate().getDayOfWeek())){
                                    if (e.isGroup()){
                                        Group g = Data.groupManager.getGroupByID(e.getIdGroup());
                                        setStyle("-fx-background-color: " + g.getHexCode());
                                    }else{
                                        setStyle("-fx-background-color: " + Data.userData.getHexCode());
                                    }
                                } else if(item.isAfter(e.getDateStart().toLocalDate()) && item.isBefore(e.getDateFinish().toLocalDate().plusDays(1))){
                                    if (e.isGroup()) {
                                        Group g = Data.groupManager.getGroupByID(e.getIdGroup());
                                        setStyle("-fx-background-color: " + g.getHexCode());
                                    } else {
                                        setStyle("-fx-background-color: " + Data.userData.getHexCode());
                                    }
                                }
                            }else if (e.getTypeEvent() == 2){
                                if (item.getDayOfMonth() == e.getDateStart().toLocalDate().getDayOfMonth()){
                                    if (e.isGroup()){
                                        Group g = Data.groupManager.getGroupByID(e.getIdGroup());
                                        setStyle("-fx-background-color: " + g.getHexCode());
                                    }else{
                                        setStyle("-fx-background-color: " + Data.userData.getHexCode());
                                    }
                                } else if(item.isAfter(e.getDateStart().toLocalDate()) && item.isBefore(e.getDateFinish().toLocalDate().plusDays(1))){
                                    if (e.isGroup()) {
                                        Group g = Data.groupManager.getGroupByID(e.getIdGroup());
                                        setStyle("-fx-background-color: " + g.getHexCode());
                                    } else {
                                        setStyle("-fx-background-color: " + Data.userData.getHexCode());
                                    }
                                }
                            }else if (e.getTypeEvent() == 3){
                                if (MonthDay.from(item).equals(MonthDay.of(e.getDateStart().toLocalDate().getMonth(), e.getDateStart().toLocalDate().getDayOfMonth()))){
                                    if (e.isGroup()){
                                        Group g = Data.groupManager.getGroupByID(e.getIdGroup());
                                        setStyle("-fx-background-color: " + g.getHexCode());
                                    }else{
                                        setStyle("-fx-background-color: " + Data.userData.getHexCode());
                                    }
                                } else if(item.isAfter(e.getDateStart().toLocalDate()) && item.isBefore(e.getDateFinish().toLocalDate().plusDays(1))){
                                    if (e.isGroup()) {
                                        Group g = Data.groupManager.getGroupByID(e.getIdGroup());
                                        setStyle("-fx-background-color: " + g.getHexCode());
                                    } else {
                                        setStyle("-fx-background-color: " + Data.userData.getHexCode());
                                    }
                                }
                            }

                        }
                    }
                };
            }
        };

        return dayCellFactory;
    }
}
