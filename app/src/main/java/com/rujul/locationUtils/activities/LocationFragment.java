package com.rujul.locationUtils.activities;

import android.app.Fragment;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rujul.locationUtils.R;
import com.rujul.locationUtils.Utils.CurrentLocationListener;
import com.rujul.locationUtils.Utils.FusedLocationUtils;
import com.rujul.locationUtils.databinding.FragmentLocationBinding;

/**
 * Created by imobdev-rujul on 1/5/18.
 */

public class LocationFragment extends Fragment implements CurrentLocationListener {
    private FragmentLocationBinding binding;
    private FusedLocationUtils fusedLocationUtils;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fusedLocationUtils = FusedLocationUtils.getInstance(getActivity());
        fusedLocationUtils.setRepeativeUpdate(true);
        fusedLocationUtils.setInterval(1000);
        fusedLocationUtils.setCurrentLocationListener(this);
        fusedLocationUtils.createLocationRequest();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = FragmentLocationBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Fragment locationFragment = new LocationFragment();
        getChildFragmentManager().beginTransaction().replace(R.id.sub_container, locationFragment).commit();
    }

    @Override
    public void onLocationUpdate(Location location) {
        binding.tvLocation.setText(String.valueOf(location.getLatitude() + " : " + location.getLongitude()));
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        fusedLocationUtils.setOnActivityResult(requestCode, resultCode, data);
    }
}
