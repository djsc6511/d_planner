package planner.com.d_planner.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import planner.com.d_planner.R;
import planner.com.d_planner.model.ListInfo;
import planner.com.d_planner.ui.util.AutoLayout;
import planner.com.d_planner.ui.util.StaticData;

/**
 * Created by soongu on 2016-11-02.
 */

public class ListAdapter extends ArrayAdapter<Object> {

    public LayoutInflater vl;
    private ArrayList<ListInfo> items = null;

    private CourseTableDBAdapter dbAdapter;
    private static final String TAG = "CourseTableDbAdapter";

    private StaticData mData = StaticData.GetInstance();
    private AutoLayout autoLayout = AutoLayout.GetInstance();

    private Context mContext;

    @SuppressWarnings("unchecked")
    public ListAdapter(Context context, int textViewResourceId,
                       @SuppressWarnings("rawtypes") ArrayList getItems,
                       LayoutInflater getInflater) {
        super(context, textViewResourceId, getItems);

        mContext = context;

        vl = getInflater;
        this.items = getItems;
        // TODO Auto-generated constructor stub

        Log.d(TAG, "DatabaseTest :: onCreate()");
        this.dbAdapter = new CourseTableDBAdapter(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {

            v = vl.inflate(R.layout.model_list_item, null);

            FrameLayout mainLayout = (FrameLayout) v
                    .findViewById(R.id.model_list_layout);

            autoLayout.setView(mainLayout);

        }

        ListInfo p = (ListInfo) items.get(position);

        if (p != null) {

            TextView tvTitle = (TextView) v.findViewById(R.id.tvTitle);
            TextView tvTime = (TextView) v.findViewById(R.id.tvTime);
            TextView tvRoom = (TextView) v.findViewById(R.id.tvRoom);
            Button btnRegist = (Button) v.findViewById(R.id.btnRegist1);

            tvTitle.setText(p.getTitle() + "(" + p.getTeacher() + ")");
            tvTime.setText(p.getTime());
            tvRoom.setText(p.getRoom());
            final String coursenum = p.getCoursenum();
            final String title = p.getTitle();
            final String teacher = p.getTeacher();
            final String time = p.getTime();
            final String room = p.getRoom();

            btnRegist.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    // Intent intent = new Intent();
                    Toast.makeText(mContext, "강의가 등록되었습니다", Toast.LENGTH_LONG).show();

                    dbAdapter.open();
                    dbAdapter.createCourse(coursenum, title, teacher, time, room);
                    dbAdapter.close();
                }
            });
        }

        return v;
    }

    private int calWidth(int getwidth) {
        return getwidth * mData.disWidth / 720;
    }

}

