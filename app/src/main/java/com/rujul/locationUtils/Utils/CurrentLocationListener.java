package com.rujul.locationUtils.Utils;

import android.location.Location;

/**
 * Created by Rujul Gandhi on 21/12/17.
 */

public interface CurrentLocationListener {
    void onLocationUpdate(Location location);

    void onNoLocationFound();

    void onRequestPermission();
}
