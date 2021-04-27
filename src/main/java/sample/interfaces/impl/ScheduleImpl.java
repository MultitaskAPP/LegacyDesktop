package sample.interfaces.impl;

import org.json.JSONArray;
import org.json.JSONObject;
import sample.interfaces.ISchedule;
import sample.models.Schedule;
import sample.utils.ConnAPI;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ScheduleImpl implements ISchedule {

    @Override
    public List<Schedule> getAllSchedulesByUser(int userID) {

        List<Schedule> listSchedules = new ArrayList();

        JSONObject requestJSON = new JSONObject();
        requestJSON.put("id", userID);

        ConnAPI connAPI = new ConnAPI("/api/schedules/readAllByUser", "POST", false);
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
            scheduleObj.setGroup(false);
            listSchedules.add(scheduleObj);
        }

        return listSchedules;
    }
}
