package com.example.gaurav.restaurantfinder;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.WorkInfo;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.example.gaurav.restaurantfinder.adapter.RestaurantsAdapter;
import com.example.gaurav.restaurantfinder.database.RestaurantContract;
import com.example.gaurav.restaurantfinder.model.Location;
import com.example.gaurav.restaurantfinder.model.Restaurant;
import com.example.gaurav.restaurantfinder.model.RestaurantInfo;
import com.example.gaurav.restaurantfinder.model.UserRating;
import com.example.gaurav.restaurantfinder.preference.ConfigurationManager;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.widget.RelativeLayout.TRUE;
import static com.example.gaurav.restaurantfinder.Utils.isOnline;

public class MainActivityFragment extends Fragment implements RestaurantsAdapter.ClickListener, LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = "MainActivityFragment";
    public static final String RESTAURANT = "RESTAURANT";
    private static final int LOADER_ID = 123;
    private static final String SHOWING_FAV = "SHOWING_FAV";
    public static final String PAID = "paid";
    @BindView(R.id.recycler_view_main)
    RecyclerView recyclerView;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.results_not_found)
    LinearLayout noResult;

    @BindView(R.id.main_container)
    RelativeLayout mainContainer;
    RestaurantViewModel viewModel;
    RestaurantsAdapter adapter;
    private boolean favDisplaying;
    private List<RestaurantInfo> restaurants = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setAddView();
        getContext().registerReceiver(connectivityChangeReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        int gridColumn = getResources().getInteger(R.integer.grid_column_number);
        viewModel = ViewModelProviders.of(getActivity()).get(RestaurantViewModel.class);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), gridColumn));

        adapter = new RestaurantsAdapter(viewModel.restaurants, this);
        recyclerView.setAdapter(adapter);
        if (savedInstanceState != null && savedInstanceState.getBoolean(SHOWING_FAV)) {
            favDisplaying = true;
            fetchFavRestaurants();
            return;
        }
        if (!isOnline()) {
            Snackbar.make(getView(), R.string.no_connection, Snackbar.LENGTH_LONG).show();
            return;
        }
        observe();
    }

    private void setAddView() {
        if (BuildConfig.FLAVOR.equals(PAID)) {
            return;
        }
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.ad_layout, null);
        AdView adView = view.findViewById(R.id.adView);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, TRUE);
        mainContainer.addView(view, params);
        // Create an ad request. Check logcat output for the hashed device ID to
        // get test ads on a physical device. e.g.
        // "Use AdRequest.Builder.addTestDevice("ABCDEF012345") to get test ads on this device."
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        adView.loadAd(adRequest);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_fav:
                favDisplaying = true;
                fetchFavRestaurants();
                return true;
            case R.id.menu_all:
                favDisplaying = false;
                observe();
                return true;
            case R.id.menu_search:
                startSearchActivity();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    private void startSearchActivity() {
        ConfigurationManager.getInstance().clear();
        Intent intent = new Intent(getContext(), SearchActivity.class);
        getContext().startActivity(intent);
        getActivity().finish();
    }

    private void fetchFavRestaurants() {
        getActivity().getSupportLoaderManager().restartLoader(LOADER_ID, null, this);
    }

    private void observe() {
        if (favDisplaying) {
            fetchFavRestaurants();
            return;
        }
        viewModel.getRestaurants().observe(this, result -> {
            if (result == null || result.isEmpty()) {
                noResult.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                return;
            }
            onlineViewChanges();
            adapter.updateRestaurants(result);

        });

        viewModel.workInfo.observe(this, workInfo -> {
            if(workInfo!=null && workInfo.getState() == WorkInfo.State.SUCCEEDED){
                viewModel.loadRestaurants();
            }
        });
    }

    private BroadcastReceiver connectivityChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (isOnline()) {
                observe();
            } else if (viewModel == null || viewModel.restaurants == null) {
                offlineViewChanges();
            }
        }
    };

    private void offlineViewChanges() {
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        noResult.setVisibility(View.VISIBLE);
    }

    private void onlineViewChanges() {
        progressBar.setVisibility(View.VISIBLE);
        if (viewModel != null && viewModel.restaurants != null) {
            progressBar.setVisibility(View.GONE);
            noResult.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            getActivity().unregisterReceiver(connectivityChangeReceiver);
        } catch (Exception e) {
            Log.i(TAG, "Exception occurred during un-registering network receiver");
        }
    }

    @Override
    public void accept(int position, ImageView imageView) {
        List<Restaurant> value = new ArrayList<>();
        if (favDisplaying) {
            getRestaurantList(value);
        } else {
            value = viewModel.getRestaurants().getValue();
        }
        if (value == null) {
            Log.i(TAG, "onRecipeClicked: No recipe");
            return;
        }
        Restaurant restaurant = value.get(position);
        Intent intent = new Intent(getContext(), DetailActivity.class);
        intent.putExtra(RESTAURANT, restaurant);
        startActivity(intent);

        Log.d(TAG, "onRecipeClicked() called with: recipe = [" + position + "]");
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(
                getActivity(),
                RestaurantContract.RestaurantEntry.CONTENT_URI,
                null,
                null,
                null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        List<Restaurant> restaurantList = new ArrayList<>();
        restaurants.clear();
        if (!favDisplaying) {
            return;
        }
        if (data != null && data.moveToFirst()) {
            do {
                String restaurantId = data.getString(data.getColumnIndex(RestaurantContract.RestaurantEntry.RESTAURANT_ID));
                String restaurantName = data.getString(data.getColumnIndex(RestaurantContract.RestaurantEntry.RESTAURANT_NAME));
                String restaurantBackDrop = data.getString(data.getColumnIndex(RestaurantContract.RestaurantEntry.RESTAURANT_BACKDROP_URI));
                String cuisines = data.getString(data.getColumnIndex(RestaurantContract.RestaurantEntry.RESTAURANT_CUISINES));
                Integer cost = data.getInt(data.getColumnIndex(RestaurantContract.RestaurantEntry.RESTAURANT_COST));
                String currency = data.getString(data.getColumnIndex(RestaurantContract.RestaurantEntry.RESTAURANT_CURRENCY));
                String rating = data.getString(data.getColumnIndex(RestaurantContract.RestaurantEntry.RESTAURANT_RATING));
                Integer online = data.getInt(data.getColumnIndex(RestaurantContract.RestaurantEntry.RESTAURANT_ONLINE));
                String address = data.getString(data.getColumnIndex(RestaurantContract.RestaurantEntry.RESTAURANT_ADDRESS));
                String city = data.getString(data.getColumnIndex(RestaurantContract.RestaurantEntry.RESTAURANT_CITY));
                String lat = data.getString(data.getColumnIndex(RestaurantContract.RestaurantEntry.RESTAURANT_LAT));
                String lon = data.getString(data.getColumnIndex(RestaurantContract.RestaurantEntry.RESTAURANT_LON));
                RestaurantInfo restaurantInfo = new RestaurantInfo(
                        restaurantId, restaurantName, new Location(address, city, lat, lon)
                        , cuisines, cost, currency, online, restaurantBackDrop, new UserRating(rating));

                restaurants.add(restaurantInfo);
            } while (data.moveToNext());
            getRestaurantList(restaurantList);
            adapter.updateRestaurants(restaurantList);
        }
        if (data == null || !data.moveToFirst()) {
            adapter.updateRestaurants(restaurantList);
            noResult.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }

    private void getRestaurantList(List<Restaurant> restaurantList) {
        for (RestaurantInfo restaurant : restaurants) {
            Restaurant rest = new Restaurant();
            rest.setRestaurant(restaurant);
            restaurantList.add(rest);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SHOWING_FAV, favDisplaying);
    }
}
