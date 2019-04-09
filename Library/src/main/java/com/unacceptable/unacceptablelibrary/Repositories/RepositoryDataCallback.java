package com.unacceptable.unacceptablelibrary.Repositories;

import com.android.volley.VolleyError;

/**
 * The same as RepositoryCallback, but it requires data from the API as a Response object with the model stored in the Message field
 *
 * Ok, doesn't work because you need to call Gson and give it a class, and T.class doesn't work.
 * @param <T>
 */
public interface RepositoryDataCallback<T> {
    void onSuccess(T obj);
    void onError(VolleyError error);
}
