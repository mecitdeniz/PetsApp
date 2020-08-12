package com.mecitdeniz.petto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.mecitdeniz.petto.Objects.Kullanici;

public class SplashActivity extends AppCompatActivity {

    boolean isUser = false;
    private int kullaniciID;
    SharedPreferences sharedpreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splahs);


        //kullanıcı id'sini kaydetmek için
        sharedpreferences = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedpreferences.edit();
        String value = sharedpreferences.getString("id","0");
        kullaniciID = Integer.parseInt(value);
        if (kullaniciID != 0){ //eğer giriş yapılmamışsa id 0 olarak dönüyor
            isUser=true;
        }
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!isUser) { //id 0 ise kullanıcı giriş yap ekranına yönlendiriliyo
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    //kayıtlı id varsa kullanıcı anasayfaya yönlendiriliyor
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, 500);

    }
}
