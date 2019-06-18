package com.ai.weather.database;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.ai.weather.CityForecast;
import com.ai.weather.retrofit.ForecastClient;
import com.google.gson.Gson;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class AppRepository {

    private static final String BASE_URL = "https://api.openweathermap.org/";
    private static final String APP_ID = "6fd8b27b8ac6d588eb0dfb6fd559b239";

    private static AppRepository instance;
    public LiveData<List<City>> cities;
    private AppDatabase database;
    private Executor executor = Executors.newSingleThreadExecutor();
    private ForecastClient client;
    private Gson gson = new Gson();

    public static AppRepository getInstance(Context context) {
        if (instance == null) {
            instance = new AppRepository(context);
        }
        return instance;
    }

    private AppRepository(Context context) {
        database = AppDatabase.getInstance(context);
        cities = getAllCities();

        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

        client = retrofit.create(ForecastClient.class);
    }

    public void getDataFromApi(long cityId, MutableLiveData<CityForecast> liveData, Context context, boolean isUpdate) {

        Call<CityForecast> call = client.getForecast(cityId, APP_ID);
        call.enqueue(new Callback<CityForecast>() {
            @Override
            public void onResponse(Call<CityForecast> call, Response<CityForecast> response) {
                if (response.body() != null && response.isSuccessful()) {
                    if (liveData != null) {
                        liveData.postValue(response.body());
                    }

                    if (isUpdate) {
                        executor.execute(() -> {
                            database.cityDAO().updateLastModifiedTime(System.currentTimeMillis(), cityId);
                            database.cityDAO().updateCityForecast(cityId, gson.toJson(response.body()));
                        });
                    } else {
                        City city = new City();

                        city.setName(response.body().getCity().getName());
                        city.setCityId(response.body().getCity().getId());
                        city.setForecast(gson.toJson(response.body()));
                        city.setLastModifiedTime(System.currentTimeMillis());
                        if (city.getCityId() == 707860) {
                            city.setIsLastChosen(true);
                        } else {
                            city.setIsLastChosen(false);
                        }

                        executor.execute(() -> {
                            database.cityDAO().addCity(city);
                        });
                    }
                } else if (response.code() == 404) {
                    Toast.makeText(context, "Населённый пункт не найден", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<CityForecast> call, Throwable t) {
                Log.d(" -----> RETROFIT CONNECTION TO SERVER FAILED <-----", t.getMessage());
            }
        });
    }

    public void deleteCity(City city) {
        executor.execute(() -> {
            database.cityDAO().deleteCity(city);
        });
    }

    private LiveData<List<City>> getAllCities() {
        return database.cityDAO().getAll();
    }

    public City getLastChosenCity() {
        return database.cityDAO().getLastChosenCity();
    }

//    private boolean isConnectedToInternet() {
//        try {
//            int timeout = 1500;
//            Socket socket = new Socket();
//            SocketAddress address = new InetSocketAddress("8.8.8.8", 53);
//            socket.connect(address, timeout);
//            socket.close();
//
//            return true;
//        } catch (IOException e) {
//            e.printStackTrace();
//            return false;
//        }
//    }

    public void getCityForecast(long cityId, MutableLiveData<CityForecast> liveData) {
        liveData.postValue(gson.fromJson(database.cityDAO().getCityForecast(cityId), CityForecast.class));
    }

    public long getLastModifiedTime(long cityId) {
        return database.cityDAO().getLastModifiedTime(cityId);
    }

    public void makeCityLastChosen(long cityId) {
        executor.execute(() -> {
            database.cityDAO().assignNewLastChosen(cityId);
        });
    }

    public void displayCitiesLike(String q, MutableLiveData<List<CityInfo>> liveData) {
        executor.execute(() -> {
            liveData.postValue(database.cityInfoDAO().getCitiesLike("%" + q + "%"));
        });
    }
}
