package com.wangzhen.weatherview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.wangzhen.weatherview.view.WeatherView;

public class MainActivity extends AppCompatActivity {

    private WeatherView weatherView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        weatherView = (WeatherView) findViewById(R.id.weather_view);
    }
}
