package com.unacceptable.unacceptablelibrary.Tools;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zak on 1/12/2017.
 */

public class Network {
    private static Network mInstance;
    private RequestQueue mRequestQueue;
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

                params.put("Authorization", "bearer " + Tools.GetAPIToken());
                return params;
            }
        };

        Network.getInstance(mCtx).addToRequestQueue(stringRequest);
        //addToRequestQueue(stringRequest);
        //Network.getInstance(c).addToRequestQueue(stringRequest);
    }

    public static void WebRequest(int method, String url, final byte[] data, Response.Listener<String> listener, Response.ErrorListener errorListener) {
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

                params.put("Authorization", "bearer " + Tools.GetAPIToken());
                return params;
            }
        };
        Network.getInstance(mCtx).addToRequestQueue(request);
    }

}
