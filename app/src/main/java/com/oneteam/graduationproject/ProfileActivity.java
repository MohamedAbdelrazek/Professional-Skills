package com.oneteam.graduationproject;

import android.content.Intent;
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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.oneteam.graduationproject.Utils.Constant;
import com.oneteam.graduationproject.Utils.NetworkUtils;
import com.oneteam.graduationproject.adapters.PostsAdapter;
import com.oneteam.graduationproject.models.QuestionModel;
import com.oneteam.graduationproject.models.UserModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ProfileActivity extends AppCompatActivity {
    private UserSession zUserSession;
    private Toolbar toolbar;
    private String username;
    private RecyclerView mRecyclerView;
    private PostsAdapter mAdapter;
    public static final int LOADER_ID = 201;

    @Bind(R.id.user_profile_name)
    TextView userNameTextView;
    @Bind(R.id.mobile_number)
    TextView mobileNumberTextView;
    @Bind(R.id.email)
    TextView emailTextView;
    @Bind(R.id.post_content_edit_txt)
    EditText postContentEditTxt;
    @Bind(R.id.post_title_edit_txt)
    EditText postTitleEditTxt;
    @Bind(R.id.post_btn)
    Button postBtn;


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
        if (!username.equalsIgnoreCase(new UserSession(getApplicationContext()).getUserDetails().get(UserSession.KEY_EMAIL))) {
            postTitleEditTxt.setVisibility(View.GONE);
            postContentEditTxt.setVisibility(View.GONE);
            postBtn.setVisibility(View.GONE);
        }
        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QuestionModel questionModel = new QuestionModel();
                questionModel.setContent(postContentEditTxt.getText().toString());
                questionModel.setTitle(postTitleEditTxt.getText().toString());
                questionModel.setAuthorName(new UserSession(getApplicationContext()).getUserDetails().get(UserSession.KEY_NAME));
                questionModel.setUserId(Integer.parseInt(new UserSession(getApplicationContext()).getUserDetails().get(UserSession.KEY_ID)));

                if (postTitleEditTxt.getText().toString().length() < 10 && postContentEditTxt.length() < 10) {
                    Toast.makeText(ProfileActivity.this, "too short !", Toast.LENGTH_SHORT).show();

                } else {
                    addQuestion(questionModel);
                    postContentEditTxt.clearFocus();
                    postContentEditTxt.setText("");
                    postTitleEditTxt.clearFocus();
                    postTitleEditTxt.setText("");
                }

                getSupportLoaderManager().restartLoader(LOADER_ID, null, userQuestionLoader);

            }
        });
    }

    private void initViews() {
        mRecyclerView = (RecyclerView) findViewById(R.id.user_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));


    }

    private void addQuestion(QuestionModel mQuestionModel) {

        JSONObject JS = new JSONObject();
        try {
            JS.put(Constant.QAUTHORNAME, new UserSession(this).getUserDetails().get(UserSession.KEY_NAME));
            JS.put(Constant.QCONTENT, mQuestionModel.getContent());
            JS.put(Constant.QTITLE, mQuestionModel.getTitle());
            JS.put(Constant.QUSERID, mQuestionModel.getUserId());


        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.POST, Constant.ADD_QUESTION_URL, JS,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("ZOKA", "" + response.toString());

                    }
                }, new Response.ErrorListener() {


            @Override
            public void onErrorResponse(VolleyError error) {

                Log.i("ZOKA", "" + error.toString());


            }

        }) {

            /**
             * Passing some request headers
             */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }

        };
        // Adding request to request queue
        Volley.newRequestQueue(this).add(jsonObjReq);
        getSupportLoaderManager().restartLoader(LOADER_ID, null, userQuestionLoader);
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
            mid = data.getId();

            getSupportLoaderManager().initLoader(LOADER_ID, null, userQuestionLoader);

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

            Collections.reverse(data);
            mAdapter = new PostsAdapter(data, new PostsAdapter.onRecyclerClickListener() {
                @Override
                public void onClick(QuestionModel questionModel) {
                    Intent intent = new Intent(getApplicationContext(), CommentActivity.class);
                    intent.putExtra(Intent.EXTRA_TEXT, questionModel);
                    startActivity(intent);
                }
            });
            mRecyclerView.setAdapter(mAdapter);

        }

        @Override
        public void onLoaderReset(Loader<ArrayList<QuestionModel>> loader) {

        }
    };

}
