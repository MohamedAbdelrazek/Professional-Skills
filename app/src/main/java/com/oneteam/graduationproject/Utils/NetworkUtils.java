package com.oneteam.graduationproject.Utils;

import android.net.Uri;
import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;

import static com.oneteam.graduationproject.Utils.Constant.LOGIN_URL;

/**
 * Created by ehabhamdy on 2/17/17.
 */

public class NetworkUtils {
    final static String TAG = "NetworkUtils";
    final static String KEY_USERNAME = "username";
    final static String KEY_PASSWORD = "password";

    public static URL buildLoginUrl(String email, String password) {
        Uri builtUri = Uri.parse(LOGIN_URL).buildUpon()
                .appendQueryParameter(KEY_USERNAME, email)
                .appendQueryParameter(KEY_PASSWORD, password)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {

        }
        Log.v(TAG, "Built URI " + url);
        return url;
    }
}
