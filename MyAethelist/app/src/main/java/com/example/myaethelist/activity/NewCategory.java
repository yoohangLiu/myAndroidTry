package com.example.myaethelist.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.example.myaethelist.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Date;

import model.Category;
import model.Item;
import presenter.MyDatabase;

public class NewCategory extends AppCompatActivity {
    EditText ed_category;
    FloatingActionButton floatingActionButton;
    MyDatabase myDatabase;
    Category data;
    int ids;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_category);
          ed_category = findViewById(R.id.new_category);
        floatingActionButton =findViewById(R.id.finish);
        Intent intent = this.getIntent();
        ids = intent.getIntExtra("category_id",0);
        myDatabase = new MyDatabase(this);
        if (ids != 0){
            data = myDatabase.getCategory(ids);
            ed_category.setText(data.getCatergory());
        }
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSave();
            }
        });
    }
    @Override
    public void onBackPressed() {     //重写返回建方法，如果是属于新建则插入数据表并返回主页面，如果是修改，修改表中数据并返回主页面
        new AlertDialog.Builder(NewCategory.this)
                //.setTitle("确定要删除此便签？")
                .setMessage("确定要退出此便签编辑？（不保存）")
                .setNegativeButton("取消",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("确定",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(NewCategory.this, ShowCategory.class);
                        // intent.putExtra("ids",arrayList.get(position).getIds());
                        startActivity(intent);
                        NewCategory.this.finish();
                    }
                })
                .create()
                .show();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = new Date(System.currentTimeMillis());
        String time = simpleDateFormat.format(date);
        String category = ed_category.getText().toString();
        if(ids!=0){
            data=new Category();
            /* myDatabase.toUpdate(data);*/
            Intent intent=new Intent(NewCategory.this,ShowCategory.class);
            startActivity(intent);
            NewCategory.this.finish();
        }
        //新建日记
        else{
            data=new Category();
            /*myDatabase.toInsert(data);*/
            Intent intent=new Intent(NewCategory.this,ShowCategory.class);
            startActivity(intent);
            NewCategory.this.finish();
        }

    }
    private void isSave(){   //写一个方法进行调用，如果是属于新建则插入数据表并返回主页面，如果是修改，修改表中数据并返回主页面
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd HH：mm");
        Date date = new Date(System.currentTimeMillis());
        String time = simpleDateFormat.format(date);
        Log.d("new_note", "isSave: "+time);
        String category = ed_category.getText().toString();
        System.out.println("注意了："+category);
        if(ids!=0){
            data=new Category();
            data.setCategoryId(ids);
            data.setCatergory(category);
            myDatabase.toUpdateCategory(data);
            Intent intent=new Intent(NewCategory.this,ShowCategory.class);
            startActivity(intent);
            NewCategory.this.finish();
        }
        //新建日记
        else{
            myDatabase.toInsertCategory(category);
            Intent intent=new Intent(NewCategory.this,ShowCategory.class);
            startActivity(intent);
            NewCategory.this.finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.new_lo,menu);
        return true;
    }

}
