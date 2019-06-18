package com.ai.weather.retrofit;

import com.ai.weather.CityForecast;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ForecastClient {

    @GET("data/2.5/forecast?units=metric&lang=ru")
    Call<CityForecast> getForecast(@Query("id") long cityId, @Query("appid") String appId);
}
