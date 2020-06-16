package com.example.myaethelist.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.toolbox.HttpResponse;
import com.example.myaethelist.NetworkModel.GetCategoryResult;
import com.example.myaethelist.NetworkModel.HttpUtils;
import com.example.myaethelist.OnRecyclerViewClickListener;
import com.example.myaethelist.ProductGridItemDecoration;
import com.example.myaethelist.R;
import com.example.myaethelist.SimpleCategoryTouchHelperCallback;
import com.example.myaethelist.URLadapter.CategoryListRecyclerViewAdapter2;
import com.example.myaethelist.adapter.CategoryListRecyclerViewAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import model.Category;
import presenter.MyDatabase;

public class ShowCategory extends AppCompatActivity {
    RecyclerView listView;
    ArrayList<Category> arrayList;
    MyDatabase myDatabase;
    Context context=this;
    Message message;
    String session;
     Handler handler=new Handler(){
         @Override
         public void handleMessage(Message msg) {
             super.handleMessage(msg);
             switch (msg.what) {
                 case 1:
                     final ArrayList<com.example.myaethelist.NetworkModel.Category> UrlGetCategoryList=new ArrayList<>();
                     for (int i=0;i<((ArrayList<com.example.myaethelist.NetworkModel.Category>) msg.obj).size();i++) {
                         UrlGetCategoryList.add( ((ArrayList<com.example.myaethelist.NetworkModel.Category>) msg.obj).get(i));
                     }
                     CategoryListRecyclerViewAdapter2 adapter = new CategoryListRecyclerViewAdapter2(
                             UrlGetCategoryList);
                     ItemTouchHelper.Callback callback =
                             new SimpleCategoryTouchHelperCallback(adapter);
                     ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
                     touchHelper.attachToRecyclerView(listView);
                     listView.setAdapter(adapter);
                     listView.setLayoutManager(new GridLayoutManager(getApplicationContext(),1));
                     //添加Android自带的分割线
                     int largePadding = getResources().getDimensionPixelSize(R.dimen.shr_product_grid_spacing);
                     int smallPadding = getResources().getDimensionPixelSize(R.dimen.shr_product_grid_spacing_small);
                     listView.addItemDecoration(new ProductGridItemDecoration(largePadding, smallPadding));

                     //listView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
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
                             Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                             int position = listView.getChildAdapterPosition(view);
                             System.out.println("当前categaryid:"+UrlGetCategoryList.get(position).getId());
                             intent.putExtra("session",session);
                             String GetItemOfCategoryPath="https://www.foodiesnotalone.cn/aetherlist/server.php?opcode=getItemOfCategory&session="+session+"&id="+UrlGetCategoryList.get(position).getId();
                             intent.putExtra("GetItemOfCategoryPath",GetItemOfCategoryPath);
                             startActivity(intent);
                             ShowCategory.this.finish();
                         }

                         @Override
                         public void onItemLongClickListener(final View view) {
                             new AlertDialog.Builder(ShowCategory.this)
                                     //.setTitle("确定要删除此便签？")
                                     .setMessage("确定要删除此便签？")
                                     .setNegativeButton("取消",new DialogInterface.OnClickListener(){
                                         @Override
                                         public void onClick(DialogInterface dialog, int which) {

                                         }
                                     })
                                     .setPositiveButton("确定",new DialogInterface.OnClickListener(){
                                         @Override
                                         public void onClick(DialogInterface dialog, int which) {
                                             int position = listView.getChildAdapterPosition(view);
                                             myDatabase.toDeleteCategory(arrayList.get(position));

                                         }
                                     })
                                     .create()
                                     .show();
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
        getMenuInflater().inflate(R.menu.category_toolbar_menu, menu);
        System.out.println("获得infalter成功");
        super.onCreateOptionsMenu(menu);
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_category);
        Toolbar toolbar = findViewById(R.id.category_app_bar);
        setSupportActionBar(toolbar);
        listView=findViewById(R.id.category_recycler_view);

        FloatingActionButton floatingActionButton = findViewById(R.id.category_floating_action_button);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {   //点击悬浮按钮时，跳转到新建页面
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), NewCategory.class);
                startActivity(intent);
                ShowCategory.this.finish();
            }
        });
        Intent intent = this.getIntent();
        final String getCategoryURL = intent.getStringExtra("getCategoryURL");
         session=intent.getStringExtra("session");
        if(session==null){
            myDatabase = new MyDatabase(this);
            arrayList = myDatabase.getCategoryArray();

            CategoryListRecyclerViewAdapter adapter = new CategoryListRecyclerViewAdapter(
                    arrayList,this);
            ItemTouchHelper.Callback callback =
                    new SimpleCategoryTouchHelperCallback(adapter);
            ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
            touchHelper.attachToRecyclerView(listView);
            listView.setAdapter(adapter);
            listView.setLayoutManager(new GridLayoutManager(getApplicationContext(),1));
            //添加Android自带的分割线
            int largePadding = getResources().getDimensionPixelSize(R.dimen.shr_product_grid_spacing);
            int smallPadding = getResources().getDimensionPixelSize(R.dimen.shr_product_grid_spacing_small);
            listView.addItemDecoration(new ProductGridItemDecoration(largePadding, smallPadding));

            //listView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
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
                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    int position = listView.getChildAdapterPosition(view);
                    System.out.println("当前categaryid:"+arrayList.get(position).getCategoryId());
                    intent.putExtra("category_id",arrayList.get(position).getCategoryId());
                    startActivity(intent);
                    ShowCategory.this.finish();
                }

                @Override
                public void onItemLongClickListener(final View view) {
                    new AlertDialog.Builder(ShowCategory.this)
                            //.setTitle("确定要删除此便签？")
                            .setMessage("确定要删除此便签？")
                            .setNegativeButton("取消",new DialogInterface.OnClickListener(){
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .setPositiveButton("确定",new DialogInterface.OnClickListener(){
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    int position = listView.getChildAdapterPosition(view);
                                    myDatabase.toDeleteCategory(arrayList.get(position));

                                }
                            })
                            .create()
                            .show();
                }
            });
        }else {
            requestResouces(getCategoryURL);
        }

    }
    private void requestResouces(String url){

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            final ArrayList<com.example.myaethelist.NetworkModel.Category> UrlGetCategoryList=new ArrayList<>();
            @Override
            public void onFailure(Request request, IOException e) {
                Log.i("登录服务器失败：", "注意网络连接");return;
            }

            @Override
            public void onResponse(Response response) throws IOException {

                if (response.isSuccessful()) {
                    String json = response.body().string();
                    Log.i("ShowCategory", "json=" + json);
                    Gson gson = new Gson();
                    GetCategoryResult categoryResult = gson.fromJson(json, GetCategoryResult.class);
                    if (categoryResult.getResult().trim().equals("success")){
                        for (int i=0;i<categoryResult.getData().size();i++){
                            UrlGetCategoryList.add(categoryResult.getData().get(i));
                            System.out.println(UrlGetCategoryList.get(i).getCategory_name());
                        }
                        message =new Message();
                        message.what=1;
                        message.obj=UrlGetCategoryList;
                        handler.sendMessage(message);
                    }else {
                        Log.i("登录：", "失败");
                        return;
                    }
                }
            }
        });


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_new_category:
                Intent intent = new Intent(getApplicationContext(), NewCategory.class);
                startActivity(intent);
                ShowCategory.this.finish();
                break;
            case R.id.login:
                Intent intent2 = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent2);
                ShowCategory.this.finish();
                break;
            case R.id.menu_exit:
                ShowCategory.this.finish();
                break;
            case R.id.register:
                Intent intent3 = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent3);
                ShowCategory.this.finish();
                break;
            default:
                break;
        }
        return  true;
        //return false;????是用哪个true or false？
    }
    @Override
    public void onBackPressed() {     //重写返回建方法，如果是属于新建则插入数据表并返回主页面，如果是修改，修改表中数据并返回主页面
        new AlertDialog.Builder(ShowCategory.this)
                //.setTitle("确定要删除此便签？")
                .setMessage("确定要退出此应用？")
                .setNegativeButton("取消",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("确定",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        /*Intent intent = new Intent(ShowCategory.this, MainActivity.class);
                        startActivity(intent);*/
                        ShowCategory.this.finish();
                    }
                })
                .create()
                .show();
    }

}

