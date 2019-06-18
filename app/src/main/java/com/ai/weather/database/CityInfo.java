package com.ai.weather.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "AllCities")
public class CityInfo {

    @PrimaryKey(autoGenerate = true)
    private long id;
    private long cityId;
    private String name;
    private String country;
    private double latitude;
    private double longitude;

    public CityInfo(long id, long cityId, String name, String country, double latitude, double longitude) {
        this.id = id;
        this.cityId = cityId;
        this.name = name;
        this.country = country;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Ignore
    public CityInfo() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCityId() {
        return cityId;
    }

    public void setCityId(long cityId) {
        this.cityId = cityId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void clear() {
        cityId = 0;
        name = null;
        country = null;
        longitude = 0;
        latitude = 0;
    }
}
