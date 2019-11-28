package com.example.gaurav.restaurantfinder.notification;

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import androidx.core.app.NotificationCompat;

import com.example.gaurav.restaurantfinder.App.RestaurantApp;
import com.example.gaurav.restaurantfinder.DetailActivity;
import com.example.gaurav.restaurantfinder.MainActivity;
import com.example.gaurav.restaurantfinder.R;
import com.example.gaurav.restaurantfinder.Utils;
import com.example.gaurav.restaurantfinder.model.Restaurant;

import static com.example.gaurav.restaurantfinder.MainActivityFragment.RESTAURANT;

public class StatusManager {

    private static final Context context = RestaurantApp.getAppContext();
    public static final String CHANNEL_ID = "com.example.gaurav.restaurantfinder";

    static NotificationManager mNotificationManager =
            (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);


    static {
        if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            createChannel();
        }
    }


    @TargetApi(Build.VERSION_CODES.O)
    public static void createChannel() {

        // The id of the channel.
        String id = CHANNEL_ID;
        // The user-visible name of the channel.
        CharSequence name = context.getString(R.string.channel_name);
        // The user-visible description of the channel.
        String description = context.getString(R.string.channel_description);
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel mChannel = null;

        mChannel = new NotificationChannel(id, name, importance);

        // Configure the notification channel.
        mChannel.setDescription(description);
        mChannel.enableLights(true);
        // Sets the notification light color for notifications posted to this
        // channel, if the device supports this feature.
        mChannel.setLightColor(Color.RED);
        mChannel.enableVibration(true);
        mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
        mNotificationManager.createNotificationChannel(mChannel);
    }

    public static void addNotification(String id) {
        // Create an Intent to launch ExampleActivity
        Intent intent = new Intent(context, DetailActivity.class);
        Restaurant favRestaurant = Utils.getFavRestaurant(id);
        intent.putExtra(RESTAURANT, favRestaurant);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(RestaurantApp.getAppContext());
        // Adds the back stack
        stackBuilder.addParentStack(MainActivity.class);
        // Adds the Intent to the top of the stack
        stackBuilder.addNextIntent(intent);
        // Gets a PendingIntent containing the entire back stack
        PendingIntent pendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context, CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_bookmark)
                        .setContentTitle(context.getString(R.string.fav_restaurant_nearby))
                        .setContentText(context.getString(R.string.checkout, favRestaurant == null ? context.getString(R.string.your_fav) : favRestaurant.getRestaurant().getName()));
        mBuilder.setContentIntent(pendingIntent);
        mNotificationManager.notify(Integer.valueOf(id), mBuilder.build());
    }

    public static void removeNotification(String id) {
        mNotificationManager.cancel(Integer.valueOf(id));
    }
}
