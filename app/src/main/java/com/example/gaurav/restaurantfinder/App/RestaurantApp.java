package com.example.gaurav.restaurantfinder.App;

import android.app.Application;
import android.content.Context;

public class RestaurantApp extends Application {

    private static RestaurantApp context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = (RestaurantApp) getApplicationContext();
    }

    public static Context getAppContext() {
        return context;
    }
}
