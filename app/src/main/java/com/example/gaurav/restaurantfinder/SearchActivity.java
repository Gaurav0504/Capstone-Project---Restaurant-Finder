package com.example.gaurav.restaurantfinder;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.gaurav.restaurantfinder.preference.ConfigurationManager;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchActivity extends AppCompatActivity {

    @BindView(R.id.find_my_location)
    Button detectMe;

    @BindView(R.id.search_location)
    Button search;

    @BindView(R.id.place_query)
    EditText location;

    FusedLocationProviderClient mFusedLocationClient;

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 12;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);
        ConfigurationManager instance = ConfigurationManager.getInstance();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        ButterKnife.bind(this);

        detectMe.setOnClickListener(view -> {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                retrieveLocation();
            } else {
                permissionCheck();
            }
        });
        location.setOnEditorActionListener(((textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_DONE) {
                performSearch(instance);
                return true;
            }
            return false;
        }));

        search.setOnClickListener(view -> {
            if(location.getText().toString().equals(getString(R.string.Enter_Your_Location)) || location.getText().toString().trim().isEmpty()){
                Toast.makeText(this, "Please Enter a valid location", Toast.LENGTH_SHORT).show();
            } else {
                performSearch(instance);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        ConfigurationManager instance = ConfigurationManager.getInstance();
        if (!instance.getLocation().isEmpty() || !(instance.getLatitude() == 0.0f && instance.getLongitude() == 0.0f)) {
            startMainActivity();
        }
    }

    private void performSearch(ConfigurationManager instance) {
        String locationString = location.getText().toString();
        if (!locationString.isEmpty()) {
            if(Utils.isOnline()) {
                instance.putLocationQuery(locationString);
                startMainActivity();
            } else {
                Toast.makeText(this, getString(R.string.no_connection), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @SuppressLint("MissingPermission")
    private void retrieveLocation() {
        mFusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
            //save lat, lon to preference
            ConfigurationManager instance = ConfigurationManager.getInstance();
            if(location != null) {
                instance.putLatitude(location.getLatitude());
                instance.putLongitude(location.getLongitude());
                startMainActivity();
            } else {
                Toast.makeText(this, "Unable to detect location", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(this, R.string.unable_to_detect_location, Toast.LENGTH_LONG).show();
        });
    }

    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void permissionCheck() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                Snackbar.make(findViewById(android.R.id.content), R.string.grant_location_permission, Snackbar.LENGTH_LONG).show();

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    retrieveLocation();
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
