package sample.interfaces.impl;

import org.json.JSONArray;
import org.json.JSONObject;
import sample.interfaces.ISchedule;
import sample.models.Schedule;
import sample.utils.ConnAPI;
import sample.utils.Data;

import java.awt.*;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ScheduleImpl implements ISchedule {

    @Override
    public List<Schedule> getAllSchedulesByUser(int userID) {

        List<Schedule> listSchedules = new ArrayList();

        JSONObject requestJSON = new JSONObject();
        requestJSON.put("id", userID);

        ConnAPI connAPI = new ConnAPI("/api/schedules/readAll", "POST", false);
        connAPI.setData(requestJSON);
        connAPI.establishConn();

        JSONObject responseJSON = connAPI.getDataJSON();
        JSONArray dataJSON = responseJSON.getJSONArray("data");
        for (int i = 0; i < dataJSON.length(); i++){
            JSONObject rawJSON = dataJSON.getJSONObject(i);
            Schedule scheduleObj = new Schedule();
            scheduleObj.setIdSchedule(rawJSON.getInt("idSchedule"));
            scheduleObj.setNameSchedule(rawJSON.getString("nameSchedule"));
            scheduleObj.setIdUser(rawJSON.getInt("idUser"));
            scheduleObj.setColourSchedule(Color.decode(rawJSON.getString("colourSchedule")));
            scheduleObj.setListsSchedules(new JSONArray(rawJSON.getString("listsSchedule")));

            try {
                if (!(rawJSON.isNull("creationDate"))){
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    java.util.Date dateParsed = simpleDateFormat.parse(rawJSON.getString("creationDate"));
                    scheduleObj.setCreationDate(new Date(dateParsed.getTime()));
                }else{
                    scheduleObj.setCreationDate(new Date(Calendar.getInstance().getTime().getTime()));
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

            scheduleObj.setGroup(false);
            listSchedules.add(scheduleObj);
        }

        return listSchedules;
    }

    @Override
    public List<Schedule> getAllSchedulesByGroup(ArrayList<String> allGroupIDs) {
        List<Schedule> scheduleGroupList = new ArrayList<>();

        String parsedIDs = allGroupIDs.toString().replaceAll("\\[", "(").replaceAll("\\]", ")");

        JSONObject requestJSON = new JSONObject();
        requestJSON.put("id", parsedIDs);

        ConnAPI connAPI = new ConnAPI("/api/schedules/groups/readAll", "POST", false);
        connAPI.setData(requestJSON);
        connAPI.establishConn();

        if (connAPI.getStatus() == 200){
            JSONObject responseJSON = connAPI.getDataJSON();
            JSONArray arrayJSON = responseJSON.getJSONArray("data");

            for (int i = 0; i < arrayJSON.length(); i++){
                JSONObject rawJSON = arrayJSON.getJSONObject(i);
                Schedule schedule = new Schedule();
                schedule.setIdSchedule(rawJSON.getInt("idGroupSchedule"));
                schedule.setNameSchedule(rawJSON.getString("nameGroupSchedule"));
                schedule.setIdGroup(rawJSON.getInt("idGroup"));
                schedule.setColourSchedule(Data.groupManager.getGroupByID(schedule.getIdGroup()).getColourGroup());
                schedule.setListsSchedules(new JSONArray(rawJSON.getString("listsSchedule")));

                try {
                    if (!(rawJSON.isNull("creationDate"))){
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        java.util.Date dateParsed = simpleDateFormat.parse(rawJSON.getString("creationDate"));
                        schedule.setCreationDate(new Date(dateParsed.getTime()));
                    }else{
                        schedule.setCreationDate(new Date(Calendar.getInstance().getTime().getTime()));
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                schedule.setGroup(true);
                scheduleGroupList.add(schedule);

            }
        }

        return scheduleGroupList;

    }

    @Override
    public boolean insertSchedule(Schedule schedule) {

        JSONObject requestJSON = new JSONObject();
        requestJSON.put("data", schedule.toJSONObject());

        ConnAPI connAPI = new ConnAPI("/api/schedules/createOne", "POST", false);
        connAPI.setData(requestJSON);
        connAPI.establishConn();

        int status = connAPI.getStatus();
        return status == 200;
    }

    @Override
    public boolean insertGroupSchedule(Schedule schedule) {

        JSONObject requestJSON = new JSONObject();
        requestJSON.put("data", schedule.toJSONObject());

        ConnAPI connAPI = new ConnAPI("/api/schedules/group/createOne", "POST", false);
        connAPI.setData(requestJSON);
        connAPI.establishConn();

        int status = connAPI.getStatus();
        return status == 200;
    }

    @Override
    public boolean updateSchedule(Schedule schedule) {

        JSONObject requestJSON = new JSONObject();
        requestJSON.put("data", schedule.toJSONObject());

        ConnAPI connAPI = new ConnAPI("/api/schedules/updateOne", "PUT", false);
        connAPI.setData(requestJSON);
        connAPI.establishConn();

        int status = connAPI.getStatus();
        return status == 200;
    }

    @Override
    public boolean updateGroupSchedule(Schedule schedule) {

        JSONObject requestJSON = new JSONObject();
        requestJSON.put("data", schedule.toJSONObject());

        ConnAPI connAPI = new ConnAPI("/api/schedules/group/updateOne", "PUT", false);
        connAPI.setData(requestJSON);
        connAPI.establishConn();

        int status = connAPI.getStatus();
        return status == 200;
    }

    @Override
    public boolean deleteSchedule(Schedule s) {

        JSONObject requestJSON = new JSONObject();
        requestJSON.put("id", s.getIdSchedule());

        ConnAPI connAPI = new ConnAPI("/api/schedules/deleteOne", "DELETE", false);
        connAPI.setData(requestJSON);
        connAPI.establishConn();

        int status = connAPI.getStatus();
        return status == 200;
    }

    @Override
    public boolean deleteGroupSchedule(Schedule s) {

        JSONObject requestJSON = new JSONObject();
        requestJSON.put("id", s.getIdSchedule());

        ConnAPI connAPI = new ConnAPI("/api/schedules/group/deleteOne", "DELETE", false);
        connAPI.setData(requestJSON);
        connAPI.establishConn();

        int status = connAPI.getStatus();
        return status == 200;
    }
}
