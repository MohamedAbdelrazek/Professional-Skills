package com.oneteam.graduationproject;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.oneteam.graduationproject.Utils.NetworkUtils;
import com.oneteam.graduationproject.models.UserModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

public class ProfileActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<UserModel> {
    private UserSession zUserSession;
    private Toolbar toolbar;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        zUserSession = new UserSession(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        username = getIntent().getStringExtra("username");
        getSupportLoaderManager().initLoader(23, null, this);
    }

    @Override
    public Loader<UserModel> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<UserModel>(this) {
            UserModel userModel = new UserModel();

            @Override
            protected void onStartLoading() {
                if (args != null)
                    deliverResult(userModel);
                else
                    forceLoad();
            }

            @Override
            public UserModel loadInBackground() {

                URL userRequestUrl = NetworkUtils.getUserUrl(username);

                try {
                    String JsonUserResponse = NetworkUtils.getResponseFromHttpUrl(userRequestUrl);
                    JSONObject jsonObject = new JSONObject(JsonUserResponse);
                    userModel.setFirstName(jsonObject.getString("firstName"));
                    userModel.setLastName(jsonObject.getString("lastName"));
                    userModel.setEmailAddress(jsonObject.getString("userName"));
                    userModel.setMobileNumber(jsonObject.getString("phone"));
                    userModel.setAddress(jsonObject.getString("address"));

                    Log.i("ZOKA", "USER MODEL == " + userModel.getAddress());

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return userModel;
            }

            @Override
            public void deliverResult(UserModel data) {
                super.deliverResult(data);
            }
        };
    }


    @Override
    public void onLoadFinished(Loader<UserModel> loader, UserModel data) {
        Toast.makeText(this, "انا زهقت !", Toast.LENGTH_SHORT).show();

        Log.i("ZOKA", "profile =>  " + data.getEmailAddress());
    }

    @Override
    public void onLoaderReset(Loader<UserModel> loader) {

    }
}
