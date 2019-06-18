package com.ai.weather.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.ai.weather.database.AppRepository;
import com.ai.weather.database.City;

import java.util.List;

public class ChangeCityViewModel extends AndroidViewModel {

    public LiveData<List<City>> cities;
    private AppRepository repository;

    public ChangeCityViewModel(@NonNull Application application) {
        super(application);

        repository = AppRepository.getInstance(application.getApplicationContext());
        cities = repository.cities;
    }

    public void deleteCity(City city) {
        repository.deleteCity(city);
    }
}
