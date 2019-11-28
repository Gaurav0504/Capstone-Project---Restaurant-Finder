package com.example.gaurav.restaurantfinder.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class RestaurantProvider extends ContentProvider {
    private RestaurantDbHelper restaurantDbHelper;
    private static UriMatcher sUriMatcher = buildUriMatcher();


    private static final int RESTAURANTS = 100;
    private static final int RESTAURANT_ID = 101;

    public RestaurantProvider() {
    }

    @Override
    public boolean onCreate() {
        restaurantDbHelper = new RestaurantDbHelper(getContext());
        return true;
    }


    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = RestaurantContract.AUTHORITY;
        matcher.addURI(authority, RestaurantContract.PATH_RESTAURANTS, RESTAURANTS);
        matcher.addURI(authority, RestaurantContract.PATH_RESTAURANTS + "/#", RESTAURANT_ID);

        return matcher;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor;
        switch (sUriMatcher.match(uri)) {
            case RESTAURANTS:
                cursor = getRestaurants(uri, projection, selection, selectionArgs, sortOrder);
                break;
            case RESTAURANT_ID:
                cursor = getRestaurantsWithID(uri, projection, selection, selectionArgs, sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri for query: " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    private Cursor getRestaurantsWithID(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor restaurantCursor;
        SQLiteDatabase database = restaurantDbHelper.getReadableDatabase();
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(RestaurantContract.RestaurantEntry.TABLE_NAME);
        builder.appendWhere(RestaurantContract.RestaurantEntry.RESTAURANT_ID + "=" + uri.getLastPathSegment());
        restaurantCursor = builder.query(database,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);
        return restaurantCursor;
    }

    private Cursor getRestaurants(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor restaurantCursor;
        SQLiteDatabase database = restaurantDbHelper.getReadableDatabase();
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(RestaurantContract.RestaurantEntry.TABLE_NAME);
        restaurantCursor = builder.query(database,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);
        return restaurantCursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues contentValues) {
        final SQLiteDatabase db = restaurantDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;
        switch (match) {
            case RESTAURANTS: {
                long id = db.insertOrThrow(RestaurantContract.RestaurantEntry.TABLE_NAME, null, contentValues);
                if (id > 0)
                    returnUri = ContentUris.withAppendedId(RestaurantContract.RestaurantEntry.CONTENT_URI, id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = restaurantDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted = 0;
        if (selection == null)
            selection = "1";// This makes delete all rows return the number of rows deleted

        switch (match) {
            case RESTAURANTS:
                rowsDeleted = db.delete(RestaurantContract.RestaurantEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = restaurantDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated = 0;

        switch (match) {
            case RESTAURANTS:
                rowsUpdated = db.update(RestaurantContract.RestaurantEntry.TABLE_NAME, contentValues, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }
}
