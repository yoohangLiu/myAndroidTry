package com.example.myaethelist.NetworkModel;


import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import java.io.IOException;

    /**
     * Created by 01457141 on 2017/8/8.
     */
    public class HttpUtils {
        private static OkHttpClient client = new OkHttpClient();
        public static String get(String url) throws IOException {
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            Response response = client.newCall(request).execute();
            String res=response.body().string();
            return res;
        }

    }

