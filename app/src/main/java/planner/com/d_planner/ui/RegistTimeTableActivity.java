package planner.com.d_planner.ui;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import jxl.Sheet;
import jxl.Workbook;
import planner.com.d_planner.R;
import planner.com.d_planner.adapter.CourseTableDBAdapter;
import planner.com.d_planner.adapter.ListAdapter;
import planner.com.d_planner.model.ListInfo;
import planner.com.d_planner.ui.util.AutoLayout;
import planner.com.d_planner.ui.util.StaticData;

/**
 * Created by soongu on 2016-11-02.
 */

public class RegistTimeTableActivity extends AppCompatActivity {

    private CourseTableDBAdapter dbAdapter;
    private static final String TAG = "CourseTableDbAdapter";

    private StaticData mData = StaticData.GetInstance();
    private AutoLayout autoLayout = AutoLayout.GetInstance();

    // 스피너 셋팅
    private Spinner spinner;
    private ArrayList<String> spinnerData = getSpinnerData();
    private ArrayAdapter<String> spinnerAdapter;

    private ListView listView;
    private ListAdapter mListAdapter;
    private ArrayList<ListInfo> mListData = new ArrayList<ListInfo>();
    private ArrayList<ListInfo> mSearchListData = new ArrayList<ListInfo>();

    private EditText coursename;
    private EditText teachername;

    //

