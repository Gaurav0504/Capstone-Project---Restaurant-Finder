package com.example.gaurav.restaurantfinder.database;

import android.net.Uri;
import android.provider.BaseColumns;

public class RestaurantContract {

    /* Add content provider constants to the Contract
     Clients need to know how to access the task data, and it's your job to provide
     these content URI's for the path to that data:
        1) Content authority,
        2) Base content URI,
        3) Path(s) to the tasks directory
        4) Content URI for data in the TaskEntry class
      */

    // The authority, which is how your code knows which Content Provider to access
    public static final String AUTHORITY = "com.example.gaurav.restaurantfinder";

    // The base content URI = "content://" + <authority>
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    // Define the possible paths for accessing data in this contract
    // This is the path for the "tasks" directory
    public static final String PATH_RESTAURANTS = "restaurants";


    public static final class RestaurantEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_RESTAURANTS).build();

        public static final String TABLE_NAME = "restaurants";
        public static final String RESTAURANT_ID = "id";
        public static final String RESTAURANT_BACKDROP_URI = "backdrop_path";
        public static final String RESTAURANT_NAME = "name";
        public static final String RESTAURANT_CUISINES = "cuisines";
        public static final String RESTAURANT_COST = "averageCostForTwo";
        public static final String RESTAURANT_CURRENCY = "currency";
        public static final String RESTAURANT_RATING = "rating";
        public static final String RESTAURANT_ONLINE = "online";
        public static final String RESTAURANT_ADDRESS = "address";
        public static final String RESTAURANT_CITY = "city";
        public static final String RESTAURANT_LAT = "latitude";
        public static final String RESTAURANT_LON = "longitude";


    }
}
