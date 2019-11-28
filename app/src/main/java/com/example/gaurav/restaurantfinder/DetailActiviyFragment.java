package com.example.gaurav.restaurantfinder;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gaurav.restaurantfinder.model.Location;
import com.example.gaurav.restaurantfinder.model.RestaurantInfo;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActiviyFragment extends AbstractRestaurantFragment {


    @BindView(R.id.cuisines)
    TextView cuisines;

    @BindView(R.id.cost)
    TextView cost;

    @BindView(R.id.ratings)
    TextView ratings;

    @BindView(R.id.online)
    TextView online;

    @BindView(R.id.address)
    TextView address;

    @BindView(R.id.open_in_map)
    TextView map;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.restaurant_detail_fragment, container, false);
        ButterKnife.bind(this, view);
        setRetainInstance(true);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getRestaurantFromBundle();
        loadContent();
    }

    private void loadContent() {
        RestaurantInfo restaurantInfo = restaurant.getRestaurant();
        cuisines.setText(restaurantInfo.getCuisines());
        cost.setText(String.format(getResources().getString(R.string.space), restaurantInfo.getCurrency(), restaurantInfo.getAverageCostForTwo()));
        ratings.setText(String.format(getString(R.string.concat), restaurantInfo.getUserRating().getAggregateRating(), "/5"));
        int visible = restaurantInfo.getHasOnlineDelivery() == 1 ? View.VISIBLE : View.GONE;
        online.setVisibility(visible);
        final Location location = restaurantInfo.getLocation();
        address.setText(location.getAddress());
        map.setOnClickListener(view -> {
            openMaps(location);
        });
    }

    private void openMaps(Location location) {
        try {
            String uri = String.format(Locale.ENGLISH, "geo:%f,%f", Float.parseFloat(location.getLatitude()), Float.parseFloat(location.getLongitude()));
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            getActivity().startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getActivity(), R.string.install_map, Toast.LENGTH_LONG).show();
        }

    }
}
