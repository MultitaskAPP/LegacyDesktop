package sample.interfaces.impl;

import org.json.JSONObject;
import sample.interfaces.IUser;
import sample.models.User;
import java.util.Date;

public class UserImpl implements IUser {

    @Override
    public User getUserData(JSONObject rawData) {

        User userObj = new User();
        userObj.setIdUser(rawData.getInt("idUser"));
        userObj.setBirthday(new Date());
        userObj.setPass(rawData.getString("password"));
        userObj.setName(rawData.getString("name"));
        userObj.setColourUser(rawData.getString("colourUser"));
        userObj.setEmail(rawData.getString("email"));
        userObj.setFirstSurname(rawData.getString("firstSurname"));
        userObj.setLastSurname(rawData.getString("lastSurname"));
        userObj.setTlf(rawData.getInt("tlf"));

        return userObj;
    }
}
