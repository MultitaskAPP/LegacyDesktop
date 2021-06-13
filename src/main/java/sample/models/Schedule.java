package sample.models;

import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
import java.sql.Date;

public class Schedule {

    private int idSchedule;
    private String nameSchedule;
    private int idUser, idGroup;
    private Color colourSchedule;
    private JSONArray listsSchedules;
    private Date creationDate;

    private boolean isGroup;

    public Schedule() {}

    public Schedule(int idSchedule, String nameSchedule, int idUser, Color colourSchedule, JSONArray listsSchedules, boolean isGroup, int idGroup, Date creationDate) {
        this.idSchedule = idSchedule;
        this.nameSchedule = nameSchedule;
        this.idUser = idUser;
        this.colourSchedule = colourSchedule;
        this.listsSchedules = listsSchedules;
        this.isGroup = isGroup;
        this.idGroup = idGroup;
        this.creationDate = creationDate;
    }

    public int getIdSchedule() {
        return idSchedule;
    }

    public void setIdSchedule(int idSchedule) {
        this.idSchedule = idSchedule;
    }

    public String getNameSchedule() {
        return nameSchedule;
    }

    public void setNameSchedule(String nameSchedule) {
        this.nameSchedule = nameSchedule;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public Color getColourSchedule() {
        return colourSchedule;
    }

    public void setColourSchedule(Color colourSchedule) {
        this.colourSchedule = colourSchedule;
    }

    public JSONArray getListsSchedules() {
        return listsSchedules;
    }

    public void setListsSchedules(JSONArray listsSchedules) {
        this.listsSchedules = listsSchedules;
    }

    public boolean isGroup() {
        return isGroup;
    }

    public void setGroup(boolean group) {
        isGroup = group;
    }

    public int getIdGroup() {
        return idGroup;
    }

    public void setIdGroup(int idGroup) {
        this.idGroup = idGroup;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getHexCode(){

        int r = colourSchedule.getRed();
        int g = colourSchedule.getGreen();
        int b = colourSchedule.getBlue();

        return String.format("#%02X%02X%02X", r, g, b);
    }

    public JSONObject toJSONObject(){

        JSONObject scheduleJSON = new JSONObject();
        scheduleJSON.put("idSchedule", idSchedule);
        scheduleJSON.put("nameSchedule", nameSchedule);
        scheduleJSON.put("idUser", idUser);
        scheduleJSON.put("idGroup", idGroup);
        scheduleJSON.put("listsSchedule", listsSchedules.toString());
        scheduleJSON.put("creationDate", creationDate);

        return scheduleJSON;

    }

    @Override
    public String toString() {
        return nameSchedule;
    }
}
