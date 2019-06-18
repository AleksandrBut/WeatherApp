package com.ai.weather.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ai.weather.R;
import com.ai.weather.database.CityInfo;
import com.ai.weather.recyclerViewAdapters.SearchCityAdapter;
import com.ai.weather.viewmodel.AddCityViewModel;

import java.util.ArrayList;
import java.util.List;

public class AddCityActivity extends AppCompatActivity {

    private AddCityViewModel mViewModel;
    private EditText newCityName;
    private ImageButton clearTextButton;
    private TextView noResultText;
    private RecyclerView rvCitiesWithSimilarName;
    private List<CityInfo> cityInfoList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_city);

        newCityName = findViewById(R.id.newCityName);
        clearTextButton = findViewById(R.id.clearTextButton);
        noResultText = findViewById(R.id.noResultText);
        rvCitiesWithSimilarName = findViewById(R.id.rvCitiesWithSimilarName);

        newCityName.requestFocus();
        initViewModel();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvCitiesWithSimilarName.setLayoutManager(layoutManager);

//        findViewById(R.id.addCity).setOnClickListener(v -> {
//            String cityName = newCityName.getText().toString();
//
//            if (!cityName.isEmpty()) {
//                mViewModel.addCity(cityName.trim(), this);
//                finish();
//            } else {
//                Toast.makeText(this, "Введите имя города", Toast.LENGTH_SHORT).show();
//            }
//        });

        clearTextButton.setOnClickListener(v -> {
            newCityName.getText().clear();
        });


        newCityName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(newCityName.getText().toString())) {
                    clearTextButton.setVisibility(View.VISIBLE);

                    new Thread(() -> {
                        String lastEnteredText = s.toString();
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        if (s.toString().equals(lastEnteredText)) {
                            mViewModel.getSimilarCities(s.toString());
                        }
                    }).start();

                } else {
                    clearTextButton.setVisibility(View.GONE);
                    rvCitiesWithSimilarName.setVisibility(View.GONE);

                    if (noResultText.getVisibility() != View.GONE) {
                        noResultText.setVisibility(View.GONE);
                    }
                }
            }
        });
    }

    private void initViewModel() {
        mViewModel = ViewModelProviders.of(this).get(AddCityViewModel.class);

        mViewModel.cityInfoLiveData.observe(this, cityInfoList -> {
            this.cityInfoList = cityInfoList;
            showSimilarCities();
        });
    }

    private void showSimilarCities() {
        if (cityInfoList.size() == 0) {
            if (rvCitiesWithSimilarName.getVisibility() != View.GONE) {
                rvCitiesWithSimilarName.setVisibility(View.GONE);
            }
            if (noResultText.getVisibility() != View.VISIBLE) {
                noResultText.setVisibility(View.VISIBLE);
            }
        } else {
            rvCitiesWithSimilarName.setAdapter(new SearchCityAdapter(cityInfoList));
            if (rvCitiesWithSimilarName.getVisibility() == View.GONE) {
                rvCitiesWithSimilarName.setVisibility(View.VISIBLE);
            }
            if (noResultText.getVisibility() != View.GONE) {
                noResultText.setVisibility(View.GONE);
            }
        }
    }

    public void chooseCity(long cityId) {
        mViewModel.addCity(cityId, this);
        finish();
    }
}
