package sample.interfaces;

import sample.models.Notification;

import java.util.List;

public interface INotification {

    public List<Notification> getAllNotifications();

    public void showNotification(Notification n);

}
