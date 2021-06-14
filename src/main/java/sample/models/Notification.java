package sample.models;

import javafx.scene.control.Button;
import javafx.scene.image.ImageView;

public class Notification {

    private String textNotification;
    private boolean isGroup;
    private int idGroup;

    public Notification(){};

    public Notification(String textNotification, boolean isGroup, int idGroup) {
        this.textNotification = textNotification;
        this.isGroup = isGroup;
        this.idGroup = idGroup;
    }

    public String getTextNotification() {
        return textNotification;
    }

    public void setTextNotification(String textNotification) {
        this.textNotification = textNotification;
    }

    public boolean isGroup() {
        return isGroup;
    }

    public void setGroup(boolean group) {
        isGroup = group;
    }

    public int getIdGroup() {
        return idGroup;
    }

    public void setIdGroup(int idGroup) {
        this.idGroup = idGroup;
    }

    @Override
    public String toString() {
        return textNotification;
    }
}
