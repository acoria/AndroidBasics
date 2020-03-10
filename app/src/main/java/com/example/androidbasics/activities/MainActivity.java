package com.example.androidbasics.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.androidbasics.R;
import com.jakewharton.rxbinding2.view.RxView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RxView.clicks(findViewById(R.id.db_connection))
            .subscribe(click -> startActivity(new Intent(this, DBConnectionActivity.class)));
    }
}
