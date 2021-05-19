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

        ConnAPI connAPI = new ConnAPI("/api/events/readAll", "POST", false);
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
                event.setGroup(false);

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

        List<Event> eventList = new ArrayList<>();

        String parsedIDs = allGroupIDs.toString().replaceAll("\\[", "(").replaceAll("\\]", ")");
        JSONObject requestJSON = new JSONObject();
        requestJSON.put("id", parsedIDs);

        ConnAPI connAPI = new ConnAPI("/api/events/group/readAll", "POST", false);
        connAPI.setData(requestJSON);
        connAPI.establishConn();

        if (connAPI.getStatus() == 200){

            JSONObject responseJSON = connAPI.getDataJSON();
            JSONArray arrayJSON = responseJSON.getJSONArray("data");
            for (int i = 0; i < arrayJSON.length(); i++){
                JSONObject rawJSON = arrayJSON.getJSONObject(i);
                Event event = new Event();

                event.setIdEvent(rawJSON.getInt("idGroupsEvents"));
                event.setTextEvent(rawJSON.getString("textGroupEvent"));
                event.setTypeEvent(rawJSON.getInt("typeEvent"));
                event.setIdGroup(rawJSON.getInt("idGroup"));
                event.setHourStart(Time.valueOf(rawJSON.getString("hourStart")));
                event.setHourFinish(Time.valueOf(rawJSON.getString("hourFinish")));
                event.setGroup(true);

                try {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    java.util.Date dateParsed = simpleDateFormat.parse(rawJSON.getString("dateGroupEvent"));
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
    public boolean insertEvent(Event e) {

        JSONObject requestJSON = new JSONObject();
        requestJSON.put("data", e.toJSONObject());

        ConnAPI connAPI = new ConnAPI("/api/events/createOne", "POST", false);
        connAPI.setData(requestJSON);
        connAPI.establishConn();

        return connAPI.getStatus() == 200;
    }

    @Override
    public boolean insertGroupEvent(Event e) {

        JSONObject requestJSON = new JSONObject();
        requestJSON.put("data", e.toJSONObject());

        ConnAPI connAPI = new ConnAPI("/api/events/group/createOne", "POST", false);
        connAPI.setData(requestJSON);
        connAPI.establishConn();

        return connAPI.getStatus() == 200;
    }

    @Override
    public boolean updateEvent(Event e) {

        JSONObject requestJSON = new JSONObject();
        requestJSON.put("data", e.toJSONObject());

        ConnAPI connAPI = new ConnAPI("/api/events/updateOne", "PUT", false);
        connAPI.setData(requestJSON);
        connAPI.establishConn();

        return connAPI.getStatus() == 200;
    }

    @Override
    public boolean updateGroupEvent(Event e) {

        JSONObject requestJSON = new JSONObject();
        requestJSON.put("data", e.toJSONObject());

        ConnAPI connAPI = new ConnAPI("/api/events/group/updateOne", "PUT", false);
        connAPI.setData(requestJSON);
        connAPI.establishConn();

        return connAPI.getStatus() == 200;
    }

    @Override
    public boolean deleteEvent(Event e) {

        JSONObject requestJSON = new JSONObject();
        requestJSON.put("id", e.getIdEvent());

        ConnAPI connAPI = new ConnAPI("/api/events/deleteOne", "DELETE", false);
        connAPI.setData(requestJSON);
        connAPI.establishConn();

        return connAPI.getStatus() == 200;
    }

    @Override
    public boolean deleteGroupEvent(Event e) {

        JSONObject requestJSON = new JSONObject();
        requestJSON.put("id", e.getIdEvent());

        ConnAPI connAPI = new ConnAPI("/api/events/group/deleteOne", "DELETE", false);
        connAPI.setData(requestJSON);
        connAPI.establishConn();

        return connAPI.getStatus() == 200;
    }
}
