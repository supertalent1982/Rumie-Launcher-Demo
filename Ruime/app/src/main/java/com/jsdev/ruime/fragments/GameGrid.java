package com.jsdev.ruime.fragments;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.jsdev.ruime.R;
import com.jsdev.ruime.adapters.GridAdapter;
import com.jsdev.ruime.services.GameAlarm;
import com.jsdev.ruime.structures.AppInfo;
import com.jsdev.ruime.structures.Code;
import com.jsdev.rumie.utility.CodeHelper;
import com.jsdev.rumie.utility.PrefsHelper;

import java.util.Calendar;
import java.util.List;

public class GameGrid extends Fragment {

    private static final String TAG = "GameGrid";
    private GridAdapter gridAdapter;

    private static List<AppInfo> appList;

    public static GameGrid createFragment(List<AppInfo> apps) {
        if (apps == null) {
            Log.w(TAG, "Game App List is null");
        }
        GameGrid fragment = new GameGrid();
        fragment.setApps(apps);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_grid, null);

        if (appList == null) {
            appList = PrefsHelper.getPackages(getActivity());
        } else {
            Log.d(TAG, "Game size: " + appList.size());
            GridView grid = (GridView) view.findViewById(R.id.mainGrid);
            gridAdapter = new GridAdapter(getActivity(), appList);

            grid.setAdapter(gridAdapter);

            grid.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, final int pos, long id) {
                    if (PrefsHelper.isActive) {
                        AppInfo app = (AppInfo) gridAdapter.getItem(pos);

                        PrefsHelper.setTime(getActivity(), System.currentTimeMillis());
                        PrefsHelper.setRunningPackage(getActivity(), app.getPackageName());

                        Intent startApp = new Intent();
                        ComponentName component = new ComponentName(app.getPackageName(), app.getClassName());
                        startApp.setComponent(component);
                        startApp.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                        startApp.setAction(Intent.ACTION_MAIN);

                        startActivity(startApp);

                        return;
                    }

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Enter Game Code:");

                    final EditText edit = new EditText(getActivity());
                    builder.setView(edit);

                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Code code = CodeHelper.isValid(getActivity(), edit.getText().toString());

                            if (code != null) {
                                dialog.dismiss();

                                PrefsHelper.isActive = true;

                                Calendar c = Calendar.getInstance();
                                c.setTimeInMillis(c.getTimeInMillis() + (code.validDuration * 60000));

                                AlarmManager am = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
                                Intent i = new Intent(getActivity(), GameAlarm.class);
                                i.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
                                PendingIntent pi = PendingIntent.getBroadcast(getActivity(), 0, i, 0);
                                am.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pi);

                                AppInfo app = (AppInfo) gridAdapter.getItem(pos);

                                PrefsHelper.setTime(getActivity(), System.currentTimeMillis());
                                PrefsHelper.setRunningPackage(getActivity(), app.getPackageName());

                                Intent startApp = new Intent();
                                ComponentName component = new ComponentName(app.getPackageName(), app.getClassName());
                                startApp.setComponent(component);
                                startApp.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                                startApp.setAction(Intent.ACTION_MAIN);

                                startActivity(startApp);
                            } else {
                                edit.setText("");
                                Toast.makeText(getActivity(), "Error, invalid code.", Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    builder.create().show();
                }
            });
        }

        return view;
    }

    protected void setApps(List<AppInfo> apps) {
        appList = apps;
    }
}
