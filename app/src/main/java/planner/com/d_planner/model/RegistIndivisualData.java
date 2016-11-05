package planner.com.d_planner.model;

/**
 * Created by soongu on 2016-11-02.
 */

public class RegistIndivisualData {
    private String weekInt;
    private String Week;
    private String StartTime;
    private String EndTime;

    public RegistIndivisualData(String weekInt, String Week,String StartTime,String EndTime) {
        this.weekInt = weekInt;
        this.Week = Week;
        this.StartTime = StartTime;
        this.EndTime = EndTime;

    }

    public String getWeekInt() {
        return weekInt;
    }
    public String getWeek() {
        return Week;
    }

    public String getStartTime() {
        return StartTime;
    }

    public String getEndTime() {
        return EndTime;
    }




}

