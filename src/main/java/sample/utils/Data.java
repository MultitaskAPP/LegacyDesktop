package sample.utils;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import java.util.Date;

public class Data {

    public static Date todayDate;
    public static Cloudinary cloudAPI = new Cloudinary(ObjectUtils.asMap(
            "cloud_name", "multitaskapp",
            "api_key", "169861562753297",
            "api_secret", "v2xeZBtJ1egIcNRuNeawrubIhjY"
    ));


}
