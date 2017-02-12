package com.oneteam.graduationproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.Bind;

import static com.oneteam.graduationproject.Utils.Constant.KEY_ADDRESS;
import static com.oneteam.graduationproject.Utils.Constant.KEY_F_NAME;
import static com.oneteam.graduationproject.Utils.Constant.KEY_L_NAME;
import static com.oneteam.graduationproject.Utils.Constant.KEY_PASSWORD;
import static com.oneteam.graduationproject.Utils.Constant.KEY_PHONE;

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";

    @Bind(R.id.input_email)
    EditText zEmailAddress;
    @Bind(R.id.input_password)
    EditText zPassword;
    @Bind(R.id.first_name)
    EditText zFirstName;
    @Bind(R.id.last_name)
    EditText ZLastName;
    @Bind(R.id.address)
    EditText zAddress;
    @Bind(R.id.input_mobile)
    EditText zMobileNumber;
    @Bind(R.id.btn_signup)
    Button zCreatAccount;
    @Bind(R.id.link_login)
     TextView zLoginLink;
    private UserModel zUser;
    private ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        zCreatAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        zLoginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }

    public void signup() {
        if (!validate()) {
            onSignupFailed();
            return;
        }

        zCreatAccount.setEnabled(false);

        progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        JSONObject JS = new JSONObject();
        try {

            JS.put(Constant.KEY_EMAIL, zUser.getEmailAddress());
            JS.put(KEY_PASSWORD, zUser.getPassword());
            JS.put(KEY_F_NAME, zUser.getFirstName());
            JS.put(KEY_L_NAME, zUser.getLastName());
            JS.put(KEY_PHONE, zUser.getMobileNumber());
            JS.put(KEY_ADDRESS, zUser.getAddress());


        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String REGISTER_URL = "https://professionalskills.eu-gb.mybluemix.net/restapi/user/register";
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(
                Request.Method.POST, REGISTER_URL, JS,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        checkSignupState(response.toString());

                    }
                }, new Response.ErrorListener() {


            @Override
            public void onErrorResponse(VolleyError error) {


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
        progressDialog.dismiss();
    }

    private void checkSignupState(String JsonString) {
        JSONObject _json = null;
        try {
            _json = new JSONObject(JsonString);
        } catch (JSONException e) {

        }
        try {
            if (_json.getBoolean("statue")) {
                Toast.makeText(this, "SignedUp!", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                finish();
            } else {
                Toast.makeText(this, _json.getString("error_message"), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                zCreatAccount.setEnabled(false);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    public void onSignupSuccess() {
        zCreatAccount.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "signUp failed", Toast.LENGTH_LONG).show();

        zCreatAccount.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;
        zUser = new UserModel();

        zUser.setFirstName(zFirstName.getText().toString());
        zUser.setLastName(ZLastName.getText().toString());
        zUser.setAddress(zAddress.getText().toString());
        zUser.setEmailAddress(zEmailAddress.getText().toString());
        zUser.setMobileNumber(zMobileNumber.getText().toString());
        zUser.setPassword(zPassword.getText().toString());

        if (zUser.getFirstName().isEmpty() || zUser.getFirstName().length() < 3) {
            zFirstName.setError("at least 3 characters");
            valid = false;
        } else {
            zFirstName.setError(null);
        }
        if (zUser.getLastName().isEmpty() || zUser.getLastName().length() < 3) {
            ZLastName.setError("at least 3 characters");
            valid = false;
        } else {
            ZLastName.setError(null);
        }


        if (zUser.getEmailAddress().isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(zUser.getEmailAddress()).matches()) {
            zEmailAddress.setError("enter a valid email address");
            valid = false;
        } else {
            zEmailAddress.setError(null);
        }

        if (zUser.getMobileNumber().isEmpty()) {
            zMobileNumber.setError("Enter Valid Mobile Number");
            valid = false;
        } else {
            zMobileNumber.setError(null);
        }

        if (zUser.getPassword().isEmpty() || zUser.getPassword().length() < 6) {
            zPassword.setError("password is Too short !");
            valid = false;
        } else {
            zPassword.setError(null);
        }

        return valid;
    }
}