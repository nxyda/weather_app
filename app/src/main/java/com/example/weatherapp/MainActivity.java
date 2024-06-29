package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    private EditText editTextCity;
    private Button buttonGetWeather;
    private TextView textViewResult;
    private GridLayout gridLayoutDefaultCities;

    private final String API_KEY = ""; // Klucz API

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextCity = findViewById(R.id.editTextCity);
        buttonGetWeather = findViewById(R.id.buttonGetWeather);
        textViewResult = findViewById(R.id.textViewResult);
        gridLayoutDefaultCities = findViewById(R.id.gridLayoutDefaultCities);

        buttonGetWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city = editTextCity.getText().toString();
                new GetWeatherTask(textViewResult, null).execute(city);
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
                    String temperature = main.getString("temp");
                    String description = jsonObject.getJSONArray("weather").getJSONObject(0).getString("description");

                    if (resultTextView != null) {
                        resultTextView.setText(temperature + "°C");
                    }

                    if (weatherImageView != null) {
                        switch (description.toLowerCase()) {
                            case "clear sky":
                                weatherImageView.setImageResource(R.drawable.sun);
                                break;
                            case "few clouds":
                            case "scattered clouds":
                            case "broken clouds":
                                weatherImageView.setImageResource(R.drawable.sun_and_clouds);
                                break;
                            case "shower rain":
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
