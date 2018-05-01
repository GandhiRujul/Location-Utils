package com.rujul.locationUtils.activities;

import android.app.Fragment;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.rujul.locationUtils.R;
import com.rujul.locationUtils.databinding.ActivityMainBinding;

/**
 * Created by Rujul Gandhi on 1/5/18.
 */

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        Fragment fragment = new LocationFragment();
        getFragmentManager().beginTransaction().replace(R.id.container, fragment).addToBackStack(null).commit();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment frg = getFragmentManager().findFragmentById(R.id.container);
        if (frg != null) {
            frg.onActivityResult(requestCode, resultCode, data);
        }
    }
}
