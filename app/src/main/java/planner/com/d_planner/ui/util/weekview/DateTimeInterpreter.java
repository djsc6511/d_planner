package planner.com.d_planner.ui.util.weekview;

import java.util.Calendar;

/**
 * Created by soongu on 2016-11-02.
 */

public interface DateTimeInterpreter {
    String interpretDate(Calendar date);
    String interpretTime(int hour);
}
