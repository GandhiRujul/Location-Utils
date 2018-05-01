package com.rujul.locationUtils.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.rujul.locationUtils.R;

public class ChooseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);
    }


    public void openFragment(View view) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    public void openActivity(View view) {
        Intent intent = new Intent(getApplicationContext(), LocationActivity.class);
        startActivity(intent);
    }
}
