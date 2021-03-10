package com.postterminal.postterminal;

import android.provider.Settings;
import android.service.autofill.Dataset;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.postterminal.postterminal.JsonObjects.Result;
import com.postterminal.postterminal.JsonObjects.UserList;

import java.util.ArrayList;

public class PaymentHistoryAdapter extends RecyclerView.Adapter< PaymentHistoryAdapter.ViewHolder> {

    private ArrayList<Result> dataSet;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView Status;
        private TextView Amount;
        private TextView Date;
        private TextView document_id;
        private TextView card_number;

        public ViewHolder(View Statisticsview) {
            super(Statisticsview);
            // Define click listener for the ViewHolder's View

            this.Status = (TextView) Statisticsview.findViewById(R.id.status);
            this.Amount = (TextView) Statisticsview.findViewById(R.id.Amount);
            this.Date = (TextView) Statisticsview.findViewById(R.id.date);
            this.document_id = (TextView) Statisticsview.findViewById(R.id.document_id);
            this.card_number = (TextView) Statisticsview.findViewById(R.id.Card_Number);
        }
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param data String[] containing the data to populate views to be used
     * by RecyclerView.
     */
    public PaymentHistoryAdapter(ArrayList< Result > data) {
        this.dataSet = data;
        Log.d ("eeeeeeee", dataSet.size ()+"");
        Log.d ("eeeeeeee", dataSet+"");
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.country_item, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        TextView Status = holder.Status;
        TextView Date = holder.Date;
        TextView Amount = holder.Amount;
        TextView document_id = holder.document_id;
        TextView card_number = holder.card_number;

        // Get element from your dataset at this position and replace the
        // contents of the view with that element

        Status.setText (dataSet.get (position).getState ());
        Date.setText (dataSet.get (position).getTran_date ());
        Amount.setText (dataSet.get (position).getAmout () + "");
        if (document_id == null){
            document_id.setText ("Платёжь не совершён");
        }
        document_id.setText ((Integer) dataSet.get (position).getDocument_id ());
        card_number.setText (dataSet.get (position).getCard_pan () + "");

        if (card_number == null){
            card_number.setText ("Платёжь не совершён");
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return dataSet.size ();
    }
}
