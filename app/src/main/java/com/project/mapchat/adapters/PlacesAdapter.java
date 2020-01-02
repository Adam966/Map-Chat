package com.project.mapchat.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.project.mapchat.R;

import java.util.ArrayList;

public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.ViewHolder> {
    private ArrayList<CarmenFeature> list;
    private ItemClick itemClick;

    public PlacesAdapter(ArrayList<CarmenFeature> list, ItemClick itemClick) {
        this.list = list;
        this.itemClick = itemClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.places_adapter_layout, parent, false);
        return new ViewHolder(view, itemClick);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textView.setText(list.get(position).placeName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView textView;
        ItemClick itemClick;

        public ViewHolder(@NonNull View itemView, ItemClick itemClick) {
            super(itemView);

            textView = itemView.findViewById(R.id.placeName);
            this.itemClick = itemClick;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            itemClick.onItemClick(list.get(getAdapterPosition()));
        }
    }

    public interface ItemClick {
        void onItemClick(CarmenFeature feature);
    }
}
