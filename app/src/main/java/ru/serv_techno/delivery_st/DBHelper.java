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
        db.execSQL("create table catalogs("
                        + "id integer primary key autoincrement,"
                        + "active integer,"
                        + "name text,"
                        + "parent_id integer,"
                        + "countProducts integer,"
                        + ");");

        //создадим таблицу products
        db.execSQL("create table products("
                + "id integer primary key autoincrement,"
                + "active integer,"
                + "name text,"
                + "description text,"
                + "weight integer,"
                + "catalog_1 integer,"
                + "catalog_2 integer,"
                + "main_view integer,"
                + "imageLink text,"
                + ");");

        //создадим таблицу orders
        db.execSQL("create table products("
                + "id integer primary key autoincrement,"
                + "price real,"
                + "created_at integer,"
                + "number_person integer,"
                + "delivery integer,"
                + "orderClientName text,"
                + "orderClientPhone text,"
                + "orderClientAddress text,"
                + ");");

        //создадим таблицу orderProducts
        db.execSQL("create table products("
                + "id integer primary key autoincrement,"
                + "product_id integer,"
                + "amount real,"
                + "order_id integer,"
                + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
