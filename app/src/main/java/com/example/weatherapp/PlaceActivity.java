//package com.example.weatherapp;
//
//import android.content.pm.PackageManager;
//import android.location.Location;
//import android.location.LocationListener;
//import android.location.LocationManager;
//import android.os.Bundle;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.ContextCompat;
//
//public class PlaceActivity extends AppCompatActivity {
//
//    private TextView textViewLocation;
//    private TextView textViewWeather;
//
//    private LocationManager locationManager;
//    private LocationListener locationListener;
//
//    private final String API_KEY = ""; //klucz API
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_place);
//
//        textViewLocation = findViewById(R.id.textViewLocation);
//        textViewWeather = findViewById(R.id.textViewWeather);
//
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this,
//                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
//        } else {
//            startLocationListener();
//        }
//    }
//
//    private void startLocationListener() {
//        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
//
//        locationListener = new LocationListener() {
//            @Override
//            public void onLocationChanged(Location location) {
//                double latitude = location.getLatitude();
//                double longitude = location.getLongitude();
//
//                String cityName = getCityName(latitude, longitude);
//                if (cityName != null) {
//                    textViewLocation.setText("Current Location: " + cityName);
//                    fetchWeather(cityName);
//                } else {
//                    textViewLocation.setText("Current Location: Unknown");
//                    Toast.makeText(PlaceActivity.this, "City not found", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onStatusChanged(String provider, int status, Bundle extras) {}
//
//            @Override
//            public void onProviderEnabled(String provider) {}
//
//            @Override
//            public void onProviderDisabled(String provider) {}
//        };
//
//        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
//                3000, 10, locationListener);
//    }
//    private String getCityName(double latitude, double longitude) {
//        break
//    }
//
//}
