package planner.com.d_planner.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.TextView;

import planner.com.d_planner.ui.util.AutoLayout;
import planner.com.d_planner.ui.util.StaticData;

/**
 * Created by soongu on 2016-11-02.
 */

public class FriendsFindActivity extends AppCompatActivity {

    //private CourseTableDbAdapter dbAdapter;
    private static final String TAG = "CourseTableDbAdapter";

    private StaticData mData = StaticData.GetInstance();
    private AutoLayout autoLayout = AutoLayout.GetInstance();
    //private ListView listView;
    //private ListAdapter mListAdapter;
    //private ArrayList<ListInfo> mListData = new ArrayList<ListInfo>();
    //private ArrayList<ListInfo> mSearchListData = new ArrayList<ListInfo>();

    private EditText std_id;
//    private phpdo task;
    protected TextView textview1;

    protected void onCreate(Bundle savedInstanceState) {

//        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//
//
//        setContentView(R.layout.activity_friends_search);
//
//        // 최상위 레이아웃을 가져옴
//        LinearLayout mainLayout = (LinearLayout) findViewById(R.id.LinearLayout1);
//        // 최상위 레이아웃을 기준으로 화면 사이즈 리사이징
//
//        autoLayout.setView(mainLayout);
//
//        //Log.d(TAG, "DatabaseTest :: onCreate()");
//        //this.dbAdapter = new CourseTableDbAdapter(this);
//
//        Button btnBackButton = (Button) findViewById(R.id.btnBackButton);
//        Button btnSearch = (Button) findViewById(R.id.std_id_search);
//        textview1 = (TextView) findViewById(R.id.textView1);
//
//        btnBackButton.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//
//        std_id = (EditText) findViewById(R.id.editText1);
//        btnSearch.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                String id = std_id.getText().toString();
//                task = new phpdo();
//                task.execute(id);
//            }
//        });
//
//
//		/*// 리스트 뷰 셋팅 // 데이터 셋팅// 아답터 생성
//		ListView listView = (ListView) findViewById(R.id.listView1);
//		// mListData = setListData();
//
//		LayoutInflater inflacter = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		final ArrayList mSearchListData = null;
//		final ListAdapter mListAdapter = new ListAdapter(this, R.layout.model_list_item,
//				mSearchListData, inflacter);
//
//		// 아답터 배치
//		listView.setAdapter(mListAdapter);*/
//
//
//    }
//
//    // 엑셀데이터를 데이터베이스로
//
//    // DB설정 끝//
//
//
//    private class phpdo extends AsyncTask<String,Void,String> {
//
//        protected void onPreExecute(){
//
//        }
//
//        @Override
//        protected String doInBackground(String... arg0) {
//
//            try {
////	            String id =  arg0[0];
////	            String paramString = URLEncoder.encode(id, "utf-8");
////	            String link = "http://donggukplanner.dothome.co.kr/index.php?ID=" + paramString;
////	            URL url = new URL(link);
//////	            HttpClient client = new DefaultHttpClient();
//////	            HttpGet request = new HttpGet();
//////	            request.setURI(new URI(link));
//////	            HttpResponse response = client.execute(request);
////	            BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
////
////	            StringBuffer sb = new StringBuffer("");
////	            String line = "";
////
////	            while ((line = in.readLine()) != null) {
////	                sb.append(line);
////	                break;
////	            }
////	            in.close();
////	            return sb.toString();
//                return "";
//            } catch (Exception e) {
//                return new String("Exception: " + e.getMessage());
//            }
//
//        }
//
//        @Override
//        protected void onPostExecute(String result){
//            //txtview.setText("Login Successful");
//            textview1.setText(result);
//            //Toast.makeText(null, "잘되요", 1000);
//        }
    }
}


