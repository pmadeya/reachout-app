package ca.sfu.iat381.reachout_app.views;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.widget.Toast;

import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntro2Fragment;

import ca.sfu.iat381.reachout_app.R;


/*
This activity will be loaded only ONCE at the start of the application
to give the user a short tutorial on how to use the basic features of
the app.
 */
public class IntroActivity extends AppIntro2 {

    private static final int LOCATION_PERMISSION = 1;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.



                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Should we show an explanation?
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            Toast.makeText(this, "Location permission is needed to show events nearby", Toast.LENGTH_SHORT).show();
        }
        else {
            //Request the permission
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addSlide(AppIntro2Fragment.newInstance("Reachout", "Find events around you by choosing a category, see a map, or search by keyword", R.drawable.mainactivity_screenshot, ContextCompat.getColor(this, R.color.android_green_background)));
        addSlide(AppIntro2Fragment.newInstance("Map", "Pan around a map full of events closest to your current location", R.drawable.mapview_screenshot, ContextCompat.getColor(this, R.color.android_green_background)));
        addSlide(AppIntro2Fragment.newInstance("", "Get details of a specific event happening in the area", R.drawable.eventdetails_screenshot, ContextCompat.getColor(this, R.color.android_green_background)));
        addSlide(AppIntro2Fragment.newInstance("", "Search events by category, nearest to you", R.drawable.categoryactivity_screenshot, ContextCompat.getColor(this, R.color.android_green_background)));
        addSlide(AppIntro2Fragment.newInstance("Keep track of your favorite events", "Click to get started!", R.drawable.eventlistings_screenshot, ContextCompat.getColor(this, R.color.android_green_background)));
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        finish();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);

        Intent i = new Intent(IntroActivity.this, MainActivity.class);
        startActivity(i);
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
    }
}
