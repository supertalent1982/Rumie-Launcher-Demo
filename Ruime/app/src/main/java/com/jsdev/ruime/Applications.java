package com.jsdev.ruime;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import com.jsdev.ruime.structures.AppInfo;
import com.jsdev.rumie.utility.PrefsHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Applications {
    private final List<ResolveInfo> activityList;
    private Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
    private PackageManager packMan = null;
    private Context context;

    public Applications(Context context, PackageManager packageManager) {
        this.context = context;

        packMan = packageManager;
        activityList = this.createActivityList();
        this.createPackageList(false);
    }

    public List<ResolveInfo> getActivityList() {
        return activityList;
    }

    private List<ResolveInfo> createActivityList() {
        List<ResolveInfo> aList = packMan.queryIntentActivities(mainIntent, 0);

        Collections.sort(aList, new ResolveInfo.DisplayNameComparator(packMan));

        return aList;
    }

    private void createPackageList(boolean getSystemPackages) {
        List<AppInfo> appList = new ArrayList<AppInfo>();
        List<PackageInfo> packs = packMan.getInstalledPackages(0);

        for (PackageInfo packInfo : packs) {
            if ((!getSystemPackages) && (packInfo.versionName == null)) {
                continue;
            }

            AppInfo appInfo = new AppInfo(packInfo.applicationInfo.loadLabel(packMan).toString(), packInfo.packageName, null);
            appList.add(appInfo);
        }

        if (activityList == null) {
            return;
        }

        String tempName = "";

        for (AppInfo appInfo : appList) {
            tempName = appInfo.getPackageName();

            for (ResolveInfo resolveInfo : activityList) {
                if (tempName.equals(resolveInfo.activityInfo.applicationInfo.packageName)) {
                    appInfo.setClassName(resolveInfo.activityInfo.name);
                }
            }
        }
        PrefsHelper.setPackages(context, appList);
    }
}
