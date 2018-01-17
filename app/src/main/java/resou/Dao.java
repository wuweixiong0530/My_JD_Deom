package resou;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shinelon on 2017/12/2.
 */

public class Dao  {

    private Myhelpher my;
    private SQLiteDatabase db;
    private SQLiteDatabase d;

    public Dao(Context context) {
     my=  new Myhelpher(context);
    }

    /**
     * 插入数据的操作
     */
    public int insertJson(String json){

        SQLiteDatabase database = my.getWritableDatabase();
        //再去添加
        ContentValues values = new ContentValues();
        values.put("json",json);
        database.insert("shuju1",null,values);
        //关闭
        database.close();
        return 1;
    }

    public List<String> sel(){
        d = my.getReadableDatabase();

        List<String> list=new ArrayList<>();
        Cursor cursor = d.rawQuery("select * from shuju1", null);

        while (cursor.moveToNext()){
            String s = cursor.getString(1);
            list.add(s);


        }
        return list;


    }


    public void del(){
         db = my.getWritableDatabase();
        db.execSQL("delete from shuju1");


    }
    public int delyi(String i){
        db = my.getWritableDatabase();
        db.execSQL("delete from shuju1 where json=?",new String[]{i});

        return 1;

    }



}
