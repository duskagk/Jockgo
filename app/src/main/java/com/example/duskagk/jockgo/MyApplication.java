package com.example.duskagk.jockgo;

import android.app.Application;

public class MyApplication extends Application {
    private boolean mGlobalLogin = false;
    private int mGlobalNo;
    private String mGlobalName;
    private String mGlobalSchool;

    public int getNo() {
        return mGlobalNo;
    }

    public boolean isLogin() {
        return mGlobalLogin;
    }

    public String getName() {
        return mGlobalName;
    }

    public String getSchool() {
        return mGlobalSchool;
    }

    public void setLogin(boolean mGlobalLogin) {
        this.mGlobalLogin = mGlobalLogin;
    }

    public void setName(String mGlobalName) {
        this.mGlobalName = mGlobalName;
    }

    public void setNo(int mGlobalNo) {
        this.mGlobalNo = mGlobalNo;
    }

    public void setSchool(String mGlobalSchool) {
        this.mGlobalSchool = mGlobalSchool;
    }
}
