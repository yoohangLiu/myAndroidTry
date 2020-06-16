package com.example.myaethelist.holder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.NetworkImageView;
import com.example.myaethelist.R;

public class CategoryCardViewHolder extends RecyclerView.ViewHolder {
    public TextView categoryTitle;
    /*public TextView productPrice;*/

    public CategoryCardViewHolder(@NonNull View itemView) {
        super(itemView);
        categoryTitle = itemView.findViewById(R.id.new_category);
        /*productPrice = itemView.findViewById(R.id.product_price);*/
    }
}
