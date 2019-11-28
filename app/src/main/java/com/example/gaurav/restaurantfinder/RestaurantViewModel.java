package com.example.gaurav.restaurantfinder;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.example.gaurav.restaurantfinder.WorkManager.MyWorkScheduler;
import com.example.gaurav.restaurantfinder.model.LocationQuery;
import com.example.gaurav.restaurantfinder.model.Restaurant;
import com.example.gaurav.restaurantfinder.model.Result;
import com.example.gaurav.restaurantfinder.preference.ConfigurationManager;

import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantViewModel extends AndroidViewModel {

    private static final String TAG = "RestaurantViewModel";


    private Context context;


    public MutableLiveData<List<Restaurant>> restaurants;

    LiveData<WorkInfo> workInfo;

    public RestaurantViewModel(@NonNull Application application) {
        super(application);
        context = application;
        initWorkManager();
    }

    private void initWorkManager() {
        Log.d(TAG, "Inside Work Manager");
        PeriodicWorkRequest.Builder builder = new PeriodicWorkRequest.Builder(MyWorkScheduler.class, 5, TimeUnit.MINUTES);
        builder.setConstraints(new Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build());
        PeriodicWorkRequest work = builder.build();
        WorkManager.getInstance().enqueueUniquePeriodicWork("p_work", ExistingPeriodicWorkPolicy.REPLACE, work);
        workInfo = WorkManager.getInstance().getWorkInfoByIdLiveData(work.getId());
    }



    public LiveData<List<Restaurant>> getRestaurants() {
        if (restaurants == null) {
            restaurants = new MutableLiveData<>();
            loadRestaurants(); //async
        }
        return restaurants;
    }


    public void loadRestaurants() {

        ConfigurationManager instance = ConfigurationManager.getInstance();
        NetworkOperations networkOperations = new NetworkOperations();
        if (!instance.getLocation().isEmpty()) {
            Call<LocationQuery> locationResult = networkOperations.getLocationResult();
            locationResult.enqueue(new Callback<LocationQuery>() {
                @Override
                public void onResponse(Call<LocationQuery> call, Response<LocationQuery> response) {
                    LocationQuery body = response.body();
                    if (body == null) {
                        Log.e(TAG, "onResponse: null");
                        return;
                    }
                    Integer entityId = body.getLocationSuggestions().get(0).getEntityId();
                    String entityType = body.getLocationSuggestions().get(0).getEntityType();
                    instance.putEntity(entityId);
                    instance.putEntityType(entityType);
                    new AsyncTaskWrapper().doTask(() -> {
                        getRestaurantsFromLocation();
                    });
                }

                @Override
                public void onFailure(Call<LocationQuery> call, Throwable t) {
                    if (!call.isCanceled()) {
                        Toast.makeText(context, R.string.result_retrieve_error,
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {

            new AsyncTaskWrapper<>().doTask(() -> {
                getRestaurantList(networkOperations);
            });

        }

    }

    private void getRestaurantList(NetworkOperations networkOperations) {
        Call<Result> resultCall = networkOperations.getRestaurantResult();
        resultCall.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(@NonNull Call<Result> call, @NonNull Response<Result> response) {
                Result body = response.body();
                if (body == null) {
                    Log.e(TAG, "onResponse: null");
                    return;
                }
                restaurants.postValue(body.getRestaurants());
            }

            @Override
            public void onFailure(@NonNull Call<Result> call, @NonNull Throwable t) {
                if (!call.isCanceled()) {
                    Toast.makeText(context, R.string.result_retrieve_error,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getRestaurantsFromLocation() {
        Call<Result> resultCall = new NetworkOperations().getRestaurantResultFromLocation();
        resultCall.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(@NonNull Call<Result> call, @NonNull Response<Result> response) {
                Result body = response.body();
                if (body == null) {
                    Log.e(TAG, "onResponse: null");
                    return;
                }
                restaurants.postValue(body.getRestaurants());
            }

            @Override
            public void onFailure(@NonNull Call<Result> call, @NonNull Throwable t) {
                if (!call.isCanceled()) {
                    Toast.makeText(context, R.string.result_retrieve_error,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
