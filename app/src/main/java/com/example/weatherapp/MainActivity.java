package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private EditText editTextCity;
    private Button buttonGetWeather;
    private TextView textViewResult;

    private final String API_KEY = ""; // Klucz API

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextCity = findViewById(R.id.editTextCity);
        buttonGetWeather = findViewById(R.id.buttonGetWeather);
        textViewResult = findViewById(R.id.textViewResult);

        buttonGetWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city = editTextCity.getText().toString();
                new GetWeatherTask().execute(city);
            }
        });
    }

    private class GetWeatherTask extends AsyncTask<String, Void, String> {
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
                    String temperature = main.getString("temp");
                    String description = jsonObject.getJSONArray("weather").getJSONObject(0).getString("description");
                    textViewResult.setText("Temperature: " + temperature + "°C\nWeather: " + description);
                } catch (Exception e) {
                    e.printStackTrace();
                    textViewResult.setText("Error parsing weather data.");
                }
            } else {
                textViewResult.setText("Error fetching weather data.");
            }
        }
    }
}
