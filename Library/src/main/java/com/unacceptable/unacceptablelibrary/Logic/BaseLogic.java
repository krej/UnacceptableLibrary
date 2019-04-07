package com.unacceptable.unacceptablelibrary.Logic;

import com.unacceptable.unacceptablelibrary.Repositories.ILibraryRepository;

import java.io.Serializable;

public abstract class BaseLogic<V> implements Serializable {

    protected V view;
    protected ILibraryRepository m_LibraryRepo;

    public void attachView(V v) {
        view = v;
    }

    public void detachView() {
        view = null;
    }
}
