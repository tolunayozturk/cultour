package com.ozturktolunay.cultour.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Tasks;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.ozturktolunay.cultour.R;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class PlaceDetailActivity extends AppCompatActivity implements OnMapReadyCallback {

    //region Resource declaration
    MapView map_place;

    TextView text_place_name;
    //endregion

    private Double latitude;
    private Double longitude;

    private Place place;
    private GoogleMap googleMap;
    private List<Place.Field> placeFields;

    private String placesApiKey;
    private Context applicationContext;

    final String TAG = "PlaceDetailActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_detail);

        //region Get intent extras
        String placeId = getIntent().getExtras().getString("placeId");

        latitude = getIntent().getExtras().getDouble("latitude");
        longitude = getIntent().getExtras().getDouble("longitude");
        //endregion

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

        PlacesClient placesClient = Places.createClient(this);

        //region Resource assignment
        map_place = findViewById(R.id.map_PlaceDetail_place);
        text_place_name = findViewById(R.id.text_place_name);
        //endregion

        map_place.getMapAsync(this);
        map_place.onCreate(savedInstanceState);

        placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME,
                Place.Field.LAT_LNG, Place.Field.ADDRESS, Place.Field.ADDRESS_COMPONENTS,
                Place.Field.OPENING_HOURS, Place.Field.PHONE_NUMBER, Place.Field.PRICE_LEVEL,
                Place.Field.RATING, Place.Field.WEBSITE_URI, Place.Field.UTC_OFFSET,
                Place.Field.PHOTO_METADATAS);


        FetchPlaceRequest fetchPlaceRequest = FetchPlaceRequest.newInstance(placeId, placeFields);
        placesClient.fetchPlace(fetchPlaceRequest).addOnSuccessListener(new OnSuccessListener<FetchPlaceResponse>() {
            @Override
            public void onSuccess(FetchPlaceResponse fetchPlaceResponse) {
                place = fetchPlaceResponse.getPlace();
                text_place_name.setText(place.getName());
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng latLng = new LatLng(latitude, longitude);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                latLng, 15F));

        googleMap.addMarker(new MarkerOptions().position(latLng).title(""));
    }

    @Override
    protected void onStart() {
        super.onStart();
        map_place.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        map_place.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        map_place.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        map_place.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        map_place.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        map_place.onLowMemory();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        map_place.onSaveInstanceState(outState);

    }
}
