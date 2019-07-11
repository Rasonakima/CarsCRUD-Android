package com.example.cars_crud;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.giphy.sdk.core.GiphyCore;
import com.giphy.sdk.core.models.Media;
import com.giphy.sdk.core.models.enums.RenditionType;
import com.giphy.sdk.core.network.api.CompletionHandler;
import com.giphy.sdk.core.network.response.ListMediaResponse;
import com.giphy.sdk.ui.views.GifView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.util.List;

public class CarEditActivity extends AppCompatActivity {
    Car car;
    EditText editText;
    GifView gifView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_edit);

        editText = (EditText) findViewById(R.id.carName);
        gifView = (GifView) findViewById(R.id.giphyImage);

        getCar(getIntent().getLongExtra("CarID", 0));

    }

    private void getCar(Long id) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, MainActivity.carApi + "/" + id, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                car = (Car) new Gson().fromJson(String.valueOf(response), new TypeToken<Car>() {
                }.getType());
                editText.setText(car.getName());
                GiphyCore.apiClient.search(car.getName(), null, 1, null, null, null, null, new CompletionHandler<ListMediaResponse>() {
                    @Override
                    public void onComplete(@Nullable ListMediaResponse listMediaResponse, @Nullable Throwable throwable) {
                        List<Media> mediaList = listMediaResponse.getData();
                        gifView.setMedia(mediaList.get(0), RenditionType.original, Color.WHITE);
                    }
                });

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(CarEditActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }
}
