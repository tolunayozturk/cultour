package com.ozturktolunay.cultour.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.ozturktolunay.cultour.Activity.FindLocationActivity;
import com.ozturktolunay.cultour.Activity.PlaceDetailActivity;
import com.ozturktolunay.cultour.GPlace;
import com.ozturktolunay.cultour.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class GPlaceAdapter extends RecyclerView.Adapter<GPlaceAdapter.GPlaceViewHolder> {

    private Context context;

    private ArrayList<GPlace> gPlacesList;

    private String placesApiKey;

    public GPlaceAdapter(Context context, ArrayList<GPlace> gPlacesList, String placesApiKey) {
        this.context = context;
        this.gPlacesList = gPlacesList;
        this.placesApiKey = placesApiKey;
    }

    public class GPlaceViewHolder extends RecyclerView.ViewHolder {

        //region Resource declaration
        CardView card_gplace;

        ImageView image_gplace;

        TextView text_gplace_name;
        TextView text_gplace_rating;
        //endregion

        public GPlaceViewHolder(@NonNull View itemView) {
            super(itemView);

            //region Resource assignment
            card_gplace = itemView.findViewById(R.id.card_gplace);

            image_gplace = itemView.findViewById(R.id.image_gplace);

            text_gplace_name = itemView.findViewById(R.id.text_gplace_name);
            text_gplace_rating = itemView.findViewById(R.id.text_gplace_rating);
            //endregion

        }

    }

    @NonNull
    @Override
    public GPlaceAdapter.GPlaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.card_gplace, parent, false);
        return new GPlaceViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull GPlaceAdapter.GPlaceViewHolder holder, int position) {

        final GPlace current = gPlacesList.get(position);

        final String photoReference = current.getPhotoReference();
        final String name = current.getName();
        final String address = current.getAddress();
        final String id = current.getId();

        final Double rating = current.getRating();

        final Intent intent = new Intent(context.getApplicationContext(), PlaceDetailActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        holder.card_gplace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("placeId", id);
                context.startActivity(intent);
            }
        });

        holder.text_gplace_name.setText(name);
        holder.text_gplace_rating.setText(String.format("⭐️ %s", rating.toString()));
        Picasso.with(context).load("https://maps.googleapis.com/maps/api/place/" +
                "photo?maxwidth=400&photoreference=" + photoReference + "&key=" + placesApiKey)
        .fit().centerInside().into(holder.image_gplace);
    }

    @Override
    public int getItemCount() { return gPlacesList.size(); }
}
