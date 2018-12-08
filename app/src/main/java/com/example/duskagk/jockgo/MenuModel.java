package com.example.duskagk.jockgo;

import android.content.Intent;

public class MenuModel {
    public String menuName, url;
    public boolean hasChildren, isGroup;
    Intent intent;

    public MenuModel(String menuName, boolean isGroup, boolean hasChildren, String url) {
        this.menuName = menuName;
        this.url = url;
        this.isGroup = isGroup;
        this.hasChildren = hasChildren;
    }

}
