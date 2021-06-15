package sample.tests;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.sun.javafx.scene.control.CustomColorDialog;
import javafx.stage.Stage;
import sample.models.Notification;
import sample.utils.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Sandbox {

    public static void main(String[] args) {

        Notification notification = new Notification("Esta es una notificacion de prueba", false, 0);
        Data.notificationManager.showNotification(notification);

    }

}
