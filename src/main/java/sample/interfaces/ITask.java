package sample.interfaces;

import sample.models.Event;
import sample.models.Note;
import sample.models.Task;

import java.net.PortUnreachableException;
import java.util.ArrayList;
import java.util.List;

public interface ITask {

    public List<Task> getAllTasksByUser(int idUser);

    public List<Task> getAllTasksByGroup(ArrayList<String> allGroupIDs);

    public List<Task> getAllTaksBySchedule(int scheduleID, boolean isGroup);

    public Task insertTask(Task task);

    public Task insertGroupTask(Task task);

    public boolean updateTask(Task task);

    public boolean updateGroupTask(Task task);

    public boolean deleteTask(Task task);

    public boolean deleteGroupTask(Task task);

}
