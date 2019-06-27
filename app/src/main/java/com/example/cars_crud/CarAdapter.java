package com.example.cars_crud;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

public class CarAdapter extends RecyclerView.Adapter<CarAdapter.CarViewHolder> {
    Context context;
    private List<Car> cars;

    public class CarViewHolder extends RecyclerView.ViewHolder {
        public TextView description;
        public NetworkImageView giphyImage;

        public CarViewHolder(View view) {
            super(view);
            description = (TextView) view.findViewById(R.id.carName);
            giphyImage = (NetworkImageView) view.findViewById(R.id.giphyImage);
        }
    }

    public void add(int position, Car car) {
        cars.add(position, car);
        notifyItemInserted(position);
    }

    public void remove(Car car) {
        int position = cars.indexOf(car);
        cars.remove(position);
        notifyItemRemoved(position);
    }

    public CarAdapter(List<Car> cars) {
        this.cars = cars;
    }

    @NonNull
    @Override
    public CarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_car, parent, false);
        CarViewHolder viewHolder = new CarViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CarViewHolder holder, int position) {
        holder.description.setText(cars.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return cars.size();
    }
}
