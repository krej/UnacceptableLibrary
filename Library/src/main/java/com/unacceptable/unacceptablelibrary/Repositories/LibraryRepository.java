package com.unacceptable.unacceptablelibrary.Repositories;

import com.android.volley.Request;
import com.unacceptable.unacceptablelibrary.Tools.Network;

public class LibraryRepository implements ILibraryRepository {
    @Override
    public void Save(String sUrl, byte[] data, RepositoryCallback callback) {
        Network.WebRequest(Request.Method.POST, sUrl, data, callback, true);
    }
}
