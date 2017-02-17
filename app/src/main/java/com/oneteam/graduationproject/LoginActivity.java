package com.oneteam.graduationproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.oneteam.graduationproject.Utils.NetworkUtils;

import java.io.IOException;
import java.net.URL;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Boolean> {

    public int LOGIN_LOADER_ID = 22;

    @Bind(R.id.input_email)
    EditText _emailText;
    @Bind(R.id.input_password)
    EditText _passwordText;
    @Bind(R.id.btn_login)
    Button _loginButton;
    @Bind(R.id.link_signup)
    TextView _signupLink;

    ProgressDialog progressDialog;
    private UserSession zUserSession;
    private String zEmail;
    private String zPassword;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        zUserSession = new UserSession(getApplicationContext());
        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                finish();
            }
        });


    }

    public void login() {
        if (!validate()) {
            onLoginFailed();
            return;
        }
        _loginButton.setEnabled(false);

        progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        int loaderId = LOGIN_LOADER_ID;

        LoaderManager.LoaderCallbacks<Boolean> callback = this;
        getSupportLoaderManager().initLoader(loaderId, null, callback);

    }

    private void checkIfAuthorized(Boolean status) {

        if (status) {
            Toast.makeText(this, "Authoized !", Toast.LENGTH_SHORT).show();


            zUserSession.createLoginSession(zPassword, zEmail);
            // Staring MainActivity
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            progressDialog.dismiss();
            finish();
        } else {
            Toast.makeText(this, "Un Authoized !", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
            _loginButton.setEnabled(true);
        }

    }


    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();


        // I disabled checking for email format to be able to send usernames

        /*if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("Enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }*/

        if (password.isEmpty() || password.length() < 6) {
            _passwordText.setError("Password is too short !");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }

    @Override
    public Loader<Boolean> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<Boolean>(this) {
            boolean status;

            @Override
            protected void onStartLoading() {
                if (args != null)
                    deliverResult(status);
                else
                    forceLoad();
            }

            @Override
            public Boolean loadInBackground() {
                URL url = NetworkUtils.buildLoginUrl(zEmail, zPassword);
                String jsonLoginRespose = null;
                try {
                    jsonLoginRespose = NetworkUtils.getLoginResponse(url);
                    return NetworkUtils.getStatus(jsonLoginRespose);
                } catch (IOException e) {
                    Log.e("Error:", "Error making login request");
                    return null;
                }
            }

            @Override
            public void deliverResult(Boolean data) {
                status = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Boolean> loader, Boolean data) {
        checkIfAuthorized(data);

    }

    @Override
    public void onLoaderReset(Loader<Boolean> loader) {

    }
}
