package sample.controllers.views;

import javafx.fxml.Initializable;
import sample.models.Schedule;
import sample.utils.Data;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class TaskViewController implements Initializable {

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        getAllSchedules();

    }

    private void getAllSchedules(){

        List<Schedule> scheduleList = Data.scheduleManager.getAllSchedulesByUser(Data.userData.getIdUser());
        System.out.println(scheduleList.toString());

    }

}
