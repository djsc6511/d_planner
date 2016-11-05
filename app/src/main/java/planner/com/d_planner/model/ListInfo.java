package planner.com.d_planner.model;

/**
 * Created by soongu on 2016-11-02.
 */

public class ListInfo {

    private String coursenum;
    private String courseTitle;
    private String time;
    private String room;
    private String teacher;

    public ListInfo(String coursenum, String text1, String text2, String text3, String text4) {
        this.coursenum=coursenum;
        this.courseTitle = text1;
        this.time = text2;
        this.room = text3;
        this.teacher = text4;
    }
    public String getCoursenum(){
        return coursenum;
    }
    public String getTitle() {
        return courseTitle;
    }

    public String getTime() {
        return time;
    }

    public String getRoom() {
        return room;
    }

    public String getTeacher() {
        return teacher;
    }

}

