package planner.com.d_planner;

import android.app.AlertDialog;
import android.appwidget.AppWidgetManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.os.Environment;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.StringTokenizer;

import planner.com.d_planner.adapter.CourseTableDBAdapter;
import planner.com.d_planner.ui.FriendsFindActivity;
import planner.com.d_planner.ui.IntroTutorialActivity;
import planner.com.d_planner.ui.RegistTimeTableActivity;
import planner.com.d_planner.ui.util.AutoLayout;
import planner.com.d_planner.ui.util.DateUtil;
import planner.com.d_planner.ui.util.StaticData;
import planner.com.d_planner.ui.util.rippleview.RippleView;
import planner.com.d_planner.ui.util.weekview.WeekView;
import planner.com.d_planner.ui.util.weekview.WeekViewEvent;

public class MainActivity extends AppCompatActivity implements
        WeekView.MonthChangeListener, WeekView.EventClickListener,
        WeekView.EventLongPressListener {

    private static final int TYPE_DAY_VIEW = 1;
    private static final int TYPE_THREE_DAY_VIEW = 2;
    private static final int TYPE_WEEK_VIEW = 3;
    private int mWeekViewType = TYPE_THREE_DAY_VIEW;
    private WeekView mWeekView;
    private int startTime;
    private int visibleDay;
    private int tableSize;

    private LinearLayout container;
    private RelativeLayout bottombar;

    private StaticData mData = StaticData.GetInstance();
    private AutoLayout autoLayout = AutoLayout.GetInstance();

    private CourseTableDBAdapter dbAdapter;
    private static final String TAG = "NotesDbAdapter";

    private DateUtil dateUtil;

    private String[] navItems = { "강의목록", "채팅", "시간표비교",	"튜토리얼", "버전", "설정"  };

    private ListView lvNavList;
    private FrameLayout flContainer;
    private DrawerLayout dlDrawer;
    private ActionBarDrawerToggle dtToggle;

    private SharedPreferences shPref;
    int countValue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setWindowAnimations(0);
        init(); // AutoLayout을 사용하기 위한 초기화시키는 함수 setContentView 바로 위에서 호출해주어야함
        setContentView(R.layout.activity_main);

        // 처음실행인지아닌지 카운트 - 앱사용설명서 띄우기 위해
        shPref = getSharedPreferences("MyPref", 0);
        countValue = shPref.getInt("Count", -100);
        if (countValue == -100) {
            countValue = 1; // 처음실행
        } else {
            countValue = 2; // 처음실행 아님
        }
        this.dbAdapter = new CourseTableDBAdapter(this);

        startTime = shPref.getInt("StartTime", 9);
        visibleDay = shPref.getInt("VisibleDay", 5);
//		tableSize = shPref.getInt("TableSize", 9);

        mWeekView = (WeekView) findViewById(R.id.weekView);
        mWeekView.setOnEventClickListener(this);
        mWeekView.setMonthChangeListener(this);
        mWeekView.setEventLongPressListener(this);
        initWeekView(); // week view 첫 화면 구성 메소드

        // 드로어



        lvNavList = (ListView) findViewById(R.id.lv_activity_main_nav_list);
        flContainer = (FrameLayout) findViewById(R.id.fl_activity_main_container);

        View header = getLayoutInflater().inflate(R.layout.header, null, false);
        lvNavList.addHeaderView(header);

        lvNavList.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, navItems));
        lvNavList.setOnItemClickListener(new DrawerItemClickListener());

        dlDrawer = (DrawerLayout) findViewById(R.id.dl_activity_main_drawer);
        dtToggle = new ActionBarDrawerToggle(this, dlDrawer,
                R.drawable.list, R.string.open_drawer,
                R.string.close_drawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

        };
        dlDrawer.setDrawerListener(dtToggle);
