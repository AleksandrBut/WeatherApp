package com.ai.weather.recyclerViewAdapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ai.weather.R;
import com.ai.weather.activity.AddCityActivity;
import com.ai.weather.database.CityInfo;

import java.util.List;

public class SearchCityAdapter extends RecyclerView.Adapter<SearchCityAdapter.SearchCityViewHolder> {

    private List<CityInfo> cities;

    public SearchCityAdapter(List<CityInfo> cities) {
        this.cities = cities;
    }

    @NonNull
    @Override
    public SearchCityViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rv_search_city_item, viewGroup, false);
        return new SearchCityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchCityViewHolder searchCityViewHolder, int i) {
        searchCityViewHolder.cityName.setText(cities.get(i).getName());
        searchCityViewHolder.cityName.append(", " + cities.get(i).getCountry());

        searchCityViewHolder.itemView.setOnClickListener(v -> {
            AddCityActivity activity = (AddCityActivity) searchCityViewHolder.itemView.getContext();
            activity.chooseCity(cities.get(i).getCityId());
        });
    }

    @Override
    public int getItemCount() {
        if (cities.size() > 15) {
            return 15;
        } else {
            return cities.size();
        }
    }

    public static class SearchCityViewHolder extends RecyclerView.ViewHolder {
        private TextView cityName;

        public SearchCityViewHolder(@NonNull View itemView) {
            super(itemView);

            cityName = itemView.findViewById(R.id.searchCityName);
        }
    }

}
