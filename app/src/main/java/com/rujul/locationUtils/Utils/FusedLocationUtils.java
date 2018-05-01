package com.rujul.locationUtils.Utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import static android.app.Activity.RESULT_OK;

/**
 * Created by imobdev-rujul on 1/5/18.
 */

public class FusedLocationUtils implements OnSuccessListener, OnFailureListener {

    @SuppressLint("StaticFieldLeak")
    private static FusedLocationUtils fusedLocation;
    private LocationRequest locationRequest;
    private LocationSettingsRequest.Builder locationSettingsRequest;
    private Activity mActivity;
    private CurrentLocationListener currentLocationListener;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback mLocationCallback;
    private Location mCurrentLocation;
    private static final int GPS_REQ_CODE = 100;
    private boolean need_repetive_updates = false;
    private int interval = 2000;

    private FusedLocationUtils(Activity activity) {
        this.mActivity = activity;
    }

    public static FusedLocationUtils getInstance(Activity activity) {
        if (fusedLocation == null) {
            fusedLocation = new FusedLocationUtils(activity);
        }
        return fusedLocation;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public void setCurrentLocationListener(CurrentLocationListener currentLocationListener) {
        this.currentLocationListener = currentLocationListener;
    }

    public void setRepeativeUpdate(boolean need_repetive_updates) {
        this.need_repetive_updates = need_repetive_updates;
    }

        public void createLocationRequest() {
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setFastestInterval(interval);
        locationRequest.setInterval(interval);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(mActivity);
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                mCurrentLocation = locationResult.getLastLocation();
                if (currentLocationListener != null) {
                    if (mCurrentLocation != null && mCurrentLocation.getLongitude() != 0.0) {
                        currentLocationListener.onLocationUpdate(mCurrentLocation);
                        if (!need_repetive_updates) {
                            stopLocationUpdates();
                        }
                    }
                }
            }
        };
        locationSettingsRequest = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        SettingsClient client = LocationServices.getSettingsClient(mActivity);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(locationSettingsRequest.build());
        task.addOnSuccessListener(this).addOnFailureListener(this);
    }

    @Override
    public void onSuccess(Object object) {
        if (object instanceof LocationSettingsResponse) {
            getLocation();
        } else if (object instanceof Location) {
            Location location = (Location) object;
            if (currentLocationListener != null) {
                if (location != null && location.getLongitude() != 0.0) {
                    currentLocationListener.onLocationUpdate(location);
                    if (!need_repetive_updates) {
                        stopLocationUpdates();
                    }
                }
            }
        }
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            currentLocationListener.onRequestPermission();
        } else {
            startLocationUpdates();
        }
    }

    @Override
    public void onFailure(@NonNull Exception e) {
        if (e instanceof ResolvableApiException) {
            try {
                ResolvableApiException resolvable = (ResolvableApiException) e;
                resolvable.startResolutionForResult(mActivity,
                        GPS_REQ_CODE);
            } catch (IntentSender.SendIntentException sendEx) {
                // Ignore the error.
            }
        }
    }

    private void startLocationUpdates() {
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            currentLocationListener.onRequestPermission();
            return;
        }
        mFusedLocationClient.requestLocationUpdates(locationRequest,
                mLocationCallback, Looper.myLooper());
    }

    private void stopLocationUpdates() {
        if (mFusedLocationClient != null && mLocationCallback != null) {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        }
    }

    private void deniedGPS() {
        currentLocationListener.onNoLocationFound();
    }

    public void setOnActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GPS_REQ_CODE) {
            if (resultCode == RESULT_OK) {
                createLocationRequest();
            } else {
                deniedGPS();
            }
        }
    }

}
