package com.unacceptable.unacceptablelibrary.Adapters;

import android.view.View;
import android.widget.TextView;

import com.unacceptable.unacceptablelibrary.Models.ListableObject;
import com.unacceptable.unacceptablelibrary.R;
import com.unacceptable.unacceptablelibrary.Tools.Tools;

public abstract class SimpleAdapterViewControl extends BaseAdapterViewControl {
    @Override
    public void SetupViewInList(NewAdapter.ViewHolder view, ListableObject i) {
        TextView tv = view.view.findViewById(R.id.firstLine);
        Tools.SetText(tv, i.name);
    }
}
