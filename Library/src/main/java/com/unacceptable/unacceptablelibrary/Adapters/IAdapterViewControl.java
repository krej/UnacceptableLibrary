package com.unacceptable.unacceptablelibrary.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.unacceptable.unacceptablelibrary.Models.ListableObject;

import java.io.Serializable;

public interface IAdapterViewControl {
    boolean bShowDialog = true;

    void SetupDialog(View root, ListableObject i);
    void SetupViewInList(NewAdapter.ViewHolder view, ListableObject i);
    void onItemClick(View v, ListableObject i);
    void onItemLongPress(View v, ListableObject i);
    boolean onDialogOkClicked(Dialog d, ListableObject i);

    void attachAdapter(NewAdapter adapter);
    boolean AlternateRowColors();
    String GetAlternateRowBackgroundColor();
    boolean AddItemUsesActivity();
    Intent SetupNewActivity(Context c, ListableObject i);
    void setReadOnly(NewAdapter.ViewHolder viewHolder, boolean bReadOnly);
}
