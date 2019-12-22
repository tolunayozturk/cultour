package com.ozturktolunay.cultour.Fragment;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

public class PoiFragment extends Fragment {

    //region Resource declaration
    RecyclerView recycler_museum;
    RecyclerView recycler_aquarium;
    RecyclerView recycler_art_gallery;

    TextView text_museum;
    TextView text_aquarium;
    TextView text_art_gallery;
    //endregion

    private String placesApiKey;
    private Context applicationContext;
    private GPlaceAdapter gPlaceAdapter;
    private final String TAG = "PoiFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_poi, container, false);

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
        recycler_museum = view.findViewById(R.id.recycler_PoiFragment_museum);
        recycler_aquarium = view.findViewById(R.id.recycler_PoiFragment_aquarium);
        recycler_art_gallery = view.findViewById(R.id.recycler_PoiFragment_art_gallery);

        text_museum = view.findViewById(R.id.text_museum);
        text_aquarium = view.findViewById(R.id.text_aquarium);
        text_art_gallery = view.findViewById(R.id.text_art_gallery);
        //endregion

        View v = inflater.inflate(R.layout.card_gplace, container, false);
        RelativeLayout relativeLayout = v.findViewById(R.id.relative_card_gplace);
        relativeLayout.getLayoutParams().width = 1000;

        //region Set layout attr
        recycler_museum.setHasFixedSize(true);
        recycler_museum.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.HORIZONTAL, false));
        recycler_museum.addItemDecoration(new DividerItemDecoration(
                applicationContext, DividerItemDecoration.HORIZONTAL));

        recycler_aquarium.setHasFixedSize(true);
        recycler_aquarium.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.HORIZONTAL, false));
        recycler_aquarium.addItemDecoration(new DividerItemDecoration(
                applicationContext, DividerItemDecoration.HORIZONTAL));

        recycler_art_gallery.setHasFixedSize(true);
        recycler_art_gallery.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.HORIZONTAL, false));
        recycler_art_gallery.addItemDecoration(new DividerItemDecoration(
                applicationContext, DividerItemDecoration.HORIZONTAL));
        //endregion

        String url_museum = RequestUtility.getRequestUrl(latitude, longitude, GPlaceType.MUSEUM,
                placesApiKey);

        String url_aquarium = RequestUtility.getRequestUrl(latitude, longitude, GPlaceType.AQUARIUM,
                placesApiKey);

        String url_art_gallery = RequestUtility.getRequestUrl(latitude, longitude,
                GPlaceType.ART_GALLERY, placesApiKey);


        ArrayList<GPlace> museumList = new ArrayList<>();
        ArrayList<GPlace> aquariumList = new ArrayList<>();
        ArrayList<GPlace> artGalleryList = new ArrayList<>();

        FetchAndUpdate(url_museum, museumList, recycler_museum);
        FetchAndUpdate(url_aquarium, aquariumList, recycler_aquarium);
        FetchAndUpdate(url_art_gallery, artGalleryList, recycler_art_gallery);

        return view;
    }

    public void FetchAndUpdate(final String url, final ArrayList<GPlace> placeArrayList, final RecyclerView recyclerView) {
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("results");
                            String status = response.getString("status");
                            if (!status.equals("OK")) {
                                Uri uri = Uri.parse(url);
                                String type = uri.getQueryParameter("type");

                                if (type.equals(GPlaceType.MUSEUM)) {
                                    text_museum.setVisibility(View.GONE);
                                } else if (type.equals(GPlaceType.AQUARIUM)) {
                                    text_aquarium.setVisibility(View.GONE);
                                } else if (type.equals(GPlaceType.ART_GALLERY)) {
                                    text_art_gallery.setVisibility(View.GONE);
                                }
                            }

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
