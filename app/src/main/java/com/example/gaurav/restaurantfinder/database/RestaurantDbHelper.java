package com.example.gaurav.restaurantfinder.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static android.provider.BaseColumns._ID;
import static com.example.gaurav.restaurantfinder.database.RestaurantContract.RestaurantEntry.RESTAURANT_ADDRESS;
import static com.example.gaurav.restaurantfinder.database.RestaurantContract.RestaurantEntry.RESTAURANT_BACKDROP_URI;
import static com.example.gaurav.restaurantfinder.database.RestaurantContract.RestaurantEntry.RESTAURANT_CITY;
import static com.example.gaurav.restaurantfinder.database.RestaurantContract.RestaurantEntry.RESTAURANT_COST;
import static com.example.gaurav.restaurantfinder.database.RestaurantContract.RestaurantEntry.RESTAURANT_CUISINES;
import static com.example.gaurav.restaurantfinder.database.RestaurantContract.RestaurantEntry.RESTAURANT_CURRENCY;
import static com.example.gaurav.restaurantfinder.database.RestaurantContract.RestaurantEntry.RESTAURANT_ID;
import static com.example.gaurav.restaurantfinder.database.RestaurantContract.RestaurantEntry.RESTAURANT_LAT;
import static com.example.gaurav.restaurantfinder.database.RestaurantContract.RestaurantEntry.RESTAURANT_LON;
import static com.example.gaurav.restaurantfinder.database.RestaurantContract.RestaurantEntry.RESTAURANT_NAME;
import static com.example.gaurav.restaurantfinder.database.RestaurantContract.RestaurantEntry.RESTAURANT_ONLINE;
import static com.example.gaurav.restaurantfinder.database.RestaurantContract.RestaurantEntry.RESTAURANT_RATING;
import static com.example.gaurav.restaurantfinder.database.RestaurantContract.RestaurantEntry.TABLE_NAME;

public class RestaurantDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "restaurants.db";
    public static final int DATABASE_VERSION = 1;

    public RestaurantDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String SQL_CREATE_RESTAURANT_TABLE = "CREATE TABLE " + TABLE_NAME + "(" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                RESTAURANT_ID + " TEXT UNIQUE NOT NULL," +
                RESTAURANT_NAME + " TEXT NOT NULL," +
                RESTAURANT_CUISINES + " TEXT NOT NULL," +
                RESTAURANT_COST + " TEXT NOT NULL," +
                RESTAURANT_ONLINE + " INTEGER NOT NULL," +
                RESTAURANT_RATING + " TEXT NOT NULL," +
                RESTAURANT_ADDRESS + " TEXT NOT NULL," +
                RESTAURANT_BACKDROP_URI + " TEXT NOT NULL," +
                RESTAURANT_CITY + " TEXT NOT NULL," +
                RESTAURANT_CURRENCY + " TEXT NOT NULL," +
                RESTAURANT_LAT + " TEXT NOT NULL," +
                RESTAURANT_LON + " TEXT NOT NULL," +

                "UNIQUE (" + RESTAURANT_ID + ") ON CONFLICT IGNORE" +
                " );";
        sqLiteDatabase.execSQL(SQL_CREATE_RESTAURANT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
