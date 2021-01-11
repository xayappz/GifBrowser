package com.xay.gifbrowser.ui.home;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.xay.gifbrowser.R;
import com.xay.gifbrowser.utils.Images;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.xay.gifbrowser.utils.Apis.ImageApi;

public class ImagesRepositoy {
    private RequestQueue queue;
    private StringRequest request;
    private static Context mCtx;
    private static ImagesRepositoy instance;
    private ArrayList<Images> arrayList = new ArrayList<>();
    private MutableLiveData<ArrayList<Images>> url;
    ArrayList<Images> dataset;


    private MutableLiveData<ArrayList<Images>> data;

    public ImagesRepositoy(Context context) {

        mCtx = context;
    }

    public ImagesRepositoy() {
    }


    public static ImagesRepositoy getInstance() {
        if (instance == null) {
            instance = new ImagesRepositoy();
        }
        return instance;
    }

    public MutableLiveData<ArrayList<Images>> getIMAGES() {
        getCallApi();
        MutableLiveData<ArrayList<Images>> MYDATA = new MutableLiveData<>();
        MYDATA.setValue(dataset);
        return data;

    }


    public void getCallApi() {
        dataset = new ArrayList<>();
        data = new MutableLiveData<>();
        request = new StringRequest(Request.Method.GET, ImageApi + mCtx.getString(R.string.Apikey) + "&limit=25", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("responsefromserv", response + ".");
                try {


                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject images, original, jsonObject3;
                    JSONArray jsonArray = jsonObject.getJSONArray("data");


                    for (int i = 0; i < jsonArray.length(); i++) {
                        Images Imgs = new Images();
                        images = jsonArray.getJSONObject(i);
                        original = images.getJSONObject("images");
                        jsonObject3 = original.getJSONObject("fixed_height_downsampled");
                        Imgs.setUrl(jsonObject3.getString("url"));
                        dataset.add(new Images(Imgs.getUrl()));
                        data.setValue(dataset);

                    }
                    Log.e("datavalue", data.getValue().size() + "");


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error", error.toString());
            }
        });
        queue = Volley.newRequestQueue(mCtx);
        queue.add(request);
    }
}


