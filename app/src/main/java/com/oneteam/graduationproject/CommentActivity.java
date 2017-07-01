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
import com.oneteam.graduationproject.adapters.CommentAdapter;
import com.oneteam.graduationproject.models.CommentModel;
import com.oneteam.graduationproject.models.QuestionModel;

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

public class CommentActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<CommentModel>> {

    private QuestionModel mQuestionModel;
    private int mQId;
    private RecyclerView mRecyclerView;
    private CommentAdapter mAdapter;
    private final int LOADER_ID = 101;
    @Bind(R.id.user_name)
    TextView userNameTxtView;
    @Bind(R.id.title)
    TextView titleTxtView;
    @Bind(R.id.content)
    TextView contentTxtView;
    @Bind(R.id.comment_edit_txt)
    EditText commentEditTxt;
    @Bind(R.id.comment_button)
    Button commentBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        ButterKnife.bind(this);
        mQuestionModel = getIntent().getParcelableExtra(Intent.EXTRA_TEXT);
        mQId = mQuestionModel.getId();
        setQuestionContent();
        initViews();
        getSupportLoaderManager().initLoader(LOADER_ID, null, this);
        commentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (commentEditTxt.getText().toString().length() > 1) {
                    addComment();
                    getSupportLoaderManager().restartLoader(LOADER_ID, null, CommentActivity.this);

                } else {
                    Toast.makeText(CommentActivity.this, "comment empty, Write Something !", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void addComment() {


        JSONObject JS = new JSONObject();
        try {
            JS.put(Constant.QAUTHORNAME, new UserSession(this).getUserDetails().get(UserSession.KEY_NAME));
            JS.put(Constant.QCONTENT, commentEditTxt.getText().toString());
            JS.put(Constant.QUSERID, mQuestionModel.getUserId());
            JS.put(Constant.QUESTION_ID, mQuestionModel.getId());


        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.POST, Constant.ADD_COMMENT_URL, JS,
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
        getSupportLoaderManager().restartLoader(LOADER_ID, null, CommentActivity.this);
        commentEditTxt.setText("");

    }


    private void initViews() {
        mRecyclerView = (RecyclerView) findViewById(R.id.commen_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));


    }

    private void setQuestionContent() {
        userNameTxtView.setText(mQuestionModel.getAuthorName());
        titleTxtView.setText(mQuestionModel.getTitle());
        contentTxtView.setText(mQuestionModel.getContent());
    }

    @Override
    public Loader<ArrayList<CommentModel>> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<ArrayList<CommentModel>>(this) {
            ArrayList<CommentModel> questionModel = new ArrayList<>();

            @Override
            protected void onStartLoading() {
                if (args != null)
                    deliverResult(questionModel);
                else
                    forceLoad();
            }

            @Override
            public ArrayList<CommentModel> loadInBackground() {

                URL userRequestUrl = NetworkUtils.getComments(mQId);

                try {
                    String JsonUserResponse = NetworkUtils.getResponseFromHttpUrl(userRequestUrl);

                    return Parser.getComments(JsonUserResponse);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            public void deliverResult(ArrayList<CommentModel> data) {
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<CommentModel>> loader, ArrayList<CommentModel> data) {

        Collections.reverse(data);
        mAdapter = new CommentAdapter(data, new CommentAdapter.onRecyclerClickListener() {
            @Override
            public void onClick(CommentModel commentModel) {

            }
        });
        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    public void onLoaderReset(Loader<ArrayList<CommentModel>> loader) {

    }
}
