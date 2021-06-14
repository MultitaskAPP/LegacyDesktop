package sample.utils;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import javafx.fxml.FXMLLoader;
import javafx.scene.effect.BoxBlur;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import sample.interfaces.impl.*;
import sample.models.Contact;
import sample.models.Group;
import sample.models.Notification;
import sample.models.User;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

public class Data {

    public static Properties properties = new Properties();
    public static Stage mainStage;

    public static String API_URL = "https://multitaskapp.herokuapp.com";
    public static String LOCALHOST = "http://localhost:5000";
    public static SimpleDateFormat format = new SimpleDateFormat("yyyy MM dd kk:mm:ss zzz", Locale.US);       // Formato DATES de MongoDB

    public static User userData;
    public static ArrayList<Group> arrayGroupsUser = new ArrayList<>();
    public static List<Contact> contactList = new ArrayList<>();

    public static Cloudinary cloudAPI = new Cloudinary(ObjectUtils.asMap(
            "cloud_name", "multitaskapp",
            "api_key", "169861562753297",
            "api_secret", "v2xeZBtJ1egIcNRuNeawrubIhjY"
    ));

    public static UserImpl userManager = new UserImpl();
    public static GroupImpl groupManager = new GroupImpl();
    public static ScheduleImpl scheduleManager = new ScheduleImpl();
    public static TaskImpl taskManager = new TaskImpl();
    public static NoteImpl noteManager = new NoteImpl();
    public static EventImpl eventManager = new EventImpl();
    public static ContactImpl contactManager = new ContactImpl();
    public static NotificationImpl notificationManager = new NotificationImpl();

    public static void storeProperties(Properties properties){
        try(OutputStream outputStream = new FileOutputStream("config.properties")){
            properties.store(outputStream, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setBlur(){
        BoxBlur boxBlur = new BoxBlur();
        boxBlur.setIterations(1000);
        mainStage.getScene().getRoot().setEffect(boxBlur);
        mainStage.getScene().getRoot().applyCss();
        mainStage.getScene().getRoot().setDisable(true);
    }

    public static void removeBlur(){
        mainStage.getScene().getRoot().setEffect(null);
        mainStage.getScene().getRoot().setDisable(false);
    }

}
