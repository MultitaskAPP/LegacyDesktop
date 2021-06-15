package sample.models;

import org.json.JSONObject;

import java.sql.Date;

public class Note {

    private int idNote, idUser, idGroup;
    private String title, content;
    private Date creationDate;
    private boolean isGroup;

    public Note(){}

    public Note(int idNote, int idUser, int idGroup, String title, String content, Date creationDate, boolean isGroup) {
        this.idNote = idNote;
        this.idUser = idUser;
        this.idGroup = idGroup;
        this.title = title;
        this.content = content;
        this.creationDate = creationDate;
        this.isGroup = isGroup;
    }

    public int getIdNote() {
        return idNote;
    }

    public void setIdNote(int idNote) {
        this.idNote = idNote;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public boolean isGroup() {
        return isGroup;
    }

    public void setGroup(boolean group) {
        isGroup = group;
    }

    public JSONObject toJSONObject(){

        JSONObject noteJSON = new JSONObject();
        noteJSON.put("idNote", idNote);
        noteJSON.put("idGroup", idGroup);
        noteJSON.put("idUser", idUser);
        noteJSON.put("titleNote", title);
        noteJSON.put("textNote", content);
        noteJSON.put("creationDate", creationDate);

        return  noteJSON;

    }

    @Override
    public String toString() {
        return "[" + title + "]: " + content;
    }
}
