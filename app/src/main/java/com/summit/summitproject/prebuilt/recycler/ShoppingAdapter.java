package com.summit.summitproject.prebuilt.recycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.summit.summitproject.R;

import java.util.List;

/**
 * Created by Dytstudio.
 */

public class ShoppingAdapter extends RecyclerView.Adapter<ShoppingAdapter.MyViewHolder> {

    private List<ItemShopping> items;
    private Context context;

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView pieces, name;

        public MyViewHolder(View view) {
            super(view);

            pieces = (TextView) view.findViewById(R.id.tv_pieces);
            name = (TextView) view.findViewById(R.id.tv_name);
        }

    }


    public ShoppingAdapter(List<ItemShopping> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @Override
    public ShoppingAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_shopping, parent, false);

        return new ShoppingAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ShoppingAdapter.MyViewHolder holder, int position) {
        ItemShopping itemShopping = items.get(position);
        holder.name.setText(itemShopping.getName());
        holder.pieces.setText(itemShopping.getPieces());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}