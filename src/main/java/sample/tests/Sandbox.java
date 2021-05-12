package sample.tests;

import sample.utils.Data;

import java.util.ArrayList;

public class Sandbox {

    public static void main(String[] args) {

        ArrayList<String> testIDs = new ArrayList<>();
        for (int i = 15; i < 16; i++){
            testIDs.add(Integer.toString(i));
        }

        Data.scheduleManager.getAllSchedulesByGroup(testIDs);

    }

}
