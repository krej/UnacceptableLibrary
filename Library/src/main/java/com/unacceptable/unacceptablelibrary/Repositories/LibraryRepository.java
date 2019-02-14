package com.unacceptable.unacceptablelibrary.Repositories;

import com.android.volley.Request;
import com.unacceptable.unacceptablelibrary.Tools.Network;
import com.unacceptable.unacceptablelibrary.Tools.Tools;

public class LibraryRepository implements ILibraryRepository {
    @Override
    public void Save(String sUrl, byte[] data, RepositoryCallback callback) {
        Network.WebRequest(Request.Method.POST, Tools.RestAPIURL() + "/" + sUrl, data, callback, true);
    }
}
