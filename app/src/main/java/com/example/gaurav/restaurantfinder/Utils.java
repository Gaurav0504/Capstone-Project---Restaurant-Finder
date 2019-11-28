package com.example.gaurav.restaurantfinder;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.example.gaurav.restaurantfinder.App.RestaurantApp;
import com.example.gaurav.restaurantfinder.database.RestaurantContract;
import com.example.gaurav.restaurantfinder.model.Location;
import com.example.gaurav.restaurantfinder.model.Restaurant;
import com.example.gaurav.restaurantfinder.model.RestaurantInfo;
import com.example.gaurav.restaurantfinder.model.UserRating;

import java.util.ArrayList;
import java.util.List;

public class Utils {

    private static final String TAG = "Utils";
    private static final String ZOMATO_KEY = "zomato-key";

    public static String getRestaurantApiKey() {
        try {
            Context appContext = RestaurantApp.getAppContext();
            ApplicationInfo ai = appContext.getPackageManager().getApplicationInfo(appContext.getApplicationContext().getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            return bundle.getString(ZOMATO_KEY);
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "Failed to load meta-data, NameNotFound: " + e.getMessage());
        } catch (NullPointerException e) {
            Log.e(TAG, "Failed to load meta-data, NullPointer: " + e.getMessage());
        }
        return null;
    }

