package presenter;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

//
public class MyOpenHelper extends SQLiteOpenHelper {
        private  String databaseName;
    private  String tableName1="my_Item";
    private  String tableName2="my_Category";
    public MyOpenHelper(Context context) {
        //String databaseName="mydata";
        super(context,"mydate",null, 1);

    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        System.out.println("创建表");
        String sql="create table if not exists "+ tableName2+"(" + "categoryId integer PRIMARY KEY autoincrement," + "category text)";
        db.execSQL("create table if not exists "+ tableName1+"(" +
                "id integer PRIMARY KEY autoincrement," +
                "category_id integer," +
                "item_name text," +
                "finished boolean,"+
                "priority double,"+
                "enable_notification boolean,"+
                "notift_time text,"+
                "enable_time_range boolean,"+
                "description text,"+
                "due_time text,"+
                "location text," +
                "foreign key(category_id) REFERENCES my_Category(categoryId))");                              //设置时间为文本类型
        System.out.println(sql);
        db.execSQL(sql);
        System.out.println("表创建成功");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i("备忘录","版本更新"+oldVersion+"-->"+newVersion);
    }

}

