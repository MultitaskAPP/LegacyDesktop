package sample.interfaces;

import sample.models.Task;

import java.util.List;

public interface ITask {

    public List<Task> getAllTaksBySchedule(int scheduleID);

    public Task insertTask(Task task);

    public Task insertGroupTask(Task task);

}
