package sample.interfaces;

import sample.models.Schedule;

import java.util.ArrayList;
import java.util.List;

public interface ISchedule {

    public List<Schedule> getAllSchedulesByUser(int userID);

    public List<Schedule> getAllSchedulesByGroup(ArrayList<String> allGroupIDs);

    public boolean insertSchedule(Schedule schedule);

    public boolean insertGroupSchedule(Schedule schedule);

    public boolean updateSchedule(Schedule schedule);

    public boolean updateGroupSchedule(Schedule schedule);

    public boolean deleteSchedule(Schedule s);

    public boolean deleteGroupSchedule(Schedule s);

}
