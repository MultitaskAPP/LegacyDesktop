package sample.interfaces;

import sample.models.Event;
import java.util.ArrayList;
import java.util.List;

public interface IEvent {

    public List<Event> getAllEventsByUser(int idUser);

    public List<Event> getAllEventsByGroup(ArrayList<String> allGroupIDs);

    public boolean insertEvent(Event e);

    public boolean insertGroupEvent(Event e);

    public boolean updateEvent(Event e);

    public boolean updateGroupEvent(Event e);

    public boolean deleteEvent(Event e);

    public boolean deleteGroupEvent(Event e);


}
