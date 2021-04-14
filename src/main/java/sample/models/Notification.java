package sample.models;

import javafx.scene.control.Button;
import javafx.scene.image.ImageView;

public class Notification {

    private ImageView avatar;
    private Object sender;
    private String content;
    private Button btnClose;

    public Notification () {}

    public Notification(ImageView avatar, Object sender, String content, Button btnClose) {
        this.avatar = avatar;
        this.sender = sender;
        this.content = content;
        this.btnClose = btnClose;
    }

    public ImageView getAvatar() {
        return avatar;
    }

    public void setAvatar(ImageView avatar) {
        this.avatar = avatar;
    }

    public Object getSender() {
        return sender;
    }

    public void setSender(Object sender) {
        this.sender = sender;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Button getBtnClose() {
        return btnClose;
    }

    public void setBtnClose(Button btnClose) {
        this.btnClose = btnClose;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "avatar=" + avatar +
                ", sender=" + sender +
                ", content='" + content + '\'' +
                ", btnClose=" + btnClose +
                '}';
    }
}
