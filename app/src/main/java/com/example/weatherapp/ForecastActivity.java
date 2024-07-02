package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ForecastActivity extends AppCompatActivity {

    private GridLayout gridLayoutForecast;
    private final String API_KEY = ""; // Klucz API

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);

        gridLayoutForecast = findViewById(R.id.gridLayoutForecast);

        String city = getIntent().getStringExtra("city");
        if (city != null) {
            new GetForecastTask().execute(city);
        }
    }

    private class GetForecastTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String city = params[0];
            String result = "";
            try {
                String urlString = "https://api.openweathermap.org/data/2.5/forecast?q=" + city + "&appid=" + API_KEY + "&units=metric";
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
                    JSONArray list = jsonObject.getJSONArray("list");
                    for (int i = 0; i < 8; i++) {
                        JSONObject listItem = list.getJSONObject(i);
                        String dateTime = listItem.getString("dt_txt").substring(11, 16);
                        JSONObject main = listItem.getJSONObject("main");
                        double temperature = main.getDouble("temp");
                        String description = listItem.getJSONArray("weather").getJSONObject(0).getString("description");

                        LinearLayout forecastLayout = new LinearLayout(ForecastActivity.this);
                        forecastLayout.setOrientation(LinearLayout.VERTICAL);
                        forecastLayout.setPadding(10, 10, 10, 10);
                        forecastLayout.setGravity(Gravity.CENTER);
                        forecastLayout.setBackgroundResource(R.drawable.forecast_background);

                        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                        params.width = 0;
                        params.height = GridLayout.LayoutParams.WRAP_CONTENT;
                        params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1, 1f);
                        params.rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1, 1f);
                        params.setMargins(10, 10, 10, 10);
                        forecastLayout.setLayoutParams(params);

                        TextView dateTimeTextView = new TextView(ForecastActivity.this);
                        dateTimeTextView.setText(dateTime);
                        dateTimeTextView.setTextSize(16);
                        dateTimeTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

                        TextView temperatureTextView = new TextView(ForecastActivity.this);
                        int roundedTemperature = (int) Math.round(temperature);
                        temperatureTextView.setText(roundedTemperature + "°C");
                        temperatureTextView.setTextSize(18);
                        temperatureTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

                        ImageView weatherImageView = new ImageView(ForecastActivity.this);
                        switch (description.toLowerCase()) {
                            case "clear sky":
                                weatherImageView.setImageResource(R.drawable.sun);
                                break;
                            case "few clouds":
                                weatherImageView.setImageResource(R.drawable.clouds);
                            case "scattered clouds":
                                weatherImageView.setImageResource(R.drawable.clouds);
                            case "broken clouds":
                                weatherImageView.setImageResource(R.drawable.sun_and_clouds);
                                break;
                            case "shower rain":
                                weatherImageView.setImageResource(R.drawable.rain);
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
                            case "fog":
                                weatherImageView.setImageResource(R.drawable.clouds);
                                break;
                            default:
                                weatherImageView.setImageResource(R.drawable.clouds);
                                break;
                        }

                        forecastLayout.addView(dateTimeTextView);
                        forecastLayout.addView(temperatureTextView);
                        forecastLayout.addView(weatherImageView);

                        gridLayoutForecast.addView(forecastLayout);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
