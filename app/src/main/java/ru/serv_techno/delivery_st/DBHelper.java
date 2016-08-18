package ru.serv_techno.delivery_st;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Maxim on 18.08.2016.
 */
public class DBHelper extends SQLiteOpenHelper {
    String LOG_TAG = "deliveryST_log";
    public DBHelper(Context context){
        //конструктор суперкласса
        super(context, "deliveryST", null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(LOG_TAG, "onCreate database");

        //создадим таблицу catalogs

        //создадим таблицу products

        //создадим таблицу orders

        //создадим таблицу orderProducts
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
