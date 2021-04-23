package sample.models;

import org.json.JSONArray;

import java.awt.*;

public class Schedule {

    private int idSchedule;
    private String nameSchedule;
    private int idUser;
    private Color colourSchedule;
    private JSONArray listsSchedules;

    public Schedule() {}

    public Schedule(int idSchedule, String nameSchedule, int idUser, Color colourSchedule, JSONArray listsSchedules) {
        this.idSchedule = idSchedule;
        this.nameSchedule = nameSchedule;
        this.idUser = idUser;
        this.colourSchedule = colourSchedule;
        this.listsSchedules = listsSchedules;
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

    @Override
    public String toString() {
        return nameSchedule;
    }
}
