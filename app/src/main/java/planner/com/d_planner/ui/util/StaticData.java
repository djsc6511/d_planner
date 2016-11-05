package planner.com.d_planner.ui.util;

import android.graphics.Typeface;

/**
 * Created by soongu on 2016-11-02.
 */
public class StaticData {

    private static StaticData instance;

    public Typeface font = null;
    public Typeface fontBold = null;

    public int disWidth;
    public int disHeight;

    public static StaticData GetInstance() {
        return instance;
    }

    static {
        instance = new StaticData();
    }
}

