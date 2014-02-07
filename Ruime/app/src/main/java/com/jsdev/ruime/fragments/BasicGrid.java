package com.jsdev.ruime.fragments;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.jsdev.ruime.R;
import com.jsdev.ruime.adapters.GridAdapter;
import com.jsdev.ruime.structures.AppInfo;
import com.jsdev.rumie.utility.PrefsHelper;

import java.util.List;

public class BasicGrid extends Fragment {

    private GridAdapter gridAdapter;

    private static List<AppInfo> appList;

    public static BasicGrid createFragment(List<AppInfo> apps) {
        BasicGrid fragment = new BasicGrid();
        fragment.setApps(apps);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_grid, null);

        if (appList == null) {
            appList = PrefsHelper.getPackages(getActivity());
        } else {
            System.out.println("List size: " + appList.size());

            GridView grid = (GridView) view.findViewById(R.id.mainGrid);
            gridAdapter = new GridAdapter(getActivity(), appList);

            grid.setAdapter(gridAdapter);

            grid.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                    AppInfo app = (AppInfo) gridAdapter.getItem(pos);

                    PrefsHelper.setTime(getActivity(), System.currentTimeMillis());
                    PrefsHelper.setRunningPackage(getActivity(), app.getPackageName());

                    Intent startApp = new Intent();
                    ComponentName component = new ComponentName(app.getPackageName(), app.getClassName());
                    startApp.setComponent(component);
                    startApp.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startApp.setAction(Intent.ACTION_MAIN);

                    startActivity(startApp);
                }
            });
        }

        return view;
    }

    protected void setApps(List<AppInfo> apps) {
        appList = apps;
    }
}
