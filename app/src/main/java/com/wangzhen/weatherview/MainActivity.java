package com.wangzhen.weatherview;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.wangzhen.weatherview.util.TimeUtils;
import com.wangzhen.weatherview.view.WeatherView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private WeatherView weatherView;
    private List<String> listWeeks = new ArrayList<>();
    private List<String> listDayWeathers = new ArrayList<>();
    private List<Bitmap> listDayIcons = new ArrayList<>();
    private List<Integer> listDayTemperature = new ArrayList<>();
    private List<Integer> listNightTemperature = new ArrayList<>();
    private List<Bitmap> listNightIcons = new ArrayList<>();
    private List<String> listNightWeathers = new ArrayList<>();
    private List<String> listDate = new ArrayList<>();
    private List<String> listWind = new ArrayList<>();
    private List<String> listWindLevel = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        weatherView = (WeatherView) findViewById(R.id.weather_view);
        initData();
    }

    private void initData() {
        //添加星期几数据 星期六
        Date currentDate = new Date(System.currentTimeMillis());
        listWeeks.add("今天");
        listWeeks.add(TimeUtils.getWeekForDate(TimeUtils.addOneDayForDate(currentDate)));
        currentDate = TimeUtils.addOneDayForDate(currentDate);
        listWeeks.add(TimeUtils.getWeekForDate(TimeUtils.addOneDayForDate(currentDate)));
        currentDate = TimeUtils.addOneDayForDate(currentDate);
        listWeeks.add(TimeUtils.getWeekForDate(TimeUtils.addOneDayForDate(currentDate)));
        currentDate = TimeUtils.addOneDayForDate(currentDate);
        listWeeks.add(TimeUtils.getWeekForDate(TimeUtils.addOneDayForDate(currentDate)));

        //添加日期数据 04/20
        currentDate = new Date(System.currentTimeMillis());
        listDate.add(TimeUtils.formatDate("MM/dd", currentDate));
        currentDate = TimeUtils.addOneDayForDate(currentDate);
        listDate.add(TimeUtils.formatDate("MM/dd", currentDate));
        currentDate = TimeUtils.addOneDayForDate(currentDate);
        listDate.add(TimeUtils.formatDate("MM/dd", currentDate));
        currentDate = TimeUtils.addOneDayForDate(currentDate);
        listDate.add(TimeUtils.formatDate("MM/dd", currentDate));
        currentDate = TimeUtils.addOneDayForDate(currentDate);
        listDate.add(TimeUtils.formatDate("MM/dd", currentDate));

        //添加白天天气
        listDayWeathers.add("多云");
        listDayWeathers.add("多云");
        listDayWeathers.add("多云");
        listDayWeathers.add("小雨");
        listDayWeathers.add("多云");

        //添加白天天气图标
        listDayIcons.add(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_cloud));
        listDayIcons.add(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_cloud));
        listDayIcons.add(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_cloud));
        listDayIcons.add(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_rain_small));
        listDayIcons.add(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_cloud));

        //添加夜间天气图标
        listNightIcons.add(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_cloud));
        listNightIcons.add(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_cloud));
        listNightIcons.add(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_rain_small));
        listNightIcons.add(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_cloud));
        listNightIcons.add(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_cloud));

        //添加夜晚天气
        listNightWeathers.add("多云");
        listNightWeathers.add("多云");
        listNightWeathers.add("小雨");
        listNightWeathers.add("阴");
        listNightWeathers.add("多云");

        //添加风向
        listWind.add("南风");
        listWind.add("东南风");
        listWind.add("南风");
        listWind.add("北风");
        listWind.add("北风");

        //添加风力等级
        listWindLevel.add("3~4级");
        listWindLevel.add("4~5级");
        listWindLevel.add("4~5级");
        listWindLevel.add("微风");
        listWindLevel.add("微风");

        //添加白天最高温
        listDayTemperature.add(27);
        listDayTemperature.add(26);
        listDayTemperature.add(27);
        listDayTemperature.add(20);
        listDayTemperature.add(21);

        //添加夜间最低温
        listNightTemperature.add(14);
        listNightTemperature.add(12);
        listNightTemperature.add(13);
        listNightTemperature.add(13);
        listNightTemperature.add(13);

        weatherView.setWeeks(listWeeks);
        weatherView.setDayWeathers(listDayWeathers);
        weatherView.setDayWeatherIcons(listDayIcons);
        weatherView.setDayTemperatures(listDayTemperature);
        weatherView.setNightTemperatures(listNightTemperature);
        weatherView.setNightWeatherIcons(listNightIcons);
        weatherView.setNightWeathers(listNightWeathers);
        weatherView.setDates(listDate);
        weatherView.setWind(listWind);
        weatherView.setWindLevel(listWindLevel);
        weatherView.apply();
    }
}
