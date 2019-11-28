package com.example.gaurav.restaurantfinder.widget;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.gaurav.restaurantfinder.App.RestaurantApp;
import com.example.gaurav.restaurantfinder.R;
import com.example.gaurav.restaurantfinder.model.Restaurant;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class RestaurantInfoWidgetManager {


    private static final String TAG = "RestaurantInfoWidgetManager";

    private static final String PREFERENCES_NAME = "RecipeInfoWidget";
    private static final String KEY_RECIPE = "Recipe";

    private Gson gson = new Gson();

    Type listType = new TypeToken<List<Restaurant>>() {
    }.getType();

    private SharedPreferences sharedPreferences = RestaurantApp.getAppContext().getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);



    public void updateWidgetRecipe(List<Restaurant> restaurants) {

        String recipeJson = gson.toJson(restaurants, listType);

        sharedPreferences.edit().
                putString(KEY_RECIPE, recipeJson).
                commit();
        updateWidget();
    }

    private void updateWidget() {
        Intent intent = new Intent(RestaurantApp.getAppContext(), RestaurantInfoWidget.class);

        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(RestaurantApp.getAppContext());
        ComponentName componentName = new ComponentName(RestaurantApp.getAppContext(), RestaurantInfoWidget.class);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(componentName);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);

        RestaurantApp.getAppContext().sendBroadcast(intent);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_list);
    }

    public List<Restaurant> getRestaurants() {
        String restaurantList = sharedPreferences.getString(KEY_RECIPE, null);
        if (restaurantList != null) {
            return gson.fromJson(restaurantList, listType);
        }
        return null;
    }

    public String getJson(List<Restaurant> restaurants){
        return gson.toJson(restaurants, listType);
    }

    public Restaurant getInfo(String json,int pos){
        List<Restaurant> list = gson.fromJson(json, listType);
        return list.get(pos);
    }

}