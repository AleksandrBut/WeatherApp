package com.ai.weather.recyclerViewAdapters;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ai.weather.R;
import com.ai.weather.activity.MainActivity;
import com.ai.weather.CityForecast;

import java.util.List;

public class HourAdapter extends RecyclerView.Adapter<HourAdapter.HourViewHolder> {

    private int pos;
    private int numberOfHours;
    private OnHourClickListener mListener;
    private List<CityForecast.List> currentForecastList;
    protected int startPosition;

    public HourAdapter(CityForecast cityForecast, int pos, int numberOfHours, OnHourClickListener listener) {
        currentForecastList = cityForecast.getList();
        this.pos = pos;
        startPosition = pos;
        this.numberOfHours = numberOfHours;
        this.mListener = listener;

    }

    @NonNull
    @Override
    public HourViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rv_hours_item, viewGroup, false);
        return new HourViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull HourViewHolder hourViewHolder, int i) {

        CityForecast.List currentForecast = currentForecastList.get(pos);

        hourViewHolder.itemView.setOnClickListener(v -> {
            hourViewHolder.listener.onHourClick(startPosition + i);
//            hourViewHolder.itemView.setBackgroundColor(ContextCompat.getColor(hourViewHolder.itemView.getContext(), R.color.colorDivider));
        });

        hourViewHolder.hour.setText(currentForecast.getDtTxt().substring(11, 16));

        hourViewHolder.temp.setText(MainActivity.convertDoubleToString(currentForecast.getMain().getTemp()));
        hourViewHolder.temp.append("\u00B0");


        hourViewHolder.icon.setImageURI(Uri.parse("android.resource://com.ai.weather/mipmap/icon"
                + currentForecast.getWeather().get(0).getIcon()));

        pos += 1;
    }

    @Override
    public int getItemCount() {
        return numberOfHours;
    }

    public static class HourViewHolder extends RecyclerView.ViewHolder {

        private TextView hour;
        private ImageView icon;
        private TextView temp;
        private OnHourClickListener listener;

        public HourViewHolder(@NonNull View itemView, OnHourClickListener listener) {
            super(itemView);

            hour = itemView.findViewById(R.id.hour);
            icon = itemView.findViewById(R.id.hourIcon);
            temp = itemView.findViewById(R.id.hourTemp);
            this.listener = listener;
        }
    }

    public interface OnHourClickListener {
        void onHourClick(int position);
    }
}
