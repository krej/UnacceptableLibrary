package com.unacceptable.unacceptablelibrary.Screens;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.unacceptable.unacceptablelibrary.Logic.LoginLogic;
import com.unacceptable.unacceptablelibrary.R;
import com.unacceptable.unacceptablelibrary.Tools.Network;
import com.unacceptable.unacceptablelibrary.Tools.Tools;

import java.util.HashMap;
import java.util.Map;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoginLogic.View {

    // UI references.
    private AutoCompleteTextView mUsernameView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    private LoginLogic m_oLoginLogic = new LoginLogic();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        m_oLoginLogic.attachView(this);

        // Set up the login form.
        mUsernameView = (AutoCompleteTextView) findViewById(R.id.username);
        mPasswordView = (EditText) findViewById(R.id.password);
        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);

        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    passCredentialsToLogin();
                    return true;
                }
                return false;
            }
        });

        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                passCredentialsToLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    private void passCredentialsToLogin() {
        String username = mUsernameView.getText().toString();
        String password = mPasswordView.getText().toString();

        m_oLoginLogic.attemptLogin(username, password);
    }

    @Override
    protected void onDestroy() {
        m_oLoginLogic.detachView();
        super.onDestroy();
    }

    @Override
    public void clearErrors() {
        // Reset errors.
        mUsernameView.setError(null);
        mPasswordView.setError(null);
    }

    public void setPasswordError(String error) {
        mPasswordView.setError(error);
    }
    public void sendFocusToPassword() {
        mPasswordView.requestFocus();
    }

    public void setUsernameError(String error) {
        mUsernameView.setError(error);
    }

    public void sendFocusToUsername() {
        mUsernameView.requestFocus();
    }

    //TODO: This seems wrong...
    public String getMyString(int id) {
        return getString(id);
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    @Override
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    //TODO: I don't think this should be here, but to keep the app working while I get unit tests working, I'm leaving it here.
    //TODO: I'll need to redo this when I move to testing with the network calls
    public void SendLoginAttempt(final String username, final String password) {
        String sLoginURL = Tools.BeerNetAPIURL() + "/Login";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, sLoginURL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                //mTextView.setText("Respone is :" + response);// + response.substring(0, 500));
                //SetRecipeList(response);
                SharedPreferences sharedPreferences = getSharedPreferences("Prefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                String token = response.substring(1, response.length() - 1); //response comes back with quotes on both sides - toss em out
                editor.putString("APIToken", token);
                editor.commit();


                Intent i = new Intent(getApplicationContext(), getCallingActivity().getClass());
                startActivity(i);
                showProgress(false);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //mTextView.setText("That didn't work " + error.getMessage());

                Tools.ShowToast(getApplicationContext(), "Failed to login", Toast.LENGTH_LONG);
                showProgress(false);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                //params.put("Content-Type", "application/json; charset=utf-8");
                params.put("Content-Type", "application/json");
                //params.put("Authorization", "bearer " + Tools.APIToken);
                return params;
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                String body ="{username: \"" + username + "\", password: \"" + password + "\"}";
                return body.getBytes();
            }
        };

        Network.getInstance(this).addToRequestQueue(stringRequest);
    }
}

