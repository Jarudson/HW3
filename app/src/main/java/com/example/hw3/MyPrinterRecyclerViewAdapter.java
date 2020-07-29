package com.example.hw3;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hw3.PrinterList.OnListPrinterClickInteraction;
import com.example.hw3.printers.PrinterListContent.Printer;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Printer} and makes a call to the
 * specified {@link OnListPrinterClickInteraction}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyPrinterRecyclerViewAdapter extends RecyclerView.Adapter<MyPrinterRecyclerViewAdapter.ViewHolder> {

    private final List<Printer> mValues;
    private final OnListPrinterClickInteraction mListener;

    //public static final List<Printer> PRINTERS = new ArrayList<Printer>();

    public MyPrinterRecyclerViewAdapter(List<Printer> items, OnListPrinterClickInteraction listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_printer_list, parent, false);

        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.mItem = mValues.get(position);
        holder.mTextViewName.setText("Name: " + mValues.get(position).getName());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        Date date = new Date(mValues.get(position).getDate());
        String dateTxt = simpleDateFormat.format(date);
        holder.mTextViewDate.setText("Acceptance date:" +  dateTxt);

        holder.mImTrash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onButtonTrashClick(position);
            }
        });

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onListPrinterClickInteraction(holder.mItem, position);
            }
        });
        holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mListener.onListPrinterLongClickInteraction(holder.mItem);
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mTextViewDate;
        public final TextView mTextViewName;
        public final ImageButton mImTrash;
        public Printer mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTextViewName = (TextView) view.findViewById(R.id.nameView);
            mTextViewDate = (TextView) view.findViewById(R.id.dateView);
            mImTrash = (ImageButton) view.findViewById(R.id.trash);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTextViewName.getText() + "'";
        }
    }
}
