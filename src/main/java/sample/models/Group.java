package sample.models;

import javafx.scene.image.Image;

import java.awt.*;
import java.util.ArrayList;

public class Group {

    private int idGroup;
    private String nameGroup, descriptionGroup;
    private Color colourGroup;
    private Image avatarGroup;
    private int ownerUser;
    private ArrayList<User> arrayUsersGroup;

    public Group(){}

    public Group(int idGroup, String nameGroup, String descriptionGroup, Color colourGroup, Image avatarGroup, int ownerUser, ArrayList<User> arrayUsersGroup) {
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

    public int getOwnerUser() {
        return ownerUser;
    }

    public void setOwnerUser(int ownerUser) {
        this.ownerUser = ownerUser;
    }

    public ArrayList<User> getArrayUsersGroup() {
        return arrayUsersGroup;
    }

    public void setArrayUsersGroup(ArrayList<User> arrayUsersGroup) {
        this.arrayUsersGroup = arrayUsersGroup;
    }

    public Color getColourGroup() {
        return colourGroup;
    }

    public void setColourGroup(Color colourGroup) {
        this.colourGroup = colourGroup;
    }

    public String getHexCode(){

        int r = colourGroup.getRed();
        int g = colourGroup.getGreen();
        int b = colourGroup.getBlue();

        return String.format("#%02X%02X%02X", r, g, b);
    }

    @Override
    public String toString() {
        return nameGroup;
    }
}
