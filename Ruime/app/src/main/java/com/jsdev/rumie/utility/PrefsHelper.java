package com.jsdev.rumie.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

import com.jsdev.ruime.structures.AppInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PrefsHelper {
    public static final String TAG = "PrefsHelper";
    public static final int VERSION_NUMBER = 1;
    public static final String NAME = "name";
    public static final String PACKAGE = "package";
    public static final String CLASS = "class";
    public static final String PACKAGES = "packages";
    public static final String TIME = "time";
    public static final String VERSION = "version";
    public static final String USER_DATA = "User Data";

    public static boolean isActive = false;

    public static void setPackages(Context context, List<AppInfo> appInfoList) {
        final SharedPreferences prefs = getPrefs(context);
        final Editor edit = prefs.edit();
        final JSONArray packageArray;
        try {
            if (prefs.contains(PACKAGES)) {
                packageArray = new JSONArray(prefs.getString(PACKAGES, null));
            } else {
                packageArray = new JSONArray();
            }

            for (AppInfo appInfo : appInfoList) {
                JSONObject object = new JSONObject();
                object.put(NAME, appInfo.getAppName());
                object.put(PACKAGE, appInfo.getPackageName());
                object.put(CLASS, appInfo.getClassName());

                packageArray.put(object);
            }

            edit.putString(PACKAGES, packageArray.toString());
            edit.commit();
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    public static void clearPackages(Context context) {
        Editor edit = getPrefs(context).edit();
        edit.remove(PACKAGES);
        edit.commit();
    }

    public static void clearPrefs(Context context) {
        Editor edit = getPrefs(context).edit();

        edit.clear();
        edit.commit();
    }

    public static void clearTime(Context context) {
        Editor edit = getPrefs(context).edit();
        edit.remove(TIME);
        edit.commit();
    }

    public static List<AppInfo> getPackages(Context context) {
        if (getPrefs(context).contains(PACKAGES)) {
            List<AppInfo> packageList = new ArrayList<AppInfo>();
            PackageManager packManager = context.getPackageManager();

            try {
                long sTime = System.currentTimeMillis();
                Log.i("PrefsHelper", "getPackages");
                JSONArray packageArray = new JSONArray(getPrefs(context).getString(PACKAGES, null));

                for (int i = 0; i < packageArray.length(); i++) {
                    JSONObject app = packageArray.getJSONObject(i);
                    AppInfo info = new AppInfo(app.getString(NAME), app.getString(PACKAGE), packManager.getApplicationIcon(app.getString(PACKAGE)));

                    if (app.has(CLASS))
                        info.setClassName(app.getString(CLASS));

                    packageList.add(info);
                }

                Log.i(TAG, "loaded [" + (System.currentTimeMillis() - sTime) + " msec]");

            } catch (JSONException e) {
                Log.e(TAG, e.getMessage(), e);
            } catch (NameNotFoundException e) {
                Log.e(TAG, e.getMessage(), e);
            }

            return packageList;
        } else {
            return Collections.emptyList();
        }
    }

    public static String getRunningPackage(Context context) {
        return getPrefs(context).getString(PACKAGE, null);
    }

    public static long getTime(Context context) {
        return getPrefs(context).getLong(TIME, 0);
    }

    public static boolean shouldReload(Context context) {
        int oldVersion = getPrefs(context).getInt(VERSION, -1);

        return oldVersion != VERSION_NUMBER;

    }

    public static void setRunningPackage(Context context, String pack) {
        Editor edit = getPrefs(context).edit();

        edit.putString(PACKAGE, pack);
        edit.commit();
    }

    public static void setTime(Context context, long time) {
        Editor edit = getPrefs(context).edit();
        edit.putLong(TIME, time);
        edit.commit();
    }

    public static void updatedPackage(Context context) {
        Editor edit = getPrefs(context).edit();
        edit.putInt(VERSION, VERSION_NUMBER);
        edit.commit();
    }

    private static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(USER_DATA, Context.MODE_PRIVATE);
    }
}
