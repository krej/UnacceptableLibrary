package com.unacceptable.unacceptablelibrary.Repositories;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.unacceptable.unacceptablelibrary.Tools.Network;
import com.unacceptable.unacceptablelibrary.Tools.Tools;


public class LoginRepositoryImpl implements LoginRepository {
    @Override
    public void SendLoginAttempt(final String username, final String password, final RepositoryCallback callback) {
        String body ="{username: \"" + username + "\", password: \"" + password + "\"}";
        //body.getBytes
        String sLoginURL = Tools.BeerNetAPIURL() + "/Login";

        Network.WebRequest(Request.Method.POST, sLoginURL, body, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                if (response != null) {
                    callback.onSuccess(response);
                } else {
                    callback.onError(null);
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onError(error);
            }
        },
                false);

    }
}
