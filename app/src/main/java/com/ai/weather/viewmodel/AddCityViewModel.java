package com.ai.weather.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.support.annotation.NonNull;

import com.ai.weather.database.AppRepository;
import com.ai.weather.database.City;
import com.ai.weather.database.CityInfo;

import java.util.List;

public class AddCityViewModel extends AndroidViewModel {

    public MutableLiveData<City> city = new MutableLiveData<>();
    private AppRepository repository;
    public MutableLiveData<List<CityInfo>> cityInfoLiveData = new MutableLiveData<>();

    public AddCityViewModel(@NonNull Application application) {
        super(application);

        repository = AppRepository.getInstance(application.getApplicationContext());
    }

    public void addCity(long id, Context context) {
        repository.getDataFromApi(id, null, context, false);
    }

    public void getSimilarCities(String q) {
        repository.displayCitiesLike(q, cityInfoLiveData);
    }
}
