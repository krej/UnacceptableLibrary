package com.unacceptable.unacceptablelibrary.Controls;

import android.view.View;
import android.widget.AdapterView;

/***
 * This class exists because apparently when you have an OnItemSelectedListener, onItemSelected gets called twice, once to initialize and once when you call Spinner.setPosition
 * So this class exists to 'capture' the first call and ignore it.
 *
 * I don't like this if statement but apparently thats what you need to do.
 * https://stackoverflow.com/questions/1337424/android-spinner-get-the-selected-item-change-event
 * http://knewless.blogspot.com/2016/04/android-how-to-avoid-onitemselected-to.html
 */
public class CustomOnItemSelectedListener implements AdapterView.OnItemSelectedListener {
    private boolean m_bOnItemSelectedInitialRun = true;

    private IMyAdapterViewOnItemSelectedListener m_Listener;

    public interface IMyAdapterViewOnItemSelectedListener {
        void onItemSelected(AdapterView<?> parent, View view, int position, long id);
        void onNothingSelected(AdapterView<?> parent);
    }

    public CustomOnItemSelectedListener(IMyAdapterViewOnItemSelectedListener listener) {
        setListener(listener);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (m_bOnItemSelectedInitialRun) {
            m_bOnItemSelectedInitialRun = false;
            return;
        }

        m_Listener.onItemSelected(parent, view, position, id);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        m_Listener.onNothingSelected(parent);
    }

    public void setListener(IMyAdapterViewOnItemSelectedListener listener) {
        m_Listener = listener;
    }
}
