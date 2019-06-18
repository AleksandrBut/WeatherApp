package com.ai.weather.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.ai.weather.database.CityInfo;

import java.util.List;

@Dao
public interface CityInfoDAO {

    @Query("SELECT * FROM AllCities WHERE name LIKE :q")
    List<CityInfo> getCitiesLike(String q);
}
