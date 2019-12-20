package com.ozturktolunay.cultour.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.ozturktolunay.cultour.R;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class FindLocationActivity extends AppCompatActivity {

    //region Resource declaration
    TextInputEditText edit_place_search;

    MaterialButton button_find_my_location;
    //endregion

    final int AUTOCOMPLETE_REQUEST_CODE = 1;
    final int PERMISSION_REQUEST_LOCATION = 2;

    private String placesApiKey;
    private Context applicationContext;
    private final String TAG = "FindLocationActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_location);

        applicationContext = getApplicationContext();
        try {
            ApplicationInfo applicationInfo = applicationContext.getPackageManager()
                    .getApplicationInfo(applicationContext.getPackageName(),
                            PackageManager.GET_META_DATA);

            if (applicationInfo.metaData != null) {
                placesApiKey = applicationInfo.metaData.getString("PLACES_API_KEY");
            } else {
                Log.e(TAG, "Couldn't retrieve PLACES_API_KEY.");
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, e.getMessage(), e);
        }

        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, placesApiKey);
        }

        //region Resource assignment
        edit_place_search = findViewById(R.id.edit_FindLocation_place_search);

        button_find_my_location = findViewById(R.id.button_FindLocation_find_my_location);
        //endregion

        //region OnClick events
        edit_place_search.setOnClickListener(edit_place_search_listener);
        button_find_my_location.setOnClickListener(button_find_my_location_listener);
        //endregion
    }

    //region OnClick listeners
    private View.OnClickListener edit_place_search_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.d(TAG, "edit_place_search clicked!");
            /* Set the fields to specify which types of place data to
            *  return after the user has made a selection. */
            List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.LAT_LNG,
                    Place.Field.NAME);

            // Start the autocomplete intent.
            Intent intent = new Autocomplete.IntentBuilder(
                    AutocompleteActivityMode.FULLSCREEN, fields)
                    .setTypeFilter(TypeFilter.CITIES)
                    .build(applicationContext);
            startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
        }
    };

    private View.OnClickListener button_find_my_location_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.d(TAG, "button_find_my_location clicked!");
            if (checkPermissions()) {
                if (isLocationEnabled()) {
                    // TODO: Get LatLng of the last known location
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Please enable Location Services to use this feature",
                            Toast.LENGTH_LONG).show();
                }
            } else {
                requestPermissions();
            }
        }
    };
    //endregion


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = null;

                if (data != null) {
                    place = Autocomplete.getPlaceFromIntent(data);
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Something went wrong.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent(FindLocationActivity.this,
                        BottomNavigationActivity.class);

                if (place.getLatLng() != null) {
                    intent.putExtra("latitude", place.getLatLng().latitude);
                    intent.putExtra("longitude", place.getLatLng().longitude);
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Something went wrong.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                startActivity(intent);

            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    //region Location permission helper functions
    private void requestPermissions() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_REQUEST_LOCATION
        );
    }

    private boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (locationManager != null) {
            return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                    LocationManager.NETWORK_PROVIDER
            );
        }
        return false;
    }
    //endregion
}
