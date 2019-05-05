package com.unacceptable.unacceptablelibrary.Repositories;

import com.android.volley.Request;
import com.unacceptable.unacceptablelibrary.Tools.Network;
import com.unacceptable.unacceptablelibrary.Tools.Preferences;
import com.unacceptable.unacceptablelibrary.Tools.Tools;

import java.io.Serializable;

public class LibraryRepository implements ILibraryRepository, Serializable {
    @Override
    public void Save(String sUrl, byte[] data, RepositoryCallback callback) {
        Network.WebRequest(Request.Method.POST, Preferences.RestAPIURL() + "/" + sUrl, data, callback, true);
    }
    @Override
    public void Delete(String sUrl, String sIDString, RepositoryCallback callback) {
        Network.WebRequest(Request.Method.DELETE, Preferences.RestAPIURL() + "/" + sUrl + "/" + sIDString, null, callback, true);
    }
}
