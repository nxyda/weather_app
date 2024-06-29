package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ForecastActivity extends AppCompatActivity {

    private LinearLayout linearLayoutForecast;
    private final String API_KEY = ""; // Klucz API

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);

        linearLayoutForecast = findViewById(R.id.linearLayoutForecast);

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
                    for (int i = 0; i < list.length(); i++) {
                        JSONObject listItem = list.getJSONObject(i);
                        String dateTime = listItem.getString("dt_txt");
                        JSONObject main = listItem.getJSONObject("main");
                        String temperature = main.getString("temp");
                        String description = listItem.getJSONArray("weather").getJSONObject(0).getString("description");

                        TextView forecastTextView = new TextView(ForecastActivity.this);
                        forecastTextView.setText(dateTime + " - " + temperature + "°C, " + description);
                        linearLayoutForecast.addView(forecastTextView);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
