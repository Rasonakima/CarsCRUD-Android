package com.example.cars_crud;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
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

import java.io.UnsupportedEncodingException;
import java.util.List;

public class CarEditActivity extends AppCompatActivity {
    Car car;
    EditText editText;
    GifView gifView;
    Button editBtn,deleteBtn, cancelBtn;

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

        deleteBtn = (Button) findViewById(R.id.deleteBtn);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteCar(getIntent().getLongExtra("CarID", 0));
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

    private void deleteCar(Long id){
        BooleanRequest booleanRequest = new BooleanRequest(Request.Method.DELETE, MainActivity.carApi + "/" + id, null, new Response.Listener<Boolean>() {
            @Override
            public void onResponse(Boolean response) {
                Toast.makeText(CarEditActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(CarEditActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        VolleySingleton.getInstance(this).addToRequestQueue(booleanRequest);
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

    private class BooleanRequest extends Request<Boolean> {
        private final Response.Listener<Boolean> mListener;
        private final Response.ErrorListener mErrorListener;
        private final String mRequestBody;

        private final String PROTOCOL_CHARSET = "utf-8";
        private final String PROTOCOL_CONTENT_TYPE = String.format("application/json; charset=%s", PROTOCOL_CHARSET);

        public BooleanRequest(int method, String url, String requestBody, Response.Listener<Boolean> listener, Response.ErrorListener errorListener) {
            super(method, url, errorListener);
            this.mListener = listener;
            this.mErrorListener = errorListener;
            this.mRequestBody = requestBody;
        }

        @Override
        protected Response<Boolean> parseNetworkResponse(NetworkResponse response) {
            Boolean parsed;
            try {
                parsed = Boolean.valueOf(new String(response.data, HttpHeaderParser.parseCharset(response.headers)));
            } catch (UnsupportedEncodingException e) {
                parsed = Boolean.valueOf(new String(response.data));
            }
            return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
        }

        @Override
        protected VolleyError parseNetworkError(VolleyError volleyError) {
            return super.parseNetworkError(volleyError);
        }

        @Override
        protected void deliverResponse(Boolean response) {
            mListener.onResponse(response);
        }

        @Override
        public void deliverError(VolleyError error) {
            mErrorListener.onErrorResponse(error);
        }

        @Override
        public String getBodyContentType() {
            return PROTOCOL_CONTENT_TYPE;
        }

        @Override
        public byte[] getBody() throws AuthFailureError {
            try {
                return mRequestBody == null ? null : mRequestBody.getBytes(PROTOCOL_CHARSET);
            } catch (UnsupportedEncodingException uee) {
                VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                        mRequestBody, PROTOCOL_CHARSET);
                return null;
            }
        }
    }
}
