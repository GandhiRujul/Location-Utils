package com.rujul.locationUtils.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.rujul.locationUtils.R;
import com.rujul.locationUtils.databinding.ActivityLocationBinding;
import com.rujul.locationutils.CurrentLocationListener;
import com.rujul.locationutils.FusedLocationUtils;

/**
 * Created by Rujul Gandhi on 1/5/18
 */

public class LocationActivity extends AppCompatActivity implements CurrentLocationListener, OnMapReadyCallback, LocationSource {
    ActivityLocationBinding binding;
    private FusedLocationUtils locationUtils;
    private GoogleMap googleMap;
    private OnLocationChangedListener onLocationChangedListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_location);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        locationUtils = FusedLocationUtils.getInstance(this);
        locationUtils.setInterval(1000);
        locationUtils.setCurrentLocationListener(this);
        locationUtils.setRepeativeUpdate(true);
        locationUtils.createLocationRequest();
    }

    @Override
    public void onLocationUpdate(Location location) {
        binding.tvLocation.setText(String.valueOf(location.getLatitude() + " : " + location.getLongitude()));
        if (googleMap != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                onRequestPermission();
                return;
            }
            onLocationChangedListener.onLocationChanged(location);
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(20));
        }
    }

    @Override
    public void onNoLocationFound() {
        binding.tvLocation.setText("No Location Found");
    }

    @Override
    public void onRequestPermission() {
        binding.tvLocation.setText("Needs Permission");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        locationUtils.setOnActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        googleMap.setLocationSource(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        googleMap.setMyLocationEnabled(true);
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        this.onLocationChangedListener = onLocationChangedListener;
    }

    @Override
    public void deactivate() {
        onLocationChangedListener = null;
    }

}
