package com.postterminal.postterminal;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.postterminal.postterminal.JsonObjects.Result;

import java.util.ArrayList;

public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.CountryViewHolder> {

    private ArrayList< Result > resultArrayList;

    public CountryAdapter(ArrayList<Result> resultArrayList) {
        this.resultArrayList = resultArrayList;
    }



    @NonNull
    @Override
    public CountryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.country_item,
                parent, false);


        return new CountryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CountryViewHolder holder, int position) {

        holder.country2NameTextView.setText(resultArrayList.get (position).getTran_date ());
        holder.country1NameTextView.setText((int) resultArrayList.get (position).getAmout ());
        holder.countryNameTextView.setText(resultArrayList.get (position).getState ());

    }

    @Override
    public int getItemCount() {
        return resultArrayList.size();
    }

    class CountryViewHolder extends RecyclerView.ViewHolder {

        TextView countryNameTextView;
        TextView country1NameTextView;
        TextView country2NameTextView;


        public CountryViewHolder(@NonNull View itemView) {
            super(itemView);

            countryNameTextView = itemView.findViewById((R.id.status));
            country2NameTextView = itemView.findViewById((R.id.date));
        }

    }
}
