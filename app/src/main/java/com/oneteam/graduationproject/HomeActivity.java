package com.oneteam.graduationproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.oneteam.graduationproject.Utils.NetworkUtils;
import com.oneteam.graduationproject.adapters.PostsAdapter;
import com.oneteam.graduationproject.models.QuestionModel;
import com.oneteam.graduationproject.models.UserModel;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

public class HomeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<QuestionModel>> {
    private UserSession zUserSession;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private RecyclerView mRecyclerView;
    private PostsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        zUserSession = new UserSession(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initNavigationDrawer();
        initViews();
        getSupportLoaderManager().initLoader(33, null, this);
        getSupportLoaderManager().initLoader(34, null, userInfoLoader);

    }

    private void initViews() {
        mRecyclerView = (RecyclerView) findViewById(R.id.home_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));


    }

    private void initNavigationDrawer() {
        zUserSession = new UserSession(this);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                int id = menuItem.getItemId();

                switch (id) {
                    case R.id.home:
                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                        startActivity(intent);
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.settings:
                        drawerLayout.closeDrawers();
                        Toast.makeText(getApplicationContext(), "Settings", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.search:
                        drawerLayout.closeDrawers();
                        Toast.makeText(HomeActivity.this, "Searching !", Toast.LENGTH_SHORT).show();
                        Intent intent2 = new Intent(getApplicationContext(), SearchActivity.class);
                        startActivity(intent2);
                        break;
                    case R.id.logout:
                        drawerLayout.closeDrawers();
                        zUserSession.logout();
                        break;
                }
                return true;
            }
        });

        View header = navigationView.getHeaderView(0);
        TextView tv_email = (TextView) header.findViewById(R.id.tv_email);
        tv_email.setText(zUserSession.zPref.getString(zUserSession.KEY_EMAIL, null));
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {

            @Override
            public void onDrawerClosed(View v) {
                super.onDrawerClosed(v);
            }

            @Override
            public void onDrawerOpened(View v) {
                super.onDrawerOpened(v);
            }
        };
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    public void openProfile(View view) {
        Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
        intent.putExtra("username", zUserSession.getUserDetails().get(UserSession.KEY_EMAIL));
        startActivity(intent);

    }


    @Override
    public Loader<ArrayList<QuestionModel>> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<ArrayList<QuestionModel>>(this) {
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

                URL userRequestUrl = NetworkUtils.getAllUserPosts();

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

                    URL userRequestUrl = NetworkUtils.getUserUrl(zUserSession.getUserDetails().get(UserSession.KEY_EMAIL));


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

            zUserSession.createLoginSession(data.getEmailAddress(),
                    data.getPassword(), data.getFirstName() + " " + data.getLastName(), data.getId());


        }

        @Override
        public void onLoaderReset(Loader<UserModel> loader) {

        }


    };

}
