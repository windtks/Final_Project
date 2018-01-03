package com.example.windkts.final_project.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.windkts.final_project.History;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fwaa2 on 2018.01.02.
 */

public class DB extends SQLiteOpenHelper{
    private static final String DB_NAME = "Translation.db";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = "Translation";
    public DB(Context context) {
        super(context,DB_NAME,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table Translation (orginal_t text primary key, translated_t text, is_liked int )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Translation");
        onCreate(sqLiteDatabase);
    }
    public void insert(String o, String t){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("orginal_t",o);
        values.put("translated_t",t);
        values.put("is_liked",0);
        db.insertOrThrow(TABLE_NAME,null,values);
        db.close();
    }
    public void delete(String o){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME,"orginal_t=?",new String[] { o });
        db.close();
    }
    public Cursor query(String o) {
        return getReadableDatabase().query(TABLE_NAME, null, "orginal_t=?", new String[]{o}, null, null, null);
    }
    public Cursor queryAll() {
        return getReadableDatabase().query(TABLE_NAME, null, null, null, null, null, null);
    }
    public Cursor queryAllLike() {
        return getReadableDatabase().query(TABLE_NAME, null, "is_liked=?", new String[]{"1"}, null, null, null);
    }
    public List<History> getAllData() {
        Cursor all = queryAll();
        List<History> list = new ArrayList<>();
        while(all.moveToNext()){
            String o = all.getString(all.getColumnIndex("orginal_t"));
            String r = all.getString(all.getColumnIndex("translated_t"));
            int l = all.getInt(all.getColumnIndex("is_liked"));
            History h = new History(o,r,l);
            list.add(h);
        }
        return list;
    }
}
