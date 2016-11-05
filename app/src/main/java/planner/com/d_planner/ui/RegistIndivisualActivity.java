package planner.com.d_planner.ui;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;

import planner.com.d_planner.R;
import planner.com.d_planner.adapter.CourseTableDBAdapter;
import planner.com.d_planner.adapter.RegistIndivisualAdapter;
import planner.com.d_planner.model.RegistIndivisualData;
import planner.com.d_planner.ui.util.AutoLayout;
import planner.com.d_planner.ui.util.StaticData;

/**
 * Created by soongu on 2016-11-02.
 */

public class RegistIndivisualActivity extends AppCompatActivity implements
        View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    private StaticData mData = StaticData.GetInstance();
    private AutoLayout autoLayout = AutoLayout.GetInstance();

    private CourseTableDBAdapter dbAdapter;
    private static final String TAG = "CourseTableDbAdapter";

    private ListView listView;
    private RegistIndivisualAdapter mListAdapter;
    private ArrayList<RegistIndivisualData> mListData = new ArrayList<RegistIndivisualData>();

    private LinearLayout layoutEveryWeek, layoutSpecialDay;
    private Button btnMon, btnTue, btnWed, btnThu, btnFri, btnSat, btnSun;
    private Button btnBack, btnRegist1, btnRegist2;
    private RadioGroup radioTime;
    private EditText etCourseName;
    private EditText etCourseRoom;
    private EditText etTeacher;
    private TextView tvSpecialDay, tvSpecialTime1, tvSpecialTime2;

    private int year, month, day, hour, minute;

    private TimePickerDialog startTimePicker;
    private TimePickerDialog endTimePicker;
    private DatePickerDialog datePicker;

    private StringBuilder time;
    private String week, startTime, endTime;
    private String weekInt;
    private String courseName, courseRoom, Teacher, courseTime;

    private GregorianCalendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_regist_indivisual);
        // 최상위 레이아웃을 가져옴
        LinearLayout mainLayout = (LinearLayout) findViewById(R.id.LinearLayout1);
        // 최상위 레이아웃을 기준으로 화면 사이즈 리사이징
        autoLayout.setView(mainLayout);

        // 리스트 뷰 셋팅 // 데이터 셋팅// 아답터 생성
        listView = (ListView) findViewById(R.id.listDate);
        LayoutInflater inflacter = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mListAdapter = new RegistIndivisualAdapter(this,
                R.layout.model_date_list_item, mListData, inflacter);
        listView.setAdapter(mListAdapter);

        calendar = new GregorianCalendar();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);

        layoutEveryWeek = (LinearLayout) findViewById(R.id.layoutEveryWeek);
        layoutSpecialDay = (LinearLayout) findViewById(R.id.layoutSpecialDay);

        btnMon = (Button) findViewById(R.id.btnMon);
        btnTue = (Button) findViewById(R.id.btnTue);
        btnWed = (Button) findViewById(R.id.btnWed);
        btnThu = (Button) findViewById(R.id.btnThu);
        btnFri = (Button) findViewById(R.id.btnFri);
        btnSat = (Button) findViewById(R.id.btnSat);
        btnSun = (Button) findViewById(R.id.btnSun);
        btnRegist1 = (Button) findViewById(R.id.btnRegist1);
        btnRegist2 = (Button) findViewById(R.id.btnRegist2);
        btnBack = (Button) findViewById(R.id.btnBackButton);
        tvSpecialDay = (TextView) findViewById(R.id.tvSpecialDay);
        tvSpecialTime1 = (TextView) findViewById(R.id.tvSpecialTime1);
        tvSpecialTime2 = (TextView) findViewById(R.id.tvSpecialTime2);
        etCourseName = (EditText) findViewById(R.id.etCourseName);
        etCourseRoom = (EditText) findViewById(R.id.etCourseRoom);
        etTeacher = (EditText) findViewById(R.id.etTeacher);
        radioTime = (RadioGroup) findViewById(R.id.radioTime);

        btnMon.setOnClickListener(this);
        btnTue.setOnClickListener(this);
        btnWed.setOnClickListener(this);
        btnThu.setOnClickListener(this);
        btnFri.setOnClickListener(this);
        btnSat.setOnClickListener(this);
        btnSun.setOnClickListener(this);
        btnRegist1.setOnClickListener(this);
        btnRegist2.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        tvSpecialDay.setOnClickListener(this);
        tvSpecialTime1.setOnClickListener(this);
        tvSpecialTime2.setOnClickListener(this);

        layoutEveryWeek.setVisibility(View.GONE);
        layoutSpecialDay.setVisibility(View.GONE);

        radioTime.setOnCheckedChangeListener(this);

        tvSpecialDay.setText(String.format("%04d.%02d.%02d", year,
                month + 1, day));

        Log.d(TAG, "DatabaseTest :: onCreate()");
        this.dbAdapter = new CourseTableDBAdapter(this);

    }

    private Comparator<RegistIndivisualData> ALPHA_COMPARATOR = new Comparator<RegistIndivisualData>() {
        private final Collator sCollator = Collator.getInstance();

        @Override
        public int compare(RegistIndivisualData mListDate_1,
                           RegistIndivisualData mListDate_2) {
            return sCollator.compare(mListDate_1.getWeekInt(),
                    mListDate_2.getWeekInt());
        }
    };


    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            String msg = String.format("%04d.%02d.%02d", year, monthOfYear + 1,
                    dayOfMonth);
            tvSpecialDay.setText(msg);


        }

    };
    private TimePickerDialog.OnTimeSetListener timeSetListener1 = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // TODO Auto-generated method stub
            String msg = String.format("%02d:%02d", hourOfDay, minute);
            startTime = msg;
            tvSpecialTime1.setText(msg);

        }
    };
    private TimePickerDialog.OnTimeSetListener timeSetListener2 = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // TODO Auto-generated method stub
            String msg = String.format("%02d:%02d", hourOfDay, minute);
            startTime = msg;
            tvSpecialTime2.setText(msg);
        }
    };

    private TimePickerDialog.OnTimeSetListener startTimeSetListener = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // TODO Auto-generated method stub

            String msg = String.format("%02d:%02d", hourOfDay, minute);
            startTime = msg;

            endTimePicker = new TimePickerDialog(RegistIndivisualActivity.this,
                    endTimeSetListener, 11, 0, false);
            endTimePicker.show();
            endTimePicker.setTitle("종료시간");


        }

    };
    private TimePickerDialog.OnTimeSetListener endTimeSetListener = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // TODO Auto-generated method stub

            String msg = String.format("%02d:%02d", hourOfDay, minute);
            endTime = msg;

            mListData.add(new RegistIndivisualData(weekInt, week, startTime,
                    endTime));
            Collections.sort(mListData, ALPHA_COMPARATOR);
            mListAdapter.notifyDataSetChanged();
        }

    };


    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        int id = v.getId();

        switch (id) {
            case R.id.btnMon:
                startTimePicker = new TimePickerDialog(
                        RegistIndivisualActivity.this, startTimeSetListener, 9, 0,
                        false);
                startTimePicker.show();
                startTimePicker.setTitle("시작시간");
                weekInt = "1";
                week = "월";

                break;
            case R.id.btnTue:
                startTimePicker = new TimePickerDialog(
                        RegistIndivisualActivity.this, startTimeSetListener, 9, 0,
                        false);
                startTimePicker.show();
                startTimePicker.setTitle("시작시간");
                weekInt = "2";
                week = "화";
                break;
            case R.id.btnWed:
                startTimePicker = new TimePickerDialog(
                        RegistIndivisualActivity.this, startTimeSetListener, 9, 0,
                        false);
                startTimePicker.show();
                startTimePicker.setTitle("시작시간");
                weekInt = "3";
                week = "수";
                break;
            case R.id.btnThu:
                startTimePicker = new TimePickerDialog(
                        RegistIndivisualActivity.this, startTimeSetListener, 9, 0,
                        false);
                startTimePicker.show();
                startTimePicker.setTitle("시작시간");
                weekInt = "4";
                week = "목";
                break;
            case R.id.btnFri:
                startTimePicker = new TimePickerDialog(
                        RegistIndivisualActivity.this, startTimeSetListener, 9, 0,
                        false);
                startTimePicker.show();
                startTimePicker.setTitle("시작시간");
                weekInt = "5";
                week = "금";
                break;
            case R.id.btnSat:
                startTimePicker = new TimePickerDialog(
                        RegistIndivisualActivity.this, startTimeSetListener, 9, 0,
                        false);
                startTimePicker.show();
                startTimePicker.setTitle("시작시간");
                weekInt = "6";
                week = "토";
                break;
            case R.id.btnSun:
                startTimePicker = new TimePickerDialog(
                        RegistIndivisualActivity.this, startTimeSetListener, 9, 0,
                        false);
                startTimePicker.show();
                startTimePicker.setTitle("시작시간");
                weekInt = "7";
                week = "일";
                break;
            case R.id.tvSpecialDay:
                new DatePickerDialog(RegistIndivisualActivity.this,
                        dateSetListener, year, month, day).show();
                break;
            case R.id.tvSpecialTime1:
                new TimePickerDialog(RegistIndivisualActivity.this,
                        timeSetListener1, 9, 0, false).show();
                break;
            case R.id.tvSpecialTime2:
                new TimePickerDialog(RegistIndivisualActivity.this,
                        timeSetListener2, 11, 0, false).show();
                break;
            case R.id.btnRegist1:

                courseName = etCourseName.getText().toString();
                Teacher = etTeacher.getText().toString();
                courseRoom = etCourseRoom.getText().toString();
                time = new StringBuilder();
                int listviewCount = listView.getCount();
                for (int i = 0; i < listviewCount; i++) {
                    time.append(mListData.get(i).getWeek() + "/");
                    time.append(mListData.get(i).getStartTime() + "-");
                    if (i >= listviewCount - 1) {
                        time.append(mListData.get(i).getEndTime());
                    } else {

                        time.append(mListData.get(i).getEndTime() + ",");
                    }
                }

                Log.d("date::", "::" + time.toString());
                dbAdapter.open();
                dbAdapter.createCourse("IDV", courseName, Teacher, time.toString(),
                        courseRoom);
                dbAdapter.close();
                Intent intentActivity = new Intent(RegistIndivisualActivity.this,
                        CourseListActivity.class);
                startActivity(intentActivity);
                overridePendingTransition(0, 0);
                finish();

                break;

            case R.id.btnRegist2:
                courseName = etCourseName.getText().toString();
                Teacher = etTeacher.getText().toString();
                courseRoom = etCourseRoom.getText().toString();
                time = new StringBuilder();
                time.append(tvSpecialDay.getText() + "/");
                time.append(tvSpecialTime1.getText() + "-");
                time.append(tvSpecialTime2.getText());

                dbAdapter.open();
                dbAdapter.createCourse("SpecialDay", courseName, Teacher, time.toString(),
                        courseRoom);
                dbAdapter.close();
                Intent intentSubActivity = new Intent(RegistIndivisualActivity.this,
                        CourseListActivity.class);
                startActivity(intentSubActivity);
                overridePendingTransition(0, 0);
                finish();
                break;
            case R.id.btnBackButton:
                finish();
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        // TODO Auto-generated method stub
        if (radioTime.getCheckedRadioButtonId() == R.id.radioEveryWeek) {
            layoutEveryWeek.setVisibility(View.VISIBLE);
            layoutSpecialDay.setVisibility(View.GONE);
        } else if (radioTime.getCheckedRadioButtonId() == R.id.radioSpecialDay) {
            layoutEveryWeek.setVisibility(View.GONE);
            layoutSpecialDay.setVisibility(View.VISIBLE);
        }
    }

}

