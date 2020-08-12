package com.mecitdeniz.petto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mecitdeniz.petto.Objects.Kullanici;

import java.nio.channels.InterruptedByTimeoutException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {


    TextView kaydol;
    TextView sifremiUnuttum;
    Button girisYap;
    EditText editText_email;
    EditText editText_sifre;
    SharedPreferences sharedpreferences;
    private long sonTiklanmaZamani = 0;
    private PetsApi petsApi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        kaydol = findViewById(R.id.tv_kaydol);

        girisYap = findViewById(R.id.btn_girisYap);

        editText_email = findViewById(R.id.editText_email);
        editText_sifre = findViewById(R.id.editText_sifre);
        sifremiUnuttum = findViewById(R.id.tv_sifremi_unuttum);
        sharedpreferences = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);

        sifremiUnuttum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - sonTiklanmaZamani < 1000){
                    return;
                }
                sonTiklanmaZamani = SystemClock.elapsedRealtime();
                Intent i = new Intent(LoginActivity.this,SifreYenileActivity.class);
                startActivity(i);
            }
        });

        kaydol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - sonTiklanmaZamani < 1000){
                    return;
                }
                sonTiklanmaZamani = SystemClock.elapsedRealtime();

                Intent kaydol = new Intent(LoginActivity.this,SignUpActivity.class);
                startActivity(kaydol);
            }
        });

        girisYap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (SystemClock.elapsedRealtime() - sonTiklanmaZamani < 1000){
                    return;
                }
                sonTiklanmaZamani = SystemClock.elapsedRealtime();

                Log.d("Login Activity","içerde");

                String email = editText_email.getText().toString();
                String sifre = editText_sifre.getText().toString();

                if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(sifre)){
                    girisYap(email,sifre);
                }else {
                    Toast.makeText(LoginActivity.this,"Lütfen tüm alanları doldurunuz",Toast.LENGTH_SHORT).show();
                }

            }
        });



    }
    private void girisYap(String email, String sifre){

        petsApi = PetsApiClient.getClient().create(PetsApi.class);

        Call<Kullanici> call = petsApi.girisYap(email,sifre);

        call.enqueue(new Callback<Kullanici>() {
            @Override
            public void onResponse(Call<Kullanici> call, Response<Kullanici> response) {
                Log.d("Login Activity","response : "+response.body());
                if (response.body() != null){
                    Toast.makeText(LoginActivity.this,"Hoşgeldiniz "+response.body().getKullaniciAdi(),Toast.LENGTH_LONG).show();

                    String id = String.valueOf(response.body().getId());
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("id", id);
                    editor.apply();
                    Intent girisYap = new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(girisYap);
                    finish();
                }else {
                    Toast.makeText(LoginActivity.this,"Email ya da şifreniz yanlış",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<Kullanici> call, Throwable t) {
                Toast.makeText(LoginActivity.this,"Email ya da şifreniz yanlış",Toast.LENGTH_SHORT).show();
                //Log.d("Login Activity","response : "+t.getMessage());
            }
        });
    }


}
