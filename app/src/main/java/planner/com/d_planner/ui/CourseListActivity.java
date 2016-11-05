package planner.com.d_planner.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

import planner.com.d_planner.R;
import planner.com.d_planner.adapter.CourseTableDBAdapter;
import planner.com.d_planner.model.CourseListData;
import planner.com.d_planner.ui.util.AutoLayout;
import planner.com.d_planner.ui.util.StaticData;

/**
 * Created by soongu on 2016-11-02.
 */

public class CourseListActivity extends ActionBarActivity {

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        // TODO Auto-generated method stub
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (event.getKeyCode()) {
                case KeyEvent.KEYCODE_MENU:
                    // 단말기의 메뉴버튼
                    return true;
            }

        }
        return super.dispatchKeyEvent(event);
    }

    private StaticData mData = StaticData.GetInstance();
    private AutoLayout autoLayout = AutoLayout.GetInstance();
    private ArrayList<CourseListData> mListData = new ArrayList<CourseListData>();
    private ListView mListView = null;
    private ListViewAdapter mAdapter = null;

    int type = 0;

    public boolean visibleFlag = false;

    private CourseTableDBAdapter dbAdapter;
    private static final String TAG = "CourseTableDbAdapter";
    private TextView textview;

    /*
     * (non-Javadoc)
     *
     * @see android.support.v7.app.ActionBarActivity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_course_list);


        // 최상위 레이아웃을 가져옴
        LinearLayout mainLayout = (LinearLayout) findViewById(R.id.LinearLayout1);
        // 최상위 레이아웃을 기준으로 화면 사이즈 리사이징

        autoLayout.setView(mainLayout);

        Log.d(TAG, "DatabaseTest :: onCreate()");
        this.dbAdapter = new CourseTableDBAdapter(this);

        mListView = (ListView) findViewById(R.id.listView1);

        mAdapter = new ListViewAdapter(this);
        mListView.setAdapter(mAdapter);



        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

            }
        });

        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
                // TODO Auto-generated method stub
                DialogSimple(pos);

                return false;
            }
        });

        textview = (TextView) findViewById(R.id.textView2);

        // 버튼 이벤트
        Button btnRegistTimeTable = (Button) findViewById(R.id.btnRegistTimeTable);
        Button btnBackButton = (Button) findViewById(R.id.btn_back);


        btnBackButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });
        // 시간표등록 버튼 이벤트

        btnRegistTimeTable.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(CourseListActivity.this,
                        RegistTimeTableActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);

            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();

        loadData();

        if (mAdapter.isEmpty()) {
            mListView.setVisibility(View.GONE);
            textview.setVisibility(View.VISIBLE);
        } else {
            mListView.setVisibility(View.VISIBLE);
            textview.setVisibility(View.GONE);
        }

    }

    private void loadData() {
        dbAdapter.open();
        Log.d("TestAppActivity", "onResume1");
        Cursor result = dbAdapter.fetchAllCourse();
        mListData.clear();
        result.moveToFirst();
        while (!result.isAfterLast()) {
            Log.d("TestAppActivity", "onResume122222");
            String row_id = result.getString(0);
            String num = result.getString(1);
            String title = result.getString(2);
            String teacher = result.getString(3);
            String time = result.getString(4);
            String room = result.getString(5);

            mListData.add(new CourseListData(title, row_id));
            result.moveToNext();
        }
        Log.d("TestAppActivity", "onResume" + mListData.size());
        result.close();
        mAdapter.notifyDataSetChanged();
        dbAdapter.close();
    }

    private void DialogSimple(final int pos) {
        AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
        alt_bld.setMessage("선택한 과목을 삭제하시겠습니까?").setCancelable(false)
                .setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Action for 'Yes' Button
                        String rowid = mListData.get(pos).getId();
                        dbAdapter.open();
                        dbAdapter.deleteCourse(rowid); //강의삭제
                        dbAdapter.deleteNotes(mListData.get(pos).getTitle());
                        dbAdapter.close();
                        mListData.remove(pos);
                        mAdapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Action for 'NO' Button
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alt_bld.create();
        // Title for AlertDialog
        alert.setTitle("Title");
        alert.show();
    }


    private class ViewHolder {
        public LinearLayout rootLayout;
        public TextView mText;
        public Button mButton;
    }

    private class ListViewAdapter extends BaseAdapter {
        private Context mContext = null;

        public ListViewAdapter(Context mContext) {
            super();
            this.mContext = mContext;
        }

        @Override
        public int getCount() {
            Log.d("test", "size" + mListData.size());
            return mListData.size();
        }

        @Override
        public Object getItem(int position) {

            return mListData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;

            if (convertView == null) {
                holder = new ViewHolder();

                LayoutInflater inflater = (LayoutInflater) mContext
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                convertView = inflater.inflate(R.layout.model_course_list_item,
                        null);

                holder.rootLayout = (LinearLayout) convertView
                        .findViewById(R.id.LinearLayout1);
                autoLayout.setView(holder.rootLayout);

                holder.mText = (TextView) convertView.findViewById(R.id.mTitle);
                holder.mButton = (Button) convertView.findViewById(R.id.btnRegistNote);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final CourseListData mData = mListData.get(position);

            holder.mText.setTag(position);
            holder.mText.setText(mData.getTitle());
            holder.mText.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub

//                    Intent intent = new Intent(CourseListActivity.this,
//                            CourseNoteActivity.class);
//                    intent.putExtra("coursetitle", mData.getTitle());
//
//                    startActivity(intent);
                    overridePendingTransition(0, 0);
                }
            });
            holder.mText.setOnLongClickListener(new View.OnLongClickListener() {

                @Override
                public boolean onLongClick(View v) {
                    // TODO Auto-generated method stub

                    return false;
                }
            });
            holder.mButton.setTag(position);
            holder.mButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Calendar cal = Calendar.getInstance();
                    int year = cal.get(cal.YEAR);
                    int month = cal.get(cal.MONTH);
                    int day = cal.get(cal.DATE);

                    String today = String.format("%04d년 %02d월 %02d일", year, month + 1, day);

//                    Intent intent = new Intent(CourseListActivity.this,
//                            RegistActivity.class);
//
//                    intent.putExtra("flag", 1);
//                    intent.putExtra("title", mData.getTitle());
//                    intent.putExtra("date", today);
//
//                    startActivity(intent);
                    overridePendingTransition(0, 0);

                    finish();
                }
            });




            return convertView;
        }


    }


}


