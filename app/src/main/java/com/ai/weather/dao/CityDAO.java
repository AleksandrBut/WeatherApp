package com.ai.weather.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import com.ai.weather.database.City;

import java.util.List;

@Dao
public abstract class CityDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void addCity(City city);

    @Delete
    public abstract void deleteCity(City city);

    @Query("SELECT * FROM Cities")
    public abstract LiveData<List<City>> getAll();

    @Query("Select * FROM Cities WHERE isLastChosen = 1")
    public abstract City getLastChosenCity();

    @Query("SELECT lastModifiedTime FROM Cities WHERE cityId = :cityId")
    public abstract long getLastModifiedTime(long cityId);

    @Query("UPDATE cities SET forecast = :forecast WHERE cityId = :cityId")
    public abstract void updateCityForecast(long cityId, String forecast);

    @Query("UPDATE cities SET lastModifiedTime = :lastModifiedTime WHERE cityId = :cityId")
    public abstract void updateLastModifiedTime(long lastModifiedTime, long cityId);

    @Transaction
    public String getCityForecast(long cityId) {
        cancelLastChosen();
        assignNewLastChosen(cityId);
        return getForecastById(cityId);
    }

    @Query("SELECT forecast FROM Cities WHERE cityId = :cityId")
    public abstract String getForecastById(long cityId);

    @Query("UPDATE Cities SET isLastChosen = 0 WHERE isLastChosen = 1")
    public abstract void cancelLastChosen();

    @Query("UPDATE Cities SET isLastChosen = 1 WHERE cityId = :cityId")
    public abstract void assignNewLastChosen(long cityId);
}
