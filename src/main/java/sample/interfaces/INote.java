package sample.interfaces;

import sample.models.Note;

import java.util.ArrayList;
import java.util.List;

public interface INote {

    public List<Note> getAllNotesByUser(int idUser);

    public List<Note> getAllNotesByGroup(ArrayList<String> allGroupIDs);

    public boolean insertNote(Note note);

    public boolean insertGroupNote(Note note);

    public boolean updateNote(Note note);

    public boolean updateGroupNote(Note note);

    public boolean deleteNote(Note note);

    public boolean deleteGroupNote(Note note);

}
