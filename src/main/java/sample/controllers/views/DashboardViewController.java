package sample.controllers.views;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import sample.utils.Data;
import sample.utils.ImageTweakerTool;

import java.io.File;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

public class DashboardViewController implements Initializable {

    @FXML    private Label tvDay;
    @FXML    private Label tvMonth;
    @FXML    private Rectangle rectangle;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        getData();


    }

    private void getData(){

        // Llamadas a la API para obtener datos del servidor.

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd");
        Date date = new Date();
        tvDay.setText(dateFormat.format(date));

        dateFormat = new SimpleDateFormat("MMM");
        tvMonth.setText(dateFormat.format(date).toUpperCase());

        Image image = new Image(new ImageTweakerTool(Data.userData.getIdUser()).getProfilePic(), rectangle.getWidth(), rectangle.getHeight(), false, true);
        ImagePattern imagePattern = new ImagePattern(image);
        rectangle.setFill(imagePattern);


    }
}
