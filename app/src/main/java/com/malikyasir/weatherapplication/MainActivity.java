package com.malikyasir.weatherapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    EditText cityName;
    ImageView searchIcon;
    TextView temperature, feelsLike, temperatureMin, temperatureMax, pressure, humidity;

    class GetWeather extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            StringBuilder result = new StringBuilder();
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

                String line = "";
                while ((line = reader.readLine()) != null) {
                    result.append(line).append("\n");
                }
                return result.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONObject main = jsonObject.getJSONObject("main");

                String temperatureValue = main.getString("temp");
                String feelsLikeValue = main.getString("feels_like");
                String temperatureMinValue = main.getString("temp_min");
                String temperatureMaxValue = main.getString("temp_max");
                String pressureValue = main.getString("pressure");
                String humidityValue = main.getString("humidity");

                temperature.setText("Temperature: " + temperatureValue);
                feelsLike.setText("Feels Like: " + feelsLikeValue);
                temperatureMin.setText("Temperature Min: " + temperatureMinValue);
                temperatureMax.setText("Temperature Max: " + temperatureMaxValue);
                pressure.setText("Pressure: " + pressureValue);
                humidity.setText("Humidity: " + humidityValue);

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, "Enter correct city name! ", Toast.LENGTH_LONG).show();
                temperature.setText("Cannot fetch weather data");
                feelsLike.setText("");
                temperatureMin.setText("");
                temperatureMax.setText("");
                pressure.setText("");
                humidity.setText("");
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityName = findViewById(R.id.cityName);
        searchIcon = findViewById(R.id.searchIcon);
        temperature = findViewById(R.id.temperature);
        feelsLike = findViewById(R.id.feels_like);
        temperatureMin = findViewById(R.id.temperature_min);
        temperatureMax = findViewById(R.id.temperature_max);
        pressure = findViewById(R.id.pressure);
        humidity = findViewById(R.id.humidity);

        searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Weather Showed! ", Toast.LENGTH_SHORT).show();
                String city = cityName.getText().toString();
                if (city != null && !city.isEmpty()) {
                    String url = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&units=metric&appid=1cb14ced3fa426504a26cfe66b985aa9";
                    new GetWeather().execute(url);
                } else {
                    Toast.makeText(MainActivity.this, "Enter City", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