    public static final String test2 = "test2";

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);


        setContentView(R.layout.activity_regist_time_table);

        // 최상위 레이아웃을 가져옴
        LinearLayout mainLayout = (LinearLayout) findViewById(R.id.LinearLayout1);
        // 최상위 레이아웃을 기준으로 화면 사이즈 리사이징

        autoLayout.setView(mainLayout);

        Log.d(TAG, "DatabaseTest :: onCreate()");
        this.dbAdapter = new CourseTableDBAdapter(this);

        // path 부분엔 파일 경로를 지정해주세요.
        File files = new File("/sdcard/android/data/coursetable.xls");
        // 파일 유무를 확인합니다.
        if (files.exists() == true) {
            // 프로그레스바 DB 로딩
            CheckTypesTask DB_Load = new CheckTypesTask();
            DB_Load.execute();
        } else {
            Toast.makeText(getApplicationContext(), "강의시간표를 다운받아주세요",
                    Toast.LENGTH_LONG).show();
        }

        Button btnBackButton = (Button) findViewById(R.id.btnBackButton);
        Button btnRegistIndivisual = (Button) findViewById(R.id.btnRegistIndivisual);
        Button btnDownloadFile = (Button) findViewById(R.id.btnDownload);

        btnBackButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });

        btnRegistIndivisual.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(RegistTimeTableActivity.this,
                        RegistIndivisualActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });

        btnDownloadFile.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(getApplicationContext(), "강의시간표가 다운로드중입니다.",
                        Toast.LENGTH_SHORT).show();
                downloadFile();
            }

        });

        // 스피너 셋팅
        spinner = (Spinner) findViewById(R.id.spinner1);
        spinnerAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, spinnerData);
        spinner.setAdapter(spinnerAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parentView,
                                       View selectedView, int position, long id) {
                // TODO Auto-generated method stub
                loadMajorData(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        //

        // 리스트 뷰 셋팅 // 데이터 셋팅// 아답터 생성
        listView = (ListView) findViewById(R.id.listView1);
        // mListData = setListData();

        LayoutInflater inflacter = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mListAdapter = new ListAdapter(this, R.layout.model_list_item,
                mSearchListData, inflacter);

        // 아답터 배치
        listView.setAdapter(mListAdapter);

        coursename = (EditText) findViewById(R.id.editText1);
        coursename.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                // TODO Auto-generated method stub
                mSearchListData.clear();
                for (ListInfo tempInfo : mListData) {
                    if (tempInfo.getTitle().contains(arg0)) {
                        mSearchListData.add(tempInfo);
                    }
                }

                mListAdapter.notifyDataSetChanged();
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub

            }
        });
        teachername = (EditText) findViewById(R.id.editText2);
        teachername.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // TODO Auto-generated method stub
                mSearchListData.clear();
                for (ListInfo tempInfo : mListData) {
                    if (tempInfo.getTeacher().contains(s)) {
                        mSearchListData.add(tempInfo);
                    }
                }

                mListAdapter.notifyDataSetChanged();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });

    }

    private class CheckTypesTask extends AsyncTask<Void, Void, Void> {

        ProgressDialog asyncDialog = new ProgressDialog(
                RegistTimeTableActivity.this);

        @Override
        protected void onPreExecute() {
            asyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            asyncDialog.setMessage("로딩중입니다..");

            // show dialog
            asyncDialog.show();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            copyExcelDataToDatabase();

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            asyncDialog.dismiss();
//            mListAdapter.notifyDataSetChanged();
            //혹시 notifydataset 메소드 아시나요? 잘모르겠습니다.
            //일단 여기 원래 이런 소스코드가 있었는데, 아까 뜯어고치고싶다는 그부분떄문에 이 코드를 못쓰게됬어요
            //이게 강의시간표를 다운받으면 화면에 곧바로 뿌려줘야되는데, 아까 보시다시피 다운버튼누르고
            //뒤로 갔다가 왔을때 불러와진거 기억하시나요?아 그니까 바로 출력을 못하는 것을 말씀하시는거죠?
            //네네 한번 request를 때려야 ui에 반영이되요..
            //저위에있는 asynctask라는놈이 비동기처리라고하죠, 백그라운드에서 처리를 하는놈인데
            //아까 밑에있떤 thread도 비동기처리에요
            //비동기 처리간에는 순번이 없어요 누가 먼저할것없이 그냥 따로노는놈들인데
            //이거를 순번을 좀 정해줘야되는데 일이 커질것같다는게 이쪽입니다.
            //뭐 아무튼 그런 이유떄문에 현재 바로 데이터를 reset시키는건 안되는 부분이구요.
            //생각해보니 별로 한게 없네요 ㅠㅠ 에이 아닙니다! 지금 열심히 듣고있는데 이 코드 분석하시는 것도 힘드셨을텐데! 만약
            // 추가적으로 궁금하거나 한게 있음 말씀드릴꼐요! 우선 지금 말씀하신게 끝이신가요?
            //네 아마 이정도면 될듯합니다. 잠시만요! 간단히 생각좀 정리해보겠습니다!
            super.onPostExecute(result);
        }
    }

    // 엑셀데이터를 데이터베이스로
    private void copyExcelDataToDatabase() {
        Log.w("ExcelToDatabase", "copyExcelDataToDatabase()");
        Workbook workbook = null;
        Sheet sheet = null;
        File mPath = new File("/sdcard/android/data/coursetable.xls");
        try {
            // workbook = Workbook.getWorkbook(getBaseContext().getResources()
            // .getAssets().open("excel/coursetable.xls"));
            workbook = Workbook.getWorkbook(mPath);
            sheet = workbook.getSheet(0);

            if (workbook != null) {
                sheet = workbook.getSheet(0);
                if (sheet != null) {
                    int nMaxColumn = 5;
                    int nRowStartIndex = 1;
                    int nRowEndIndex = sheet.getColumn(nMaxColumn - 1).length - 1;
                    int nColumnStartIndex = 0;
                    // int nColumnEndIndex = sheet.getRow(2).length - 1;
                    // dbAdapter.open();

                    String term = sheet.getCell(0, 0).getContents(); //학기정보
//					TextView tv_termInfo = (TextView) findViewById(R.id.tv_termInfo);
//					tv_termInfo.setText(term);

                    mListData.clear();

                    for (int nRow = nRowStartIndex; nRow <= nRowEndIndex; nRow++) {
                        String num = sheet.getCell(nColumnStartIndex, nRow)
                                .getContents();
                        String title = sheet.getCell(nColumnStartIndex + 1,
                                nRow).getContents();
                        String teacher = sheet.getCell(nColumnStartIndex + 2,
                                nRow).getContents();
                        String time = sheet
                                .getCell(nColumnStartIndex + 3, nRow)
                                .getContents();
                        String room = sheet
                                .getCell(nColumnStartIndex + 4, nRow)
                                .getContents();
                        // Log.d("test", "test log" + title);
                        // dbAdapter.createNote(num, title, teacher, time,
                        // room);
                        mListData.add(new ListInfo(num, title, time, room,
                                teacher));
                    }

                    mSearchListData.clear();
                    mSearchListData.addAll(mListData);
                } else {
                    System.out.println("Sheet is null!!");
                }
            } else {
                System.out.println("WorkBook is null!!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (workbook != null) {
                workbook.close();

            }
        }
    }

    // DB설정 끝//

    private void downloadFile() {
        String File_Name = "coursetable.xls";
        String File_extend = "xls";


//		String fileURL = "https://s3-ap-northeast-1.amazonaws.com/donggukplanner/test"; // URL
//        String fileURL = "https://s3.ap-northeast-2.amazonaws.com/dplanner"; // URL
        String fileURL = "https://s3.ap-northeast-2.amazonaws.com/dplanner01";
        // 이부분이 아마존 웹서비스에 올라간 파일 불러오는 url입니다. 나중에 설정시 여기 바꿔주시면되요
        String Save_Path = "";
        String Save_folder = "/D_Planner";

        ProgressBar loadingBar;
        DownloadThread dThread;



        String ext = Environment.getExternalStorageState();
        if (ext.equals(Environment.MEDIA_MOUNTED)) {
            Save_Path = "/sdcard/android/data/";
            //여기는 핸드폰 안에 아마존 웹서비스에서 받아온 파일을 저장하게 해주는 코드인데요.
            //원본 코드랑 좀 다른게, 제 핸드폰에서 폴더생성이 안되서 강제로 경로를 지정해줬습니다.
            //이게 폰마다 좀 다른것같은데, 만약에 가지고계신 핸드폰에서 원본코드대로 작동하면 그대로 쓰셔도 무방합니다.
        }

        File dir = new File(Save_Path);

        if (!dir.exists()) {
            dir.mkdirs();
        }

        //여기는 안드로이드 6.0이상 버전부터 권한체크를 수시로 해줘야되요.
        //그래서 추가해놓은 소스이구요 한번 권한받으면 다음부턴 안받아도 되는 부분입니다.
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
            }
        }
        else {
            dThread = new DownloadThread(fileURL + "/" + File_Name, Save_Path + "/"
                    + File_Name);
            dThread.start();
        }


		/*
		 * if (new File(Save_Path + "/" + File_Name).exists() == false) {
		 * //loadingBar.setVisibility(View.VISIBLE);
		 *
		 * } else { //loadingBar.setVisibility(View.VISIBLE); dThread = new
		 * DownloadThread(fileURL + "/" + File_Name, Save_Path + "/" +
		 * File_Name); dThread.start(); // showDownloadFile(); }
		 */
    }

    //프로그래밍을 얼마나 아시는지는 잘 모르겠지만, 권한설정을 누를 때 확인 또는 취소를 누르잖아요???
    //그때 requestcode라는 것을 보내서 내가 어떤 요청을 했고, 어떤 요청에 대한 응답을 받는지 알려주는 코드입니다.
    //흔히 callback메소드라고하는데, 현재는 100번인 요청에대한 처리를 해주는겁니다.
    //아까 위에서 권한요청을 하고 확인을 누르면 밑에 소스를 동작하게 하는 부분입니다.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 100: {
                if (grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults.length >0 && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    String File_Name = "coursetable.xls";
                    String fileURL = "https://s3.ap-northeast-2.amazonaws.com/dplanner01";
                    String Save_folder = "/D_Planner";
                    String Save_Path = "/sdcard/android/data/" + Save_folder;;

                    DownloadThread dThread;
                    dThread = new DownloadThread(fileURL + "/" + File_Name, Save_Path + "/"
                            + File_Name);
                    dThread.start();
                }
            }
        }
    }

    //사실 여기를 다 뜯어고치고싶은데,, 그러면 일이 너무 커질거같아서 일단 냅뒀습니다.
    class DownloadThread extends Thread {
        String ServerUrl;
        String LocalPath;

        DownloadThread(String serverPath, String localPath) {
            ServerUrl = serverPath;
            LocalPath = localPath;
        }

        @Override
        public void run() {
            URL imgurl;
            int Read;
            try {
                imgurl = new URL(ServerUrl);
                HttpURLConnection conn = (HttpURLConnection) imgurl
                        .openConnection();
                int len = conn.getContentLength();
                byte[] tmpByte = new byte[len];
                InputStream is = conn.getInputStream();

                File file = new File(LocalPath);
                FileOutputStream fos = new FileOutputStream(file, false);
                for (; ; ) {
                    Read = is.read(tmpByte);
                    if (Read <= 0) {
                        break;
                    }
                    fos.write(tmpByte, 0, Read);
                }
                is.close();
                fos.close();
                conn.disconnect();

            } catch (MalformedURLException e) {
                Log.e("ERROR1", e.getMessage());
            } catch (IOException e) {
                Log.e("ERROR2", e.getMessage());
                e.printStackTrace();
            }
            mAfterDown.sendEmptyMessage(0);
        }
    }

    Handler mAfterDown = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub

            copyExcelDataToDatabase();
            Toast.makeText(getApplicationContext(), "강의시간표가 다운로드 되었습니다.",
                    Toast.LENGTH_SHORT).show();
            // loadingBar.setVisibility(View.GONE);
            // ���� �ٿ�ε� ���� �� �ٿ���� ������ �����Ų��.
            // showDownloadFile();
        }

    };


    private ArrayList<String> getSpinnerData() {

        ArrayList<String> getSpinnerData = new ArrayList<String>();
        getSpinnerData.add("전체");
        getSpinnerData.add("공통교양");
        getSpinnerData.add("핵심교양");
        getSpinnerData.add("일반교양");
        getSpinnerData.add("학문기초");
        getSpinnerData.add("대학전공기초");
        getSpinnerData.add("교직");
        getSpinnerData.add("자기개발");
        getSpinnerData.add("군사학");
        getSpinnerData.add("평생교육");
        getSpinnerData.add("경영대학 경영정보학전공");
        getSpinnerData.add("경영대학 경영학부");
        getSpinnerData.add("경영대학 회계학전공");
        getSpinnerData.add("공과대학 건축공학전공");
        getSpinnerData.add("공과대학 건축학전공");
        getSpinnerData.add("공과대학 공과대");
        getSpinnerData.add("공과대학 기계로봇에너지공학과");
        getSpinnerData.add("공과대학 멀티미디어공학과");
        getSpinnerData.add("공과대학 사회환경시스템공학과");
        getSpinnerData.add("공과대학 산업시스템공학과");
        getSpinnerData.add("공과대학 융합에너지신소재공학과");
        getSpinnerData.add("공과대학 전자전기공학전공");
        getSpinnerData.add("공과대학 정보통신공학전공");
        getSpinnerData.add("공과대학 컴퓨터공학전공");
        getSpinnerData.add("공과대학 화공생물공학과");
        getSpinnerData.add("문과대학 국어국문학전공");
        getSpinnerData.add("문과대학 독어문화학전공");
        getSpinnerData.add("문과대학 사학과");
        getSpinnerData.add("문과대학 영어문학전공");
        getSpinnerData.add("문과대학 영어통번역학전공");
        getSpinnerData.add("문과대학 윤리문화학전공");
        getSpinnerData.add("문과대학 일어일문학과");
        getSpinnerData.add("문과대학 중어중문학과");
        getSpinnerData.add("문과대학 철학전공");
        getSpinnerData.add("바이오시스템대학 바이오환경과학과");
        getSpinnerData.add("바이오시스템대학 생명과학과");
        getSpinnerData.add("바이오시스템대학 식품생명공학과");
        getSpinnerData.add("바이오시스템대학 의생명공학과");
        getSpinnerData.add("법과대학 법학과");
        getSpinnerData.add("불교대학 불교학전공");
        getSpinnerData.add("불교대학 사회복지학전공");
        getSpinnerData.add("사범대학 가정교육과");
        getSpinnerData.add("사범대학 교육학과");
        getSpinnerData.add("사범대학 국어교육과");
        getSpinnerData.add("사범대학 수학교육과");
        getSpinnerData.add("사범대학 역사교육과");
        getSpinnerData.add("사범대학 지리교육과");
        getSpinnerData.add("사범대학 체육교육과");
        getSpinnerData.add("사회과학대학 경제학전공");
        getSpinnerData.add("사회과학대학 경찰행정학과");
        getSpinnerData.add("사회과학대학 광고홍보학과");
        getSpinnerData.add("사회과학대학 국제통상학전공");
        getSpinnerData.add("사회과학대학 글로벌무역학전공");
        getSpinnerData.add("사회과학대학 북한학전공");
        getSpinnerData.add("사회과학대학 사회학전공");
        getSpinnerData.add("사회과학대학 식품산업관리학과");
        getSpinnerData.add("사회과학대학 신문방송학전공");
        getSpinnerData.add("사회과학대학 정치외교학전공");
        getSpinnerData.add("사회과학대학 행정학전공");
        getSpinnerData.add("약학대학 약학과");
        getSpinnerData.add("연계전공 문화공학");
        getSpinnerData.add("연계전공 문화기획");
        getSpinnerData.add("연계전공 문화학");
        getSpinnerData.add("연계전공 융합소프트웨어공학");
        getSpinnerData.add("예술대학 문예창작학과");
        getSpinnerData.add("예술대학 뮤지컬전공");
        getSpinnerData.add("예술대학 미술학부");
        getSpinnerData.add("예술대학 불교미술전공");
        getSpinnerData.add("예술대학 서양화전공");
        getSpinnerData.add("예술대학 연극전공");
        getSpinnerData.add("예술대학 연극학부");
        getSpinnerData.add("예술대학 영화영상학과");
        getSpinnerData.add("예술대학 조소전공");
        getSpinnerData.add("예술대학 한국화전공");
        getSpinnerData.add("이과대학 물리반도체과학부");
        getSpinnerData.add("이과대학 물리학과");
        getSpinnerData.add("이과대학 반도체과학과");
        getSpinnerData.add("이과대학 수학과");
        getSpinnerData.add("이과대학 통계학과");
        getSpinnerData.add("이과대학 화학과");
        // 데이터 추가
        return getSpinnerData;
    }

    private void loadMajorData(int position) {
        // TODO Auto-generated method stub
        mSearchListData.clear();

        if (spinnerData.get(position) == "전체") {
            for (ListInfo tempInfo : mListData) {
                mSearchListData.add(tempInfo);
            }
        } else if (spinnerData.get(position) == "공통교양") {
            for (ListInfo tempInfo : mListData) {
                if (tempInfo.getCoursenum().contains("RGC")) {
                    mSearchListData.add(tempInfo);
                }
            }
        } else if (spinnerData.get(position) == "핵심교양") {
            for (ListInfo tempInfo : mListData) {
                if (tempInfo.getCoursenum().contains("COR")) {
                    mSearchListData.add(tempInfo);
                }
            }
        } else if (spinnerData.get(position) == "일반교양") {
            for (ListInfo tempInfo : mListData) {
                if (tempInfo.getCoursenum().contains("EGC")) {
                    mSearchListData.add(tempInfo);
                }
            }
        } else if (spinnerData.get(position) == "학문기초") {
            for (ListInfo tempInfo : mListData) {
                if (tempInfo.getCoursenum().contains("PRI")) {
                    mSearchListData.add(tempInfo);
                }
            }
        } else if (spinnerData.get(position) == "대학전공기초") {
            for (ListInfo tempInfo : mListData) {
                if (tempInfo.getCoursenum().contains("DUF")
                        || tempInfo.getCoursenum().contains("SSC")) {
                    mSearchListData.add(tempInfo);
                }
            }
        } else if (spinnerData.get(position) == "교직") {
            for (ListInfo tempInfo : mListData) {
                if (tempInfo.getCoursenum().contains("ERC")) {
                    mSearchListData.add(tempInfo);
                }
            }
        } else if (spinnerData.get(position) == "자기개발") {
            for (ListInfo tempInfo : mListData) {
                if (tempInfo.getCoursenum().contains("DEV")) {
                    mSearchListData.add(tempInfo);
                }
            }
        } else if (spinnerData.get(position) == "군사학") {
            for (ListInfo tempInfo : mListData) {
                if (tempInfo.getCoursenum().contains("MRC")) {
                    mSearchListData.add(tempInfo);
                }
            }
        } else if (spinnerData.get(position) == "평생교육") {
            for (ListInfo tempInfo : mListData) {
                if (tempInfo.getCoursenum().contains("LED")) {
                    mSearchListData.add(tempInfo);
                }
            }
        } else if (spinnerData.get(position) == "경영대학 경영정보학전공") {
            for (ListInfo tempInfo : mListData) {
                if (tempInfo.getCoursenum().contains("MIS")) {
                    mSearchListData.add(tempInfo);
                }
            }
        } else if (spinnerData.get(position) == "경영대학 경영학부") {
            for (ListInfo tempInfo : mListData) {
                if (tempInfo.getCoursenum().contains("DBA")) {
                    mSearchListData.add(tempInfo);
                }
            }
        } else if (spinnerData.get(position) == "경영대학 경영학전공") {
            for (ListInfo tempInfo : mListData) {
                if (tempInfo.getCoursenum().contains("MGT")) {
                    mSearchListData.add(tempInfo);
                }
            }
        } else if (spinnerData.get(position) == "경영대학 회계학전공") {
            for (ListInfo tempInfo : mListData) {
                if (tempInfo.getCoursenum().contains("ACG")) {
                    mSearchListData.add(tempInfo);
                }
            }
        } else if (spinnerData.get(position) == "공과대학 건축공학전공") {
            Log.d("건축공학", "왜안돼 : " + spinnerData.get(position));
            for (ListInfo tempInfo : mListData) {
                if (tempInfo.getCoursenum().contains("ARC")) {
                    mSearchListData.add(tempInfo);
                }
            }
        } else if (spinnerData.get(position) == "공과대학 건축학전공") {
            Log.d("건축공학", "왜안돼 : " + spinnerData.get(position));
            for (ListInfo tempInfo : mListData) {
                if (tempInfo.getCoursenum().contains("ARD")) {
                    mSearchListData.add(tempInfo);
                }
            }
        } else if (spinnerData.get(position) == "공과대학 공과대") {
            for (ListInfo tempInfo : mListData) {
                if (tempInfo.getCoursenum().contains("DES")) {
                    mSearchListData.add(tempInfo);
                }
            }
        } else if (spinnerData.get(position) == "공과대학 기계로봇에너지공학과") {
            for (ListInfo tempInfo : mListData) {
                if (tempInfo.getCoursenum().contains("MEC")) {
                    mSearchListData.add(tempInfo);
                }
            }
        } else if (spinnerData.get(position) == "공과대학 멀티미디어공학과") {
            for (ListInfo tempInfo : mListData) {
                if (tempInfo.getCoursenum().contains("MME")) {
                    mSearchListData.add(tempInfo);
                }
            }
        } else if (spinnerData.get(position) == "공과대학 사회환경시스템공학과") {
            for (ListInfo tempInfo : mListData) {
                if (tempInfo.getCoursenum().contains("CIV")) {
                    mSearchListData.add(tempInfo);
                }
            }
        } else if (spinnerData.get(position) == "공과대학 산업시스템공학과") {
            for (ListInfo tempInfo : mListData) {
                if (tempInfo.getCoursenum().contains("ISE")) {
                    mSearchListData.add(tempInfo);
                }
            }
        } else if (spinnerData.get(position) == "공과대학 융합에너지신소재공학과") {
            for (ListInfo tempInfo : mListData) {
                if (tempInfo.getCoursenum().contains("EME")) {
                    mSearchListData.add(tempInfo);
                }
            }
        } else if (spinnerData.get(position) == "공과대학 전자전기공학전공") {
            for (ListInfo tempInfo : mListData) {
                if (tempInfo.getCoursenum().contains("ENE")) {
                    mSearchListData.add(tempInfo);
                }
            }
        } else if (spinnerData.get(position) == "공과대학 정보통신공학전공") {
            for (ListInfo tempInfo : mListData) {
                if (tempInfo.getCoursenum().contains("INC")) {
                    mSearchListData.add(tempInfo);
                }
            }
        } else if (spinnerData.get(position) == "공과대학 컴퓨터공학전공") {
            for (ListInfo tempInfo : mListData) {
                if (tempInfo.getCoursenum().contains("CSE")) {
                    mSearchListData.add(tempInfo);
                }
            }
        } else if (spinnerData.get(position) == "공과대학 화공생물공학과") {
            for (ListInfo tempInfo : mListData) {
                if (tempInfo.getCoursenum().contains("CEN")) {
                    mSearchListData.add(tempInfo);
                }
            }
        } else if (spinnerData.get(position) == "문과대학 국어국문학전공") {
            for (ListInfo tempInfo : mListData) {
                if (tempInfo.getCoursenum().contains("KOR")) {
                    mSearchListData.add(tempInfo);
                }
            }
        } else if (spinnerData.get(position) == "문과대학 독어문화학전공") {
            for (ListInfo tempInfo : mListData) {
                if (tempInfo.getCoursenum().contains("GER")) {
                    mSearchListData.add(tempInfo);
                }
            }
        } else if (spinnerData.get(position) == "문과대학 사학과") {
            for (ListInfo tempInfo : mListData) {
                if (tempInfo.getCoursenum().contains("HIS")) {
                    mSearchListData.add(tempInfo);
                }
            }
        } else if (spinnerData.get(position) == "문과대학 영어문학전공") {
            for (ListInfo tempInfo : mListData) {
                if (tempInfo.getCoursenum().contains("ENG")) {
                    mSearchListData.add(tempInfo);
                }
            }
        } else if (spinnerData.get(position) == "문과대학 영어통번역학전공") {
            for (ListInfo tempInfo : mListData) {
                if (tempInfo.getCoursenum().contains("EIT")) {
                    mSearchListData.add(tempInfo);
                }
            }
        } else if (spinnerData.get(position) == "문과대학 윤리문화학전공") {
            for (ListInfo tempInfo : mListData) {
                if (tempInfo.getCoursenum().contains("ETH")) {
                    mSearchListData.add(tempInfo);
                }
            }
        } else if (spinnerData.get(position) == "문과대학 일어일문학과") {
            for (ListInfo tempInfo : mListData) {
                if (tempInfo.getCoursenum().contains("JAP")) {
                    mSearchListData.add(tempInfo);
                }
            }
        } else if (spinnerData.get(position) == "문과대학 중어중문학과") {
            for (ListInfo tempInfo : mListData) {
                if (tempInfo.getCoursenum().contains("CHI")) {
                    mSearchListData.add(tempInfo);
                }
            }
        } else if (spinnerData.get(position) == "문과대학 철학전공") {
            for (ListInfo tempInfo : mListData) {
                if (tempInfo.getCoursenum().contains("PHI")) {
                    mSearchListData.add(tempInfo);
                }
            }
        } else if (spinnerData.get(position) == "바이오시스템대학 바이오환경과학과") {
            for (ListInfo tempInfo : mListData) {
                if (tempInfo.getCoursenum().contains("BES")) {
                    mSearchListData.add(tempInfo);
                }
            }
        } else if (spinnerData.get(position) == "바이오시스템대학 생명과학과") {
            for (ListInfo tempInfo : mListData) {
                if (tempInfo.getCoursenum().contains("BIO")) {
                    mSearchListData.add(tempInfo);
                }
            }
        } else if (spinnerData.get(position) == "바이오시스템대학 식품생명공학과") {
            for (ListInfo tempInfo : mListData) {
                if (tempInfo.getCoursenum().contains("FOO")) {
                    mSearchListData.add(tempInfo);
                }
            }
        } else if (spinnerData.get(position) == "바이오시스템대학 의생명공학과") {
            for (ListInfo tempInfo : mListData) {
                if (tempInfo.getCoursenum().contains("BME")) {
                    mSearchListData.add(tempInfo);
                }
            }
        } else if (spinnerData.get(position) == "법과대학 법학과") {
            for (ListInfo tempInfo : mListData) {
                if (tempInfo.getCoursenum().contains("LAW")) {
                    mSearchListData.add(tempInfo);
                }
            }
        } else if (spinnerData.get(position) == "불교대학 불교학전공") {
            for (ListInfo tempInfo : mListData) {
                if (tempInfo.getCoursenum().contains("BIS")) {
                    mSearchListData.add(tempInfo);
                }
            }
        } else if (spinnerData.get(position) == "불교대학 사회복지학전공") {
            for (ListInfo tempInfo : mListData) {
                if (tempInfo.getCoursenum().contains("SOW")) {
                    mSearchListData.add(tempInfo);
                }
            }
        } else if (spinnerData.get(position) == "사범대학 가정교육과") {
            for (ListInfo tempInfo : mListData) {
                if (tempInfo.getCoursenum().contains("HOM")) {
                    mSearchListData.add(tempInfo);
                }
            }
        } else if (spinnerData.get(position) == "사범대학 교육학과") {
            for (ListInfo tempInfo : mListData) {
                if (tempInfo.getCoursenum().contains("EDU")) {
                    mSearchListData.add(tempInfo);
                }
            }
        } else if (spinnerData.get(position) == "사범대학 국어교육과") {
            for (ListInfo tempInfo : mListData) {
                if (tempInfo.getCoursenum().contains("KLE")) {
                    mSearchListData.add(tempInfo);
                }
            }
        } else if (spinnerData.get(position) == "사범대학 수학교육과") {
            for (ListInfo tempInfo : mListData) {
                if (tempInfo.getCoursenum().contains("MAE")) {
                    mSearchListData.add(tempInfo);
                }
            }
        } else if (spinnerData.get(position) == "사범대학 역사교육과") {
            for (ListInfo tempInfo : mListData) {
                if (tempInfo.getCoursenum().contains("HIE")) {
                    mSearchListData.add(tempInfo);
                }
            }
        } else if (spinnerData.get(position) == "사범대학 지리교육과") {
            for (ListInfo tempInfo : mListData) {
                if (tempInfo.getCoursenum().contains("GEO")) {
                    mSearchListData.add(tempInfo);
                }
            }
        } else if (spinnerData.get(position) == "사범대학 체육교육과") {
            for (ListInfo tempInfo : mListData) {
                if (tempInfo.getCoursenum().contains("PHE")) {
                    mSearchListData.add(tempInfo);
                }
            }
        } else if (spinnerData.get(position) == "사회과학대학 경제학전공") {
            for (ListInfo tempInfo : mListData) {
                if (tempInfo.getCoursenum().contains("ECO")) {
                    mSearchListData.add(tempInfo);
                }
            }
        } else if (spinnerData.get(position) == "사회과학대학 경찰행정학과") {
            for (ListInfo tempInfo : mListData) {
                if (tempInfo.getCoursenum().contains("POA")) {
                    mSearchListData.add(tempInfo);
                }
            }
        } else if (spinnerData.get(position) == "사회과학대학 광고홍보학과") {
            for (ListInfo tempInfo : mListData) {
                if (tempInfo.getCoursenum().contains("ADV")) {
                    mSearchListData.add(tempInfo);
                }
            }
        } else if (spinnerData.get(position) == "사회과학대학 국제통상학전공") {
            for (ListInfo tempInfo : mListData) {
                if (tempInfo.getCoursenum().contains("INT")) {
                    mSearchListData.add(tempInfo);
                }
            }
        } else if (spinnerData.get(position) == "사회과학대학 글로벌무역학전공") {
            for (ListInfo tempInfo : mListData) {
                if (tempInfo.getCoursenum().contains("TRA")) {
                    mSearchListData.add(tempInfo);
                }
            }
        } else if (spinnerData.get(position) == "사회과학대학 북한학전공") {
            for (ListInfo tempInfo : mListData) {
                if (tempInfo.getCoursenum().contains("NOR")) {
                    mSearchListData.add(tempInfo);
                }
            }
        } else if (spinnerData.get(position) == "사회과학대학 사회학전공") {
            for (ListInfo tempInfo : mListData) {
                if (tempInfo.getCoursenum().contains("SOC")) {
                    mSearchListData.add(tempInfo);
                }
            }
        } else if (spinnerData.get(position) == "사회과학대학 식품산업관리학과") {
            for (ListInfo tempInfo : mListData) {
                if (tempInfo.getCoursenum().contains("FIS")) {
                    mSearchListData.add(tempInfo);
                }
            }
        } else if (spinnerData.get(position) == "사회과학대학 신문방송학전공") {
            for (ListInfo tempInfo : mListData) {
                if (tempInfo.getCoursenum().contains("COS")) {
                    mSearchListData.add(tempInfo);
                }
            }
        } else if (spinnerData.get(position) == "사회과학대학 정치외교학전공") {
            for (ListInfo tempInfo : mListData) {
                if (tempInfo.getCoursenum().contains("POL")) {
                    mSearchListData.add(tempInfo);
                }
            }
        } else if (spinnerData.get(position) == "사회과학대학 행정학전공") {
            for (ListInfo tempInfo : mListData) {
                if (tempInfo.getCoursenum().contains("PUB")) {
                    mSearchListData.add(tempInfo);
                }
            }
        } else if (spinnerData.get(position) == "약학대학 약학과") {
            for (ListInfo tempInfo : mListData) {
                if (tempInfo.getCoursenum().contains("PMY")) {
                    mSearchListData.add(tempInfo);
                }
            }
        } else if (spinnerData.get(position) == "연계전공 문화공학") {
            for (ListInfo tempInfo : mListData) {
                if (tempInfo.getCoursenum().contains("CTE")) {
                    mSearchListData.add(tempInfo);
                }
            }
        } else if (spinnerData.get(position) == "연계전공 문화기획") {
            for (ListInfo tempInfo : mListData) {
                if (tempInfo.getCoursenum().contains("CTM")) {
                    mSearchListData.add(tempInfo);
                }
            }
        } else if (spinnerData.get(position) == "연계전공 문화학") {
            for (ListInfo tempInfo : mListData) {
                if (tempInfo.getCoursenum().contains("CTS")) {
                    mSearchListData.add(tempInfo);
                }
            }
        } else if (spinnerData.get(position) == "연계전공 융합소프트웨어공학") {
            for (ListInfo tempInfo : mListData) {
                if (tempInfo.getCoursenum().contains("SCS")) {
                    mSearchListData.add(tempInfo);
                }
            }
        } else if (spinnerData.get(position) == "예술대학 문예창작학과") {
            for (ListInfo tempInfo : mListData) {
                if (tempInfo.getCoursenum().contains("CRE")) {
                    mSearchListData.add(tempInfo);
                }
            }
        } else if (spinnerData.get(position) == "예술대학 뮤지컬전공") {
            for (ListInfo tempInfo : mListData) {
                if (tempInfo.getCoursenum().contains("MUT")) {
                    mSearchListData.add(tempInfo);
                }
            }
        } else if (spinnerData.get(position) == "예술대학 미술학부") {
            for (ListInfo tempInfo : mListData) {
                if (tempInfo.getCoursenum().contains("IDP")) {
                    mSearchListData.add(tempInfo);
                }
            }
        } else if (spinnerData.get(position) == "예술대학 불교미술전공") {
            for (ListInfo tempInfo : mListData) {
                if (tempInfo.getCoursenum().contains("BUA")) {
                    mSearchListData.add(tempInfo);
                }
            }
        } else if (spinnerData.get(position) == "예술대학 서양화전공") {
            for (ListInfo tempInfo : mListData) {
                if (tempInfo.getCoursenum().contains("PAI")) {
                    mSearchListData.add(tempInfo);
                }
            }
        } else if (spinnerData.get(position) == "예술대학 연극전공") {
            for (ListInfo tempInfo : mListData) {
                if (tempInfo.getCoursenum().contains("DRA")) {
                    mSearchListData.add(tempInfo);
                }
            }
        } else if (spinnerData.get(position) == "예술대학 연극학부") {
            for (ListInfo tempInfo : mListData) {
                if (tempInfo.getCoursenum().contains("THE")) {
                    mSearchListData.add(tempInfo);
                }
            }
        } else if (spinnerData.get(position) == "예술대학 영화영상학과") {
            for (ListInfo tempInfo : mListData) {
                if (tempInfo.getCoursenum().contains("FIL")) {
                    mSearchListData.add(tempInfo);
                }
            }
        } else if (spinnerData.get(position) == "예술대학 조소전공") {
            for (ListInfo tempInfo : mListData) {
                if (tempInfo.getCoursenum().contains("SCU")) {
                    mSearchListData.add(tempInfo);
                }
            }
        } else if (spinnerData.get(position) == "예술대학 한국화전공") {
            for (ListInfo tempInfo : mListData) {
                if (tempInfo.getCoursenum().contains("KOP")) {
                    mSearchListData.add(tempInfo);
                }
            }
        } else if (spinnerData.get(position) == "이과대학 물리반도체과학부") {
            for (ListInfo tempInfo : mListData) {
                if (tempInfo.getCoursenum().contains("PSS")) {
                    mSearchListData.add(tempInfo);
                }
            }
        } else if (spinnerData.get(position) == "이과대학 물리학과") {
            for (ListInfo tempInfo : mListData) {
                if (tempInfo.getCoursenum().contains("PHY")) {
                    mSearchListData.add(tempInfo);
                }
            }
        } else if (spinnerData.get(position) == "이과대학 반도체과학과") {
            for (ListInfo tempInfo : mListData) {
                if (tempInfo.getCoursenum().contains("SEM")) {
                    mSearchListData.add(tempInfo);
                }
            }
        } else if (spinnerData.get(position) == "이과대학 수학과") {
            for (ListInfo tempInfo : mListData) {
                if (tempInfo.getCoursenum().contains("MAT")) {
                    mSearchListData.add(tempInfo);
                }
            }
        } else if (spinnerData.get(position) == "이과대학 통계학과") {
            for (ListInfo tempInfo : mListData) {
                if (tempInfo.getCoursenum().contains("STA")) {
                    mSearchListData.add(tempInfo);
                }
            }
        } else if (spinnerData.get(position) == "이과대학 화학과") {
            for (ListInfo tempInfo : mListData) {
                if (tempInfo.getCoursenum().contains("CHE")) {
                    mSearchListData.add(tempInfo);
                }
            }
        }
        mListAdapter.notifyDataSetChanged();

    }
}

