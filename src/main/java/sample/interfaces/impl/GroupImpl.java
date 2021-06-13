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
import java.util.HashMap;
import java.util.List;

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
                group.setVersionAvatar(rawData.getInt("versionAvatar"));

                if (group.getVersionAvatar() == 0){
                    group.setAvatarGroup(new Image(new ImageTweakerTool(group.getIdGroup()).getProfilePicGroup()));
                }else{
                    group.setAvatarGroup(new Image(new ImageTweakerTool(group.getIdGroup(), group.getVersionAvatar()).getProfilePicGroup()));
                }

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
                    user.setVersionAvatar(rawUser.getInt("versionAvatar"));

                    if (user.getVersionAvatar() == 0){
                        user.setAvatarUser(new Image(new ImageTweakerTool(user.getIdUser()).getProfilePicUser()));
                    }else{
                        user.setAvatarUser(new Image(new ImageTweakerTool(user.getIdUser(), user.getVersionAvatar()).getProfilePicUser()));
                    }

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

    @Override
    public List<Group> getAllGroupRequests() {

        List<Group> groupRequestsList = new ArrayList<>();

        JSONObject requestJSON = new JSONObject();
        requestJSON.put("userID", Data.userData.getIdUser());

        ConnAPI connAPI = new ConnAPI("/api/groups/getUserRequests", "POST", false);
        connAPI.setData(requestJSON);
        connAPI.establishConn();

        if (connAPI.getStatus() == 200){
            JSONObject responseJSON = connAPI.getDataJSON();
            JSONArray arrayJSON = responseJSON.getJSONArray("data");
            for (int i = 0; i < arrayJSON.length(); i++){
                JSONObject rawJSON = arrayJSON.getJSONObject(i);
                Group group = new Group();
                group.setIdGroup(rawJSON.getInt("idGroup"));
                group.setNameGroup(rawJSON.getString("nameGroup"));
                group.setDescriptionGroup(rawJSON.getString("descGroup"));
                group.setOwnerUser(rawJSON.getInt("ownerUser"));
                group.setColourGroup(Color.decode(rawJSON.getString("colourGroup")));
                group.setAvatarGroup(new Image(new ImageTweakerTool(group.getIdGroup()).getProfilePicGroup()));
                groupRequestsList.add(group);
            }
        }

        return groupRequestsList;
    }

    @Override
    public Group createGroup(Group g) {

        JSONObject requestJSON = new JSONObject();
        requestJSON.put("data", g.toJSONObject());

        ConnAPI connAPI = new ConnAPI("/api/groups/createGroup", "POST", false);
        connAPI.setData(requestJSON);
        connAPI.establishConn();

        if (connAPI.getStatus() == 200){
            JSONObject responseJSON = connAPI.getDataJSON();
            g.setIdGroup(responseJSON.getInt("data"));
            return g;
        }

        return null;

    }

    @Override
    public Group updateGroup(Group g) {

        JSONObject requestJSON = new JSONObject();
        requestJSON.put("data", g.toJSONObject());

        ConnAPI connAPI = new ConnAPI("/api/groups/updateGroup", "PUT", false);
        connAPI.setData(requestJSON);
        connAPI.establishConn();

        if (connAPI.getStatus() == 200)
            return g;

        return null;
    }

    @Override
    public boolean leaveGroup(Group g) {

        JSONObject requestJSON = new JSONObject();
        requestJSON.put("userID", Data.userData.getIdUser());
        requestJSON.put("groupID", g.getIdGroup());

        ConnAPI connAPI = new ConnAPI("/api/groups/leaveGroup", "DELETE", true);
        connAPI.setData(requestJSON);
        connAPI.establishConn();

        return connAPI.getStatus() == 200;
    }

    @Override
    public boolean deleteGroup(Group g) {

        JSONObject requestJSON = new JSONObject();
        requestJSON.put("groupID", g.getIdGroup());

        ConnAPI connAPI = new ConnAPI("/api/groups/deleteGroup", "DELETE", false);
        connAPI.setData(requestJSON);
        connAPI.establishConn();

        return connAPI.getStatus() == 200;
    }

    @Override
    public HashMap<Integer, Integer> addUserToGroup(String email, Group g) {

        HashMap<Integer, Integer> hashMap = new HashMap<>();

        if (email.equalsIgnoreCase(Data.userData.getEmail())) {
            hashMap.put(404, 0);
            return hashMap;
        }

        JSONObject requestJSON = new JSONObject();
        requestJSON.put("groupID", g.getIdGroup());
        requestJSON.put("email", email);

        ConnAPI connAPI = new ConnAPI("/api/groups/addUser", "POST", false);
        connAPI.setData(requestJSON);
        connAPI.establishConn();

        if (connAPI.getStatus() == 200)
            hashMap.put(200, connAPI.getDataJSON().getInt("data"));
        else
            hashMap.put(connAPI.getStatus(), 0);

        return hashMap;
    }

    @Override
    public boolean removeUserToGroup(int userID, Group g) {

        JSONObject requestJSON = new JSONObject();
        requestJSON.put("userID", userID);
        requestJSON.put("groupID", g.getIdGroup());

        ConnAPI connAPI = new ConnAPI("/api/groups/removeUser", "DELETE", false);
        connAPI.setData(requestJSON);
        connAPI.establishConn();

        return connAPI.getStatus() == 200;
    }

    @Override
    public boolean acceptGroupRequest(Group g) {

        JSONObject requestJSON = new JSONObject();
        requestJSON.put("userID", Data.userData.getIdUser());
        requestJSON.put("groupID", g.getIdGroup());

        ConnAPI connAPI = new ConnAPI("/api/groups/accept", "PUT", false);
        connAPI.setData(requestJSON);
        connAPI.establishConn();

        return connAPI.getStatus() == 200;
    }

    @Override
    public boolean rejectGroupRequest(Group g) {

        JSONObject requestJSON = new JSONObject();
        requestJSON.put("userID", Data.userData.getIdUser());
        requestJSON.put("groupID", g.getIdGroup());

        ConnAPI connAPI = new ConnAPI("/api/groups/reject", "PUT", false);
        connAPI.setData(requestJSON);
        connAPI.establishConn();

        return connAPI.getStatus() == 200;
    }

    @Override
    public boolean uploadAvatar(int groupID, int versionAvatar) {

        JSONObject requestJSON = new JSONObject();
        requestJSON.put("id", groupID);
        requestJSON.put("versionAvatar", versionAvatar);

        ConnAPI connAPI = new ConnAPI("/api/groups/updateAvatar", "PUT", false);
        connAPI.setData(requestJSON);
        connAPI.establishConn();

        return connAPI.getStatus() == 200;
    }

    @Override
    public List<User> getInvitedUsersToGroup(int groupID) {

        List<User> requestUserList = new ArrayList<>();

        JSONObject requestJSON = new JSONObject();
        requestJSON.put("id", groupID);

        ConnAPI connAPI = new ConnAPI("/api/groups/getGroupRequests", "POST", false);
        connAPI.setData(requestJSON);
        connAPI.establishConn();

        if (connAPI.getStatus() == 200){
            JSONObject responseJSON = connAPI.getDataJSON();
            JSONArray arrayJSON = responseJSON.getJSONArray("data");
            for (int i = 0; i < arrayJSON.length(); i++){
                JSONObject rawJSON = arrayJSON.getJSONObject(i);
                User user = new User();
                user.setIdUser(rawJSON.getInt("idUser"));
                user.setName(rawJSON.getString("name"));
                user.setEmail(rawJSON.getString("email"));
                user.setFirstSurname(rawJSON.getString("firstSurname"));
                user.setLastSurname(rawJSON.getString("lastSurname"));
                user.setVersionAvatar(rawJSON.getInt("versionAvatar"));

                if (user.getVersionAvatar() == 0){
                    user.setAvatarUser(new Image(new ImageTweakerTool(user.getIdUser()).getProfilePicUser()));
                }else{
                    user.setAvatarUser(new Image(new ImageTweakerTool(user.getIdUser(), user.getVersionAvatar()).getProfilePicUser()));
                }

                requestUserList.add(user);
            }
        }

        return requestUserList;
    }
}
