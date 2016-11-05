package planner.com.d_planner.model;

/**
 * Created by soongu on 2016-11-02.
 */

public class CourseListData {

    private String mTitle;
    private String id;

    public CourseListData(String mTitle, String id) {
        this.mTitle = mTitle;
        this.id = id;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getId() {
        return id;
    }
}
