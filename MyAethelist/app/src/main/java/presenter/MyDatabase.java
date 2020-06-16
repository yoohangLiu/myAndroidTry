package presenter;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import model.Category;
import model.Item;

public class MyDatabase {
    String ItemTable = "my_Item";
    String CategoryTable = "my_Category";
    Context context;
    MyOpenHelper myOpenHelper;
    SQLiteDatabase mydatabase;

    public MyDatabase(Context context) {
        this.context = context;
        myOpenHelper = new MyOpenHelper(context);
    }
    public ArrayList<Category> getCategoryArray(){
        ArrayList<Category> arr = new ArrayList<>();
        mydatabase = myOpenHelper.getReadableDatabase();
        Cursor cursor = mydatabase.rawQuery("select * from " + CategoryTable, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String category = cursor.getString(cursor.getColumnIndex("category"));
            Integer categoryId = cursor.getInt(cursor.getColumnIndex("categoryId"));
            Category category1=new Category(categoryId,category);
            arr.add(category1);
            cursor.moveToNext();
        }
        mydatabase.close();
        return arr;
    }
    public ArrayList<Item> getItemarrays(int category_ID){            //获取listview中要显示的数据
        ArrayList<Item> arr = new ArrayList<>();
        ArrayList<Item> arr1 = new ArrayList<>();
        System.out.println("将要打开数据库");
        mydatabase = myOpenHelper.getReadableDatabase();
        String sql="select * from " + ItemTable+" where category_id="+category_ID;
        System.out.println(sql);
        Cursor cursor = mydatabase.rawQuery(sql, null);
        System.out.println("查出个数："+cursor.getCount());
        if (cursor.getCount()==0){
            return arr;
        }else {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Item data = new Item();
                data.setId(cursor.getInt(cursor.getColumnIndex("id")));
                data.setCategoryId(cursor.getInt(cursor.getColumnIndex("category_id")));
                data.setItemName(cursor.getString(cursor.getColumnIndex("item_name")));
                data.setFinished(Boolean.valueOf(cursor.getString(cursor.getColumnIndex("finished"))));
                data.setPriority(Double.valueOf(cursor.getString(cursor.getColumnIndex("priority"))));
                data.setEnableNotification(Boolean.valueOf(cursor.getString(cursor.getColumnIndex("enable_notification"))));
                data.setNotifyTime(cursor.getString(cursor.getColumnIndex("notift_time")));
                data.setDuration(Boolean.valueOf(cursor.getString(cursor.getColumnIndex("enable_time_range"))));
                data.setDueTime(cursor.getString(cursor.getColumnIndex("due_time")));
                data.setDescription(cursor.getString(cursor.getColumnIndex("description")));
                data.setLocation(cursor.getString(cursor.getColumnIndex("location")));
                arr.add(data);
                cursor.moveToNext();
            }
        }
        mydatabase.close();
        for (int i = arr.size(); i >0; i--) {
            arr1.add(arr.get(i-1));
        }
        return arr1;
    }

    public Category getCategory(int id) {           //获取要修改数据（就是当选择listview子项想要修改数据时，获取数据显示在新建页面）
        mydatabase = myOpenHelper.getWritableDatabase();
        Cursor cursor = mydatabase.rawQuery("select * from " + CategoryTable + " where categoryId=" + id , null);
        cursor.moveToFirst();
        Category data = new Category();
        while (!cursor.isAfterLast()) {
            Integer categoryId = cursor.getInt(cursor.getColumnIndex("categoryId"));
            String category = cursor.getString(cursor.getColumnIndex("category"));
            data.setCategoryId(categoryId);
            data.setCatergory(category);
            cursor.moveToNext();
        }
        mydatabase.close();
        return data;
    }

    public Item getCurrentItem(int id){           //获取要修改数据（就是当选择listview子项想要修改数据时，获取数据显示在新建页面）
        mydatabase = myOpenHelper.getWritableDatabase();
        Cursor cursor = mydatabase.rawQuery("select * from " + ItemTable + " where id='" + id + "'", null);
        cursor.moveToFirst();
        Item data = new Item();
        data.setId(cursor.getInt(cursor.getColumnIndex("id")));
        data.setCategoryId(cursor.getInt(cursor.getColumnIndex("category_id")));
        data.setItemName(cursor.getString(cursor.getColumnIndex("item_name")));
        data.setFinished(Boolean.valueOf(cursor.getString(cursor.getColumnIndex("finished"))));
        data.setPriority(Double.valueOf(cursor.getString(cursor.getColumnIndex("priority"))));
        data.setEnableNotification(Boolean.valueOf(cursor.getString(cursor.getColumnIndex("enable_notification"))));
        data.setNotifyTime(cursor.getString(cursor.getColumnIndex("notift_time")));
        data.setDuration(Boolean.valueOf(cursor.getString(cursor.getColumnIndex("enable_time_range"))));
        data.setDueTime(cursor.getString(cursor.getColumnIndex("due_time")));
        data.setDescription(cursor.getString(cursor.getColumnIndex("description")));
        data.setLocation(cursor.getString(cursor.getColumnIndex("location")));
        mydatabase.close();
        return data;
    }

    public void toUpdate(Item data) {           //修改表中数据
        mydatabase = myOpenHelper.getWritableDatabase();
        mydatabase.execSQL(
                "update "+ItemTable +
                        " set "+
                        "item_name='"+data.getItemName()+
                        "',finished='"+data.isFinished() +
                        "',priority='"+data.getPriority() +
                        "',enable_notification='"+data.isEnableNotification() +
                        "',notift_time='"+data.getNotifyTime() +
                        "',enable_time_range='"+data.isDuration() +
                        "',description='"+data.getDescription() +
                        "',due_time='"+data.getDueTime() +
                        "',location='"+data.getLocation() +
                        "' where id='"+ data.getId()+"'");
        mydatabase.close();
    }
    public void toUpdatePriority(Item data,double p) {           //修改表中数据
        mydatabase = myOpenHelper.getWritableDatabase();
        mydatabase.execSQL(
                "update "+ItemTable +
                        " set "+
                        "priority='"+p+
                        "' where id='"+ data.getId()+"'");
        mydatabase.close();
    }


    public void toUpdateCategory(Category data) {           //修改表中数据
        mydatabase = myOpenHelper.getWritableDatabase();
        mydatabase.execSQL(
                "update " + CategoryTable + " set categoryId=" + data.getCatergory() +
                        ",category='" + data.getCatergory() +
                        "' where ids='" + data.getCategoryId() + "'");
        mydatabase.close();
    }

    public void toInsert(Item data) {           //在表中插入新建的便签的数据
        System.out.println("将要插入新事件");
        mydatabase = myOpenHelper.getWritableDatabase();
        mydatabase.execSQL("insert into "+ ItemTable
                +"(category_id,item_name,finished,priority,enable_notification," +
                "notift_time,enable_time_range,description,due_time,location)" +
                "values('"
                + data.getCategoryId()+"','"
                +data.getItemName()+"','"
                + data.isFinished()+"','"
                +data.getPriority()+"','"
                + data.isEnableNotification()+"','"
                +data.getNotifyTime()+"','"
                + data.isDuration()+"','"
                +data.getDescription()+"','"
                + data.getDueTime()+"','"
                +data.getLocation()
                +"')");
        System.out.println("插入成功");
        mydatabase.close();
    }

    public void toInsertCategory(String categary) {           //在表中插入新建的便签的数据
        mydatabase = myOpenHelper.getWritableDatabase();
        String sql = "insert into " + CategoryTable + "(category)values(" + "'" + categary + "')";
        System.out.println(sql);
        mydatabase.execSQL(sql);
        mydatabase.close();
/*
        mydatabase = myOpenHelper.getWritableDatabase();
        mydatabase.execSQL("insert into mybook(title,content,times)values('"
                + data.getTitle()+"','"
                +data.getContent()+"','"
                +data.getTimes()
                +"')");
        mydatabase.close();*/
    }
    public void toDelete(int ids){            //在表中删除数据
        mydatabase  = myOpenHelper.getWritableDatabase();
        mydatabase.execSQL("delete from "+ ItemTable+" where id="+ids+"");
        mydatabase.close();
    }
    public void toDeleteCategory(Category category){            //在表中删除数据
        int ids=category.getCategoryId();
        mydatabase  = myOpenHelper.getWritableDatabase();
        mydatabase.execSQL("delete from "+ CategoryTable+" where categoryId="+ids+"");
        mydatabase.close();
    }

}
