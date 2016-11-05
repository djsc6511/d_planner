package planner.com.d_planner.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import planner.com.d_planner.R;
import planner.com.d_planner.model.RegistIndivisualData;
import planner.com.d_planner.ui.util.AutoLayout;
import planner.com.d_planner.ui.util.StaticData;

/**
 * Created by soongu on 2016-11-02.
 */

public class RegistIndivisualAdapter extends ArrayAdapter<Object> {


    public LayoutInflater vl;
    private ArrayList<RegistIndivisualData> items = null;


    private StaticData mData = StaticData.GetInstance();
    private AutoLayout autoLayout = AutoLayout.GetInstance();

    private Context mContext;

    @SuppressWarnings("unchecked")
    public RegistIndivisualAdapter(Context context, int textViewResourceId,
                                   @SuppressWarnings("rawtypes") ArrayList getItems,
                                   LayoutInflater getInflater) {
        super(context, textViewResourceId, getItems);

        mContext = context;

        vl = getInflater;
        this.items = getItems;
        // TODO Auto-generated constructor stub

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {

            v = vl.inflate(R.layout.model_date_list_item, null);

            LinearLayout mainLayout = (LinearLayout) v
                    .findViewById(R.id.model_datelist_layout);

            autoLayout.setView(mainLayout);

        }

        RegistIndivisualData p = (RegistIndivisualData) items.get(position);

        if (p != null) {

            TextView tvDate = (TextView) v.findViewById(R.id.tvDate);
            Button btnCancel = (Button) v.findViewById(R.id.btnCancel);

            tvDate.setText(p.getWeek()+"     "+p.getStartTime()+"-"+p.getEndTime());

            btnCancel.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    items.remove(position);
                    notifyDataSetChanged();

                }
            });
        }

        return v;
    }

}

