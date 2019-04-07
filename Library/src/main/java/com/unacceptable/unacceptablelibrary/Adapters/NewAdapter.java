package com.unacceptable.unacceptablelibrary.Adapters;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import com.unacceptable.unacceptablelibrary.R;
import com.unacceptable.unacceptablelibrary.Models.ListableObject;

/**
 * Created by zak on 11/16/2016.
 */

//This is going to be the new adapter to replace the old one
public class NewAdapter extends RecyclerView.Adapter<NewAdapter.ViewHolder>
        //implements View.OnClickListener, View.OnLongClickListener
{
    protected IAdapterViewControl m_vControl;

    protected ArrayList<ListableObject> m_Dataset;
    protected int m_iLayout;
    protected int m_iDialogLayout;
    protected int m_iClickedItem;
    protected LayoutInflater inflater;
    private boolean m_bAddEmptyItem;

    public NewAdapter(int iLayout, int iDialogLayout) {
        this(iLayout, iDialogLayout, true, null);
    }

    public NewAdapter(int iLayout, int iDialogLayout, boolean bAddEmpty, IAdapterViewControl viewControl) {
        m_Dataset = new ArrayList<ListableObject>();
        m_iLayout = iLayout;
        m_iDialogLayout = iDialogLayout;
        m_bAddEmptyItem = bAddEmpty;
        if (bAddEmpty)
            add(new ListableObject());
        m_vControl = viewControl;
        m_vControl.attachAdapter(this);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(m_iLayout, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (OnlyEmptyIngredientExists()) return;

        ListableObject i = (ListableObject) m_Dataset.get(position);
        m_vControl.SetupViewInList(holder, i);

        if (m_vControl.AlternateRowColors()) {
            if (position % 2 == 0) {
                holder.view.setBackgroundColor(Color.parseColor("#FFFFFF"));
            } else {
                //view.view.setBackgroundColor(Color.parseColor("#208f82"));
                holder.view.setBackgroundColor(Color.parseColor(m_vControl.GetAlternateRowBackgroundColor()));
            }
        }

        //final String name = mDataset.get(position).Name;
        //holder.txtHeader.setText(m_Dataset.get(position).Name);
        /*holder.txtHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove(name);
            }
        });*/

        //holder.txtFooter.setText("Time: " + mDataset.get(position).Time + " min");
    }

    @Override
    public int getItemCount() {
        return m_Dataset.size();
    }

    public void add(int position, ListableObject item) {
        removeEmptyIngredient();
        m_Dataset.add(position, item);
        notifyItemInserted(position);
    }

    public void add(ListableObject item) {
        removeEmptyIngredient();

        m_Dataset.add(item);
        notifyItemInserted(m_Dataset.size());
    }

    private void removeEmptyIngredient() {
        if (OnlyEmptyIngredientExists()) {
            remove(m_Dataset.get(0));
        }
    }

    public void remove(ListableObject item) {
        int position = m_Dataset.indexOf(item);
        m_Dataset.remove(position);
        notifyItemRemoved(position);
        /*if (m_bAddEmptyItem) {
            add(new ListableObject());
            notifyDataSetChanged();
        }*/

    }

    protected boolean OnlyEmptyIngredientExists() {
        return (m_Dataset.size() == 1 && m_Dataset.get(0).toString() == "Empty");
    }

    protected boolean AddItem(Dialog d, boolean bExisting, String sExtraData) {
        return false;
    }

    /*@Override
    public boolean onLongClick(View v) {
        return false;
    }*/

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtHeader;
        public TextView txtFooter;
        public TextView txtThirdLine, txtFourthLine;
        public View view;

        public ViewHolder(View v) {
            super(v);
            view = v;
            txtHeader = (TextView) v.findViewById(R.id.firstLine);
            try {
                txtFooter = (TextView) v.findViewById(R.id.secondLine);

                txtThirdLine = (TextView) v.findViewById(R.id.thirdLine);
                txtFourthLine = (TextView) v.findViewById(R.id.fourthLine);
            } finally {

            }
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //This toast event works... I think I'll need to move the pop up Add/Edit window into this class
                    //and then possibly see if I can do a RaiseEvent thing to tell the RecipeEditor class to refresh the stats
                    //Toast.makeText(v.getContext(), "Test" + getLayoutPosition(), Toast.LENGTH_LONG).show();
                    m_iClickedItem = getLayoutPosition();
                    if (size() > 0) {
                        //AddItem(v.getContext(), m_Dataset.get(m_iClickedItem));
                        m_vControl.onItemClick(v, m_Dataset.get(m_iClickedItem));
                    }
                }
            });

            v.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    m_vControl.onItemLongPress(v, m_Dataset.get(getLayoutPosition()));
                    return false;
                }
            });
        }
    }

    public int clickedPosition() {
        return m_iClickedItem;
    }

    public ListableObject get(int i) {
        return m_Dataset.get(i);
    }

    public int size() {
        if (OnlyEmptyIngredientExists()) return 0;

        return m_Dataset.size();
    }

    public ArrayList<ListableObject> Dataset() {
        return m_Dataset;
    }

    public void showAddItemDialog(final Context c, final ListableObject i) {
        if (m_vControl.AddItemUsesActivity()) {
            Intent intent = m_vControl.SetupNewActivity(c, i);
            if (intent != null)
                c.startActivity(intent);
        } else {
            startDialog(c, i);
        }
    }

    private void startDialog(final Context c, final ListableObject i) {
        AlertDialog.Builder builder = new AlertDialog.Builder(c);

        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });


        builder.setView(SetupDialog(c, i));
        final AlertDialog dialog = builder.create();


        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (!m_vControl.onDialogOkClicked(dialog, i)) return;

                notifyDataSetChanged();
                dialog.dismiss();
            }
        });
    }

    protected View SetupDialog(Context c, ListableObject i) {

        LayoutInflater inflater = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View root = inflater.inflate(m_iDialogLayout, null);

        //if (i != null)
            m_vControl.SetupDialog(root, i);

        return root;
    }

    public void InfoMissing(Context c) {
        CharSequence text = "Info Missing";
        Toast t = Toast.makeText(c, text, Toast.LENGTH_SHORT);
        t.show();
    }

    protected ListableObject GetClickedItem() {
        return m_Dataset.get(m_iClickedItem);
    }

    public void clear() {
        m_Dataset.clear();
        if ( m_bAddEmptyItem) add(new ListableObject());
        notifyDataSetChanged();
    }

    public IAdapterViewControl getAdapterViewControl() {
        return m_vControl;
    }
}
