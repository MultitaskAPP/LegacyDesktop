package sample.models;


import org.json.JSONObject;

import java.sql.Date;
import java.sql.Time;

public class Event {

    private int idEvent, idUser, idGroup;
    private String textEvent;
    private Date dateStart, dateFinish;
    private Time hourStart, hourFinish;
    private int typeEvent;
    private boolean isGroup;

    public Event() {}

    public Event(int idEvent, int idUser, int idGroup, String textEvent, Date dateStart, Date dateFinish, Time hourStart, Time hourFinish, int typeEvent, boolean isGroup) {
        this.idEvent = idEvent;
        this.idUser = idUser;
        this.idGroup = idGroup;
        this.textEvent = textEvent;
        this.dateStart = dateStart;
        this.dateFinish = dateFinish;
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

    public Date getDateStart() {
        return dateStart;
    }

    public void setDateStart(Date dateStart) {
        this.dateStart = dateStart;
    }

    public Time getHourStart() {
        return hourStart;
    }

    public Date getDateFinish() {
        return dateFinish;
    }

    public void setDateFinish(Date dateFinish) {
        this.dateFinish = dateFinish;
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
        eventJSON.put("dateStart", dateStart);
        eventJSON.put("dateFinish", dateFinish);
        eventJSON.put("hourStart", hourStart);
        eventJSON.put("hourFinish", hourFinish);
        eventJSON.put("typeEvent", typeEvent);
        eventJSON.put("idUser", idUser);
        eventJSON.put("idGroup", idGroup);

        return eventJSON;

    }

    @Override
    public String toString() {
        return "[" + dateStart + "] - " + textEvent;
    }
}
