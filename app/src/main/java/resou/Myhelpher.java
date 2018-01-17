package resou;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Shinelon on 2017/12/2.
 */

public class Myhelpher extends SQLiteOpenHelper {
    public Myhelpher(Context context) {
        super(context,"sss.db",null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //创建表
        sqLiteDatabase.execSQL("create table shuju1(id integer primary key autoincrement,json text not null)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
