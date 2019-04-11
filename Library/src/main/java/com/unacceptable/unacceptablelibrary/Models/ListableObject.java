package com.unacceptable.unacceptablelibrary.Models;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.unacceptable.unacceptablelibrary.Repositories.ILibraryRepository;
import com.unacceptable.unacceptablelibrary.Repositories.LibraryRepository;
import com.unacceptable.unacceptablelibrary.Repositories.RepositoryCallback;

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

    public ListableObject(String sName) {
        name = sName;
    }

    public ListableObject() {
    }

    public void Save() {
        Save(new LibraryRepository());
    }
    public void Save(ILibraryRepository repository) {
        Save(repository, getSingularName());
    }

    public void Save(ILibraryRepository repository, String sCollectionName) {
        String sRecipeURL = sCollectionName + "/";// "/recipe/";
        if ( idString != null && idString.length() > 0 ) {
            sRecipeURL += idString;
        }

        repository.Save(sRecipeURL, BuildRestData(), new RepositoryCallback() {
            @Override
            public void onSuccess(String response) {
                // your response
                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.create();
                com.unacceptable.unacceptablelibrary.Models.Response res = gson.fromJson(response, com.unacceptable.unacceptablelibrary.Models.Response.class);
                if (res.Success) {
                    //TODO: Verify that responses are always idString when Success.
                    idString = res.Message;
                }
            }

            @Override
            public void onError(VolleyError error) {
                String response = error.getMessage();
                response += "";
            }
        });
    }

    public byte[] BuildRestData() {
        //GsonBuilder gsonBuilder = new GsonBuilder().setExclusionStrategies(new JsonExclusion());
        GsonBuilder gsonBuilder = new GsonBuilder().excludeFieldsWithoutExposeAnnotation();
        Gson gson = gsonBuilder.create();
        String json = gson.toJson(this);
        return json.getBytes();
    }

    public String toString() {
        return name;
    }

    public String getSingularName() {
        return this.getClass().getSimpleName().toLowerCase();
    }

    @Override
    public boolean equals(Object obj) {
        if (! (obj instanceof ListableObject)) return  false;
        ListableObject o = (ListableObject)obj;

        if (IsEmptyObject() && o.IsEmptyObject()) return true;

        if (idString == null) {
            return name.equals(o.name) && getSingularName().equals(o.getSingularName());
        }
        return idString.equals(o.idString) && name.equals(o.name) && getSingularName().equals(o.getSingularName());
    }

    @Override
    public int hashCode() {
        if (idString == null) return 0;
        return idString.hashCode();
    }

    public boolean IsEmptyObject() {
        return name.equals("Empty") && idString == null;
    }
}
