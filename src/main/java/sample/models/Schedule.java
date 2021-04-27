package sample.models;

import org.json.JSONArray;

import java.awt.*;

public class Schedule {

    private int idSchedule;
    private String nameSchedule;
    private int idUser;
    private Color colourSchedule;
    private JSONArray listsSchedules;

    private boolean isGroup;

    public Schedule() {}

    public Schedule(int idSchedule, String nameSchedule, int idUser, Color colourSchedule, JSONArray listsSchedules, boolean isGroup) {
        this.idSchedule = idSchedule;
        this.nameSchedule = nameSchedule;
        this.idUser = idUser;
        this.colourSchedule = colourSchedule;
        this.listsSchedules = listsSchedules;
        this.isGroup = isGroup;
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

    public String getHexCode(){

        int r = colourSchedule.getRed();
        int g = colourSchedule.getGreen();
        int b = colourSchedule.getBlue();

        return String.format("#%02X%02X%02X", r, g, b);
    }

    @Override
    public String toString() {
        return nameSchedule;
    }
}
