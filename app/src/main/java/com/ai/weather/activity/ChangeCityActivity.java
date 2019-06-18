package com.ai.weather.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ai.weather.R;
import com.ai.weather.database.City;
import com.ai.weather.recyclerViewAdapters.CityAdapter;
import com.ai.weather.viewmodel.ChangeCityViewModel;

import java.util.ArrayList;
import java.util.List;

public class ChangeCityActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<City> mCities = new ArrayList<>();
    private ChangeCityViewModel mViewModel;
    private CityAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_city);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);

        initViewModel();

        findViewById(R.id.addCityButton).setOnClickListener(v -> {
            openAddCityActivity();
        });
    }

    private void openAddCityActivity() {
        Intent intent = new Intent(this, AddCityActivity.class);
        startActivity(intent);
    }

    public void deleteCity(City city) {
        mViewModel.deleteCity(city);
    }

    private void initViewModel() {
        final Observer<List<City>> observer = cities -> {
            Intent intent = getIntent();
            if (cities.isEmpty() && !intent.getBooleanExtra("is_opened_by_user", false)) {
                intent.putExtra("is_opened_by_user", true);
                openAddCityActivity();
            } else {
                mCities.clear();
                mCities.addAll(cities);

                if (adapter == null) {
                    adapter = new CityAdapter(mCities);
                    recyclerView.setAdapter(adapter);
                } else {
                    adapter.notifyDataSetChanged();
                }
            }
        };
        mViewModel = ViewModelProviders.of(this).get(ChangeCityViewModel.class);
        mViewModel.cities.observe(this, observer);
    }
}
