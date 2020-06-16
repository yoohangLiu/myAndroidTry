package com.example.myaethelist.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.myaethelist.NetworkModel.LoginResult;
import com.example.myaethelist.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.List;

import model.Category;
import model.GetURLConnection;

public class LoginActivity extends AppCompatActivity {
    TextView ToRegister;
    /*private  String result;
    private String session;*/
   /* String username;
    String password;*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        final TextInputEditText userNameInput=findViewById(R.id.login_username_text_input);
        final TextInputEditText passwordInput=findViewById(R.id.login_password_text_input);
        MaterialButton nextButton=findViewById(R.id.next_button);
        MaterialButton cancelButton=findViewById(R.id.cancel_button);
        ToRegister=findViewById(R.id.xinyonghuzhuce);
        ToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),RegisterActivity.class);
                startActivity(intent);
                LoginActivity.this.finish();
            }
        });
        /*final TextInputLayout passwordTextInput =findViewById(R.id.password_text_input_layout);
        final TextInputLayout userNameTextInput =findViewById(R.id.username_text_input_layout);*/
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),ShowCategory.class);
                startActivity(intent);
                LoginActivity.this.finish();
            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* String LoginPath="https://www.foodiesnotalone.cn/aetherlist/server.php?opcode=login&user_name="+username
                        +"&passwd="+password+"&device_number=null";*/
                requestResouces(userNameInput.getText().toString().trim(),passwordInput.getText().toString().trim());
                /*System.out.println(result);
                if(result.equals("success")&&session!=null){
                   String GetDataPath="https://www.foodiesnotalone.cn/aetherlist/server.php?opcode=getCategory&session="+session;
                    Intent intent = new Intent(getApplicationContext(), ShowCategory.class);
                    intent.putExtra("getCategoryURL",GetDataPath);
                    intent.putExtra("session",session);
                    startActivity(intent);
                    LoginActivity.this.finish();
                }else {

                }*/


            }
        });
         Intent intent=this.getIntent();
         String regieterUsername=intent.getStringExtra("username");
        String regieterPassword=intent.getStringExtra("password");
         if (intent.getStringExtra("username")!=null&&intent.getStringExtra("password")!=null){
                   userNameInput.setText(regieterUsername);
                   passwordInput.setText(regieterPassword);
         }
    }
    private void requestResouces(String username,String password){
       String url="https://www.foodiesnotalone.cn/aetherlist/server.php?opcode=login&user_name="
               +username +"&passwd="+password+"&device_number=null";
        System.out.println(username+","+password);
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
                    Log.i("LoginActivity", "json=" + json);
                    Gson gson = new Gson();
                    LoginResult loginResult = gson.fromJson(json, LoginResult.class);
                    if (loginResult.getResult().trim().equals("success")){
                        String result=loginResult.getResult().trim();
                        String session=loginResult.getSession().trim();
                        System.out.println(result);
                        String GetDataPath="https://www.foodiesnotalone.cn/aetherlist/server.php?opcode=getCategory&session="+session;
                        Intent intent = new Intent(getApplicationContext(), ShowCategory.class);
                        intent.putExtra("getCategoryURL",GetDataPath);
                        intent.putExtra("session",session);
                        startActivity(intent);
                        LoginActivity.this.finish();
                    }else {
                        Log.i("登录：", "失败");
                    }
                }
            }
        });


    }
    private boolean isPasswordValid(@Nullable Editable text) {
        return text != null && text.length() >= 8;
    }

}
/* if (!isPasswordValid(passwordInput.getText())) {
                    passwordTextInput.setError(getString(R.string.shr_error_password));
                } else {
                    passwordTextInput.setError(null); // Clear the error
                    //((NavigationHost) getActivity()).navigateTo(new ProductGridFragment(), false);
                }*/


       /* // Clear the error once more than 8 characters are typed.
        passwordInput.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (isPasswordValid(passwordInput.getText())) {
                    passwordTextInput.setError(null); //Clear the error
                }
                return false;
            }
        });*/
        /* ChipGroup a=findViewById(R.id.tagList);
        Chip a1=new Chip(getApplicationContext());
        a.addView(a1);*/