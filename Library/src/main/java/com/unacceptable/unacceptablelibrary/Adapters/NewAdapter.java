package com.unacceptable.unacceptablelibrary.Adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

import com.unacceptable.unacceptablelibrary.R;
import com.unacceptable.unacceptablelibrary.Models.ListableObject;
import com.unacceptable.unacceptablelibrary.Tools.RecyclerViewSwipe.IItemTouchHelperAdapter;
import com.unacceptable.unacceptablelibrary.Tools.RecyclerViewSwipe.SimpleItemTouchHelperCallback;
import com.unacceptable.unacceptablelibrary.Tools.Tools;

/**
 * Created by zak on 11/16/2016.
 */

//This is going to be the new adapter to replace the old one
public class NewAdapter extends RecyclerView.Adapter<NewAdapter.ViewHolder>
        implements IItemTouchHelperAdapter, Filterable
        //implements View.OnClickListener, View.OnLongClickListener
{
    private IAdapterViewControl m_vControl;

    private ArrayList<ListableObject> m_Dataset;
    private ArrayList<ListableObject> m_DatasetFiltered;
    private int m_iLayout;
    private int m_iDialogLayout;
    private int m_iClickedItem;
    protected LayoutInflater inflater;
    private boolean m_bAddEmptyItem;
    private ItemTouchHelper m_SimpleItemTouchHelper = null;
    private SimpleItemTouchHelperCallback m_SimpleItemTouchHelperCallback = null;

    public interface INotifySwipeDelete {
        //void notifyDelete(BaseLogic controller, int position);
        void notifyDelete(int position, ListableObject i);
    }

    private INotifySwipeDelete m_notifiySwipeDelete;

    public NewAdapter(int iLayout, int iDialogLayout) {
        this(iLayout, iDialogLayout, true, null);
    }

    public NewAdapter(int iLayout, int iDialogLayout, boolean bAddEmpty, IAdapterViewControl viewControl) {
        m_Dataset = new ArrayList<>();
        m_DatasetFiltered = new ArrayList<>();
        m_iLayout = iLayout;
        m_iDialogLayout = iDialogLayout;
        m_bAddEmptyItem = bAddEmpty;
        if (bAddEmpty)
            add(new ListableObject());
        m_vControl = viewControl;
        m_vControl.attachAdapter(this);
        getFilter().filter(""); //default to no filter
    }

    public void setNotifySwipeDelete(INotifySwipeDelete nsd) {
        m_notifiySwipeDelete = nsd;
    }

    public void attachTouchCallback(ItemTouchHelper touchHelper, SimpleItemTouchHelperCallback callback) {
        m_SimpleItemTouchHelper = touchHelper;
        m_SimpleItemTouchHelperCallback = callback;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(m_iLayout, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (OnlyEmptyIngredientExists()) return;

        if (holder.handleView != null) {
            holder.handleView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                        //m_SimpleItemTouchHelperCallback.startDrag(holder);
                        m_SimpleItemTouchHelper.startDrag(holder);
                    }
                    return false;
                }
            });
        }

        //ListableObject i = (ListableObject) m_Dataset.get(position);
        ListableObject i = (ListableObject) m_DatasetFiltered.get(position);
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
        return m_DatasetFiltered.size();
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
        if (m_bAddEmptyItem && OnlyEmptyIngredientExists()) {
            remove(m_Dataset.get(0));
        }
    }

    public void remove(ListableObject item) {
        int position = m_Dataset.indexOf(item);
        remove(position);
        //m_Dataset.remove(position);
        //notifyItemRemoved(position);
        /*if (m_bAddEmptyItem) {
            add(new ListableObject());
            notifyDataSetChanged();
        }*/

    }

    public void remove(int position) {
        m_Dataset.remove(position);
        notifyItemRemoved(position);
    }

    protected boolean OnlyEmptyIngredientExists() {
        if (!m_bAddEmptyItem) return false;
        return (m_Dataset.size() == 1 && m_Dataset.get(0).IsEmptyObject());
    }

    protected boolean AddItem(Dialog d, boolean bExisting, String sExtraData) {
        return false;
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(m_Dataset, i, i+1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(m_Dataset, i, i - 1);
            }
        }

        notifyItemMoved(fromPosition, toPosition);
        //return true;
    }

    @Override
    public void onItemDismiss(int position) {
        ListableObject i = m_Dataset.get(position);
        m_Dataset.remove(position);
        notifyItemRemoved(position);
        if (m_notifiySwipeDelete != null)
            m_notifiySwipeDelete.notifyDelete(position, i);
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

        public final ImageView handleView;

        public ViewHolder(View v) {
            super(v);
            view = v;
            txtHeader = (TextView) v.findViewById(R.id.firstLine);
            try {
                txtFooter = (TextView) v.findViewById(R.id.secondLine);

                txtThirdLine = (TextView) v.findViewById(R.id.thirdLine);
                txtFourthLine = (TextView) v.findViewById(R.id.fourthLine);

                handleView = v.findViewById(R.id.drag_handle);
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
                        m_vControl.onItemClick(v, m_DatasetFiltered.get(m_iClickedItem));
                    }
                }
            });

            v.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    m_vControl.onItemLongPress(v, m_DatasetFiltered.get(getLayoutPosition()));
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
            if (intent != null) {
                c.startActivity(intent);
                //c.startActivityForResult(intent, 1);

            }
        } else {
            startDialog(c, i);
        }
    }

    public void showAddItemDialog(Activity a, Context c, int iRequestCode, ListableObject i) {
        if (m_vControl.AddItemUsesActivity()) {
            Intent intent = m_vControl.SetupNewActivity(a.getApplicationContext(), i);
            if (intent != null) {
                //c.startActivity(intent);
                //c.startActivityForResult(intent, 1);
                intent.putExtra("requestCode", iRequestCode);
                a.startActivityForResult(intent, iRequestCode);
            }
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

        View v = SetupDialog(c,i);
        builder.setView(v);
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

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                if (Tools.IsEmptyString(charString)) {
                    m_DatasetFiltered = m_Dataset;
                } else {
                    ArrayList<ListableObject> filtered = new ArrayList<>();
                    for (ListableObject o : m_Dataset) {
                        if (o.name.toLowerCase().contains(charString.toLowerCase())) {
                            filtered.add(o);
                        }
                    }

                    m_DatasetFiltered = filtered;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = m_DatasetFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                m_DatasetFiltered = (ArrayList<ListableObject>)results.values;
                notifyDataSetChanged();
            }
        };
    }

    public void setReadOnly(boolean bReadOnly, RecyclerView recyclerView) {
        setAllowSwipe(!bReadOnly);

        for (int i = 0; i < recyclerView.getChildCount(); i++) {
            ViewHolder holder = (ViewHolder)recyclerView.findViewHolderForAdapterPosition(i);
            m_vControl.setReadOnly(holder, bReadOnly);
        }

    }

    public void setAllowSwipe(boolean bAllowSwipe) {
        m_SimpleItemTouchHelperCallback.setAllowSwipe(bAllowSwipe);
    }

    public void setSwipeFlags(int iSwipeFlags) {
        m_SimpleItemTouchHelperCallback.setSwipeFlags(iSwipeFlags);
    }
}
