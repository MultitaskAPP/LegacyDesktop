package sample.models;

import org.json.JSONObject;

import java.sql.Date;
import java.sql.Time;

public class Event {

    private int idEvent, idUser, idGroup;
    private String textEvent;
    private Date dateEvent;
    private Time hourStart, hourFinish;
    private int typeEvent;
    private boolean isGroup;

    public Event() {}

    public Event(int idEvent, int idUser, int idGroup, String textEvent, Date dateEvent, Time hourStart, Time hourFinish, int typeEvent, boolean isGroup) {
        this.idEvent = idEvent;
        this.idUser = idUser;
        this.idGroup = idGroup;
        this.textEvent = textEvent;
        this.dateEvent = dateEvent;
        this.hourStart = hourStart;
        this.hourFinish = hourFinish;
        this.typeEvent = typeEvent;
        this.isGroup = isGroup;
    }

    public int getIdEvent() {
        return idEvent;
    }

    public void setIdEvent(int idEvent) {
        this.idEvent = idEvent;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public int getIdGroup() {
        return idGroup;
    }

    public void setIdGroup(int idGroup) {
        this.idGroup = idGroup;
    }

    public String getTextEvent() {
        return textEvent;
    }

    public void setTextEvent(String textEvent) {
        this.textEvent = textEvent;
    }

    public Date getDateEvent() {
        return dateEvent;
    }

    public void setDateEvent(Date dateEvent) {
        this.dateEvent = dateEvent;
    }

    public Time getHourStart() {
        return hourStart;
    }

    public void setHourStart(Time hourStart) {
        this.hourStart = hourStart;
    }

    public Time getHourFinish() {
        return hourFinish;
    }

    public void setHourFinish(Time hourFinish) {
        this.hourFinish = hourFinish;
    }

    public int getTypeEvent() {
        return typeEvent;
    }

    public void setTypeEvent(int typeEvent) {
        this.typeEvent = typeEvent;
    }

    public boolean isGroup() {
        return isGroup;
    }

    public void setGroup(boolean group) {
        isGroup = group;
    }

    public JSONObject toJSONObject(){

        JSONObject eventJSON = new JSONObject();
        eventJSON.put("idEvent", idEvent);
        eventJSON.put("textEvent", textEvent);
        eventJSON.put("dateEvent", dateEvent);
        eventJSON.put("hourStart", hourStart);
        eventJSON.put("hourFinish", hourFinish);
        eventJSON.put("typeEvent", typeEvent);
        eventJSON.put("idUser", idUser);
        eventJSON.put("idGroup", idGroup);

        return eventJSON;

    }

    @Override
    public String toString() {
        return "[" + dateEvent + "] - " + textEvent;
    }
}
