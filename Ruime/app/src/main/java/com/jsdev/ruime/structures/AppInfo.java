package com.jsdev.ruime.structures;

import android.graphics.drawable.Drawable;

public class AppInfo {
    private Drawable imageDrawable;

    private String appName;
    private String packageName;
    private String className;

    public AppInfo(String name, String packageName, Drawable icon) {
        this.imageDrawable = icon;
        this.appName = name;
        this.packageName = packageName;
    }

    public String getAppName() {
        return appName;
    }

    public String getClassName() {
        return className;
    }

    public Drawable getIconDrawable() {
        return imageDrawable;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setClassName(String name) {
        this.className = name;
    }
}
