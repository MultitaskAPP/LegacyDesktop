package sample.models;

import org.json.JSONObject;

import java.sql.Date;


public class Task {

    private int idTask;
    private String textTask;
    private int durationTask;
    private int priorityTask;
    private Date limitDateTask, creationDate;
    private int idSchedule;
    private String listTask;
    private boolean isFinished;

    private boolean isGroup;

    public Task(){}

    public Task(int idTask, String textTask, int durationTask, int priorityTask, Date limitDateTask, int idSchedule, String listTask, boolean isGroup, boolean isFinished, Date creationDate) {
        this.idTask = idTask;
        this.textTask = textTask;
        this.durationTask = durationTask;
        this.priorityTask = priorityTask;
        this.limitDateTask = limitDateTask;
        this.idSchedule = idSchedule;
        this.listTask = listTask;
        this.isGroup = isGroup;
        this.isFinished = isFinished;
        this.creationDate = creationDate;
    }

    public int getIdTask() {
        return idTask;
    }

    public void setIdTask(int idTask) {
        this.idTask = idTask;
    }

    public String getTextTask() {
        return textTask;
    }

    public void setTextTask(String textTask) {
        this.textTask = textTask;
    }

    public int getDurationTask() {
        return durationTask;
    }

    public void setDurationTask(int durationTask) {
        this.durationTask = durationTask;
    }

    public int getPriorityTask() {
        return priorityTask;
    }

    public void setPriorityTask(int priorityTask) {
        this.priorityTask = priorityTask;
    }

    public Date getLimitDateTask() {
        return limitDateTask;
    }

    public void setLimitDateTask(Date limitDateTask) {
        this.limitDateTask = limitDateTask;
    }

    public int getIdSchedule() {
        return idSchedule;
    }

    public void setIdSchedule(int idSchedule) {
        this.idSchedule = idSchedule;
    }

    public String getListTask() {
        return listTask;
    }

    public void setListTask(String listTask) {
        this.listTask = listTask;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }

    public boolean isGroup() {
        return isGroup;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public void setGroup(boolean group) {
        isGroup = group;
    }

    public JSONObject toJSONObject(){

        JSONObject taskJSON = new JSONObject();
        taskJSON.put("idTask", idTask);
        System.out.println(taskJSON.getInt("idTask"));
        taskJSON.put("textTask", textTask);
        taskJSON.put("durationTask", durationTask);
        taskJSON.put("priorityTask", priorityTask);
        taskJSON.put("limitDateTask", limitDateTask);
        taskJSON.put("idSchedule", idSchedule);
        taskJSON.put("listTask", listTask);
        taskJSON.put("creationDate", creationDate);

        if (isFinished)
            taskJSON.put("isFinished", 1);
        else
            taskJSON.put("isFinished", 0);

        return taskJSON;

    }

    @Override
    public String toString() {
        return textTask;
    }
}