/*
class mhandler extends Handler{
    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what) {
            case 1:
                ArrayList<com.example.myaethelist.NetworkModel.Category> UrlGetCategoryList=new ArrayList<>();
                for (int i=0;i<((ArrayList<com.example.myaethelist.NetworkModel.Category>) msg.obj).size();i++) {
                    UrlGetCategoryList.add( ((ArrayList<com.example.myaethelist.NetworkModel.Category>) msg.obj).get(i));
                }
                CategoryListRecyclerViewAdapter2 adapter = new CategoryListRecyclerViewAdapter2(
                        UrlGetCategoryList);
                ItemTouchHelper.Callback callback =
                        new SimpleCategoryTouchHelperCallback(adapter);
                ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
                touchHelper.attachToRecyclerView(listView);
                listView.setAdapter(adapter);
                listView.setLayoutManager(new GridLayoutManager(getApplicationContext(),1));
                //添加Android自带的分割线
                int largePadding = getResources().getDimensionPixelSize(R.dimen.shr_product_grid_spacing);
                int smallPadding = getResources().getDimensionPixelSize(R.dimen.shr_product_grid_spacing_small);
                listView.addItemDecoration(new ProductGridItemDecoration(largePadding, smallPadding));

                //listView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
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
                        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                        int position = listView.getChildAdapterPosition(view);
                        System.out.println("当前categaryid:"+arrayList.get(position).getCategoryId());
                        intent.putExtra("category_id",arrayList.get(position).getCategoryId());
                        startActivity(intent);
                        ShowCategory.this.finish();
                    }

                    @Override
                    public void onItemLongClickListener(final View view) {
                        new AlertDialog.Builder(ShowCategory.this)
                                //.setTitle("确定要删除此便签？")
                                .setMessage("确定要删除此便签？")
                                .setNegativeButton("取消",new DialogInterface.OnClickListener(){
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })
                                .setPositiveButton("确定",new DialogInterface.OnClickListener(){
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        int position = listView.getChildAdapterPosition(view);
                                        myDatabase.toDeleteCategory(arrayList.get(position));

                                    }
                                })
                                .create()
                                .show();
                    }
                });
                break;
            default:
                break;
        }
    }
    }
*/
