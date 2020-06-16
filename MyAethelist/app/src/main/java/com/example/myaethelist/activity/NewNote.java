package com.example.myaethelist.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.myaethelist.NetworkModel.GetItemOfCategory;
import com.example.myaethelist.R;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import model.Category;
import model.Item;
import presenter.MyDatabase;

public class NewNote extends AppCompatActivity {
    SwitchMaterial isFinished;
    EditText ed_title;
    EditText ed_content;
    SeekBar priority;
    Double priority_value;
    TextView show_priority_value;
    SwitchMaterial dueTime;
    TextView time_range_show;
    SwitchMaterial ToNotifyTime;
    TextView notify_time_show;
    TextInputEditText location;
    TextInputEditText description;
    FloatingActionButton floatingActionButton;
    MyDatabase myDatabase;
    Item data;
    Integer ids;
    Integer category_id;
    @SuppressLint("WrongViewCast")

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);
        Intent intent = this.getIntent();
        String session=intent.getStringExtra("session");
        System.out.println("session"+session);
        if (session==null) {
            myDatabase = new MyDatabase(this);
            ids = intent.getIntExtra("item_id", -1);
            System.out.println("ids:"+ids);
            intent.removeExtra("item_id");
            category_id = intent.getIntExtra("category_id", -1);
            System.out.println("category_id为:" + category_id);
            intent.removeExtra("category_id");
            isFinished = findViewById(R.id.isfinished);
            ed_title = findViewById(R.id.itemName);
            ed_content = findViewById(R.id.catagory);
            Category ca=myDatabase.getCategory(category_id);
            ed_content.setText(ca.getCatergory());
            priority = findViewById(R.id.priority);
            show_priority_value = findViewById(R.id.priority_value);
            dueTime = findViewById(R.id.timeRange);
            time_range_show = findViewById(R.id.time_range_show);
            ToNotifyTime = findViewById(R.id.toNotify_time);
            notify_time_show = findViewById(R.id.notify_time_show);
            location = findViewById(R.id.location_place);
            description = findViewById(R.id.description_of_item);
            floatingActionButton = findViewById(R.id.finish);
            final Calendar mcalendar = Calendar.getInstance();
            final int year = mcalendar.get(Calendar.YEAR); // 得到当前年
            final int month = mcalendar.get(Calendar.MONTH)+1; // 得到当前月
            final int day = mcalendar.get(Calendar.DAY_OF_MONTH); // 得到当前日
            isFinished.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                }
            });
            dueTime.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        time_range_show.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                new DatePickerDialog(NewNote.this, new DatePickerDialog.OnDateSetListener() {      //  日期选择对话框
                                    @Override
                                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                        time_range_show.setText(year + "年" + (month+1)%12 + "月" + dayOfMonth + "日");
                                    }
                                }, year, month, day).show();
                            }
                        });
                    }
                }
            });
            ToNotifyTime.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        notify_time_show.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                new DatePickerDialog(NewNote.this, new DatePickerDialog.OnDateSetListener() {      //  日期选择对话框
                                    @Override
                                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                        notify_time_show.setText(year + "年" + (month+1)%12 + "月" + dayOfMonth + "日");
                                    }
                                }, year, month, day).show();
                            }
                        });
                    }
                }
            });

            priority.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    priority_value = (double) progress * 0.01;
                    show_priority_value.setText(String.valueOf(priority_value));
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
            floatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ids != -1) {
                        isSave(ids);
                    } else {
                        System.out.println("isSave2" + category_id);
                        isSave2(category_id);
                    }
                }
            });
            if (ids != -1) {
                setContentofThis(ids);
            }

        }else {
            Bundle bundle=intent.getExtras();
            com.example.myaethelist.NetworkModel.Item item= (com.example.myaethelist.NetworkModel.Item) bundle.getSerializable("item");
            setContentofThis(item);
            isFinished = findViewById(R.id.isfinished);
            ed_title = findViewById(R.id.itemName);
            ed_content = findViewById(R.id.catagory);
            priority = findViewById(R.id.priority);
            show_priority_value = findViewById(R.id.priority_value);
            dueTime = findViewById(R.id.timeRange);
            time_range_show = findViewById(R.id.time_range_show);
            ToNotifyTime = findViewById(R.id.toNotify_time);
            notify_time_show = findViewById(R.id.notify_time_show);
            location = findViewById(R.id.location_place);
            description = findViewById(R.id.description_of_item);
            floatingActionButton = findViewById(R.id.finish);
            final Calendar mcalendar = Calendar.getInstance();
            final int year = mcalendar.get(Calendar.YEAR); // 得到当前年
            final int month = mcalendar.get(Calendar.MONTH)+1; // 得到当前月
            final int day = mcalendar.get(Calendar.DAY_OF_MONTH); // 得到当前日
            isFinished.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (!isChecked) {
                        dueTime.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                if (isChecked) {
                                    time_range_show.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            new DatePickerDialog(NewNote.this, new DatePickerDialog.OnDateSetListener() {      //  日期选择对话框
                                                @Override
                                                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                                    time_range_show.setText(year + "年" + (month+1)%13 + "月" + dayOfMonth + "日");
                                                }
                                            }, year, month, day).show();
                                        }
                                    });
                                }
                            }
                        });
                        ToNotifyTime.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                if (isChecked) {
                                    notify_time_show.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            new DatePickerDialog(NewNote.this, new DatePickerDialog.OnDateSetListener() {      //  日期选择对话框
                                                @Override
                                                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                                    notify_time_show.setText(year + "年" + (month+1)%12 + "月" + dayOfMonth + "日");
                                                }
                                            }, year, month, day).show();
                                        }
                                    });
                                }
                            }
                        });
                        priority.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                            @Override
                            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                priority_value = (double) progress * 0.01;
                                show_priority_value.setText(String.valueOf(priority_value));
                            }

                            @Override
                            public void onStartTrackingTouch(SeekBar seekBar) {

                            }

                            @Override
                            public void onStopTrackingTouch(SeekBar seekBar) {

                            }
                        });

                    }
                }
            });

            floatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ids != -1) {
                        isSave(ids);
                    } else {
                        System.out.println("isSave2" + category_id);
                        isSave2(category_id);
                    }
                }
            });
        }


    }
    public void setContentofThis(int id){
        myDatabase=new MyDatabase(this);
        data = myDatabase.getCurrentItem(id);
        ed_title.setText(data.getItemName());
        Category ca=myDatabase.getCategory(category_id);
        System.out.println(ca.getCatergory());
        ed_content.setText(ca.getCatergory());
        priority.setProgress((int)(data.getPriority()*100));
        show_priority_value.setText(String.valueOf(data.getPriority()));
        if(data.isFinished()){
            isFinished.setChecked(true);
        }else {
            isFinished.setChecked(false);
        }
        if(data.isDuration()){
            dueTime.setChecked(true);
            time_range_show.setText(data.getDueTime());
        }else {
            dueTime.setChecked(false);
            time_range_show.setText("null");
        }
        if(data.isEnableNotification()){
            ToNotifyTime.setChecked(true);
            notify_time_show=findViewById(R.id.notify_time_show);
            notify_time_show.setText(data.getNotifyTime());
        }else {
            ToNotifyTime.setChecked(false);
            notify_time_show=findViewById(R.id.notify_time_show);
            notify_time_show.setText("null");
        }
        location=findViewById(R.id.location_place);
        location.setText(data.getLocation());
        description=findViewById(R.id.description_of_item);
        description.setText(data.getDescription());
        //a.getCheckedChipId();
    }
    public void setContentofThis(com.example.myaethelist.NetworkModel.Item data){

        ed_title.setText(data.getItem_name());
        Category ca=myDatabase.getCategory(category_id);
        System.out.println(ca.getCatergory());
        ed_content.setText(ca.getCatergory());
        priority.setProgress((int)(data.getPriority()*100));
        show_priority_value.setText(String.valueOf(data.getPriority()));
        if(data.isFinished()){
            isFinished.setChecked(true);
        }else {
            isFinished.setChecked(false);
        }
        if(data.isEnable_time_range()){
            dueTime.setChecked(true);
            time_range_show.setText(data.getDue_time());
        }else {
            dueTime.setChecked(false);
            time_range_show.setText("null");
        }
        if(data.isEnable_notification()){
            ToNotifyTime.setChecked(true);
            notify_time_show=findViewById(R.id.notify_time_show);
            notify_time_show.setText(data.getNotify_time());
        }else {
            ToNotifyTime.setChecked(false);
            notify_time_show=findViewById(R.id.notify_time_show);
            notify_time_show.setText("null");
        }
        location=findViewById(R.id.location_place);
        location.setText(data.getLocation());
        description=findViewById(R.id.description_of_item);
        description.setText(data.getDescription());
        //a.getCheckedChipId();
    }

    @Override
    public void onBackPressed() {     //重写返回建方法，如果是属于新建则插入数据表并返回主页面，如果是修改，修改表中数据并返回主页面
        new AlertDialog.Builder(NewNote.this)
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
                        Intent intent = new Intent(NewNote.this, MainActivity.class);
                        intent.putExtra("category_id",category_id);
                        startActivity(intent);
                        NewNote.this.finish();
                    }
                })
                .create()
                .show();
    }

    private void isSave(int item_id) {
        Item data = new Item();
        Integer itemID=item_id;
        data.setId(itemID);
        Integer categoryID=category_id;
        data.setCategoryId(categoryID);
        String title = ed_title.getText().toString();
        data.setItemName(title);
        Boolean finished=isFinished.isChecked();
        data.setFinished(finished);
        Double priority=Double.parseDouble(show_priority_value.getText().toString());
        data.setPriority(priority);
        Boolean isduration=dueTime.isChecked();
        data.setDuration(isduration);
        String due_Time=time_range_show.getText().toString();
        data.setDueTime(due_Time);
        Boolean isNotification=ToNotifyTime.isChecked();
        data.setEnableNotification(isNotification);
        String notify_Time=notify_time_show.getText().toString();
        data.setNotifyTime(notify_Time);
        String location1=location.getText().toString();
        data.setLocation(location1);
        String description1=description.getText().toString();
        data.setDescription(description1);
           myDatabase.toUpdate(data);
            Intent intent=new Intent(NewNote.this,MainActivity.class);
            intent.putExtra("category_id",category_id);
            startActivity(intent);
            NewNote.this.finish();

    }
    private void isSave2(Integer category_id) {
        Item data = new Item();
        Integer categoryID=category_id;
        data.setCategoryId(categoryID);
        data.setItemName(ed_title.getText().toString().trim());
        Boolean finished=isFinished.isChecked();
        data.setFinished(finished);
        Double priority=priority_value;
        data.setPriority(priority);
        Boolean isduration=dueTime.isChecked();
        data.setDuration(isduration);
        //String due_Time=time_range_show.getText().toString().trim();
        data.setDueTime(time_range_show.getText().toString().trim());
        Boolean isNotification=ToNotifyTime.isActivated();
        data.setEnableNotification(isNotification);
        //String notify_Time=notify_time_show.getText().toString().trim();
        data.setNotifyTime(notify_time_show.getText().toString().trim());
        String location1=location.getText().toString();
        data.setLocation(location1);
        String description1=description.getText().toString();
        data.setDescription(description1);
        myDatabase.toInsert(data);
        Intent intent=new Intent(NewNote.this,MainActivity.class);
        intent.putExtra("category_id",category_id);
        System.out.println("category_id:"+category_id);
        startActivity(intent);
        NewNote.this.finish();

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
                   /* if (getItemOfCategory.getResult().trim().equals("success")){
                        for (int i=0;i<getItemOfCategory.getData().size();i++){
                            ItemList.add(getItemOfCategory.getData().get(i));
                        }
                        message =new Message();
                        message.what=1;
                        message.obj=ItemList;
                        handler.sendMessage(message);
                    }else {
                        Log.i("获取当前item：", "失败");
                    }*/
                }
            }
        });


    }
}
