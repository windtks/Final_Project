package com.example.windkts.final_project.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
        sqLiteDatabase.execSQL("create table Translation (orginal_t text primary key, translated_t text )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    public void insert(String o, String t){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("orginal_t",o);
        values.put("translated_t",t);
        db.insertOrThrow(TABLE_NAME,null,values);
        db.close();
    }
    public void delete(String o){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME,"orginal_t text=?",new String[] { o });
        db.close();
    }
    public Cursor query(String o) {
        return getReadableDatabase().query(TABLE_NAME, null, "orginal_t text=?", new String[]{o}, null, null, null);
    }
    public Cursor queryAll() {
        return getReadableDatabase().query(TABLE_NAME, null, null, null, null, null, null);
    }
}
