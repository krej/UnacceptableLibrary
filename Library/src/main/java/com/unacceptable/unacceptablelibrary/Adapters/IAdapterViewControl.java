package com.unacceptable.unacceptablelibrary.Adapters;

import android.app.Dialog;
import android.view.View;

import com.unacceptable.unacceptablelibrary.Models.ListableObject;

public interface IAdapterViewControl {
    boolean bShowDialog = true;

    void SetupDialog(View root, ListableObject i);
    void SetupViewInList(NewAdapter.ViewHolder view, ListableObject i);
    void onItemClick(View v, ListableObject i);
    boolean onDialogOkClicked(Dialog d, ListableObject i);

    void attachAdapter(NewAdapter adapter);
    boolean AlternateRowColors();
    String GetAlternateRowBackgroundColor();
}
