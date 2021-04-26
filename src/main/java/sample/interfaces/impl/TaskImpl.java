package sample.interfaces.impl;

import org.json.JSONArray;
import org.json.JSONObject;
import sample.interfaces.ITask;
import sample.models.Task;
import sample.utils.ConnAPI;

import java.util.ArrayList;
import java.util.List;

public class TaskImpl implements ITask {

    @Override
    public List<Task> getAllTaksBySchedule(int scheduleID) {

        List<Task> taskList = new ArrayList();

        JSONObject requestJSON = new JSONObject();
        requestJSON.put("id", scheduleID);

        ConnAPI connAPI = new ConnAPI("/api/tasks/readAllBySchedule", "POST", false);
        connAPI.setData(requestJSON);
        connAPI.establishConn();

        JSONObject responseJSON = connAPI.getDataJSON();
        JSONArray dataJSON = responseJSON.getJSONArray("data");
        for (int i = 0; i < dataJSON.length(); i++){
            JSONObject rawJSON = dataJSON.getJSONObject(i);
            Task taskObj = new Task();
            taskObj.setIdTask(rawJSON.getInt("idTask"));
            taskObj.setTextTask(rawJSON.getString("textTask"));
            taskObj.setDurationTask(rawJSON.getInt("durationTask"));
            taskObj.setPriorityTask(rawJSON.getInt("priorityTask"));
            taskObj.setIdSchedule(rawJSON.getInt("idSchedule"));
            taskObj.setListTask(rawJSON.getString("listTask"));
            taskList.add(taskObj);
        }

        return taskList;
    }

    @Override
    public Task insertTask(Task task) {

        JSONObject requestJSON = new JSONObject();
        requestJSON.put("data", task.toJSONObject());

        ConnAPI connAPI = new ConnAPI("/api/tasks/createOne", "POST", true);
        connAPI.setData(requestJSON);
        connAPI.establishConn();

        int status = connAPI.getStatus();
        if (status == 200) {
            JSONObject responseJSON = connAPI.getDataJSON();
            task.setIdTask(responseJSON.getInt("data"));
            return task;
        }

        return task;

    }

    @Override
    public Task insertGroupTask(Task task) {
        return task;
    }
}
