package com.skywalkers.hotelmanagement;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DropDownAdapter extends ArrayAdapter {

    int[] Images;
    String[] values;
    Context context;

    public DropDownAdapter(@NonNull Context context, int resource, String[] objects, String[] values, int[] Images) {
        super(context, resource,objects);
        this.context=context;
        this.values=values;
        this.Images=Images;
    }

    public View getCustomView(int position, View convertView, ViewGroup parent, Context context, String values[], int Images[]){
        //inflate the view
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout=inflater.inflate(R.layout.custom_spinner,parent,false);

        //Declaring and setting values to textView and ImageView
        TextView textView=(TextView)layout.findViewById(R.id.textView);
        textView.setText(values[position]);
        ImageView imageView=(ImageView)layout.findViewById(R.id.imageView);
        imageView.setImageResource(Images[position]);

        return layout;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position,convertView,parent,context,values,Images);
    }
    @Override
    public View getView(int position,@Nullable View convertView,@NonNull ViewGroup parent) {
        return getCustomView(position,convertView,parent,context,values,Images);
    }
}

