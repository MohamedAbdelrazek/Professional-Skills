package com.oneteam.graduationproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

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

    public void createLoginSession(String name, String email) {
        zEditor.putBoolean(IS_LOGIN, true);

        zEditor.putString(KEY_NAME, name);

        zEditor.putString(KEY_EMAIL, email);
        zEditor.commit();
    }

    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     */
    public void checkLogin() {
        // Check login status
        if (this.isLoggedIn()) {
            // user is not logged in redirect him to Login Activity
            zContext.startActivity(new Intent(zContext, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }

    }


    /**
     * Get stored session data
     */
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        // user name
        user.put(KEY_NAME, zPref.getString(KEY_NAME, null));

        // user email id
        user.put(KEY_EMAIL, zPref.getString(KEY_EMAIL, null));

        // return user
        return user;
    }

    /**
     * Clear session details
     */
    public void logout() {
        zEditor.clear();
        zEditor.commit();
        zContext.startActivity(new Intent(zContext, LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    public boolean isLoggedIn() {
        return zPref.getBoolean(IS_LOGIN, false);
    }
}