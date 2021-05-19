package sample.interfaces;

import sample.models.Event;
import java.util.ArrayList;
import java.util.List;

public interface IEvent {

    public List<Event> getAllEventsByUser(int idUser);

    public List<Event> getAllEventsByGroup(ArrayList<String> allGroupIDs);


}
