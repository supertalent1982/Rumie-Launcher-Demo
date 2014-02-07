package com.jsdev.ruime.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jsdev.ruime.R;
import com.jsdev.ruime.structures.AppInfo;

import java.util.List;

public class GridAdapter extends BaseAdapter {

    private List<AppInfo> appList;
    private Context context;

    public GridAdapter(Context context, List<AppInfo> data) {
        this.appList = data;
        this.context = context;
    }

    @Override
    public int getCount() {
        return appList.size();
    }

    @Override
    public Object getItem(int position) {
        return appList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;

        if (convertView == null) {
            view = View.inflate(context, R.layout.grid_item, null);
        } else {
            view = convertView;
        }

        ImageView image = (ImageView) view.findViewById(R.id.gridImage);
        TextView text = (TextView) view.findViewById(R.id.gridText);

        image.setImageDrawable(appList.get(position).getIconDrawable());
        text.setText(appList.get(position).getAppName());

        return view;
    }
}
