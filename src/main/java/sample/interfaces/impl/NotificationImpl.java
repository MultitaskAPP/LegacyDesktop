package sample.interfaces.impl;

import sample.interfaces.INotification;
import sample.models.Contact;
import sample.models.Group;
import sample.models.Notification;
import sample.models.User;
import sample.utils.Data;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class NotificationImpl implements INotification {
    @Override
    public List<Notification> getAllNotifications() {

        List<Notification> notificationsList = new ArrayList<>();

        List<Contact> friendshipRequestsList = Data.contactManager.getFriendshipRequests();
        List<Group> groupRequestsList = Data.groupManager.getAllGroupRequests();

        Notification friendshipsNotif = new Notification("Tienes " + friendshipRequestsList.size() + " solicitud/es de amistad pendiente/s.", false, 0);
        Notification groupsNotif = new Notification("Tienes " + groupRequestsList.size() + " invitacion/es a grupo/s pendiente/s.", false, 0);

        notificationsList.add(friendshipsNotif);
        notificationsList.add(groupsNotif);

        return notificationsList;
    }

    @Override
    public void showNotification(Notification n) {
        if (SystemTray.isSupported()){
            Data.trayIcon.displayMessage("MultitaskAPP", n.getTextNotification(), TrayIcon.MessageType.NONE);
        }
    }
}
