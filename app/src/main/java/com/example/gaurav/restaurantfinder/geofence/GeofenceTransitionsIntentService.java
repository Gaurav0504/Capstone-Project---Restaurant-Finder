package com.example.gaurav.restaurantfinder.geofence;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.example.gaurav.restaurantfinder.R;
import com.example.gaurav.restaurantfinder.notification.StatusManager;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.location.GeofencingEvent;

import java.util.List;

public class GeofenceTransitionsIntentService extends IntentService {
    private static final String TAG = "GeofenceTransitionsInte";


    public GeofenceTransitionsIntentService() {
        super("GeoFence");
    }

    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "onHandleIntent: got geofence trigger");
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) {
            String errorMessage = GeofenceStatusCodes.getStatusCodeString(
                    geofencingEvent.getErrorCode());
            Log.e(TAG, errorMessage);
            return;
        }
        List<Geofence> triggeringGeofences = geofencingEvent.getTriggeringGeofences();
        // Get the transition type.
        int geofenceTransition = geofencingEvent.getGeofenceTransition();

        // Test that the reported transition was of interest.
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {
            for (Geofence triggeringGeofence : triggeringGeofences) {
                StatusManager.addNotification(triggeringGeofence.getRequestId());
            }

        } else if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {
            for (Geofence triggeringGeofence : triggeringGeofences) {
                StatusManager.removeNotification(triggeringGeofence.getRequestId());
            }


        } else {
            // Log the error.
            Log.e(TAG, getString(R.string.geofence_transition_invalid_type,
                    geofenceTransition));
        }
    }

}