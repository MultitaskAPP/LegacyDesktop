package sample.tests;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import sample.utils.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Sandbox {

    public static void main(String[] args) {

        /* ArrayList<String> testIDs = new ArrayList<>();
        for (int i = 15; i < 16; i++){
            testIDs.add(Integer.toString(i));
        }

        Data.scheduleManager.getAllSchedulesByGroup(testIDs); */

       Cloudinary cloudAPI = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "multitaskapp",
                "api_key", "169861562753297",
                "api_secret", "v2xeZBtJ1egIcNRuNeawrubIhjY"
        ));

       Map<String, String> mapImage = new HashMap<>();
       mapImage.put("version", "1621880482");

        System.out.println(cloudAPI.url().secure(true).version("latest").imageTag("profilePics/users/65.jpg"));
    }

}
