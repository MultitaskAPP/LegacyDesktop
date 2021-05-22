package sample.interfaces.impl;

import javafx.scene.image.Image;
import org.json.JSONArray;
import org.json.JSONObject;
import sample.interfaces.IGroup;
import sample.models.Group;
import sample.models.User;
import sample.utils.ConnAPI;
import sample.utils.Data;
import sample.utils.ImageTweakerTool;

import java.awt.*;
import java.util.ArrayList;

public class GroupImpl implements IGroup {

    @Override
    public ArrayList<Group> getAllGroups(int userID) {

        ArrayList<Group> arrayGroups = new ArrayList<>();

        JSONObject requestJSON = new JSONObject();
        requestJSON.put("id", userID);

        ConnAPI connAPI = new ConnAPI("/api/groups/getAllUserGroups", "POST", false);
        connAPI.setData(requestJSON);
        connAPI.establishConn();

        int status = connAPI.getStatus();
        if (status == 200){
            JSONObject responseJSON = connAPI.getDataJSON();
            JSONArray arrayData = responseJSON.getJSONArray("data");
            for (int i = 0; i < arrayData.length(); i++){
                JSONObject rawData = arrayData.getJSONObject(i);
                Group group = new Group();
                group.setIdGroup(rawData.getInt("idGroup"));
                group.setNameGroup(rawData.getString("nameGroup"));
                group.setDescriptionGroup(rawData.getString("descGroup"));
                group.setOwnerUser(rawData.getInt("ownerUser"));
                group.setColourGroup(Color.decode(rawData.getString("colourGroup")));
                group.setAvatarGroup(new Image(new ImageTweakerTool(group.getIdGroup()).getProfilePicGroup()));

                ArrayList<User> arrayUsers = new ArrayList<>();
                JSONArray arrayDataUsers = rawData.getJSONArray("users");
                for (int j = 0; j < arrayDataUsers.length(); j++){
                    JSONObject rawUser = arrayDataUsers.getJSONObject(j);
                    User user = new User();
                    user.setIdUser(rawUser.getInt("idUser"));
                    user.setEmail(rawUser.getString("email"));
                    user.setName(rawUser.getString("name"));
                    user.setFirstSurname(rawUser.getString("firstSurname"));
                    user.setLastSurname(rawUser.getString("lastSurname"));

                    arrayUsers.add(user);

                }
                group.setArrayUsersGroup(arrayUsers);
                arrayGroups.add(group);
            }
        }

        return arrayGroups;
    }

    @Override
    public Group getGroupByID(int idGroup) {
        for (Group g : Data.arrayGroupsUser) {
            if (g.getIdGroup() == idGroup)
                return g;
        }

        return null;
    }

    @Override
    public ArrayList<String> getAllGroupsForSQLQuery() {

        ArrayList<String> arrayGroupIDs = new ArrayList<>();
        for (Group g : Data.arrayGroupsUser) {
            arrayGroupIDs.add(Integer.toString(g.getIdGroup()));
        }

        return arrayGroupIDs;
    }
}
