package com.unacceptable.unacceptablelibrary.Adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.unacceptable.unacceptablelibrary.Logic.BaseLogic;
import com.unacceptable.unacceptablelibrary.Models.ListableObject;

import java.io.Serializable;

public abstract class BaseAdapterViewControl implements IAdapterViewControl, Serializable {
    protected NewAdapter m_Adapter;
    protected boolean m_bAlternateRowColors;
    protected String m_sAlternateRowBackgroundColor;
    public Activity m_Activity;

    public void attachAdapter(NewAdapter adapter) {
        m_Adapter = adapter;
    }

    public void detachAdapter() {
        m_Adapter = null;
    }

    public boolean AlternateRowColors() {
        return m_bAlternateRowColors;
    }

    public String GetAlternateRowBackgroundColor() {
        return m_sAlternateRowBackgroundColor;
    }

    public boolean AddItemUsesActivity() {
        return false;
    }

    public Intent SetupNewActivity(Context c, ListableObject i) {
        return null;
    }

    public void setReadOnly(NewAdapter.ViewHolder viewHolder, boolean bReadOnly) {
        //m_bReadOnly = bReadOnly;
    }

}
