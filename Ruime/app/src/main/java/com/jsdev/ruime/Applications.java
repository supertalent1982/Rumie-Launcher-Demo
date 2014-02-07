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
    private List<ResolveInfo> activityList;
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

        for (int i = 0; i < packs.size(); i++) {
            PackageInfo packInfo = packs.get(i);

            if ((!getSystemPackages) && (packInfo.versionName == null)) {
                continue;
            }

            AppInfo newInfo = new AppInfo(packInfo.applicationInfo.loadLabel(packMan).toString(), packInfo.packageName, packInfo.applicationInfo.loadIcon(packMan));
            appList.add(newInfo);
        }

        if (activityList == null) {
            return;
        }

        String tempName = "";

        for (int i = 0; i < appList.size(); ++i) {
            tempName = appList.get(i).getPackageName();

            for (int j = 0; j < activityList.size(); ++j) {
                if (tempName.equals(activityList.get(
                        j).activityInfo.applicationInfo.packageName)) {
                    appList.get(i).setClassName(activityList.get(j).activityInfo.name);
                }
            }

            PrefsHelper.addPackage(context, appList.get(i));
        }
    }
}
