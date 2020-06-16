package com.example.myaethelist.holder;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myaethelist.R;

public class ItemListViewHolder2 extends RecyclerView.ViewHolder {
    //public NetworkImageView productImage;
    public TextView ItemTitle;
    public ItemListViewHolder2(@NonNull View itemView) {
        super(itemView);
        //productImage = itemView.findViewById(R.id.product_image);
        ItemTitle = itemView.findViewById(R.id.list_title);
        /*ItemTime = itemView.findViewById(R.id.time_range_show);*/
        // 为ItemView添加点击事件

    }

}
