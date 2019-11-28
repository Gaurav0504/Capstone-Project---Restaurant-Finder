package com.example.gaurav.restaurantfinder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.gaurav.restaurantfinder.geofence.GeoFenceHelper;
import com.example.gaurav.restaurantfinder.model.Restaurant;
import com.example.gaurav.restaurantfinder.model.RestaurantInfo;

import java.util.List;

public class BootCompletedIntentReceiver extends BroadcastReceiver {


    private static final String TAG = "BootCompletedIntentRece";

    public static final String BOOT_COMPLETED_ACTION = "android.intent.action.BOOT_COMPLETED";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (BOOT_COMPLETED_ACTION.equals(intent.getAction())) {
            Log.d(TAG, "onReceive: ");
            List<Restaurant> favRestaurants = Utils.getFavRestaurants();
            GeoFenceHelper geoFenceHelper = new GeoFenceHelper();
            for (Restaurant favRestaurant : favRestaurants) {
                RestaurantInfo restaurant = favRestaurant.getRestaurant();
                geoFenceHelper.addGeoFence(restaurant.getId(), Double.valueOf(restaurant.getLocation().getLatitude()), Double.valueOf(restaurant.getLocation().getLongitude()));
            }

        }
    }
}
