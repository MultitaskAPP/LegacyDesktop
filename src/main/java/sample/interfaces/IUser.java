package sample.interfaces;

import org.json.JSONObject;
import sample.models.User;

public interface IUser {

    public User getUserData(JSONObject rawData);

    public boolean updateAvatar(int idUser, int versionAvatar);

    public boolean updateColour();
}
