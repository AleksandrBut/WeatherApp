package com.ai.weather.recyclerViewAdapters;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ai.weather.R;
import com.ai.weather.activity.ChangeCityActivity;
import com.ai.weather.activity.MainActivity;
import com.ai.weather.database.City;

import java.util.List;

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.CityViewHolder> {

    private List<City> cities;

    public CityAdapter(List<City> cities) {
        this.cities = cities;
    }

    @NonNull
    @Override

    public CityViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rv_cities_item, viewGroup, false);
        return new CityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CityViewHolder cityViewHolder, int i) {
        cityViewHolder.cityName.setText(cities.get(i).getName());
//        cityViewHolder.cityName.append(", " + cities.get(i).getCountry());
// TODO 16.06: Implement city.getCountry()

        ChangeCityActivity activity = (ChangeCityActivity) cityViewHolder.itemView.getContext();

        cityViewHolder.deleteCityButton.setOnClickListener(v -> {
            activity.deleteCity(cities.get(i));
        });

        cityViewHolder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(activity, MainActivity.class);
            intent.putExtra("cityId", cities.get(i).getCityId());
            activity.setResult(101, intent);
            activity.finish();
        });
    }

    @Override
    public int getItemCount() {
        return cities.size();
    }

    public static class CityViewHolder extends RecyclerView.ViewHolder {

        private TextView cityName;
        private ImageButton deleteCityButton;

        public CityViewHolder(@NonNull View itemView) {
            super(itemView);

            cityName = itemView.findViewById(R.id.cityName);
            deleteCityButton = itemView.findViewById(R.id.deleteCityButton);
        }
    }
}
