package com.example.windkts.final_project.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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
        sqLiteDatabase.execSQL("create table Translation (orginal_t text primary key, translated_t text, lan_from text, lan_to, is_liked int )");
    }
    public void rebuild(){
        SQLiteDatabase db = null;
        try{
            db = getWritableDatabase();
            db.delete(TABLE_NAME,null,null);
        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            if(db!=null){
                db.close();
            }
        }
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Translation");
        onCreate(sqLiteDatabase);
    }
    public void insert(String o, String t,String from, String to){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("orginal_t",o);
        values.put("translated_t",t);
        values.put("is_liked",0);
        values.put("lan_from",from);
        values.put("lan_to",to);
        db.insertOrThrow(TABLE_NAME,null,values);
        db.close();
    }
    public void delete(String o){
        SQLiteDatabase db = null;
        try{
            db = getWritableDatabase();
            db.delete(TABLE_NAME,"orginal_t=?",new String[]{o});
        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            if(db!=null){
                db.close();
            }
        }
    }

    public Cursor queryAllLike() {
        return getReadableDatabase().query(TABLE_NAME, null, "is_liked=?", new String[]{"1"}, null, null, null);
    }
    public boolean queryisliked(String o) {
        SQLiteDatabase db = null;
        Cursor c = null;
        try{
            db = getWritableDatabase();
            c = db.query(TABLE_NAME,null,"orginal_t=?",new String[]{o},null, null, null);
            if(c.moveToNext()){
                Log.e("lhl","liked"+String.valueOf(c.getInt(c.getColumnIndex("is_liked"))));
                if(c.getInt(c.getColumnIndex("is_liked")) == 1 ){
                    return true;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        finally{
            if (c != null) {
                c.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return false;
    }
    public List<History> getAllData() {
        SQLiteDatabase db = null;
        Cursor all = null;
        //SQLiteDatabase db = getWritableDatabase();
       // Cursor all = db.query(TABLE_NAME,null,null,null,null,null,null);
        List<History> list = new ArrayList<>();
        try{
            db = getWritableDatabase();
            all = db.query(TABLE_NAME,null,null,null,null,null,null);
            while(all!=null&&all.moveToNext()) {
                String o = all.getString(all.getColumnIndex("orginal_t"));
                String r = all.getString(all.getColumnIndex("translated_t"));
                String f = all.getString(all.getColumnIndex("lan_from"));
                String t = all.getString(all.getColumnIndex("lan_to"));
                int l = all.getInt(all.getColumnIndex("is_liked"));
                History h = new History(o, r, l, f, t);
                list.add(h);
            }
            return list;
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (all != null) {
                all.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return list;
    }
    public List<History> getAllLike() {
        SQLiteDatabase db = null;
        Cursor all = null;
        //SQLiteDatabase db = getWritableDatabase();
        // Cursor all = db.query(TABLE_NAME,null,null,null,null,null,null);
        List<History> list = new ArrayList<>();
        try{
            db = getWritableDatabase();
            all = db.query(TABLE_NAME,null,"is_liked = ?",new String[]{"1"},null,null,null);
            while(all!=null&&all.moveToNext()) {
                String o = all.getString(all.getColumnIndex("orginal_t"));
                String r = all.getString(all.getColumnIndex("translated_t"));
                String f = all.getString(all.getColumnIndex("lan_from"));
                String t = all.getString(all.getColumnIndex("lan_to"));
                int l = all.getInt(all.getColumnIndex("is_liked"));
                History h = new History(o, r, l, f, t);
                list.add(h);
            }
            return list;
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (all != null) {
                all.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return list;
    }
    public void setisLiked(String o,int i){
        SQLiteDatabase localSQLiteDatabase = getWritableDatabase();
        ContentValues localContentValues = new ContentValues();
        localContentValues.put("is_liked", i);
        localSQLiteDatabase.update(TABLE_NAME, localContentValues, "orginal_t = ?", new String []{o});
        localSQLiteDatabase.close();
    }
}
