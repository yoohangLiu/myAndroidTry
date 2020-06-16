package com.example.myaethelist.activity;


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.util.Log;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.myaethelist.NetworkModel.GetItemOfCategory;
import com.example.myaethelist.NetworkModel.LoginResult;
import com.example.myaethelist.ProductGridItemDecoration;
import com.example.myaethelist.URLadapter.ItemListRecyclerViewAdapter2;
import com.example.myaethelist.adapter.ItemListRecyclerViewAdapter;
import com.example.myaethelist.OnRecyclerViewClickListener;
import com.example.myaethelist.R;
import com.example.myaethelist.SimpleItemTouchHelperCallback;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.lang.reflect.Array;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import model.Item;
import presenter.MyDatabase;

public class MainActivity extends AppCompatActivity {
    RecyclerView listView;
    ArrayList<Item> arrayList;
    MyDatabase myDatabase;
    Integer categoryID;
    String session;
    String GetItemOfCategoryPath;
    Message message;
    private ArrayList<com.example.myaethelist.NetworkModel.Item> ItemList=new ArrayList<>();
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    final ArrayList<com.example.myaethelist.NetworkModel.Item> GetItemOfCategory=new ArrayList<>();
                    for (int i=0;i<((ArrayList<com.example.myaethelist.NetworkModel.Category>) msg.obj).size();i++) {
                        GetItemOfCategory.add( ((ArrayList<com.example.myaethelist.NetworkModel.Item>) msg.obj).get(i));
                    }
                    Collections.sort(GetItemOfCategory, new Comparator<com.example.myaethelist.NetworkModel.Item>() {

                        @Override
                        public int compare(com.example.myaethelist.NetworkModel.Item o1, com.example.myaethelist.NetworkModel.Item o2) {
                            if (o1.getPriority()>o2.getPriority()){
                                return -1;
                            }else if(o1.getPriority()==o2.getPriority()){
                                return 0;
                            }else {
                                return 1;
                            }
                        }
                    });
                    System.out.println("成功获取arraylist");
                    System.out.println(GetItemOfCategory.toString());
                    ItemListRecyclerViewAdapter2 adapter = new ItemListRecyclerViewAdapter2(
                            GetItemOfCategory);
                    ItemTouchHelper.Callback callback =
                            new SimpleItemTouchHelperCallback(adapter);
                    ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
                    touchHelper.attachToRecyclerView(listView);
                    listView.setAdapter(adapter);
                    listView.setLayoutManager(new GridLayoutManager(getApplicationContext(),1));
                    int largePadding = getResources().getDimensionPixelSize(R.dimen.shr_product_grid_spacing);
                    int smallPadding = getResources().getDimensionPixelSize(R.dimen.shr_product_grid_spacing_small);
                    listView.addItemDecoration(new ProductGridItemDecoration(largePadding, smallPadding));
                    /*listView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));*/
                    listView.setAdapter(adapter);
                    listView.setOnDragListener(new View.OnDragListener() {
                        @Override
                        public boolean onDrag(View v, DragEvent event) {
                            return false;
                        }
                    });
                    adapter.setItemClickListener(new OnRecyclerViewClickListener() {
                        @Override
                        public void onItemClickListener(View view) {
                            Intent intent = new Intent(getApplicationContext(),NewNote.class);
                            int position = listView.getChildAdapterPosition(view);
                            intent.putExtra("category_id",categoryID);
                            intent.putExtra("item_id",GetItemOfCategory.get(position).getId());
                            startActivity(intent);
                            MainActivity.this.finish();
                        }

                        @Override
                        public void onItemLongClickListener(View view) {

                        }
                    });
                    break;
                default:
                    break;
            }
        }
    };
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        System.out.println("获得infalter成功");
        super.onCreateOptionsMenu(menu);
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        listView=findViewById(R.id.recycler_view);
        Intent intent = this.getIntent();
        session=intent.getStringExtra("session");
        categoryID=intent.getIntExtra("category_id",-1);
        GetItemOfCategoryPath=intent.getStringExtra("GetItemOfCategoryPath");
        System.out.println("MainActivity当前categoryID: "+categoryID);;
        intent.removeExtra("category_id");
        intent.removeExtra("session");
        FloatingActionButton floatingActionButton = findViewById(R.id.floating_action_button);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {   //点击悬浮按钮时，跳转到新建页面
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), NewNote.class);
                intent.putExtra("category_id",categoryID);
                startActivity(intent);
                MainActivity.this.finish();
            }
        });
        /*userID = intent.getIntExtra("user_id",0);*/
        if (session==null){
            System.out.println("当前无已经登陆用户");
            myDatabase = new MyDatabase(this);
            arrayList = myDatabase.getItemarrays(categoryID);
           Collections.sort(arrayList, new Comparator<Item>() {

               @Override
               public int compare(Item o1, Item o2) {
                   if (o1.getPriority()>o2.getPriority()){
                       return -1;
                   }else if(o1.getPriority()==o2.getPriority()){
                       return 0;
                   }else {
                       return 1;
                   }
               }
           });
            System.out.println("成功获取arraylist");
            System.out.println(arrayList.toString());
            ItemListRecyclerViewAdapter adapter = new ItemListRecyclerViewAdapter(
                    arrayList,this);
            ItemTouchHelper.Callback callback =
                    new SimpleItemTouchHelperCallback(adapter);
            ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
            touchHelper.attachToRecyclerView(listView);
            listView.setAdapter(adapter);
            listView.setLayoutManager(new GridLayoutManager(getApplicationContext(),1));
            int largePadding = getResources().getDimensionPixelSize(R.dimen.shr_product_grid_spacing);
            int smallPadding = getResources().getDimensionPixelSize(R.dimen.shr_product_grid_spacing_small);
            listView.addItemDecoration(new ProductGridItemDecoration(largePadding, smallPadding));
            /*listView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));*/
            listView.setAdapter(adapter);
            listView.setOnDragListener(new View.OnDragListener() {
                @Override
                public boolean onDrag(View v, DragEvent event) {
                    return false;
                }
            });
            adapter.setItemClickListener(new OnRecyclerViewClickListener() {
                @Override
                public void onItemClickListener(View view) {
                    Intent intent = new Intent(getApplicationContext(),NewNote.class);
                    int position = listView.getChildAdapterPosition(view);
                    intent.putExtra("category_id",categoryID);
                    intent.putExtra("item_id",arrayList.get(position).getId());
                    startActivity(intent);
                    MainActivity.this.finish();
                }

                @Override
                public void onItemLongClickListener(View view) {

                }
            });
        }else {
            requestResouces(GetItemOfCategoryPath);
        }


    }
    private void requestResouces(String url){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.i("登录服务器失败：", "注意网络连接");
            }

            @Override
            public void onResponse(Response response) throws IOException {

                if (response.isSuccessful()) {
                    String json = response.body().string();
                    Log.i("MainActivity", "json=" + json);
                    Gson gson = new Gson();
                    GetItemOfCategory getItemOfCategory = gson.fromJson(json, GetItemOfCategory.class);
                    if (getItemOfCategory.getResult().trim().equals("success")){
                        for (int i=0;i<getItemOfCategory.getData().size();i++){
                            ItemList.add(getItemOfCategory.getData().get(i));
                        }
                        message =new Message();
                        message.what=1;
                        message.obj=ItemList;
                        handler.sendMessage(message);
                    }else {
                        Log.i("获取当前item：", "失败");
                    }
                }
            }
        });


    }
    @Override
    public void onBackPressed() {     //重写返回建方法，如果是属于新建则插入数据表并返回主页面，如果是修改，修改表中数据并返回主页面
        Intent intent = new Intent(getApplicationContext(), ShowCategory.class);
        startActivity(intent);
        MainActivity.this.finish();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_newnote:
                Intent intent = new Intent(getApplicationContext(), NewNote.class);
                startActivity(intent);
                MainActivity.this.finish();
                break;
                case R.id.login:
                    Intent intent2 = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent2);
                    MainActivity.this.finish();
                    break;
                case R.id.menu_exit:
                MainActivity.this.finish();
                break;
            default:
                break;
        }
        return  true;
        //return false;????是用哪个true or false？
    }
}
