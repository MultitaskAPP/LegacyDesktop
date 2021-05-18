package sample.interfaces.impl;

import org.json.JSONArray;
import org.json.JSONObject;
import sample.interfaces.INote;
import sample.models.Note;
import sample.utils.ConnAPI;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class NoteImpl implements INote {

    @Override
    public List<Note> getAllNotesByUser(int idUser) {

        List<Note> noteList = new ArrayList<>();

        JSONObject requestJSON = new JSONObject();
        requestJSON.put("id", idUser);

        ConnAPI connAPI = new ConnAPI("/api/notes/readAll", "POST", false);
        connAPI.setData(requestJSON);
        connAPI.establishConn();

        if (connAPI.getStatus() == 200){
            JSONObject responseJSON = connAPI.getDataJSON();
            JSONArray arrayData = responseJSON.getJSONArray("data");
            for (int i = 0; i < arrayData.length(); i++){
                JSONObject rawJSON = arrayData.getJSONObject(i);

                Note note = new Note();
                note.setIdNote(rawJSON.getInt("idNote"));
                note.setTitle(rawJSON.getString("titleNote"));
                note.setContent(rawJSON.getString("textNote"));
                note.setIdUser(rawJSON.getInt("idUser"));
                note.setGroup(false);

                try {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    java.util.Date dateParsed = simpleDateFormat.parse(rawJSON.getString("creationDate"));
                    note.setCreationDate(new Date(dateParsed.getTime()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                noteList.add(note);
            }
        }

        return noteList;
    }

    @Override
    public List<Note> getAllNotesByGroup(ArrayList<String> allGroupIDs) {

        List<Note> noteList = new ArrayList<>();

        String parsedIDs = allGroupIDs.toString().replaceAll("\\[", "(").replaceAll("\\]", ")");
        JSONObject requestJSON = new JSONObject();
        requestJSON.put("id", parsedIDs);

        ConnAPI connAPI = new ConnAPI("/api/notes/group/readAll", "POST", false);
        connAPI.setData(requestJSON);
        connAPI.establishConn();

        if (connAPI.getStatus() == 200){
            JSONObject responseJSON = connAPI.getDataJSON();
            JSONArray arrayData = responseJSON.getJSONArray("data");
            for (int i = 0; i < arrayData.length(); i++){
                JSONObject rawJSON = arrayData.getJSONObject(i);

                Note note = new Note();
                note.setIdNote(rawJSON.getInt("idGroupNote"));
                note.setIdGroup(rawJSON.getInt("idGroup"));
                note.setTitle(rawJSON.getString("titleNote"));
                note.setContent(rawJSON.getString("textNote"));
                note.setIdGroup(rawJSON.getInt("idGroup"));
                note.setGroup(true);

                try {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    java.util.Date dateParsed = simpleDateFormat.parse(rawJSON.getString("creationDate"));
                    note.setCreationDate(new Date(dateParsed.getTime()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                noteList.add(note);

            }
        }

        return noteList;

    }

    @Override
    public boolean insertNote(Note note) {

        JSONObject requestJSON = new JSONObject();
        requestJSON.put("data", note.toJSONObject());

        ConnAPI connAPI = new ConnAPI("/api/notes/createOne", "POST", true);
        connAPI.setData(requestJSON);
        connAPI.establishConn();

        return connAPI.getStatus() == 200;

    }

    @Override
    public boolean insertGroupNote(Note note) {

        JSONObject requestJSON = new JSONObject();
        requestJSON.put("data", note.toJSONObject());

        ConnAPI connAPI = new ConnAPI("/api/notes/group/createOne", "POST", true);
        connAPI.setData(requestJSON);
        connAPI.establishConn();

        return connAPI.getStatus() == 200;
    }

    @Override
    public boolean updateNote(Note note) {

        JSONObject requestJSON = new JSONObject();
        requestJSON.put("data", note.toJSONObject());

        ConnAPI connAPI = new ConnAPI("/api/notes/updateOne", "PUT", true);
        connAPI.setData(requestJSON);
        connAPI.establishConn();

        return connAPI.getStatus() == 200;
    }

    @Override
    public boolean updateGroupNote(Note note) {

        JSONObject requestJSON = new JSONObject();
        requestJSON.put("data", note.toJSONObject());

        ConnAPI connAPI = new ConnAPI("/api/notes/group/updateOne", "PUT", true);
        connAPI.setData(requestJSON);
        connAPI.establishConn();

        return connAPI.getStatus() == 200;
    }

    @Override
    public boolean deleteNote(Note note) {

        JSONObject requestJSON = new JSONObject();
        requestJSON.put("id", note.getIdNote());

        ConnAPI connAPI = new ConnAPI("/api/notes/deleteOne", "DELETE", true);
        connAPI.setData(requestJSON);
        connAPI.establishConn();

        return connAPI.getStatus() == 200;
    }

    @Override
    public boolean deleteGroupNote(Note note) {

        JSONObject requestJSON = new JSONObject();
        requestJSON.put("id", note.getIdNote());

        ConnAPI connAPI = new ConnAPI("/api/notes/group/deleteOne", "DELETE", true);
        connAPI.setData(requestJSON);
        connAPI.establishConn();

        return connAPI.getStatus() == 200;
    }
}
