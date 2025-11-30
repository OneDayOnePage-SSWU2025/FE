package com.example.onedayonepaper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class CustomSpinnerAdapter extends BaseAdapter {

    private Context context;
    private List<String> pages;
    private LayoutInflater inflater;

    public CustomSpinnerAdapter(Context context, List<String> pages) {
        this.context = context;
        this.pages = pages;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() { return pages.size(); }

    @Override
    public Object getItem(int position) { return pages.get(position); }

    @Override
    public long getItemId(int position) { return position; }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.custom_spinner, parent, false);
        }

        TextView tv = convertView.findViewById(R.id.tvSelected);
        tv.setText(pages.get(position));

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_spinner_dropdown, parent, false);
        }

        TextView tv = convertView.findViewById(R.id.tvDropdown);
        tv.setText(pages.get(position));

        return convertView;
    }
}
