package sample.models;

import javafx.scene.image.Image;

import java.util.ArrayList;

public class Group {

    private int idGroup;
    private String nameGroup, descriptionGroup, colourGroup;
    private Image avatarGroup;
    private User ownerUser;
    private ArrayList<User> arrayUsersGroup;

    public Group(){}

    public Group(int idGroup, String nameGroup, String descriptionGroup, String colourGroup, Image avatarGroup, User ownerUser, ArrayList<User> arrayUsersGroup) {
        this.idGroup = idGroup;
        this.nameGroup = nameGroup;
        this.descriptionGroup = descriptionGroup;
        this.colourGroup = colourGroup;
        this.avatarGroup = avatarGroup;
        this.ownerUser = ownerUser;
        this.arrayUsersGroup = arrayUsersGroup;
    }

    public int getIdGroup() {
        return idGroup;
    }

    public void setIdGroup(int idGroup) {
        this.idGroup = idGroup;
    }

    public String getNameGroup() {
        return nameGroup;
    }

    public void setNameGroup(String nameGroup) {
        this.nameGroup = nameGroup;
    }

    public String getDescriptionGroup() {
        return descriptionGroup;
    }

    public void setDescriptionGroup(String descriptionGroup) {
        this.descriptionGroup = descriptionGroup;
    }

    public Image getAvatarGroup() {
        return avatarGroup;
    }

    public void setAvatarGroup(Image avatarGroup) {
        this.avatarGroup = avatarGroup;
    }

    public User getOwnerUser() {
        return ownerUser;
    }

    public void setOwnerUser(User ownerUser) {
        this.ownerUser = ownerUser;
    }

    public ArrayList<User> getArrayUsersGroup() {
        return arrayUsersGroup;
    }

    public void setArrayUsersGroup(ArrayList<User> arrayUsersGroup) {
        this.arrayUsersGroup = arrayUsersGroup;
    }

    public String getColourGroup() {
        return colourGroup;
    }

    public void setColourGroup(String colourGroup) {
        this.colourGroup = colourGroup;
    }

    @Override
    public String toString() {
        return nameGroup;
    }
}
