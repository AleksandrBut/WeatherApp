package com.ai.weather.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ai.weather.CityForecast;
import com.ai.weather.R;
import com.ai.weather.database.City;
import com.ai.weather.recyclerViewAdapters.DayAdapter;
import com.ai.weather.recyclerViewAdapters.HourAdapter;
import com.ai.weather.viewmodel.MainViewModel;
import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity implements HourAdapter.OnHourClickListener {

    private MainViewModel mViewModel;
    private ImageView bgImage;
    private ImageView weatherIcon;
    private TextView cityName;
    private TextView temperature;
    private TextView weatherDesc;
    private TextView minTemp;
    private TextView maxTemp;
    private TextView humidity;
    private TextView pressure;
    private TextView windSpeed;
    private TextView cloudiness;
    private RecyclerView recyclerViewDays;

    private CityForecast mCityForecast;
    private long onBackCLickTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViewModel();
        initActivity();

//        deleteDatabase("appdb.db");

        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewDays.setHasFixedSize(true);
        recyclerViewDays.setLayoutManager(manager);

        findViewById(R.id.changeCityButton).setOnClickListener(v -> {
            openChangeCityActivity(true);
        });
    }

    private void openChangeCityActivity(Boolean isOpenedByUser) {
        Intent intent = new Intent(this, ChangeCityActivity.class);
        intent.putExtra("is_opened_by_user", isOpenedByUser);
        startActivityForResult(intent, 101);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 101 && resultCode != RESULT_CANCELED) {
            long cityId = data.getLongExtra("cityId", 0);
            if (cityName.getTag() != null) {
                if (Long.valueOf(cityName.getTag().toString()) != cityId) {
                    new Thread(() -> {
                        if (hasOutdatedForecast(cityId)) {
                            mViewModel.updateCityForecast(cityId, this);
                        } else {
                            mViewModel.getCityForecast(cityId);
                        }
                    }).start();
                }
            } else {
                new Thread(() -> {
                    mViewModel.getCityForecast(cityId);
                }).start();
            }
        }
    }

    private void initViewModel() {
        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        mViewModel.cityForecast.observe(this, cityForecast -> {
            if (cityForecast != null) {
                mCityForecast = cityForecast;
                showForecast();
            }
        });
    }

    private void initActivity() {

        bgImage = findViewById(R.id.bgImage);
        cityName = findViewById(R.id.city);
        weatherIcon = findViewById(R.id.weatherIcon);
        temperature = findViewById(R.id.temperature);
        weatherDesc = findViewById(R.id.desc);
        minTemp = findViewById(R.id.tempMin);
        maxTemp = findViewById(R.id.tempMax);
        humidity = findViewById(R.id.humidity);
        pressure = findViewById(R.id.pressure);
        windSpeed = findViewById(R.id.windSpeed);
        cloudiness = findViewById(R.id.cloudiness);
        recyclerViewDays = findViewById(R.id.recyclerViewDays);

        Gson gson = new Gson();

        new Thread(() -> {
            City lastChosenCity = mViewModel.getLastChosenCity();
            if (lastChosenCity != null) {
                if (hasOutdatedForecast(lastChosenCity.getCityId())) {
                    mViewModel.updateCityForecast(lastChosenCity.getCityId(), this);
                } else {
                    mCityForecast = gson.fromJson(lastChosenCity.getForecast(), CityForecast.class);
                    runOnUiThread(this::showForecast);
                }
            } else {
                openChangeCityActivity(false);
            }
        }).start();

    }

    private void showForecast() {
        if (mCityForecast != null) {

            recyclerViewDays.setAdapter(new DayAdapter(mCityForecast, this));

            CityForecast.List currentWeather = mCityForecast.getList().get(0);

            cityName.setText(mCityForecast.getCity().getName());
            cityName.setTag(mCityForecast.getCity().getId());
            weatherDesc.setText(currentWeather.getWeather().get(0).getDescription());

            weatherIcon.setImageURI(Uri.parse("android.resource://com.ai.weather/mipmap/icon"
                + currentWeather.getWeather().get(0).getIcon()));

            temperature.setText(convertDoubleToString(currentWeather.getMain().getTemp()));
            temperature.append("\u00B0");

            fillDataTable(0);
        }
    }

    private boolean hasOutdatedForecast(long cityId) {
        return System.currentTimeMillis() - mViewModel.getLastModifiedTime(cityId) > 600000; // 10 minutes
    }

    public static String convertDoubleToString(double doubleValue) {
        return String.valueOf(Math.round(doubleValue));
    }

    @Override
    public void onHourClick(int position) {
        fillDataTable(position);
    }

    public void fillDataTable(int position) {
        CityForecast.List weatherForecastByPosition = mCityForecast.getList().get(position);

        String uri = "android.resource://com.ai.weather/mipmap/bg" + weatherForecastByPosition.getWeather().get(0).getIcon();

        bgImage.setImageURI(Uri.parse(uri));

        minTemp.setText(convertDoubleToString(weatherForecastByPosition.getMain().getTempMin()));
        minTemp.append("\u00B0");

        maxTemp.setText(convertDoubleToString(weatherForecastByPosition.getMain().getTempMax()));
        maxTemp.append("\u00B0");

        humidity.setText(convertDoubleToString(weatherForecastByPosition.getMain().getHumidity()));
        humidity.append(" %");

        pressure.setText(convertDoubleToString(weatherForecastByPosition.getMain().getPressure()));
        pressure.append(" гПа");

        windSpeed.setText(String.valueOf(weatherForecastByPosition.getWind().getSpeed()));
        windSpeed.append(" м/с");

        cloudiness.setText(String.valueOf(weatherForecastByPosition.getClouds().getAll()));
        cloudiness.append(" %");
    }

    @Override
    public void onBackPressed() {
        if (onBackCLickTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
        } else {
            onBackCLickTime = System.currentTimeMillis();
            Toast.makeText(this, "Нажмите ещё раз для выхода", Toast.LENGTH_SHORT).show();
        }
    }
}
