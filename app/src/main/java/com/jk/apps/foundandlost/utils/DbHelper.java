package com.jk.apps.foundandlost.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.jk.apps.foundandlost.model.PostModel;

import java.util.ArrayList;
import java.util.List;

public class DbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "postsdb";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = "posts";

    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " ( post_id INTEGER PRIMARY KEY AUTOINCREMENT, post_name TEXT, post_phone TEXT, post_info TEXT,post_date long, post_location TEXT,post_lat TEXT,post_long TEXT,post_type INTEGER)";
        db.execSQL(query);
    }

    public void addNewAdvert(String adName, String adPhone, String adInfo, long adTime, String adLocation, double post_lat, double post_long, int adType) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("post_name", adName);
        values.put("post_phone", adPhone);
        values.put("post_info", adInfo);
        values.put("post_date", adTime);
        values.put("post_location", adLocation);
        values.put("post_lat", post_lat + "");
        values.put("post_long", post_long + "");
        values.put("post_type", adType);
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public List<PostModel> getAllData() {
        List<PostModel> postModels = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("select * from " + TABLE_NAME, null);
            if (cursor.moveToFirst()) {
                do {
                    postModels.add(new PostModel(cursor.getInt(0),
                            cursor.getString(1),
                            cursor.getString(2),
                            cursor.getString(3),
                            cursor.getLong(4),
                            cursor.getString(5),
                            cursor.getDouble(6),
                            cursor.getDouble(7),
                            cursor.getInt(8)));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
            db.close();
        }
        return postModels;
    }


    public PostModel getPostById(int post_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("select * from " + TABLE_NAME + " where post_id = " + post_id, null);
            if (cursor.moveToFirst()) {
                return new PostModel(cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getLong(4),
                        cursor.getString(5),
                        cursor.getDouble(6),
                        cursor.getDouble(7),
                        cursor.getInt(8));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
            db.close();
        }
        return null;
    }

    public void removePostById(int post_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.execSQL("delete from " + TABLE_NAME + " where post_id = " + post_id);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }

}
