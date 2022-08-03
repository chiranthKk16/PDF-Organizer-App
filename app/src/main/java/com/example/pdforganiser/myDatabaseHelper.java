package com.example.pdforganiser;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.io.File;
import java.lang.ref.PhantomReference;

public class myDatabaseHelper extends SQLiteOpenHelper {

    private static String DATABASE_NAME = "pdfOrganise.db";
    private static int DATABASE_VERSION = 1;

    private static String TABLE_NAME = "mainTable";
    private static String COLUMN_ID_MAIN = "_id";
    private static String COLUMN_ID_SUB = "_idsub";
    private static String COLUMN_TABLE_NAME_INDIVIDUAL = "col_table_indi";
    private static String COLUMN_FILE_NAME = "file_name";
    private static String COLUMN_FILE_PATH = "file_path";
    private static String COLUMN_FILE_FILE = "file_file";

    Context context;

    public myDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = " CREATE TABLE " + TABLE_NAME + " (" + COLUMN_ID_MAIN + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_TABLE_NAME_INDIVIDUAL + " TEXT); ";

        db.execSQL(query);
    }

    public void addMainTable(String tableName){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_TABLE_NAME_INDIVIDUAL, tableName);

        db.execSQL(" DROP TABLE IF EXISTS " + tableName);

        String query = " CREATE TABLE " + tableName + " (" + COLUMN_ID_SUB + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_FILE_NAME + " TEXT, " + COLUMN_FILE_PATH + " TEXT, " + COLUMN_FILE_FILE + " TEXT); ";

        db.insert(TABLE_NAME, null, cv);
        db.execSQL(query);
    }

    Cursor getMainData(){
        String query =" SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;

        if(db!=null){
            cursor = db.rawQuery(query, null);
        }

        return cursor;
    }

    public void addToTable(String tableName, String name, String path, File file){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_FILE_NAME, name);
        cv.put(COLUMN_FILE_PATH, path);
        cv.put(COLUMN_FILE_FILE, String.valueOf(file));

        long result = db.insert(tableName, null, cv);

        if(result == 1){
            Toast.makeText(context, "Added to " + tableName, Toast.LENGTH_SHORT).show();
        }
    }

    Cursor getContentData(String tableName){
        String query =" SELECT * FROM " + tableName;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;

        if(db!=null){
            cursor = db.rawQuery(query, null);
        }

        return cursor;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(" DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    void deleteTable(String id, String name){
        SQLiteDatabase db = this.getWritableDatabase();
        //String query = " DROP TABLE IF EXISTS " + table_name;
        db.execSQL(" DELETE FROM " + name);
        db.delete(TABLE_NAME, "_id=?", new String[]{id});
        //db.execSQL(query);
    }

    void delIndi(String id, String name){
        SQLiteDatabase db = this.getWritableDatabase();
        //String query = " DROP TABLE IF EXISTS " + table_name;
        //db.execSQL(" DELETE FROM " + name);
        db.delete(name, "_idsub=?", new String[]{id});
        //db.execSQL(query);
    }
}
