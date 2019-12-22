package com.ozturktolunay.cultour.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

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

        //region Set status bar color
        Window window = BottomNavigationActivity.this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(
                BottomNavigationActivity.this, R.color.quantum_white_100));
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

    public Double getLatitude() {
        return getIntent().getExtras().getDouble("latitude");
    }

    public Double getLongitude() {
        return getIntent().getExtras().getDouble("longitude");
    }

}
