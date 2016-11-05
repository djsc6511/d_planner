package planner.com.d_planner.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import planner.com.d_planner.ui.util.RecycleUtils;

/**
 * Created by soongu on 2016-11-02.
 */

public class ImageAdapter extends BaseAdapter {
    private ArrayList<Integer> mResources;
    private Context mContext;
    private List<WeakReference<View>> mRecycleList = new ArrayList<WeakReference<View>>();

    public ImageAdapter(Context c, ArrayList<Integer> resources) {
        mContext = c;
        mResources = resources;
    }

    @Override
    public int getCount() {
        return mResources.size();
    }

    @Override
    public Object getItem(int position) {
        return mResources.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void recycle() {
        RecycleUtils.recursiveRecycle(mRecycleList);
    }

    public void recycleHalf() {
        int halfSize = mRecycleList.size() / 2;
        List<WeakReference<View>> recycleHalfList = mRecycleList.subList(0,
                halfSize);
        RecycleUtils.recursiveRecycle(recycleHalfList);
        for (int i = 0; i < halfSize; i++)
            mRecycleList.remove(0);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView i = new ImageView(mContext);

        try {
            i.setImageResource(mResources.get(position));
        } catch (OutOfMemoryError e) {
            if (mRecycleList.size() <= parent.getChildCount()) {
                throw e;
            }
            Log.w(this + "", e.toString());
            recycleHalf();
            System.gc();
            return getView(position, convertView, parent);
        }
        i.setAdjustViewBounds(true);
        i.setLayoutParams(new Gallery.LayoutParams(Gallery.LayoutParams.MATCH_PARENT,
                Gallery.LayoutParams.MATCH_PARENT));
        mRecycleList.add(new WeakReference<View>(i));
        return i;
    }
}

