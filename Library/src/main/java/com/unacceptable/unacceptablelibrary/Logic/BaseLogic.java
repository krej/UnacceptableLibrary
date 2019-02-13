package com.unacceptable.unacceptablelibrary.Logic;

public abstract class BaseLogic<V> {

    protected V view;

    public void attachView(V v) {
        view = v;
    }

    public void detachView() {
        view = null;
    }
}
