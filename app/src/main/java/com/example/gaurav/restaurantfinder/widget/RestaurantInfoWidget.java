package com.example.gaurav.restaurantfinder.widget;

import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.example.gaurav.restaurantfinder.App.RestaurantApp;
import com.example.gaurav.restaurantfinder.DetailActivity;
import com.example.gaurav.restaurantfinder.MainActivity;
import com.example.gaurav.restaurantfinder.R;

public class RestaurantInfoWidget extends AppWidgetProvider {
    public static final String POSITION = "POSITION";
    public static final String RESTAURANT_LIST = "RESTAURANT_FIELD";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, new RestaurantInfoWidgetManager());
        }
    }

    void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                         int appWidgetId, RestaurantInfoWidgetManager restaurantInfoWidgetManager) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_restaurant_info);

        // Create an Intent to launch ExampleActivity
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(RESTAURANT_LIST, restaurantInfoWidgetManager.getJson(restaurantInfoWidgetManager.getRestaurants()));

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(RestaurantApp.getAppContext());
        // Adds the back stack
        stackBuilder.addParentStack(MainActivity.class);
        // Adds the Intent to the top of the stack
        stackBuilder.addNextIntent(intent);
        // Gets a PendingIntent containing the entire back stack
        PendingIntent pendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        views.setRemoteAdapter(R.id.widget_list,
                new Intent(context, RecipeInfoWidgetRemoteViewService.class));

        views.setPendingIntentTemplate(R.id.widget_list, pendingIntent);
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }
}
