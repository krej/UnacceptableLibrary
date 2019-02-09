package com.unacceptable.unacceptablelibrary.Logic;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.unacceptable.unacceptablelibrary.R;
import com.unacceptable.unacceptablelibrary.Tools.Network;
import com.unacceptable.unacceptablelibrary.Tools.Tools;

import java.util.HashMap;
import java.util.Map;

public class LoginLogic extends BaseLogic<LoginLogic.View> {

    public void attemptLogin(String username, String password) {
        view.clearErrors();

        // Check for a valid username address.
        if (Tools.IsEmptyString(username)) {
            view.setUsernameError(view.getMyString(R.string.error_field_required));
            view.sendFocusToUsername();
            return;
        } else if (!isValidUsername(username)) {
            view.setUsernameError(view.getMyString(R.string.error_invalid_username));
            view.sendFocusToUsername();
            return;
        }

        // Check for a valid password, if the user entered one.
        if (!Tools.IsEmptyString(password) && !isPasswordValid(password)) {
            view.setPasswordError(view.getMyString(R.string.error_invalid_password));
            view.sendFocusToPassword();
            return;
        } else if (Tools.IsEmptyString(password)) {
            view.setPasswordError("Password is required!");
            view.sendFocusToPassword();
            return;
        }

        view.showProgress(true);
        view.SendLoginAttempt(username, password);

    }

    private boolean isValidUsername(String username) {
        return username.length() >= 3;
        //return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() >= 6;
    }

    public interface View {
        void clearErrors();
        void setPasswordError(String error);
        void sendFocusToPassword();
        void setUsernameError(String error);
        void sendFocusToUsername();
        String getMyString(int id);
        void showProgress(final boolean show);
        void SendLoginAttempt(final String username, final String password);
    }
}
