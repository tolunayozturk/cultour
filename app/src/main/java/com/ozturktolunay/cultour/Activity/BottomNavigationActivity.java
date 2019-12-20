package com.ozturktolunay.cultour.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ozturktolunay.cultour.Fragment.LodgingFragment;
import com.ozturktolunay.cultour.Fragment.PoiFragment;
import com.ozturktolunay.cultour.Fragment.RestaurantFragment;
import com.ozturktolunay.cultour.R;

public class BottomNavigationActivity extends AppCompatActivity {

    //region Resource declaration
    BottomNavigationView bottomNavigationView;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigation);

        //region Resource assignment
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        //endregion

        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new PoiFragment()).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    switch (item.getItemId()) {
                        case R.id.nav_poi:
                            selectedFragment = new PoiFragment();
                            break;
                        case R.id.nav_restaurant:
                            selectedFragment = new RestaurantFragment();
                            break;
                        case R.id.nav_lodging:
                            selectedFragment = new LodgingFragment();
                            break;
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();

                    return true;
                }
            };

}
