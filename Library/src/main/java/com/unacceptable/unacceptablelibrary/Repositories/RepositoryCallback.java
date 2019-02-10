package com.unacceptable.unacceptablelibrary.Repositories;

import com.android.volley.VolleyError;

public interface RepositoryCallback {
    void onSuccess(String t);
    void onError(VolleyError error);
}
