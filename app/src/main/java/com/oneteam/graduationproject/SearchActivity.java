package com.oneteam.graduationproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.oneteam.graduationproject.Utils.NetworkUtils;
import com.oneteam.graduationproject.adapters.SearchAdapter;
import com.oneteam.graduationproject.models.UserModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<UserModel>> {


    private RecyclerView mRecyclerView;

    private SearchAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initViews();

        LoaderManager.LoaderCallbacks<ArrayList<UserModel>> callback = this;
        getSupportLoaderManager().initLoader(0, null, callback);
    }

    private void initViews() {
        mRecyclerView = (RecyclerView) findViewById(R.id.card_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public Loader<ArrayList<UserModel>> onCreateLoader(int id, final Bundle args) {
        final ArrayList<UserModel> userModelArrayList = new ArrayList<>();

        return new AsyncTaskLoader<ArrayList<UserModel>>(this) {

            @Override
            protected void onStartLoading() {
                if (args != null)
                    deliverResult(userModelArrayList);
                else
                    forceLoad();
            }

            @Override
            public ArrayList<UserModel> loadInBackground() {
                URL usersRequstUrl = NetworkUtils.getAllUsersUrl();

                try {
                    String JsonUsersResponse = NetworkUtils.getResponseFromHttpUrl(usersRequstUrl);
                    JSONObject jsonObject = new JSONObject(JsonUsersResponse);
                    JSONArray users = jsonObject.getJSONArray("userModel");

                    for (int i = 0; i < users.length(); i++) {
                        UserModel userModel = new UserModel();

                        JSONObject c = users.getJSONObject(i);
                        userModel.setFirstName(c.getString("firstName"));
                        userModel.setLastName(c.getString("lastName"));
                        userModel.setMobileNumber(c.getString("phone"));
                        userModel.setEmailAddress(c.getString("userName"));
                        userModel.setId(c.getString("userId"));
                        if (!c.isNull("address")) {
                            userModel.setAddress(c.getString("address"));
                        }
                        if (!c.isNull("mainSkill")) {
                            userModel.setMainSkill(c.getString("mainSkill"));
                        }
                        userModelArrayList.add(userModel);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return userModelArrayList;
            }

            @Override
            public void deliverResult(ArrayList<UserModel> data) {
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<UserModel>> loader, ArrayList<UserModel> data) {
        mAdapter = new SearchAdapter(data, new SearchAdapter.onRecyclerClickListener() {
            @Override
            public void onClick(String username) {

                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<UserModel>> loader) {

    }


    private void search(SearchView searchView) {

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if (mAdapter != null) mAdapter.getFilter().filter(newText);
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem search = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
        search(searchView);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }


}