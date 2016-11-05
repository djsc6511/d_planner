package planner.com.d_planner.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;

import java.util.ArrayList;

import planner.com.d_planner.R;
import planner.com.d_planner.adapter.ImageAdapter;
import planner.com.d_planner.ui.util.GalleryNavigator;
import planner.com.d_planner.ui.util.OneFlingGallery;
import planner.com.d_planner.ui.util.RecycleUtils;

/**
 * Created by soongu on 2016-11-02.
 */

public class IntroTutorialActivity extends AppCompatActivity {
    private ImageAdapter mAdapter;

    private Button startBtn;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setWindowAnimations(0);
        setContentView(R.layout.activity_intro_tutorial);

        startBtn = (Button) findViewById(R.id.start_btn);
        startBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                finish();
            }
        });
        final ArrayList<Integer> mResourceList = createResourceList("image_%02d");
        mAdapter = new ImageAdapter(this, mResourceList);

        OneFlingGallery gallery = (OneFlingGallery) findViewById(R.id.gallery);
        final GalleryNavigator navi = (GalleryNavigator) findViewById(R.id.navi);
        navi.setSize(mResourceList.size());

        gallery.setAdapter(mAdapter);
        gallery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int position, long arg3) {
                navi.setPosition(position);
                navi.invalidate();

                if (position == mResourceList.size() - 1)
                    startBtn.setVisibility(View.VISIBLE);
                else
                    startBtn.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        mAdapter.recycle();
        RecycleUtils.recursiveRecycle(getWindow().getDecorView());
        System.gc();
        super.onDestroy();
    }

    public ArrayList<Integer> createResourceList(String format) {
        ArrayList<Integer> resourceList = new ArrayList<Integer>();


        resourceList.add(R.drawable.tutorial_main);
        resourceList.add(R.drawable.tutorial1);
        resourceList.add(R.drawable.tutorial2);
        resourceList.add(R.drawable.tutorial3);
        resourceList.add(R.drawable.tutorial4);
        resourceList.add(R.drawable.tutorial5);
        resourceList.add(R.drawable.tutorial6);
        resourceList.add(R.drawable.tutorial7);
        resourceList.add(R.drawable.tutorial8);
        resourceList.add(R.drawable.tutorial9);

        return resourceList;
    }
}
