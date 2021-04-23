package sample.utils;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import sample.interfaces.impl.UserImpl;
import sample.models.User;

import java.security.PublicKey;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Data {

    public static String API_URL = "https://multitaskapp.herokuapp.com";
    public static String LOCALHOST = "http://localhost:5000";
    public static SimpleDateFormat format = new SimpleDateFormat("yyyy MM dd kk:mm:ss zzz", Locale.US);       // Formato DATES de MongoDB


    public static User userData;

    public static Date todayDate;
    public static Cloudinary cloudAPI = new Cloudinary(ObjectUtils.asMap(
            "cloud_name", "multitaskapp",
            "api_key", "169861562753297",
            "api_secret", "v2xeZBtJ1egIcNRuNeawrubIhjY"
    ));

    public static UserImpl userManager = new UserImpl();


}
