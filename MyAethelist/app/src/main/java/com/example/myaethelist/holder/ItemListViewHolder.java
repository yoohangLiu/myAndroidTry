package com.example.myaethelist.holder;

import android.icu.text.Transliterator;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.NetworkImageView;
import com.example.myaethelist.R;

import java.util.Date;

public class ItemListViewHolder extends RecyclerView.ViewHolder {
    //public NetworkImageView productImage;
    public TextView ItemTitle;
    /*public CheckBox isFinished;*/
    public ItemListViewHolder(@NonNull View itemView) {
        super(itemView);
        //productImage = itemView.findViewById(R.id.product_image);
        ItemTitle = itemView.findViewById(R.id.list_title);
        /*isFinished = itemView.findViewById(R.id.isfinished_checkBox);*/

        /*ItemTime = itemView.findViewById(R.id.time_range_show);*/
        // 为ItemView添加点击事件

    }

}
