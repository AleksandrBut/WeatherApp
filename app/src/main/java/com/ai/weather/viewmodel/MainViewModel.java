package com.ai.weather.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.support.annotation.NonNull;

import com.ai.weather.CityForecast;
import com.ai.weather.database.AppRepository;
import com.ai.weather.database.City;

public class MainViewModel extends AndroidViewModel {

    private AppRepository repository;
    public MutableLiveData<CityForecast> cityForecast = new MutableLiveData<>();

    public MainViewModel(@NonNull Application application) {
        super(application);

        repository = AppRepository.getInstance(application.getApplicationContext());
    }

    public City getLastChosenCity() {
        return repository.getLastChosenCity();
    }

    public void getCityForecast(long cityId) {
        repository.getCityForecast(cityId, cityForecast);
    }

    public long getLastModifiedTime(long cityId) {
        return repository.getLastModifiedTime(cityId);
    }

    public void updateCityForecast(long cityId, Context context) {
        repository.getDataFromApi(cityId, cityForecast, context, true);
    }
}
