package com.z_tech.mylauncher.bean;

import android.graphics.drawable.Drawable;

public class GridInfo {

    private Drawable appIcon;
    private String title;
    private String packageName;

    public GridInfo(Drawable appIcon,String title,String packageName) {

        this.appIcon = appIcon;
        this.title = title;
        this.packageName = packageName;

    }

    public Drawable getAppIcon() {
        return appIcon;
    }

    public String getTitle() {
        return title;
    }

    public String getPackageName(){
        return packageName;
    }

}
