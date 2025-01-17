package com.example.weatherapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    private EditText editTextCity;
    private Button buttonGetWeather;
    private Button buttonForecast;
    private TextView textViewResult;
    private GridLayout gridLayoutDefaultCities;
    private BottomNavigationView bottomNavigationView;

    private final String API_KEY = ""; // Klucz API

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextCity = findViewById(R.id.editTextCity);
        buttonGetWeather = findViewById(R.id.buttonGetWeather);
        buttonForecast = findViewById(R.id.buttonForecast);
        textViewResult = findViewById(R.id.textViewResult);
        gridLayoutDefaultCities = findViewById(R.id.gridLayoutDefaultCities);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        buttonGetWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city = editTextCity.getText().toString();
                new GetWeatherTask(textViewResult, null).execute(city);
            }
        });

        buttonForecast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city = editTextCity.getText().toString();
                Intent intent = new Intent(MainActivity.this, ForecastActivity.class);
                intent.putExtra("city", city);
                startActivity(intent);
            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        startActivity(new Intent(MainActivity.this, MainActivity.class));
                        return true;
//                    case R.id.navigation_place:
//                        startActivity(new Intent(MainActivity.this, PlaceActivity.class));
//                        return true;
                    case R.id.navigation_forecast:
                        startActivity(new Intent(MainActivity.this, SearchActivity.class));
                        return true;
                }
                return false;
            }
        });

        displayDefaultCitiesWeather();
    }

    private void displayDefaultCitiesWeather() {
        String[] cities = {"Warsaw", "London", "New York", "Berlin", "Paris", "Tokyo"};
        for (String city : cities) {
            try {
                LinearLayout cityLayout = new LinearLayout(this);
                cityLayout.setOrientation(LinearLayout.VERTICAL);
                cityLayout.setPadding(10, 10, 10, 10);
                cityLayout.setGravity(Gravity.CENTER);
                cityLayout.setBackgroundResource(R.drawable.city_background);

                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = 0;
                params.height = GridLayout.LayoutParams.WRAP_CONTENT;
                params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1, 1f);
                params.rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1, 1f);
                params.setMargins(10, 10, 10, 10);
                cityLayout.setLayoutParams(params);

                TextView cityTextView = new TextView(this);
                cityTextView.setText(city.toUpperCase());
                cityTextView.setTextSize(18);
                cityTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

                ImageView weatherImageView = new ImageView(this);
                TextView weatherTextView = new TextView(this);
                weatherTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

                cityLayout.addView(cityTextView);
                cityLayout.addView(weatherImageView);
                cityLayout.addView(weatherTextView);

                new GetWeatherTask(weatherTextView, weatherImageView).execute(city).get();

                gridLayoutDefaultCities.addView(cityLayout);
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private class GetWeatherTask extends AsyncTask<String, Void, String> {
        private final TextView resultTextView;
        private final ImageView weatherImageView;

        public GetWeatherTask(TextView resultTextView, ImageView weatherImageView) {
            this.resultTextView = resultTextView;
            this.weatherImageView = weatherImageView;
        }

        @Override
        protected String doInBackground(String... params) {
            String city = params[0];
            String result = "";
            try {
                String urlString = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + API_KEY + "&units=metric";
                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                reader.close();
                result = stringBuilder.toString();
            } catch (Exception e) {
                e.printStackTrace();
                result = null;
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONObject main = jsonObject.getJSONObject("main");
                    double temperature = main.getDouble("temp");
                    String description = jsonObject.getJSONArray("weather").getJSONObject(0).getString("description");

                    if (resultTextView != null) {
                        int roundedTemperature = (int) Math.round(temperature);
                        resultTextView.setText(roundedTemperature + "°C");
                    }

                    if (weatherImageView != null) {
                        switch (description.toLowerCase()) {
                            case "clear sky":
                                weatherImageView.setImageResource(R.drawable.sun);
                                break;
                            case "few clouds":
                                weatherImageView.setImageResource(R.drawable.sun_and_clouds);
                                break;
                            case "scattered clouds":
                                weatherImageView.setImageResource(R.drawable.sun_and_clouds);
                                break;
                            case "broken clouds":
                                weatherImageView.setImageResource(R.drawable.sun_and_clouds);
                                break;
                            case "shower rain":
                                weatherImageView.setImageResource(R.drawable.rain);
                                break;
                            case "rain":
                                weatherImageView.setImageResource(R.drawable.rain);
                                break;
                            case "thunderstorm":
                                weatherImageView.setImageResource(R.drawable.sun_and_rain);
                                break;
                            case "snow":
                                weatherImageView.setImageResource(R.drawable.snow);
                                break;
                            case "mist":
                                weatherImageView.setImageResource(R.drawable.clouds);
                                break;
                            case "fog":
                                weatherImageView.setImageResource(R.drawable.clouds);
                                break;
                            default:
                                weatherImageView.setImageResource(R.drawable.clouds);
                                break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    if (resultTextView != null) {
                        resultTextView.setText("Error parsing weather data.");
                    }
                }
            } else {
                if (resultTextView != null) {
                    resultTextView.setText("Error fetching weather data.");
                }
            }
        }
    }
}
