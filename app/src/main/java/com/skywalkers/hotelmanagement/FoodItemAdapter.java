package com.skywalkers.hotelmanagement;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FoodItemAdapter extends RecyclerView.Adapter<FoodItemAdapter.ViewHolder> {

    public interface itemClicked{
        void onItemClicked(int index);
    }

    private ArrayList<FoodMenuItem> menuList;
    itemClicked activity;

    public FoodItemAdapter(Context context, ArrayList<FoodMenuItem> list){
        menuList=list;
        activity=(itemClicked)context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView veg_non_veg;
        TextView Foodname,ingredients,price;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Foodname=itemView.findViewById(R.id.Foodname);
            ingredients=itemView.findViewById(R.id.ingredients);
            price=itemView.findViewById(R.id.price);
            veg_non_veg=itemView.findViewById(R.id.veg_non_veg);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.onItemClicked(menuList.indexOf((FoodMenuItem)v.getTag()));
                }
            });
        }
    }

    @NonNull
    @Override
    public FoodItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodItemAdapter.ViewHolder holder, int position) {
        holder.itemView.setTag(menuList.get(position));
        holder.Foodname.setText(menuList.get(position).getItemName());
        holder.ingredients.setText(menuList.get(position).getIngredientsList());
        holder.price.setText(""+menuList.get(position).getCost());
        holder.veg_non_veg.setImageResource(menuList.get(position).getVegNonVeg());
    }

    @Override
    public int getItemCount() {
        return menuList.size();
    }
}
