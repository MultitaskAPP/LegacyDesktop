package sample.utils;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import sample.interfaces.impl.GroupImpl;
import sample.interfaces.impl.ScheduleImpl;
import sample.interfaces.impl.TaskImpl;
import sample.interfaces.impl.UserImpl;
import sample.models.Group;
import sample.models.User;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;

public class Data {

    public static Properties properties = new Properties();

    public static String API_URL = "https://multitaskapp.herokuapp.com";
    public static String LOCALHOST = "http://localhost:5000";
    public static SimpleDateFormat format = new SimpleDateFormat("yyyy MM dd kk:mm:ss zzz", Locale.US);       // Formato DATES de MongoDB


    public static User userData;
    public static ArrayList<Group> arrayGroupsUser = new ArrayList<>();

    public static Date todayDate;
    public static Cloudinary cloudAPI = new Cloudinary(ObjectUtils.asMap(
            "cloud_name", "multitaskapp",
            "api_key", "169861562753297",
            "api_secret", "v2xeZBtJ1egIcNRuNeawrubIhjY"
    ));

    public static UserImpl userManager = new UserImpl();
    public static GroupImpl groupManager = new GroupImpl();
    public static ScheduleImpl scheduleManager = new ScheduleImpl();
    public static TaskImpl taskManager = new TaskImpl();

    public static void storeProperties(Properties properties){
        try(OutputStream outputStream = new FileOutputStream("config.properties")){
            properties.store(outputStream, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
