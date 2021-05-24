package sample.tests;

import sample.utils.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Sandbox {

    public static void main(String[] args) {

        /* ArrayList<String> testIDs = new ArrayList<>();
        for (int i = 15; i < 16; i++){
            testIDs.add(Integer.toString(i));
        }

        Data.scheduleManager.getAllSchedulesByGroup(testIDs); */

        Map map = new HashMap();
        map.put("url", "url");
        System.out.println(map.get("url"));
        System.out.println(map);

    }

}
