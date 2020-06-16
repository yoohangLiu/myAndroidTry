package model;

import android.provider.ContactsContract;
import android.util.Log;

import com.example.myaethelist.NetworkModel.Data;
import com.google.gson.Gson;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class GetURLConnection {
    public GetURLConnection() {

    }

    private void requestResouces(String url){
        /*String url = "http://112.124.22.238:8081/course_api/banner/query?type=1";*/
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.isSuccessful()) {
                    String json = response.body().string();
                    Log.i("HomeFragment", "json=" + json);

                    Gson gson = new Gson();
                    Data data = gson.fromJson(json, Data.class);
                }
            }
        });


    }

}
