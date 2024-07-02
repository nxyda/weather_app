package com.example.weatherapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SearchActivity extends AppCompatActivity {

    private EditText editTextSearchCity;
    private Button buttonSearch;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        editTextSearchCity = findViewById(R.id.editTextSearchCity);
        buttonSearch = findViewById(R.id.buttonSearch);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city = editTextSearchCity.getText().toString();
                Intent intent = new Intent(SearchActivity.this, ForecastActivity.class);
                intent.putExtra("city", city);
                startActivity(intent);
            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        startActivity(new Intent(SearchActivity.this, MainActivity.class));
                        return true;
//                    case R.id.navigation_place:
//                        startActivity(new Intent(SearchActivity.this, PlaceActivity.class));
//                        return true;
                    case R.id.navigation_forecast:
                        startActivity(new Intent(SearchActivity.this, SearchActivity.class));
                        return true;
                }
                return false;
            }
        });
    }
}
