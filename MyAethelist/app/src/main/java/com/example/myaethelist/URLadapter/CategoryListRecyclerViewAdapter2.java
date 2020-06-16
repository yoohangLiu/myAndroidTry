package com.example.myaethelist.URLadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myaethelist.NetworkModel.Category;
import com.example.myaethelist.OnRecyclerViewClickListener;
import com.example.myaethelist.R;
import com.example.myaethelist.adapter.ItemTouchHelperAdapter;
import com.example.myaethelist.holder.ItemListViewHolder2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Handler;

/**
 * Adapter used to show a simple grid of products.
 */
public class CategoryListRecyclerViewAdapter2 extends RecyclerView.Adapter<ItemListViewHolder2> implements ItemTouchHelperAdapter {
    private List<com.example.myaethelist.NetworkModel.Category> categoryList;
    private OnRecyclerViewClickListener listener;

    public CategoryListRecyclerViewAdapter2(ArrayList<Category> urlGetCategoryList) {
        this.categoryList=urlGetCategoryList;
    }
    /* private ImageRequester imageRequester;*/

    public void setItemClickListener(OnRecyclerViewClickListener itemClickListener) {
        listener = itemClickListener;
    }

    public CategoryListRecyclerViewAdapter2(List<Category> ItemList, Handler context) {
        this.categoryList = ItemList;
        /*imageRequester = ImageRequester.getInstance();*/
    }

    @NonNull
    @Override
    public ItemListViewHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        if(listener != null){
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClickListener(v);
                }
            });

           /* view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    listener.onItemLongClickListener(v);
                    return true;
                }
            });*/
        }
        return new ItemListViewHolder2(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemListViewHolder2 holder, int position) {
        if (categoryList != null && position < categoryList.size()) {
            Category category = categoryList.get(position);
            holder.ItemTitle.setText(category.getCategory_name());
            /*holder.ItemTime.setText(product.getDueTime());*/
            //imageRequester.setImageFromUrl(holder.productImage, product.url);
        }
    }
    @Override
    public int getItemCount() {
        if (categoryList !=null) {
            return categoryList.size();
        }else {
            return 0;
        }
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        Collections.swap(categoryList,fromPosition,toPosition);
        notifyItemMoved(fromPosition,toPosition);
    }

    @Override
    public void onItemDissmiss(int position) {
        categoryList.remove(position);
       /* MyDatabase myDatabase;
        myDatabase = new MyDatabase(this.context);
        myDatabase.toDelete(categoryList.get(position).getId());*/
        notifyItemRemoved(position);
    }
}
