package sample.interfaces;

import sample.models.Contact;

import java.util.List;

public interface IContact {

    public List<Contact> getAllContacts();

    public boolean deleteContact(int idContact);

    public boolean rejectFriendRequest(int idContact);

    public boolean acceptFriendRequest(int idContact);

    public int sendFriendshipRequest(String email);

    public List<Contact> getFriendshipRequests();

}
