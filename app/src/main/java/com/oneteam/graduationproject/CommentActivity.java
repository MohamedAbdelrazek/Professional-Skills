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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.oneteam.graduationproject.Utils.NetworkUtils;
import com.oneteam.graduationproject.adapters.CommentAdapter;
import com.oneteam.graduationproject.models.CommentModel;
import com.oneteam.graduationproject.models.QuestionModel;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

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
                getSupportLoaderManager().restartLoader(LOADER_ID, null, CommentActivity.this);
            }
        });
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
