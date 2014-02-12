package com.jsdev.ruime.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.jsdev.ruime.ListHelper;
import com.jsdev.ruime.PlayerActivity;
import com.jsdev.ruime.R;
import com.jsdev.ruime.fragments.AdminGrid;
import com.jsdev.ruime.fragments.BasicGrid;
import com.jsdev.ruime.fragments.GameGrid;
import com.jsdev.ruime.structures.AppInfo;
import com.jsdev.rumie.utility.PrefsHelper;

import java.util.ArrayList;
import java.util.List;

public class LauncherAdapter extends FragmentPagerAdapter {

    private final AppInfo demoVideoApp;
    private Context context;

    public LauncherAdapter(FragmentManager fm, Context context) {
        super(fm);

        this.context = context;
        Drawable demoLauncherIcon = context.getResources().getDrawable(R.drawable.ic_demo_launcher);
        String demoLauncherName = context.getResources().getString(R.string.demo);
        demoVideoApp = new AppInfo(demoLauncherName, "com.jsdev.ruime", demoLauncherIcon);
        demoVideoApp.setClassName(PlayerActivity.class.getName());
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return BasicGrid.createFragment(filterEducation());
            case 1:
                return GameGrid.createFragment(filterGames());
            case 2:
                return AdminGrid.createFragment(filterAdmin(), context.getResources().getDrawable(R.drawable.keyicon));
        }

        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return context.getString(R.string.learn);
            case 1:
                return context.getString(R.string.play);
            case 2:
                return context.getString(R.string.admin);
        }

        return null;
    }

    private List<AppInfo> filterAdmin() {
        List<AppInfo> apps = new ArrayList<AppInfo>();
        List<AppInfo> packs = PrefsHelper.getPackages(context);

        for (AppInfo pack : packs) {
            if (ListHelper.containsPackage(context, pack.getPackageName(), ListHelper.LIST_ADMIN)) {
                apps.add(pack);
            }
        }

        return apps;
    }

    private List<AppInfo> filterEducation() {
        List<AppInfo> apps = new ArrayList<AppInfo>();
        List<AppInfo> packs = PrefsHelper.getPackages(context);

        for (AppInfo pack : packs) {
            if (ListHelper.containsPackage(context, pack.getPackageName(), ListHelper.LIST_EDUCATION)) {
                apps.add(pack);
            }
        }
        apps.add(demoVideoApp);
        return apps;
    }

    private List<AppInfo> filterGames() {
        List<AppInfo> apps = new ArrayList<AppInfo>();
        List<AppInfo> packs = PrefsHelper.getPackages(context);

        for (AppInfo pack : packs) {
            if (ListHelper.containsPackage(context, pack.getPackageName(), ListHelper.LIST_GAMES)) {
                System.out.println(pack.getPackageName());
                apps.add(pack);
            }
        }
        return apps;
    }
}