    public static boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) RestaurantApp.getAppContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }

    public static boolean isFavorite(Context context, RestaurantInfo restaurantInfo) {
        Uri uri = ContentUris.withAppendedId(RestaurantContract.RestaurantEntry.CONTENT_URI, Long.valueOf(restaurantInfo.getId()));
        ContentResolver cr = context.getContentResolver();
        Cursor cursor = null;
        try {
            cursor = cr.query(uri, null, null, null, null);
            if (cursor != null && cursor.moveToFirst())
                return true;
        } finally {
            if (cursor != null) cursor.close();
        }
        return false;
    }

    public static void addToFavorites(Context context, RestaurantInfo restaurantInfo) {
        Uri uri = RestaurantContract.RestaurantEntry.CONTENT_URI;
        ContentResolver resolver = context.getContentResolver();
        ContentValues values = new ContentValues();
        values.put(RestaurantContract.RestaurantEntry.RESTAURANT_ID, restaurantInfo.getId());
        values.put(RestaurantContract.RestaurantEntry.RESTAURANT_BACKDROP_URI, restaurantInfo.getFeaturedImage());
        values.put(RestaurantContract.RestaurantEntry.RESTAURANT_NAME, restaurantInfo.getName());
        values.put(RestaurantContract.RestaurantEntry.RESTAURANT_ADDRESS, restaurantInfo.getLocation().getAddress());
        values.put(RestaurantContract.RestaurantEntry.RESTAURANT_CITY, restaurantInfo.getLocation().getCity());
        values.put(RestaurantContract.RestaurantEntry.RESTAURANT_COST, restaurantInfo.getAverageCostForTwo());
        values.put(RestaurantContract.RestaurantEntry.RESTAURANT_CURRENCY, restaurantInfo.getCurrency());
        values.put(RestaurantContract.RestaurantEntry.RESTAURANT_CUISINES, restaurantInfo.getCuisines());
        values.put(RestaurantContract.RestaurantEntry.RESTAURANT_LAT, restaurantInfo.getLocation().getLatitude());
        values.put(RestaurantContract.RestaurantEntry.RESTAURANT_LON, restaurantInfo.getLocation().getLongitude());
        values.put(RestaurantContract.RestaurantEntry.RESTAURANT_ONLINE, restaurantInfo.getHasOnlineDelivery());
        values.put(RestaurantContract.RestaurantEntry.RESTAURANT_RATING, restaurantInfo.getUserRating().getAggregateRating());
        resolver.insert(uri, values);
    }

    public static void removeFromFavorites(Context context, String restaurantId) {
        Uri uri = RestaurantContract.RestaurantEntry.CONTENT_URI;
        ContentResolver resolver = context.getContentResolver();

        resolver.delete(uri, RestaurantContract.RestaurantEntry.RESTAURANT_ID + " = ? ",
                new String[]{restaurantId + ""});
    }

    public static boolean hasFavourite(Context context) {
        ContentResolver cr = context.getContentResolver();
        Cursor cursor = null;
        try {
            cursor = cr.query(RestaurantContract.RestaurantEntry.CONTENT_URI, null, null, null, null);
            if (cursor != null && cursor.moveToFirst())
                return true;
        } finally {
            if (cursor != null) cursor.close();
        }
        return false;
    }

    public static Restaurant getFavRestaurant(String id) {
        RestaurantInfo restaurantInfo = null;
        ContentResolver cr = RestaurantApp.getAppContext().getContentResolver();
        Uri uri = ContentUris.withAppendedId(RestaurantContract.RestaurantEntry.CONTENT_URI, Long.parseLong(id));
        try (Cursor data = cr.query(uri, null, null, null, null)) {
            if (data != null && data.moveToFirst()) {
                String restaurantId = data.getString(data.getColumnIndex(RestaurantContract.RestaurantEntry.RESTAURANT_ID));
                String restaurantName = data.getString(data.getColumnIndex(RestaurantContract.RestaurantEntry.RESTAURANT_NAME));
                String restaurantBackDrop = data.getString(data.getColumnIndex(RestaurantContract.RestaurantEntry.RESTAURANT_BACKDROP_URI));
                String cuisines = data.getString(data.getColumnIndex(RestaurantContract.RestaurantEntry.RESTAURANT_CUISINES));
                Integer cost = data.getInt(data.getColumnIndex(RestaurantContract.RestaurantEntry.RESTAURANT_COST));
                String currency = data.getString(data.getColumnIndex(RestaurantContract.RestaurantEntry.RESTAURANT_CURRENCY));
                String rating = data.getString(data.getColumnIndex(RestaurantContract.RestaurantEntry.RESTAURANT_RATING));
                Integer online = data.getInt(data.getColumnIndex(RestaurantContract.RestaurantEntry.RESTAURANT_ONLINE));
                String address = data.getString(data.getColumnIndex(RestaurantContract.RestaurantEntry.RESTAURANT_ADDRESS));
                String city = data.getString(data.getColumnIndex(RestaurantContract.RestaurantEntry.RESTAURANT_CITY));
                String lat = data.getString(data.getColumnIndex(RestaurantContract.RestaurantEntry.RESTAURANT_LAT));
                String lon = data.getString(data.getColumnIndex(RestaurantContract.RestaurantEntry.RESTAURANT_LON));
                restaurantInfo = new RestaurantInfo(
                        restaurantId, restaurantName, new Location(address, city, lat, lon)
                        , cuisines, cost, currency, online, restaurantBackDrop, new UserRating(rating));


            }
        }
        Restaurant rest = new Restaurant();
        rest.setRestaurant(restaurantInfo);
        return rest;
    }

    public static List<Restaurant> getFavRestaurants() {
        List<Restaurant> restaurantList = new ArrayList<>();
        List<RestaurantInfo> restaurantInfos = new ArrayList<>();
        ContentResolver cr = RestaurantApp.getAppContext().getContentResolver();
        try (Cursor data = cr.query(RestaurantContract.RestaurantEntry.CONTENT_URI, null, null, null, null)) {
            if (data != null && data.moveToFirst()) {
                do {
                    String restaurantId = data.getString(data.getColumnIndex(RestaurantContract.RestaurantEntry.RESTAURANT_ID));
                    String restaurantName = data.getString(data.getColumnIndex(RestaurantContract.RestaurantEntry.RESTAURANT_NAME));
                    String restaurantBackDrop = data.getString(data.getColumnIndex(RestaurantContract.RestaurantEntry.RESTAURANT_BACKDROP_URI));
                    String cuisines = data.getString(data.getColumnIndex(RestaurantContract.RestaurantEntry.RESTAURANT_CUISINES));
                    Integer cost = data.getInt(data.getColumnIndex(RestaurantContract.RestaurantEntry.RESTAURANT_COST));
                    String currency = data.getString(data.getColumnIndex(RestaurantContract.RestaurantEntry.RESTAURANT_CURRENCY));
                    String rating = data.getString(data.getColumnIndex(RestaurantContract.RestaurantEntry.RESTAURANT_RATING));
                    Integer online = data.getInt(data.getColumnIndex(RestaurantContract.RestaurantEntry.RESTAURANT_ONLINE));
                    String address = data.getString(data.getColumnIndex(RestaurantContract.RestaurantEntry.RESTAURANT_ADDRESS));
                    String city = data.getString(data.getColumnIndex(RestaurantContract.RestaurantEntry.RESTAURANT_CITY));
                    String lat = data.getString(data.getColumnIndex(RestaurantContract.RestaurantEntry.RESTAURANT_LAT));
                    String lon = data.getString(data.getColumnIndex(RestaurantContract.RestaurantEntry.RESTAURANT_LON));
                    RestaurantInfo restaurantInfo = new RestaurantInfo(
                            restaurantId, restaurantName, new Location(address, city, lat, lon)
                            , cuisines, cost, currency, online, restaurantBackDrop, new UserRating(rating));

                    restaurantInfos.add(restaurantInfo);
                } while (data.moveToNext());
                for (RestaurantInfo restaurant : restaurantInfos) {
                    Restaurant rest = new Restaurant();
                    rest.setRestaurant(restaurant);
                    restaurantList.add(rest);
                }
            }
        }
        return restaurantList;
    }
}
