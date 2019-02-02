package com.unacceptable.unacceptablelibrary.Models;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.unacceptable.unacceptablelibrary.Tools.Network;
import com.unacceptable.unacceptablelibrary.Tools.Tools;
import java.io.Serializable;

/**
 * Created by Megatron on 10/4/2017.
 */

public class ListableObject implements Serializable {
    @Expose
    public String name = "Empty"; //Used to store 'Empty'
    @Expose
    public String idString;
    //protected String ClassName = ""; //Used for the API to know what URL to request to. Could maybe be replaced with just getting the class name later.

    public void Save() {

        //String sRecipeURL = Tools.RestAPIURL() + "/" + ClassName.toLowerCase() + "/";// "/recipe/";
        String sRecipeURL = Tools.RestAPIURL() + "/" + this.getClass().getSimpleName().toLowerCase() + "/";// "/recipe/";
        if ( idString != null && idString.length() > 0 ) {
            sRecipeURL += idString;
        }

        Network.WebRequest(Request.Method.POST, sRecipeURL, BuildRestData(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // your response
                        GsonBuilder gsonBuilder = new GsonBuilder();
                        Gson gson = gsonBuilder.create();
                        com.unacceptable.unacceptablelibrary.Models.Response res = gson.fromJson(response, com.unacceptable.unacceptablelibrary.Models.Response.class);
                        if (res.success) {
                            //TODO: Verify that responses are always idString when success.
                            idString = res.message;
                        }
                    }
                },
                 new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String response = error.getMessage();
                        response += "";
                    }
                });
    }

    protected byte[] BuildRestData() {
        //GsonBuilder gsonBuilder = new GsonBuilder().setExclusionStrategies(new JsonExclusion());
        GsonBuilder gsonBuilder = new GsonBuilder().excludeFieldsWithoutExposeAnnotation();
        Gson gson = gsonBuilder.create();
        String json = gson.toJson(this);
        return json.getBytes();
    }

    public String toString() {
        return name;
    }

}
