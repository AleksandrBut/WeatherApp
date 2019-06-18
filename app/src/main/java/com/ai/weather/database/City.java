package com.ai.weather.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "Cities")
public class City {

    @PrimaryKey(autoGenerate = true)
    private long id;
    private long cityId;
    private String name;
    private String forecast;
    private long lastModifiedTime;
    private boolean isLastChosen;

    public City(long id, long cityId, String name, String forecast, long lastModifiedTime, boolean isLastChosen) {
        this.id = id;
        this.cityId = cityId;
        this.name = name;
        this.forecast = forecast;
        this.lastModifiedTime = lastModifiedTime;
        this.isLastChosen = isLastChosen;
    }

    @Ignore
    public City() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getForecast() {
        return forecast;
    }

    public void setForecast(String forecast) {
        this.forecast = forecast;
    }

    public long getLastModifiedTime() {
        return lastModifiedTime;
    }

    public void setLastModifiedTime(long lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }

    public boolean getIsLastChosen() {
        return isLastChosen;
    }

    public void setIsLastChosen(boolean lastChosen) {
        isLastChosen = lastChosen;
    }

    public long getCityId() {
        return cityId;
    }

    public void setCityId(long cityId) {
        this.cityId = cityId;
    }
}
