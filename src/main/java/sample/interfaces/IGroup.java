package sample.interfaces;

import sample.models.Group;

import java.util.ArrayList;
import java.util.List;

public interface IGroup {

    public ArrayList<Group> getAllGroups(int userID);

    public Group getGroupByID(int idGroup);

    public ArrayList<String> getAllGroupsForSQLQuery();

    public List<Group> getAllGroupRequests();

    public Group createGroup(Group g);

    public Group updateGroup(Group g);

    public boolean leaveGroup(Group g);

    public boolean deleteGroup(Group g);

    public int addUserToGroup(String email, Group g);

    public boolean removeUserToGroup(int userID, Group g);

    public boolean acceptGroupRequest(Group g);

    public boolean rejectGroupRequest(Group g);

    public boolean uploadAvatar(int groupID, int versionAvatar);



}
