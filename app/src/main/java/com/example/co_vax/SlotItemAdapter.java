package com.example.co_vax;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SlotItemAdapter extends ArrayAdapter<SlotItem> {
    ArrayList<SlotItem> slot;
    public SlotItemAdapter(Context applicationContext, int activity_search_slot_item, ArrayList<SlotItem> slot) {
        super(applicationContext, activity_search_slot_item, slot);
        this.slot = slot;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.activity_search_slot_item, null);
        TextView txt_item_location_name = (TextView) v.findViewById(R.id.txt_item_location_name);
        TextView txt_item_location_address = (TextView) v.findViewById(R.id.txt_item_location_address);
        TextView txt_item_vaccine_name = (TextView) v.findViewById(R.id.txt_item_vaccine_name);
        TextView txt_item_date = (TextView) v.findViewById(R.id.txt_item_date);
        TextView txt_item_slot = (TextView) v.findViewById(R.id.txt_item_slot);

        txt_item_location_name.setText(slot.get(position).getLocation_name());
        txt_item_location_address.setText(slot.get(position).getLocation_address());
        txt_item_vaccine_name.setText(slot.get(position).getVaccine_name());
        txt_item_date.setText(slot.get(position).getDate());
        txt_item_slot.setText(slot.get(position).getSlot());

        return v;

    }
}
