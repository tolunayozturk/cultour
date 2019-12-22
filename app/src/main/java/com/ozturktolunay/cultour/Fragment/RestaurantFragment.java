package com.ozturktolunay.cultour.Fragment;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.libraries.places.api.Places;
import com.ozturktolunay.cultour.Activity.BottomNavigationActivity;
import com.ozturktolunay.cultour.Adapter.GPlaceAdapter;
import com.ozturktolunay.cultour.GPlace;
import com.ozturktolunay.cultour.Interface.GPlaceType;
import com.ozturktolunay.cultour.R;
import com.ozturktolunay.cultour.Utility.RequestUtility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RestaurantFragment extends Fragment {

    //region Resource declaration
    RecyclerView recycler_restaurant;
    //endregion

    private String placesApiKey;
    private Context applicationContext;
    private GPlaceAdapter gPlaceAdapter;
    private final String TAG = "RestaurantFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_restaurant, container, false);

        Double latitude = ((BottomNavigationActivity)getActivity()).getLatitude();
        Double longitude = ((BottomNavigationActivity)getActivity()).getLongitude();

        applicationContext = getActivity().getApplicationContext();
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
        recycler_restaurant = view.findViewById(R.id.recycler_RestaurantFragment_restaurant);
        //endregion

        //region Set layout attr
        recycler_restaurant.setHasFixedSize(true);
        recycler_restaurant.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false));
        recycler_restaurant.addItemDecoration(new DividerItemDecoration(
                applicationContext, DividerItemDecoration.VERTICAL));
        //endregion

        String url_restaurant = RequestUtility.getRequestUrl(latitude, longitude,
                GPlaceType.RESTAURANT, placesApiKey);

        ArrayList<GPlace> restaurantList = new ArrayList<>();

        FetchAndUpdate(url_restaurant, restaurantList, recycler_restaurant);

        return view;
    }

    public void FetchAndUpdate(final String url, final ArrayList<GPlace> placeArrayList, final RecyclerView recyclerView) {
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("results");
                            /*
                            String status = response.getString("status");
                            if (!status.equals("OK")) {
                                Uri uri = Uri.parse(url);
                                String type = uri.getQueryParameter("type");
                            }
                            */
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject res = jsonArray.getJSONObject(i);

                                String name = res.getString("name");
                                String id = res.getString("place_id");
                                String address = res.getString("vicinity");

                                String photoReference;
                                if (!res.isNull("photos")) {
                                    photoReference = res.getJSONArray("photos").getJSONObject(0).getString("photo_reference");
                                } else { continue; }

                                Double rating;
                                if (!res.isNull("rating")) {
                                    rating = res.getDouble("rating");
                                } else { continue; }

                                placeArrayList.add(new GPlace(photoReference, address, name, id, rating));
                                Log.i(TAG, String.format("Name: %s", name));
                            }

                            gPlaceAdapter = new GPlaceAdapter(applicationContext, placeArrayList, placesApiKey);
                            recyclerView.setAdapter(gPlaceAdapter);

                            //new Handler().postDelayed(() -> recyclerView.smoothScrollBy(600, 0), 2500);
                            //new Handler().postDelayed(() -> recyclerView.smoothScrollBy(-600, 0), 3750);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "", error);
            }
        });
        Volley.newRequestQueue(getActivity()).add(request);
    }

}
