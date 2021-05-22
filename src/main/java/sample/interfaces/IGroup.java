package sample.interfaces;

import sample.models.Group;

import java.util.ArrayList;

public interface IGroup {

    public ArrayList<Group> getAllGroups(int userID);

    public Group getGroupByID(int idGroup);

    public ArrayList<String> getAllGroupsForSQLQuery();


}
