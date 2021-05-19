package sample.interfaces.impl;

import org.json.JSONArray;
import org.json.JSONObject;
import sample.interfaces.IEvent;
import sample.models.Event;
import sample.utils.ConnAPI;

import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class EventImpl implements IEvent {

    @Override
    public List<Event> getAllEventsByUser(int idUser) {

        List<Event> eventList = new ArrayList<>();

        JSONObject requestJSON = new JSONObject();
        requestJSON.put("id", idUser);

        ConnAPI connAPI = new ConnAPI("/api/events/readAll", "POST", true);
        connAPI.setData(requestJSON);
        connAPI.establishConn();

        if (connAPI.getStatus() == 200){

            JSONObject responseJSON = connAPI.getDataJSON();
            JSONArray arrayJSON = responseJSON.getJSONArray("data");
            for (int i = 0; i < arrayJSON.length(); i++){
                JSONObject rawJSON = arrayJSON.getJSONObject(i);
                Event event = new Event();

                event.setIdEvent(rawJSON.getInt("idEvent"));
                event.setTextEvent(rawJSON.getString("textEvent"));
                event.setTypeEvent(rawJSON.getInt("typeEvent"));
                event.setIdUser(rawJSON.getInt("idUser"));
                event.setHourStart(Time.valueOf(rawJSON.getString("hourStart")));
                event.setHourFinish(Time.valueOf(rawJSON.getString("hourFinish")));

                try {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    java.util.Date dateParsed = simpleDateFormat.parse(rawJSON.getString("dateEvent"));
                    event.setDateEvent(new Date(dateParsed.getTime()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                eventList.add(event);

            }
        }

        return eventList;

    }

    @Override
    public List<Event> getAllEventsByGroup(ArrayList<String> allGroupIDs) {
        return null;
    }
}
