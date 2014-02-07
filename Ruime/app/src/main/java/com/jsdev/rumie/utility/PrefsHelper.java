package com.jsdev.rumie.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

import com.jsdev.ruime.structures.AppInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PrefsHelper {

    public static final int VERSION_NUMBER = 1;

    public static boolean isActive = false;

    public static void addPackage(Context context, AppInfo info) {
        SharedPreferences prefs = getPrefs(context);
        Editor edit = prefs.edit();

        try {
            JSONObject object = new JSONObject();
            object.put("name", info.getAppName());
            object.put("package", info.getPackageName());
            object.put("class", info.getClassName());

            System.out.println("Name: " + info.getAppName() + " Package: " + info.getPackageName() + " Class: " + info.getClassName());

            JSONArray packageArray;

            if (prefs.contains("packages")) {
                packageArray = new JSONArray(prefs.getString("packages", null));
            } else {
                packageArray = new JSONArray();
            }

            packageArray.put(object);
            edit.putString("packages", packageArray.toString());
            edit.commit();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void clearPackages(Context context) {
        Editor edit = getPrefs(context).edit();
        edit.remove("packages");
        edit.commit();
    }

    public static void clearPrefs(Context context) {
        Editor edit = getPrefs(context).edit();

        edit.clear();
        edit.commit();
    }

    public static void clearTime(Context context) {
        Editor edit = getPrefs(context).edit();
        edit.remove("time");
        edit.commit();
    }

    public static List<AppInfo> getPackages(Context context) {
        if (getPrefs(context).contains("packages")) {
            List<AppInfo> packageList = new ArrayList<AppInfo>();
            PackageManager packManager = context.getPackageManager();

            try {
                JSONArray packageArray = new JSONArray(getPrefs(context).getString("packages", null));

                for (int i = 0; i < packageArray.length(); i++) {
                    JSONObject app = packageArray.getJSONObject(i);
                    AppInfo info = new AppInfo(app.getString("name"), app.getString("package"), packManager.getApplicationIcon(app.getString("package")));

                    if (app.has("class"))
                        info.setClassName(app.getString("class"));

                    packageList.add(info);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (NameNotFoundException e) {
                e.printStackTrace();
            }

            return packageList;
        } else {
            return null;
        }
    }

    public static String getRunningPackage(Context context) {
        return getPrefs(context).getString("package", null);
    }

    public static long getTime(Context context) {
        return getPrefs(context).getLong("time", 0);
    }

    public static boolean shouldReload(Context context) {
        int oldVersion = getPrefs(context).getInt("version", -1);

        if (oldVersion != VERSION_NUMBER) {
            return true;
        }

        return false;
    }

    public static void setRunningPackage(Context context, String pack) {
        Editor edit = getPrefs(context).edit();

        edit.putString("package", pack);
        edit.commit();
    }

    public static void setTime(Context context, long time) {
        Editor edit = getPrefs(context).edit();
        edit.putLong("time", time);
        edit.commit();
    }

    public static void updatedPackage(Context context) {
        Editor edit = getPrefs(context).edit();
        edit.putInt("version", VERSION_NUMBER);
        edit.commit();
    }

    private static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences("User Data", Context.MODE_PRIVATE);
    }
}
