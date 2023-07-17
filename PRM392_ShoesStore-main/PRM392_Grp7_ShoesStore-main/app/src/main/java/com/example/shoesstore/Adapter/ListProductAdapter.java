package com.example.shoesstore.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.shoesstore.R;

import java.util.ArrayList;

public class ListProductAdapter extends ArrayAdapter<Listdataproduct> {


    public ListProductAdapter(@NonNull Context context, ArrayList<Listdataproduct> dataArrayList) {
        super(context, R.layout.list_item, dataArrayList );
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        Listdataproduct listdataproduct = getItem(position);

        if(view == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent,false);
        }

        ImageView listImage = view.findViewById(R.id.listImage);
        TextView listName = view.findViewById(R.id.listName);
        TextView listPrice = view.findViewById(R.id.listPrice);

        listImage.setImageResource(listdataproduct.image);
        listName.setText(listdataproduct.name);
        listPrice.setText(listdataproduct.price);

        return view;
    }
}
