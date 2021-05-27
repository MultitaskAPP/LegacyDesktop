package sample.interfaces;

import org.json.JSONArray;
import org.json.JSONObject;
import sample.models.User;

public interface IUser {

    public User getUserData(JSONObject rawData);

    public boolean updateAvatar(int idUser, int versionAvatar);

    public boolean updateColour();

    public boolean deleteAccount();

    public boolean updatePrivacity(JSONArray privacityJSON);

    public boolean changePassword(String newPass);

    public boolean updateUser(User userData);
}
