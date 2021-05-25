package sample.interfaces.impl;

import javafx.scene.image.Image;
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
}
