package com.example.myaethelist.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.myaethelist.holder.ItemListViewHolder;
import com.example.myaethelist.OnRecyclerViewClickListener;
import com.example.myaethelist.R;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import model.Item;
import presenter.MyDatabase;

/**
 * Adapter used to show a simple grid of products.
 */
public class ItemListRecyclerViewAdapter extends RecyclerView.Adapter<ItemListViewHolder> implements ItemTouchHelperAdapter {
    private Context context;
    private List<Item> ItemList;
    private OnRecyclerViewClickListener listener;
    private MyDatabase myDatabase;
   /* private ImageRequester imageRequester;*/

    public void setItemClickListener(OnRecyclerViewClickListener itemClickListener) {
        listener = itemClickListener;
    }

    public ItemListRecyclerViewAdapter(List<Item> ItemList, Context context) {
        this.ItemList = ItemList;
        this.context=context;
        /*imageRequester = ImageRequester.getInstance();*/
    }

    @NonNull
    @Override
    public ItemListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
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
        return new ItemListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemListViewHolder holder, int position) {
        if (ItemList != null && position < ItemList.size()) {
            Item product = ItemList.get(position);
            holder.ItemTitle.setText(product.getItemName());
            /*holder.isFinished.setChecked(product.isFinished());*/
            if (product.isFinished()) {
                holder.itemView.setBackgroundColor(Color.GRAY);
                myDatabase=new MyDatabase(context);
                myDatabase.toUpdatePriority(product,0.0);
            }else{
                holder.itemView.setBackgroundColor(Color.WHITE);
            }
        }
    }
    @Override
    public int getItemCount() {
        if (ItemList!=null) {
            return ItemList.size();
        }else {
            return 0;
        }
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        myDatabase=new MyDatabase(context);
        System.out.println("从"+fromPosition+"到"+toPosition);
        Double pa=ItemList.get(fromPosition).getPriority();
        Double pb=ItemList.get(toPosition).getPriority();
        Double p=0.0;
        if (toPosition==0){
            p=pb+0.01;
        }else if (toPosition==ItemList.size()-1){
            p=pa-0.01;
        }else {
            Double sum=(ItemList.get(toPosition-1).getPriority()+ItemList.get(toPosition+1).getPriority())/2;
            p=sum;
        }
        myDatabase.toUpdatePriority(ItemList.get(fromPosition),p);
        Collections.swap(ItemList,fromPosition,toPosition);
        notifyItemMoved(fromPosition,toPosition);
       /* Collections.sort(ItemList, new Comparator<Item>() {

            @Override
            public int compare(Item o1, Item o2) {
                if (o1.getPriority()>o2.getPriority()){
                    return 1;
                }else if(o1.getPriority()==o2.getPriority()){
                    return 0;
                }else {
                    return -1;
                }
            }
        });*/
    }

    @Override
    public void onItemDissmiss(int position) {
        MyDatabase myDatabase;
        myDatabase = new MyDatabase(this.context);
        myDatabase.toDelete(ItemList.get(position).getId());
        ItemList.remove(position);
        notifyItemRemoved(position);
    }
}
