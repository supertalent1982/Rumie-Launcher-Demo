package com.jsdev.ruime.fragments;

import android.content.ComponentName;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.jsdev.ruime.CodeGenerator;
import com.jsdev.ruime.R;
import com.jsdev.ruime.adapters.GridAdapter;
import com.jsdev.ruime.structures.AppInfo;
import com.jsdev.rumie.utility.PrefsHelper;

import java.util.List;

public class AdminGrid extends Fragment {

    private static final String TAG = "AdminGrid";
    public static final String CODE_GENERATOR = "Code Generator";
    public static final String PACKAGE_NAME = "com.jsdev.ruime.CodeGenerator";
    private GridAdapter gridAdapter;

    private List<AppInfo> appList;

    private RelativeLayout adminOverlay;
    private EditText adminPass;
    private Button adminSubmit;

    private boolean loggedIn = false;

    public static AdminGrid createFragment(List<AppInfo> apps, Drawable icon) {
        AppInfo codeApp = new AppInfo(CODE_GENERATOR, PACKAGE_NAME, icon);
        if (icon == null) {
            Log.w(TAG,"Icon is NULL");
        }
        apps.add(codeApp);

        AdminGrid fragment = new AdminGrid();
        fragment.setApps(apps);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.grid_admin, null);

        if (appList == null) {
            appList = PrefsHelper.getPackages(getActivity());
        } else {
            Log.d(TAG, "List size: " + appList.size());

            GridView grid = (GridView) view.findViewById(R.id.adminGrid);
            gridAdapter = new GridAdapter(getActivity(), appList);

            adminPass = (EditText) view.findViewById(R.id.adminEdit);
            adminOverlay = (RelativeLayout) view.findViewById(R.id.adminOverlay);
            adminSubmit = (Button) view.findViewById(R.id.adminSubmit);

            grid.setAdapter(gridAdapter);

            adminSubmit.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    String pass = adminPass.getText().toString().trim();
                    System.out.println("Pass: " + pass);

                    if (pass.equals("rumie1984")) {
                        loggedIn = true;
                        adminOverlay.setVisibility(View.GONE);
                        adminPass.setText("");
                    } else {
                        Toast.makeText(getActivity(), "Incorrect password.", Toast.LENGTH_LONG).show();
                        adminPass.setText("");
                    }
                }
            });

            grid.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                    if (loggedIn) {
                        if (pos != gridAdapter.getCount() - 1) {
                            AppInfo app = (AppInfo) gridAdapter.getItem(pos);

                            Intent startApp = new Intent();
                            ComponentName component = new ComponentName(app.getPackageName(), app.getClassName());
                            startApp.setComponent(component);
                            startApp.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startApp.setAction(Intent.ACTION_MAIN);

                            startActivity(startApp);
                        } else {
                            Intent startCode = new Intent(getActivity(), CodeGenerator.class);
                            startCode.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                            startActivity(startCode);
                        }
                    }
                }
            });
        }

        return view;
    }

    protected void setApps(List<AppInfo> apps) {
        appList = apps;
    }
}
