package com.oneteam.graduationproject;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.oneteam.graduationproject.Utils.Constant;
import com.oneteam.graduationproject.Utils.NetworkUtils;
import com.oneteam.graduationproject.adapters.SkillsAdapter;
import com.oneteam.graduationproject.models.SkillModel;

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

import static com.oneteam.graduationproject.ProfileActivity.LOADER_ID;

public class SkillsActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private SkillsAdapter mAdapter;
    @Bind(R.id.add_skill_btn)
    Button addSkillBtn;
    @Bind(R.id.add_skill_name_edt_txt)
    EditText addSkillTitleEditTxt;
    public static final int SKILLS_LOADER_ID = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skills);
        ButterKnife.bind(this);
        initViews();
        getSupportLoaderManager().initLoader(SKILLS_LOADER_ID, null, userSkillsLoader);
        addSkillBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSkill(addSkillTitleEditTxt.getText().toString());
                getSupportLoaderManager().restartLoader(SKILLS_LOADER_ID, null, userSkillsLoader);
                addSkillTitleEditTxt.setText("");


            }
        });

    }

    private void addSkill(String skillName) {


        JSONObject JS = new JSONObject();
        try {

            JS.put(Constant.SKILL_NAME, skillName);
            JS.put(Constant.QUSERID, new UserSession(this).getUserDetails().get(UserSession.KEY_ID));


        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.POST, Constant.ADD_SKILL_URL, JS,
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
        getSupportLoaderManager().restartLoader(LOADER_ID, null, userSkillsLoader);
    }

    private void initViews() {
        mRecyclerView = (RecyclerView) findViewById(R.id.skills_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));


    }

    private LoaderManager.LoaderCallbacks<ArrayList<SkillModel>> userSkillsLoader =

            new LoaderManager.LoaderCallbacks<ArrayList<SkillModel>>() {

                @Override
                public Loader<ArrayList<SkillModel>> onCreateLoader(int id, final Bundle args) {
                    return new AsyncTaskLoader<ArrayList<SkillModel>>(getApplicationContext()) {
                        ArrayList<SkillModel> briefSkillRateModelList = new ArrayList<>();

                        @Override
                        protected void onStartLoading() {
                            if (args != null)
                                deliverResult(briefSkillRateModelList);
                            else
                                forceLoad();
                        }

                        @Override
                        public ArrayList<SkillModel> loadInBackground() {
                            UserSession usersession = new UserSession(getContext());
                            URL skillsRequestUrl = NetworkUtils.getUserSkillsUrl(usersession.zPref.getString(usersession.KEY_ID, null));
                            try {


                                return Parser.getUSerSkills(NetworkUtils.getResponseFromHttpUrl(skillsRequestUrl));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            return null;
                        }


                        @Override
                        public void deliverResult(ArrayList<SkillModel> data) {
                            super.deliverResult(data);
                        }
                    };
                }

                @Override
                public void onLoadFinished(Loader<ArrayList<SkillModel>> loader, final ArrayList<SkillModel> data) {

                    Collections.reverse(data);
                    mAdapter = new SkillsAdapter(data, new SkillsAdapter.onRecyclerLongClickListener() {
                        @Override
                        public void onClick(final int skillId) {

                            final AlertDialog dialog = new AlertDialog.Builder(SkillsActivity.this)
                                    .create();
                            LayoutInflater layoutInflater = LayoutInflater.from(SkillsActivity.this);
                            dialog.setView(layoutInflater.inflate(R.layout.dialogue, null));
                            dialog.show();
                            Button okButton = (Button) dialog.findViewById(R.id.ok);
                            Button cancelButton = (Button) dialog.findViewById(R.id.cancel);
                            okButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                    deleteSkill(skillId);

                                    getSupportLoaderManager().restartLoader(SKILLS_LOADER_ID, null, userSkillsLoader);


                                }
                            });

                            cancelButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });


                        }
                    });
                    mRecyclerView.setAdapter(mAdapter);
                }

                @Override
                public void onLoaderReset(Loader<ArrayList<SkillModel>> loader) {

                }
            };

    private void deleteSkill(int skillid) {


        JSONObject JS = new JSONObject();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.GET, Constant.REMOVE_SKILL_URL + skillid, JS,
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
        Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
    }
}
