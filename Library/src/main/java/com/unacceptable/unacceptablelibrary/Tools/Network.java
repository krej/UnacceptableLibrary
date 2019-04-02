package com.unacceptable.unacceptablelibrary.Tools;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.unacceptable.unacceptablelibrary.Repositories.RepositoryCallback;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zak on 1/12/2017.
 */

public class Network {
    private static Network mInstance;
    private RequestQueue mRequestQueue;
    //TODO: Get rid of this context because maybe this is the reason why Instant Run doesn't work well... it does warn about that...
    private static Context mCtx;

    private Network(Context context) {
        mCtx = context;
        mRequestQueue = getRequestQueue();
    }

    public static synchronized Network getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new Network(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public static void WebRequest(int method, String url, final byte[] data) {

        StringRequest stringRequest = new StringRequest(method, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // your response

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error
            }
        }){
            @Override
            public byte[] getBody() throws AuthFailureError {
                return data;
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                params.put("Authorization", "bearer " + Preferences.GetAPIToken());
                return params;
            }
        };

        Network.getInstance(mCtx).addToRequestQueue(stringRequest);
        //addToRequestQueue(stringRequest);
        //Network.getInstance(c).addToRequestQueue(stringRequest);
    }

    public static void WebRequest(int method, String url, final byte[] data, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        WebRequest(method, url, data, listener, errorListener, true);
    }

    public static void WebRequest(int method, String url, String data, Response.Listener<String> listener, Response.ErrorListener errorListener, final boolean bAddAuthentication) {
        WebRequest(method, url, data.getBytes(), listener, errorListener, bAddAuthentication);
    }

    public static void WebRequest(int method, String url, final byte[] data, Response.Listener<String> listener, Response.ErrorListener errorListener, final boolean bAddAuthentication) {
        StringRequest request = new StringRequest(method, url, listener, errorListener) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                return data;
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                //params.put("Content-Type", "application/json");
                if (bAddAuthentication)
                    params.put("Authorization", "bearer " + Preferences.GetAPIToken());
                return params;
            }
        };
        Network.getInstance(mCtx).addToRequestQueue(request);
    }

    public static void WebRequest(int method, String url, final byte[] data, final RepositoryCallback callback, final boolean bAddAuthentication) {
        WebRequest(method, url, data,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response != null) {
                            /*try {
                                com.unacceptable.unacceptablelibrary.Models.Response result = Tools.convertJsonResponseToObject(response, com.unacceptable.unacceptablelibrary.Models.Response.class);
                                if (!result.Success) {
                                    callback.onError(new VolleyError(result.Message));
                                    return;
                                }
                            } catch ( Exception ex) {
                                //TODO: THIS ISN'T GREAT!!! I'm only doing this because not all calls to the API are in a Response format. I want to change that...
                                callback.onSuccess(response);
                                return;
                            }*/

                            callback.onSuccess(response);
                        } else {
                            callback.onError(null);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onError(error);
                    }
                }, bAddAuthentication);
    }

}
