package com.example.gaurav.restaurantfinder.widget;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.gaurav.restaurantfinder.App.RestaurantApp;
import com.example.gaurav.restaurantfinder.R;
import com.example.gaurav.restaurantfinder.model.Restaurant;

import java.util.List;

import static com.example.gaurav.restaurantfinder.widget.RestaurantInfoWidget.POSITION;

public class RecipeInfoWidgetRemoteViewService extends RemoteViewsService {


    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new MyRemoteViewFactory();
    }
}

class MyRemoteViewFactory implements RemoteViewsService.RemoteViewsFactory {

    private RestaurantInfoWidgetManager restaurantInfoWidgetManager = new RestaurantInfoWidgetManager();

    private List<Restaurant> restaurants;

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        restaurants = restaurantInfoWidgetManager.getRestaurants();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if (restaurants == null || restaurants.isEmpty()) {
            return 0;
        }
        return restaurants.size();
    }

    @Override
    public RemoteViews getViewAt(int i) {
        RemoteViews views = new RemoteViews(RestaurantApp.getAppContext().getPackageName(), R.layout.cell_wigdet_restaurant);

        Restaurant restaurant = restaurants.get(i);

        views.setTextViewText(R.id.restaurant_name, restaurant.getRestaurant().getName());

        Bundle extras = new Bundle();
        extras.putInt(POSITION, i);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);
        // Make it possible to distinguish the individual on-click
        // action of a given item
        views.setOnClickFillInIntent(R.id.restaurant_name, fillInIntent);
        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return (long) i;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