//		getActionBar().setDisplayHomeAsUpEnabled(true);


        SharedPreferences.Editor prefEditor = shPref.edit();
        prefEditor.putInt("Count", countValue);
        prefEditor.commit();
        if (countValue == 1) {
            Intent intentIntroActivity = new Intent(MainActivity.this,
                    IntroTutorialActivity.class);
            startActivity(intentIntroActivity);
            overridePendingTransition(0, 0);
            tempFriendTime();
        }

        LinearLayout mainLayout = (LinearLayout) findViewById(R.id.mainLayout);
        autoLayout.setView(mainLayout);



        // 버튼 이벤트

        container = (LinearLayout) findViewById(R.id.LinearLayout1);
        bottombar = (RelativeLayout) findViewById(R.id.RelativeLayout);

        Button btnCourseList = (Button) findViewById(R.id.btn_list);
        Button btnRegistTimeTable = (Button) findViewById(R.id.btnRegistTimeTable);
        Button btnTimeTable = (Button) findViewById(R.id.btnTimetable);
        Button btnSettings = (Button) findViewById(R.id.btnSetting);

        btnCourseList.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dlDrawer.openDrawer(GravityCompat.START);
            }
        });


        btnRegistTimeTable.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                final RippleView rippleView_plus = (RippleView) findViewById(R.id.rpl_plus);
                rippleView_plus
                        .setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {

                            @Override
                            public void onComplete(RippleView rippleView) {
                                Intent intent = new Intent(MainActivity.this,
                                        RegistTimeTableActivity.class);
                                startActivity(intent);
                                overridePendingTransition(0, 0);
                            }

                        });

            }
        });

//        btnTimeTable.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                mWeekView.goToToday();
//                initWeekView();
//            }
//        });

