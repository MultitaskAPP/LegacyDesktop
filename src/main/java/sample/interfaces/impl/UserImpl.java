package sample.interfaces.impl;

import javafx.scene.image.Image;
import org.json.JSONArray;
import org.json.JSONObject;
import sample.interfaces.IUser;
import sample.models.User;
import sample.utils.ConnAPI;
import sample.utils.Data;
import sample.utils.ImageTweakerTool;

import java.awt.*;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class UserImpl implements IUser {

    @Override
    public User getUserData(JSONObject rawData) {

        User userObj = new User();
        userObj.setIdUser(rawData.getInt("idUser"));
        userObj.setPass(rawData.getString("password"));
        userObj.setName(rawData.getString("name"));
        userObj.setColourUser(Color.decode(rawData.getString("colourUser")));
        userObj.setEmail(rawData.getString("email"));
        userObj.setFirstSurname(rawData.getString("firstSurname"));
        userObj.setLastSurname(rawData.getString("lastSurname"));
        userObj.setTlf(rawData.getInt("tlf"));
        userObj.setVersionAvatar(rawData.getInt("versionAvatar"));

        if (!(rawData.isNull("address")))
            userObj.setAddress(rawData.getString("address"));

        if (!(rawData.isNull("socialMedia")))
            if (!(rawData.getString("socialMedia").equalsIgnoreCase("undefined")))
                userObj.setSocialMedia(new JSONObject(rawData.getString("socialMedia")));

        if (!(rawData.isNull("privacitySettings")))
            userObj.setPrivacitySettings(new JSONArray(rawData.getString("privacitySettings")));

        if (userObj.getVersionAvatar() == 0){
            userObj.setAvatarUser(new Image(new ImageTweakerTool(userObj.getIdUser()).getProfilePicUser()));
        }else{
            userObj.setAvatarUser(new Image(new ImageTweakerTool(userObj.getIdUser(), userObj.getVersionAvatar()).getProfilePicUser()));
        }

        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date dateParsed = simpleDateFormat.parse(rawData.getString("birthday"));
            userObj.setBirthday(new Date(dateParsed.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return userObj;
    }

    @Override
    public boolean updateAvatar(int idUser, int versionAvatar) {

        JSONObject requestJSON = new JSONObject();
        requestJSON.put("id", idUser);
        requestJSON.put("versionAvatar", versionAvatar);

        ConnAPI connAPI = new ConnAPI("/api/users/updateAvatar", "PUT", false);
        connAPI.setData(requestJSON);
        connAPI.establishConn();

        return connAPI.getStatus() == 200;

    }

    @Override
    public boolean updateColour() {

        JSONObject requestJSON = new JSONObject();
        requestJSON.put("id", Data.userData.getIdUser());
        requestJSON.put("colourUser", Data.userData.getHexCode());

        ConnAPI connAPI = new ConnAPI("/api/users/updateColour", "PUT", false);
        connAPI.setData(requestJSON);
        connAPI.establishConn();

        return connAPI.getStatus() == 200;

    }

    @Override
    public boolean deleteAccount() {

        JSONObject requestJSON = new JSONObject();
        requestJSON.put("id", Data.userData.getIdUser());

        ConnAPI connAPI = new ConnAPI("/api/users/deleteAccount", "DELETE", false);
        connAPI.setData(requestJSON);
        connAPI.establishConn();

        return connAPI.getStatus() == 200;
    }

    @Override
    public boolean updatePrivacity(JSONArray privacityJSON) {

        JSONObject requestJSON = new JSONObject();
        requestJSON.put("id", Data.userData.getIdUser());
        requestJSON.put("privacitySettings", privacityJSON);

        ConnAPI connAPI = new ConnAPI("/api/users/updatePrivacity", "PUT", false);
        connAPI.setData(requestJSON);
        connAPI.establishConn();

        return connAPI.getStatus() == 200;

    }

    @Override
    public boolean changePassword(String newPass) {

        JSONObject requestJSON = new JSONObject();
        requestJSON.put("id", Data.userData.getIdUser());
        requestJSON.put("password", newPass);

        ConnAPI connAPI = new ConnAPI("/api/users/changePassword", "PUT", false);
        connAPI.setData(requestJSON);
        connAPI.establishConn();

        return connAPI.getStatus() == 200;
    }

    @Override
    public boolean updateUser(User userData) {

        System.out.println(userData);

        JSONObject requestJSON = new JSONObject();
        requestJSON.put("id", Data.userData.getIdUser());
        requestJSON.put("data", userData.toJSONObject());

        ConnAPI connAPI = new ConnAPI("/api/users/updateUser", "PUT", false);
        connAPI.setData(requestJSON);
        connAPI.establishConn();

        if (connAPI.getStatus() == 200){
            Data.userData.setName(userData.getName());
            Data.userData.setFirstSurname(userData.getFirstSurname());
            Data.userData.setLastSurname(userData.getLastSurname());
            Data.userData.setTlf(userData.getTlf());
            Data.userData.setBirthday(userData.getBirthday());
            Data.userData.setAddress(userData.getAddress());
            Data.userData.setSocialMedia(userData.getSocialMedia());
            return true;
        }else{
            return false;
        }

    }
}
