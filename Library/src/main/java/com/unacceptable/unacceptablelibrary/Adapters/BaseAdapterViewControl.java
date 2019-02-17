package com.unacceptable.unacceptablelibrary.Adapters;

import android.app.Dialog;
import android.view.View;

import com.unacceptable.unacceptablelibrary.Models.ListableObject;

public abstract class BaseAdapterViewControl implements IAdapterViewControl {
    protected NewAdapter m_Adapter;

    public void attachAdapter(NewAdapter adapter) {
        m_Adapter = adapter;
    }

    public void detachAdapter() {
        m_Adapter = null;
    }
}