//        btnSettings.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                Intent intentSetting = new Intent(MainActivity.this,
//                        SettingActivity.class);
//                startActivity(intentSetting);
//                overridePendingTransition(0, 0);
//            }
//        });
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();


        startTime = shPref.getInt("StartTime", 9);
        visibleDay = shPref.getInt("VisibleDay", 5);
        initWeekView(); // week view 첫 화면 구성 메소드
        mWeekView.goToToday();
    }

    private void initWeekView() {
        // TODO Auto-generated method stub
        mWeekViewType = TYPE_WEEK_VIEW;
        mWeekView.setNumberOfVisibleDays(visibleDay);

        // Lets change some dimensions to best fit the view.
        mWeekView.setColumnGap((int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 2, getResources()
                        .getDisplayMetrics()));
        mWeekView.setHourHeight((int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 40, getResources()
                        .getDisplayMetrics()));
        mWeekView.setTextSize((int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP, 10, getResources()
                        .getDisplayMetrics()));
        mWeekView.setEventTextSize((int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP, 10, getResources()
                        .getDisplayMetrics()));
        mWeekView.setEventTextSize((int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP, 12, getResources()
                        .getDisplayMetrics()));
        mWeekView.goToHour(startTime); // 9시부터 봄

    }

    private void init() {

        // AutoLayout을 사용하기 위해 각 데이터 값을 초기화
        // - 프로그램 실행 시 메인 엑티비티에서 한번만 호출해주면 됨
//		mData.font = Typeface.createFromAsset(this.getAssets(),
//				"font/NanumBarunGothic.ttf");
//		mData.fontBold = Typeface.createFromAsset(this.getAssets(),
//				"font/NanumBarunGothic.ttf");

        mData.disWidth = this.getResources().getDisplayMetrics().widthPixels;
        mData.disHeight = this.getResources().getDisplayMetrics().heightPixels;

        autoLayout.init(this, mData.disWidth, mData.disHeight);
        // 여기까지
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (dtToggle.onOptionsItemSelected(item)) {
            return true;
        }

        int id = item.getItemId();
        switch (id) {
            case R.id.action_today:
                mWeekView.goToToday();
                initWeekView();
                return true;
//            case R.id.action_tutorial:
//                Intent intentTutorial = new Intent(MainActivity.this,
//                        IntroTutorialActivity.class);
//                startActivity(intentTutorial);
//                overridePendingTransition(0, 0);
//                return true;

            case R.id.action_capture:
                int width_container = container.getWidth();// 캡쳐할 레이아웃 크기
                int height_container = container.getHeight();// 캡쳐할 레이아웃 크기
                Log.d("height", "" + height_container);
                Log.d("width", "" + width_container);
                container.setDrawingCacheEnabled(true);
                container.buildDrawingCache(true);
                /*********************** 핵심부분 **********************************/
                Bitmap captureView = Bitmap.createBitmap(
                        container.getMeasuredWidth(), container.getMeasuredHeight()
                                - bottombar.getMeasuredHeight(),
                        Bitmap.Config.ARGB_8888);
                Canvas screenShotCanvas = new Canvas(captureView);
                container.draw(screenShotCanvas);
                /*********************** 핵심부분 *****************************************/
                FileOutputStream fos;
                String str_name = "TimeTable_Capture";
                File fileRoute = null;
                fileRoute = Environment.getExternalStorageDirectory();
                try {
                    File path = new File(fileRoute, "D_Planner");
                    if (!path.exists()) {// if(!path.isDirectory()){
                        path.mkdirs();
                    }
                    fos = new FileOutputStream(fileRoute + "/D_Planner/" + str_name
                            + ".jpeg");
                    Log.d("[screenshot]", " : " + container.getDrawingCache());
                    captureView.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    container.setDrawingCacheEnabled(false);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                Toast.makeText(getApplicationContext(),
                        "위젯화면이 캡쳐되었습니다\n이제 이 화면이 위젯에 보여집니다", Toast.LENGTH_LONG)
                        .show();

                /******* 위젯 업데이트 *******/
                AppWidgetManager mgr = AppWidgetManager.getInstance(this);

                Intent update = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE);

//                update.setClass(this, CustomWidget.class);
//
//                update.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,
//                        mgr.getAppWidgetIds(new ComponentName(this,
//                                CustomWidget.class)));

                this.sendBroadcast(update);
                /**************/

                return true;
            case R.id.action_version:
                PackageInfo pi = null;

                try {

                    pi = getPackageManager().getPackageInfo(getPackageName(), 0);

                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }

                String verSion = pi.versionName;

                Toast toast = Toast.makeText(this,  "App Version : " + verSion,
                        Toast.LENGTH_SHORT);
                toast.show();
                return true;

//            case R.id.action_chat:
//                Intent intentChat = new Intent(MainActivity.this,
//                        JiverActivity.class);
//                startActivity(intentChat);
//                overridePendingTransition(0, 0);
//                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public List<WeekViewEvent> onMonthChange(int newYear, int newMonth) {

        Log.d("", "change::Year::" + newYear);
        Log.d("", "change::newMonth::" + newMonth);

        int w;
        int day;
        int startweek;
        String week[] = { "d", "일", "월", "화", "수", "목", "금", "토" };
        StringTokenizer st1;

        // Populate the week view with some events.
        List<WeekViewEvent> events = new ArrayList<WeekViewEvent>();
        int k = 1;
        int colorid = 0; // event_color임시변수

        dbAdapter.open();
        Cursor result = dbAdapter.fetchAllCourse();
        result.moveToFirst();
        while (!result.isAfterLast()) {

            // color설정
            k %= 8;
            if (k == 0)
                colorid = R.color.event_color_01;
            else if (k == 1)
                colorid = R.color.event_color_02;
            else if (k == 2)
                colorid = R.color.event_color_03;
            else if (k == 3)
                colorid = R.color.event_color_04;
            else if (k == 4)
                colorid = R.color.event_color_05;
            else if (k == 5)
                colorid = R.color.event_color_06;
            else if (k == 6)
                colorid = R.color.event_color_07;
            else if (k == 7)
                colorid = R.color.event_color_08;
            // color설정 끝
            String coursenum = result.getString(1);
            String title = result.getString(2);
            String time = result.getString(4);

            if (coursenum.equals("SpecialDay")) {
                // time 형태 2015.03.12/15:00-17:00
                st1 = new StringTokenizer(time, "/");
                String date = st1.nextToken();
                StringTokenizer st2 = new StringTokenizer(date, ".");
                String yeartemp = st2.nextToken();
                String monthtemp = st2.nextToken();
                String daytemp = st2.nextToken();

                String timeInfo = st1.nextToken();
                String StartTimeHour = timeInfo.substring(0, 2);
                String StartTimeMin = timeInfo.substring(3, 5);
                String EndTimeHour = timeInfo.substring(6, 8);
                String EndTimeMin = timeInfo.substring(9, 11);

                Calendar startTime = Calendar.getInstance();
                startTime.set(Calendar.HOUR_OF_DAY,
                        Integer.parseInt(StartTimeHour));
                startTime.set(Calendar.MINUTE,
                        Integer.parseInt(StartTimeMin) + 1);

                startTime.set(Calendar.MONTH, Integer.parseInt(monthtemp) - 1);
                startTime.set(Calendar.YEAR, Integer.parseInt(yeartemp));

                Calendar endTime = Calendar.getInstance();
                endTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(EndTimeHour));
                endTime.set(Calendar.MINUTE, Integer.parseInt(EndTimeMin));
                endTime.set(Calendar.DAY_OF_MONTH, Integer.parseInt(daytemp));
                endTime.set(Calendar.MONTH, Integer.parseInt(monthtemp) - 1);
                endTime.set(Calendar.YEAR, Integer.parseInt(yeartemp));
                // 넣는 부분
                WeekViewEvent event = new WeekViewEvent(1, title, startTime,
                        endTime);
                event.setColor(getResources().getColor(R.color.event_color_09));
                events.add(event);

                result.moveToNext();
            } else {
                st1 = new StringTokenizer(time, ",");

                while (st1.hasMoreTokens()) {
                    String timeInfo = st1.nextToken();
                    String startDate = timeInfo.substring(0, 1); // 요일
                    int tempInt1 = timeInfo.indexOf("/");
                    String TimeString = timeInfo.substring(tempInt1 + 1); // 시간
                    int tempInt2 = TimeString.indexOf("-");
                    Log.d("변수::", timeInfo + "/" + startDate + "/" + TimeString);
                    // time 기본형태 // 화6.5-7.5/14:30-16:00,목6.5-7.5/14:30-16:00

                    String StartTimeFirst = TimeString.substring(0, tempInt2);
                    String StartTimeFirstHour = StartTimeFirst.substring(0, 2);
                    String StartTimeFirstMin = StartTimeFirst.substring(3, 5);
                    String EndTimeFirst = TimeString.substring(tempInt2 + 1);
                    String EndTimeFirstHour = EndTimeFirst.substring(0, 2);
                    String EndTimeFirstMin = EndTimeFirst.substring(3, 5);

                    Log.d("test", startDate + "  time " + timeInfo + "name"
                            + title);

                    int weekInt = 0;
                    for (int i = 0; i < week.length; i++) {
                        if (week[i].equals(startDate)) {
                            weekInt = i;
                        }
                    }

                    w = dateUtil.getWeekdaysInMonth(startDate, newMonth,
                            newYear);
                    day = Integer.parseInt(dateUtil.getDate(newYear, newMonth,
                            1, weekInt)); // 그 연도 그달의 첫번째 그 요일이 몇일인지
                    startweek = 1;
                    if (day >= 7) // 그요일이 두번째 주에 처음 시작되면
                        startweek = 2;

                    for (int i = startweek; i < startweek + w; i++) {
                        Calendar startTime = Calendar.getInstance();
                        startTime.set(Calendar.DAY_OF_WEEK, weekInt);
                        startTime.set(Calendar.WEEK_OF_MONTH, i); // 추가
                        startTime.set(Calendar.HOUR_OF_DAY,
                                Integer.parseInt(StartTimeFirstHour));
                        startTime.set(Calendar.MINUTE,
                                Integer.parseInt(StartTimeFirstMin) + 1);
                        startTime.set(Calendar.MONTH, newMonth - 1);
                        startTime.set(Calendar.YEAR, newYear);

                        Calendar endTime = Calendar.getInstance();
                        endTime.set(Calendar.DAY_OF_WEEK, weekInt);
                        endTime.set(Calendar.WEEK_OF_MONTH, i); // 추가
                        endTime.set(Calendar.HOUR_OF_DAY,
                                Integer.parseInt(EndTimeFirstHour));
                        endTime.set(Calendar.MINUTE,
                                Integer.parseInt(EndTimeFirstMin));
                        endTime.set(Calendar.MONTH, newMonth - 1);
                        endTime.set(Calendar.YEAR, newYear);

                        // 넣는 부분
                        WeekViewEvent event = new WeekViewEvent(1, title,
                                startTime, endTime);

                        event.setColor(getResources().getColor(colorid));
                        events.add(event);
                    }

                }

                result.moveToNext();
                k++;
            }
        }
        result.close();
        dbAdapter.close();

        return events;
    }


    private String getEventTitle(Calendar time) {
        return String.format("Event of %02d:%02d %s/%d",
                time.get(Calendar.HOUR_OF_DAY), time.get(Calendar.MINUTE),
                time.get(Calendar.MONTH) + 1, time.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public void onEventClick(WeekViewEvent event, RectF eventRect) {
        // Toast.makeText(
        // MainActivity.this,
        // "Clicked " + event.getName()
        // + event.getStartTime().get(Calendar.MONTH) + 1 + "."
        // + event.getStartTime().get(Calendar.DATE),
        // Toast.LENGTH_SHORT).show();

        String start_dateString;

        start_dateString = String.format("%04d년 %02d월 %02d일", event
                        .getStartTime().get(Calendar.YEAR),
                event.getStartTime().get(Calendar.MONTH) + 1, event
                        .getStartTime().get(Calendar.DAY_OF_MONTH));

        // 강의노트 등록화면
//        Intent intentSubActivity = new Intent(MainActivity.this,
//                RegistActivity.class);
//        intentSubActivity.putExtra("flag", 1);
//        intentSubActivity.putExtra("title", event.getName());
//        intentSubActivity.putExtra("date", start_dateString);
//
//        startActivity(intentSubActivity);
        overridePendingTransition(0, 0);
    }

    @Override
    public void onEventLongPress(WeekViewEvent event, RectF eventRect) {
        dbAdapter.open();
        Cursor result = dbAdapter.fetchCourse_Title(event.getName());
        result.moveToFirst();
        Toast.makeText(
                MainActivity.this,
                "강의명 : " + event.getName() + "(" + result.getString(3) + ")\n"
                        + "강의실 : " + result.getString(5), Toast.LENGTH_LONG)
                .show();
        result.close();
        dbAdapter.close();
    }

    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        dtToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        dtToggle.onConfigurationChanged(newConfig);
    }

    private class DrawerItemClickListener implements
            ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapter, View view,
                                int position, long id) {
            switch (position) {
                case 1: //강의목록
//                    Intent intentSubActivity = new Intent(MainActivity.this,
//                            CourseListActivity.class);
//                    startActivity(intentSubActivity);
//                    overridePendingTransition(0, 0);
                    break;
                case 2: //채팅
//                    Intent intentChat = new Intent(MainActivity.this,
//                            JiverActivity.class);
//                    startActivity(intentChat);
//                    overridePendingTransition(0, 0);
                    break;
                case 3: //시간표비교
                    Toast.makeText(MainActivity.this, "준비중입니다 ^^;;", Toast.LENGTH_SHORT).show();
//				AlertDialog dialog = compareDialogBox();
//			     dialog.show();

                    break;
                case 4: //튜토리얼
                    Intent intentTutorial = new Intent(MainActivity.this,
                            IntroTutorialActivity.class);
                    startActivity(intentTutorial);
                    overridePendingTransition(0, 0);
                    break;
                case 5:  //버전
                    PackageInfo pi = null;

                    try {

                        pi = getPackageManager().getPackageInfo(getPackageName(), 0);

                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }

                    String verSion = pi.versionName;

                    Toast toast = Toast.makeText(MainActivity.this,"App Version : " + verSion,
                            Toast.LENGTH_SHORT);
                    toast.show();
                    break;
                case 6: //설정
//                    Intent intentSetting = new Intent(MainActivity.this,
//                            SettingActivity.class);
//                    startActivity(intentSetting);
//                    overridePendingTransition(0, 0);

                    break;

            }
            dlDrawer.closeDrawer(lvNavList);

        }

    }
    private AlertDialog compareDialogBox(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("시간표 비교");
        builder.setMessage("친구를 추가하시겠습니까");

        // msg 는 그저 String 타입의 변수, tv 는 onCreate 메서드에 글을 뿌려줄 TextView
        builder.setPositiveButton("네", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int whichButton){
                Intent intentFri = new Intent(MainActivity.this,
                        FriendsFindActivity.class);
                startActivity(intentFri);
                overridePendingTransition(0, 0);
            }
        });


        builder.setNegativeButton("아니요", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int whichButton){

            }
        });

        AlertDialog dialog = builder.create();
        return dialog;

    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            AlertDialog.Builder alertDlg = new AlertDialog.Builder(this);

            alertDlg.setMessage("종료하시겠습니까?");
            alertDlg.setPositiveButton("예",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog,
                                            int whichButton) {
                            System.exit(0);
                        }
                    });
            alertDlg.setNegativeButton("아니오",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog,
                                            int whichButton) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = alertDlg.create();
			/*
			 * alert.setTitle("뒤로가기 버튼 이벤트");
			 * alert.setIcon(R.drawable.ic_launcher);
			 */
            alert.show();
        }
        return false;
    }

    private void tempFriendTime(){
        dbAdapter.open();

        dbAdapter.createFriends("2010111827", "AAA", "친구강의1", "교수", "화1.0-2.0/09:00-10:30,목1.0-2.0/09:00-10:30" , "원흥관");
        dbAdapter.createFriends("2010111827", "AAA", "친구강의2", "교수", "수6.0-7.0/14:00-15:30,금6.0-7.0/14:00-15:30" , "원흥관");
        dbAdapter.createFriends("2010111827", "AAA", "친구강의3", "교수", "월5.5-6.5/13:30-15:00,수2.5-3.5/10:30-12:00" , "원흥관");
        dbAdapter.createFriends("2010111827", "AAA", "친구강의4", "교수", "월4.0-5.0/12:00-13:30,수1.0-2.0/09:00-10:30" , "원흥관");

        dbAdapter.close();
    }
}
