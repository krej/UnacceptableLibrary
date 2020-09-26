package com.unacceptable.unacceptablelibrary.Logic;

import android.content.SharedPreferences;

import com.android.volley.VolleyError;
import com.unacceptable.unacceptablelibrary.R;
import com.unacceptable.unacceptablelibrary.Repositories.ILoginRepository;
import com.unacceptable.unacceptablelibrary.Repositories.RepositoryCallback;
import com.unacceptable.unacceptablelibrary.Tools.Tools;


public class LoginLogic extends BaseLogic<LoginLogic.View> {
    ILoginRepository repository;
    SharedPreferences prefs;

    public LoginLogic(ILoginRepository repository, SharedPreferences prefs) {
        this.repository = repository;
        this.prefs = prefs;
    }

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
        sendLoginAttempt(username, password);

    }

    private void sendLoginAttempt(final String username, final String password) {
        repository.SendLoginAttempt(username, password, new RepositoryCallback() {
            @Override
            public void onSuccess(String s) {
                saveAPIToken(s);
                view.launchNextActivity();
                view.showProgress(false);
            }

            @Override
            public void onError(VolleyError error) {
                //String sError = error == null || error.getMessage() == null? "Unknown error" : error.getMessage();
                String sError = error != null && error.networkResponse != null ? String.valueOf(error.networkResponse.statusCode) : "Unknown error";
                view.showError("Failed to login: " + sError);
                view.showProgress(false);
            }
        });
    }

    private void saveAPIToken(String response) {
        SharedPreferences.Editor editor = prefs.edit();
        String token = response.substring(1, response.length() - 1); //response comes back with quotes on both sides - toss em out
        editor.putString("APIToken", token);
        editor.commit();
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
        void showError(String sMessage);
        void launchNextActivity();
    }
}
