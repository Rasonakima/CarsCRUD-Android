package com.example.cars_crud;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.giphy.sdk.core.GiphyCore;
import com.giphy.sdk.core.models.Media;
import com.giphy.sdk.core.models.enums.RenditionType;
import com.giphy.sdk.core.network.api.CompletionHandler;
import com.giphy.sdk.core.network.response.ListMediaResponse;
import com.giphy.sdk.core.network.response.MediaResponse;
import com.giphy.sdk.ui.GiphyCoreUI;
import com.giphy.sdk.ui.views.GifView;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CarAdapter extends RecyclerView.Adapter<CarAdapter.CarViewHolder> {
    Context context;
    private List<Car> cars;
    public static final String giphyapi = "https://media.giphy.com/media/YaOxRsmrv9IeA/giphy.gif";

    public class CarViewHolder extends RecyclerView.ViewHolder {
        public TextView description;
        public GifView giphyImage;

        public CarViewHolder(View view) {
            super(view);
            description = (TextView) view.findViewById(R.id.carName);
            giphyImage = (GifView) view.findViewById(R.id.giphyImage);
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
    public void onBindViewHolder(@NonNull final CarViewHolder holder, int position) {
        holder.description.setText(cars.get(position).getName());
        GiphyCore.apiClient.search(cars.get(position).getName(), null, 1, null, null, null, null, new CompletionHandler<ListMediaResponse>() {
            @Override
            public void onComplete(@Nullable ListMediaResponse listMediaResponse, @Nullable Throwable throwable) {
                List<Media> mediaList = listMediaResponse.getData();
                holder.giphyImage.setMedia(mediaList.get(0), RenditionType.original, Color.WHITE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cars.size();
    }
}
