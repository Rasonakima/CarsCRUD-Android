package com.example.cars_crud;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class CarEditActivity extends AppCompatActivity {
    Car car;
    EditText editText;
    GifView gifView;
    Button editBtn, cancelBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_edit);

        editText = (EditText) findViewById(R.id.carName);
        gifView = (GifView) findViewById(R.id.giphyImage);

        getCar(getIntent().getLongExtra("CarID", 0));

        editBtn = (Button) findViewById(R.id.editBtn);
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editCar(getIntent().getLongExtra("CarID", 0));
            }
        });

        cancelBtn = (Button) findViewById(R.id.cancelBtn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void editCar(Long id) {
        car.setName(editText.getText().toString());
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(new Gson().toJson(car));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, MainActivity.carApi + "/" + id, jsonObject, new Response.Listener<JSONObject>() {
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
