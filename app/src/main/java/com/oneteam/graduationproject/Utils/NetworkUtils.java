package com.oneteam.graduationproject.Utils;

import android.net.Uri;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.oneteam.graduationproject.Utils.Constant.LOGIN_URL;


public class NetworkUtils {
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

            Log.i("ZOKA", "URL == " + url);
        } catch (MalformedURLException e) {

        }
        return url;
    }

    public static String getLoginResponse(URL url) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public static Boolean getStatus(String jsonLoginRespose) {
        JSONObject _json = null;
        try {
            _json = new JSONObject(jsonLoginRespose);
        } catch (JSONException e) {

        }
        try {
            return _json.getBoolean("statue");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
