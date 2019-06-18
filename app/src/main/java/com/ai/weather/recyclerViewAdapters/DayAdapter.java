package com.ai.weather.recyclerViewAdapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ai.weather.R;
import com.ai.weather.CityForecast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class DayAdapter extends RecyclerView.Adapter<DayAdapter.DayViewHolder> {

    private CityForecast cityForecast;
    private List<String> mUniqueDays;
    private List<String> mAllDays;
    private HourAdapter.OnHourClickListener listener;

    public DayAdapter(CityForecast cityForecast, HourAdapter.OnHourClickListener listener) {
        this.cityForecast = cityForecast;
//        mAllDays = getAllDays(cityForecast);
        mUniqueDays = getmUniqueDays();
        this.listener = listener;
    }

    @NonNull
    @Override
    public DayAdapter.DayViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rv_days_item, viewGroup, false);
        return new DayViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DayAdapter.DayViewHolder viewHolder, int i) {


        String currentDay = mUniqueDays.get(i);

        viewHolder.day.setText(geFormattedDate(getPositionForHours(currentDay)));

        viewHolder.hourAdapter = new HourAdapter(cityForecast, getPositionForHours(currentDay), getHoursAmountForDay(currentDay), listener);
        viewHolder.recyclerViewHours.setAdapter(viewHolder.hourAdapter);
    }

    private String geFormattedDate(int i) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        try {
            Date d = dateFormat.parse(cityForecast.getList().get(i).getDtTxt().substring(0, 10));
            dateFormat.applyPattern("EEEE, dd MMMM");

            return dateFormat.format(d);
        } catch (ParseException e) {
            e.printStackTrace();

            return "";
        }
    }

    private int getPositionForHours(String day) {
        return getAllDays(cityForecast).indexOf(day);
    }

    private List<String> getmUniqueDays() {

        List<String> uniqueDays = getAllDays(cityForecast);

        Iterator<String> iterator = uniqueDays.iterator();
        while (iterator.hasNext()) {
            String s = iterator.next();
            if (uniqueDays.indexOf(s) != uniqueDays.lastIndexOf(s)) {
                iterator.remove();
            }
        }
        return uniqueDays;
    }

    private int getHoursAmountForDay(String day) {
        int hours = 0;

        for (String s : getAllDays(cityForecast)) {
            if (s.equals(day)) {
                hours += 1;
            }
        }
        return hours;
    }

    private List<String> getAllDays(CityForecast cityForecast) {
        List<String> allDays = new ArrayList<>();

        for (CityForecast.List forecastList : cityForecast.getList()) {
            allDays.add(forecastList.getDtTxt().substring(8, 10));
        }
        return allDays;
    }

    @Override
    public int getItemCount() {
        return mUniqueDays.size();
    }

    public static class DayViewHolder extends RecyclerView.ViewHolder {

        private RecyclerView recyclerViewHours;
        private TextView day;
        private HourAdapter hourAdapter;
        private LinearLayoutManager layoutManager = new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false);

        public DayViewHolder(@NonNull View itemView) {
            super(itemView);

            recyclerViewHours = itemView.findViewById(R.id.recyclerViewHours);
            recyclerViewHours.setLayoutManager(layoutManager);
            recyclerViewHours.setHasFixedSize(true);
            day = itemView.findViewById(R.id.day);
        }
    }
}
