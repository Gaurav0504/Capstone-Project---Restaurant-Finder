package com.example.gaurav.restaurantfinder.service;

import com.example.gaurav.restaurantfinder.model.LocationQuery;
import com.example.gaurav.restaurantfinder.model.Result;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface RestaurantService {
    @GET("/api/v2.1//search")
    Call<Result> restaurantResult(@Header("user-key") String key, @Query("lat") double lat, @Query("lon") double lon);


    @GET("/api/v2.1//search")
    Call<Result> restaurantResult(@Header("user-key") String key, @Query("entity_id") Integer id, @Query("entity_type") String type);


    @GET("/api/v2.1//locations")
    Call<LocationQuery> locationResult(@Header("user-key") String key, @Query("query") String query);


}
