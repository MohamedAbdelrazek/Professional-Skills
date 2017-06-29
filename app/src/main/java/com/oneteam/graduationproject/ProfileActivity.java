package com.oneteam.graduationproject;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import com.oneteam.graduationproject.Utils.NetworkUtils;
import com.oneteam.graduationproject.adapters.PostsAdapter;
import com.oneteam.graduationproject.models.QuestionModel;
import com.oneteam.graduationproject.models.UserModel;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ProfileActivity extends AppCompatActivity {
    private UserSession zUserSession;
    private Toolbar toolbar;
    private String username;
    private RecyclerView mRecyclerView;
    private PostsAdapter mAdapter;


    @Bind(R.id.user_profile_name)
    TextView userNameTextView;
    @Bind(R.id.mobile_number)
    TextView mobileNumberTextView;
    @Bind(R.id.email)
    TextView emailTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        zUserSession = new UserSession(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initViews();
        username = getIntent().getStringExtra("username");
        getSupportLoaderManager().initLoader(23, null, userInfoLoader);
    }

    private void initViews() {
        mRecyclerView = (RecyclerView) findViewById(R.id.user_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));


    }

    private LoaderManager.LoaderCallbacks<UserModel> userInfoLoader = new LoaderManager.LoaderCallbacks<UserModel>() {
        @Override
        public Loader<UserModel> onCreateLoader(int id, final Bundle args) {
            return new AsyncTaskLoader<UserModel>(getBaseContext()) {
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


                    String JsonUserResponse = null;
                    try {
                        JsonUserResponse = NetworkUtils.getResponseFromHttpUrl(userRequestUrl);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    userModel = Parser.getUserInfo(JsonUserResponse);
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
            userNameTextView.setText(data.getFirstName() + " " + data.getLastName());
            mobileNumberTextView.setText("" + data.getMobileNumber());
            emailTextView.setText("" + data.getEmailAddress());
            mid=data.getId();

             getSupportLoaderManager().initLoader(5, null, userQuestionLoader);

        }

        @Override
        public void onLoaderReset(Loader<UserModel> loader) {

        }


    };

    private String mid;
    private LoaderManager.LoaderCallbacks<ArrayList<QuestionModel>> userQuestionLoader = new LoaderManager.LoaderCallbacks<ArrayList<QuestionModel>>() {

        @Override
        public Loader<ArrayList<QuestionModel>> onCreateLoader(int id, final Bundle args) {
            return new AsyncTaskLoader<ArrayList<QuestionModel>>(getApplicationContext()) {
                ArrayList<QuestionModel> questionModel = new ArrayList<>();

                @Override
                protected void onStartLoading() {
                    if (args != null)
                        deliverResult(questionModel);
                    else
                        forceLoad();
                }

                @Override
                public ArrayList<QuestionModel> loadInBackground() {


                    URL userRequestUrl = NetworkUtils.getSingleUserPosts(mid);
                    Log.i("ZOKA", "inside Loader" + mid);

                    try {
                        String JsonUserResponse = NetworkUtils.getResponseFromHttpUrl(userRequestUrl);

                        return Parser.getAllPost(JsonUserResponse);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                public void deliverResult(ArrayList<QuestionModel> data) {
                    super.deliverResult(data);
                }
            };
        }

        @Override
        public void onLoadFinished(Loader<ArrayList<QuestionModel>> loader, ArrayList<QuestionModel> data) {
            mAdapter = new PostsAdapter(data, new PostsAdapter.onRecyclerClickListener() {
                @Override
                public void onClick(QuestionModel questionModel) {


                }
            });
            mRecyclerView.setAdapter(mAdapter);

        }

        @Override
        public void onLoaderReset(Loader<ArrayList<QuestionModel>> loader) {

        }
    };

}
