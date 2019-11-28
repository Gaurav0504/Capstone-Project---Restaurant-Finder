package com.example.gaurav.restaurantfinder;

import com.example.gaurav.restaurantfinder.model.LocationQuery;
import com.example.gaurav.restaurantfinder.model.Result;
import com.example.gaurav.restaurantfinder.preference.ConfigurationManager;
import com.example.gaurav.restaurantfinder.service.RestaurantService;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

class NetworkOperations {


    private static final String BASE_URL = "https://developers.zomato.com/api/v2.1/";


    private ConfigurationManager instance = ConfigurationManager.getInstance();

    Retrofit retrofit;
    RestaurantService service;

    public NetworkOperations() {
        retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient.Builder().build())
                .build();
        service = retrofit.create(RestaurantService.class);
    }


    public Call<Result> getRestaurantResult() {


        return service.restaurantResult(Utils.getRestaurantApiKey(), instance.getLatitude(), instance.getLongitude());
    }

    public Call<LocationQuery> getLocationResult() {
        return service.locationResult(Utils.getRestaurantApiKey(), instance.getLocation());
    }

    public Call<Result> getRestaurantResultFromLocation() {
        return service.restaurantResult(Utils.getRestaurantApiKey(), instance.getEntityId(), instance.getEntityType());
    }
}
