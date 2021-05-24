package sample.interfaces.impl;

import org.json.JSONArray;
import org.json.JSONObject;
import sample.interfaces.ITask;
import sample.models.Event;
import sample.models.Note;
import sample.models.Task;
import sample.utils.ConnAPI;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TaskImpl implements ITask {

    @Override
    public List<Task> getAllTasksByUser(int idUser) {
        List<Task> taskList = new ArrayList();

        JSONObject requestJSON = new JSONObject();
        requestJSON.put("id", idUser);

        ConnAPI connAPI = new ConnAPI("/api/tasks/readAll", "POST", false);
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

            try {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                java.util.Date dateParsed = simpleDateFormat.parse(rawJSON.getString("creationDate"));
                taskObj.setCreationDate(new java.sql.Date(dateParsed.getTime()));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (rawJSON.getInt("isFinished") == 1)
                taskObj.setFinished(true);
            else
                taskObj.setFinished(false);

            taskObj.setGroup(false);
            taskList.add(taskObj);
        }

        return taskList;

    }

    @Override
    public List<Task> getAllTasksByGroup(ArrayList<String> allGroupIDs) {

        List<Task> taskList = new ArrayList();

        if (allGroupIDs.size() != 0){
            String parsedIDs = allGroupIDs.toString().replaceAll("\\[", "(").replaceAll("\\]", ")");
            JSONObject requestJSON = new JSONObject();
            requestJSON.put("id", parsedIDs);

            ConnAPI connAPI = new ConnAPI("/api/tasks/group/readAll", "POST", false);
            connAPI.setData(requestJSON);
            connAPI.establishConn();

            JSONObject responseJSON = connAPI.getDataJSON();
            JSONArray dataJSON = responseJSON.getJSONArray("data");
            for (int i = 0; i < dataJSON.length(); i++){
                JSONObject rawJSON = dataJSON.getJSONObject(i);
                Task taskObj = new Task();
                taskObj.setIdTask(rawJSON.getInt("idGroupsTasks"));
                taskObj.setTextTask(rawJSON.getString("textTask"));
                taskObj.setDurationTask(rawJSON.getInt("durationTask"));
                taskObj.setPriorityTask(rawJSON.getInt("priorityTask"));
                taskObj.setIdSchedule(rawJSON.getInt("idGroupSchedule"));
                taskObj.setListTask(rawJSON.getString("listTask"));
                taskObj.setGroup(true);

                try {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    java.util.Date dateParsed = simpleDateFormat.parse(rawJSON.getString("creationDate"));
                    taskObj.setCreationDate(new java.sql.Date(dateParsed.getTime()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (rawJSON.getInt("isFinished") == 1)
                    taskObj.setFinished(true);
                else
                    taskObj.setFinished(false);

                taskList.add(taskObj);
            }
        }


        return taskList;

    }

    @Override
    public List<Task> getAllTaksBySchedule(int scheduleID, boolean isGroup) {

        List<Task> taskList = new ArrayList();

        JSONObject requestJSON = new JSONObject();
        requestJSON.put("id", scheduleID);

        if (!isGroup){
            ConnAPI connAPI = new ConnAPI("/api/tasks/readOneSchedule", "POST", false);
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

                try {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    java.util.Date dateParsed = simpleDateFormat.parse(rawJSON.getString("creationDate"));
                    taskObj.setCreationDate(new java.sql.Date(dateParsed.getTime()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (rawJSON.getInt("isFinished") == 1)
                    taskObj.setFinished(true);
                else
                    taskObj.setFinished(false);

                taskObj.setGroup(isGroup);
                taskList.add(taskObj);
            }
        }else{
            ConnAPI connAPI = new ConnAPI("/api/tasks/group/readOneSchedule", "POST", false);
            connAPI.setData(requestJSON);
            connAPI.establishConn();

            JSONObject responseJSON = connAPI.getDataJSON();
            JSONArray dataJSON = responseJSON.getJSONArray("data");
            for (int i = 0; i < dataJSON.length(); i++){
                JSONObject rawJSON = dataJSON.getJSONObject(i);
                Task taskObj = new Task();
                taskObj.setIdTask(rawJSON.getInt("idGroupsTasks"));
                taskObj.setTextTask(rawJSON.getString("textTask"));
                taskObj.setDurationTask(rawJSON.getInt("durationTask"));
                taskObj.setPriorityTask(rawJSON.getInt("priorityTask"));
                taskObj.setIdSchedule(rawJSON.getInt("idGroupSchedule"));
                taskObj.setListTask(rawJSON.getString("listTask"));
                taskObj.setGroup(isGroup);

                try {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    java.util.Date dateParsed = simpleDateFormat.parse(rawJSON.getString("creationDate"));
                    taskObj.setCreationDate(new java.sql.Date(dateParsed.getTime()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (rawJSON.getInt("isFinished") == 1)
                    taskObj.setFinished(true);
                else
                    taskObj.setFinished(false);

                taskList.add(taskObj);
            }
        }

        return taskList;
    }

    @Override
    public Task insertTask(Task task) {

        JSONObject requestJSON = new JSONObject();
        requestJSON.put("data", task.toJSONObject());

        ConnAPI connAPI = new ConnAPI("/api/tasks/createOne", "POST", false);
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

        JSONObject requestJSON = new JSONObject();
        requestJSON.put("data", task.toJSONObject());

        ConnAPI connAPI = new ConnAPI("/api/tasks/group/createOne", "POST", false);
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
    public boolean updateTask(Task task) {

        JSONObject requestJSON = new JSONObject();
        requestJSON.put("data", task.toJSONObject());

        ConnAPI connAPI = new ConnAPI("/api/tasks/updateOne", "PUT", false);
        connAPI.setData(requestJSON);
        connAPI.establishConn();

        int status = connAPI.getStatus();
        return status == 200;
    }

    @Override
    public boolean updateGroupTask(Task task) {

        JSONObject requestJSON = new JSONObject();
        requestJSON.put("data", task.toJSONObject());

        ConnAPI connAPI = new ConnAPI("/api/tasks/group/updateOne", "PUT", true);
        connAPI.setData(requestJSON);
        connAPI.establishConn();

        int status = connAPI.getStatus();
        return status == 200;
    }

    @Override
    public boolean deleteTask(Task task) {

        JSONObject requestJSON = new JSONObject();
        requestJSON.put("id", task.getIdTask());

        ConnAPI connAPI = new ConnAPI("/api/tasks/deleteOne", "DELETE", false);
        connAPI.setData(requestJSON);
        connAPI.establishConn();

        int status = connAPI.getStatus();
        return status == 200;
    }

    @Override
    public boolean deleteGroupTask(Task task) {

        JSONObject requestJSON = new JSONObject();
        requestJSON.put("id", task.getIdTask());

        ConnAPI connAPI = new ConnAPI("/api/tasks/group/deleteOne", "DELETE", false);
        connAPI.setData(requestJSON);
        connAPI.establishConn();

        int status = connAPI.getStatus();
        return status == 200;
    }
}
