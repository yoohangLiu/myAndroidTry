package com.example.myaethelist.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.myaethelist.NetworkModel.LoginResult;
import com.example.myaethelist.NetworkModel.RegisterResult;
import com.example.myaethelist.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        final TextInputEditText userNameInput = findViewById(R.id.username_text_input);
        final TextInputEditText passwordInput = findViewById(R.id.password_edit_text);
        MaterialButton nextButton = findViewById(R.id.register_next_button);
        MaterialButton cancelButton = findViewById(R.id.register_cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                RegisterActivity.this.finish();
            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                requestResouces(userNameInput.getText().toString().trim(), passwordInput.getText().toString().trim());

            }
        });
    }

    private void requestResouces(final String username, final String password) {
        String url = "https://www.foodiesnotalone.cn/aetherlist/server.php?opcode=register&user_name="
                + username + "&passwd=" + password + "&device_number=null";
        System.out.println(username + "," + password);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.i("登录服务器失败：", "注意网络连接");
                System.out.println("登录服务器失败");
            }

            @Override
            public void onResponse(Response response) throws IOException {

                if (response.isSuccessful()) {
                    String json = response.body().string();
                    Log.i("RegisterActivity", "json=" + json);
                    Gson gson = new Gson();
                    RegisterResult result1 = gson.fromJson(json, RegisterResult.class);
                    if (result1.getResult().equals("success")) {
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        intent.putExtra("username", username);
                        intent.putExtra("password", password);
                        startActivity(intent);
                        RegisterActivity.this.finish();
                    } else {
                        Log.i("登录：", "失败");
                    }
                }
            }
        });
    }
}





