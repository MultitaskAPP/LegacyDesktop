package sample.controllers.views;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.skin.DatePickerSkin;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import sample.utils.Data;
import sample.utils.ImageTweakerTool;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.ResourceBundle;

public class DashboardViewController implements Initializable {

    @FXML    private Label tvDay, tvMonth, tagSummary;
    @FXML    private VBox paneCalendar;

    private DatePicker datePicker = new DatePicker(LocalDate.now());

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        preloadData();

    }

    private void preloadData(){

        datePicker.setShowWeekNumbers(false);
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

}
