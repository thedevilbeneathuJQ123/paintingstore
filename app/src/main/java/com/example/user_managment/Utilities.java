package com.example.user_managment;

import android.os.Bundle;

public class Utilities {
    private Bundle seeDetailsBundle;
    private static Utilities instance;
    public static Utilities getInstance()
    {
        if (instance == null)
            instance = new Utilities();

        return instance;
    }
    public Utilities()
    {
        this.seeDetailsBundle=new Bundle();
    }

    public void AddSeeDetailsBundlestring(String key, String message) {
        this.seeDetailsBundle.putString(key,message);
    }

    public String getStringSeeDetailsBundle(String key) {
        return this.seeDetailsBundle.getString(key);

    }
}
