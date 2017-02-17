package com.oneteam.graduationproject;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Mohamed AbdelraZek on 2/17/2017.
 */

public class UserSession {
    SharedPreferences zPref;
    SharedPreferences.Editor zEditor;
    Context zContext;
    final int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "AndroidGProject";
    private static final String IS_LOGIN = "IsLoggedIn";
    public static final String KEY_NAME = "name";
    public static final String KEY_EMAIL = "email";

    public UserSession(Context context) {
        this.zContext = context;
        zPref = zContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        zEditor = zPref.edit();
    }
}
