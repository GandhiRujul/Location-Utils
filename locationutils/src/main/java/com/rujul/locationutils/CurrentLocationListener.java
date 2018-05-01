package com.rujul.locationutils;

import android.location.Location;

/**
 * Created by Rujul Gandhi on 1/5/18
 */

public interface CurrentLocationListener {
    void onLocationUpdate(Location location);

    void onNoLocationFound();

    void onRequestPermission();
}
