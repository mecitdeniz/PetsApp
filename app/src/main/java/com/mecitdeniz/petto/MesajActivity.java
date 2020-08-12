package com.mecitdeniz.petto;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.mecitdeniz.petto.Objects.Mesaj;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MesajActivity extends AppCompatActivity {

    RecyclerView mesajRecyclerView;
    EditText editTextMesaj;
    ImageButton btnMesajGonder;
    private int basvuruId;
    List<Mesaj> mesajlar = new ArrayList<>();
    private int kullaniciID;
    private int basvuranID;
    private int ilanSahipID;
    private PetsApi petsApi;
    Handler handler = new Handler();
    Runnable runnable;
    Mesaj mesaj = new Mesaj();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mesaj);
        SharedPreferences sharedpreferences = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        String value = sharedpreferences.getString("id","0");
        kullaniciID = Integer.parseInt(value);
        basvuruId = getIntent().getIntExtra("basvuruId",-1);
        basvuranID = getIntent().getIntExtra("basvuranID",-1);
        ilanSahipID = getIntent().getIntExtra("ilanSahipID",-1);

        mesajRecyclerView = findViewById(R.id.mesajlar_recyclerView);
        mesajRecyclerView.setLayoutManager(new LinearLayoutManager(MesajActivity.this));

        btnMesajGonder = findViewById(R.id.btn_mesaj_gonder);
        editTextMesaj = findViewById(R.id.editText_mesaj);


        petsApi = PetsApiClient.getClient().create(PetsApi.class);


        mesajlariGetir();

        btnMesajGonder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(editTextMesaj.getText())){
                    mesaj.setMesaj(editTextMesaj.getText().toString());
                    mesaj.setGonderenId(kullaniciID);
                    mesaj.setBasvuruId(basvuruId);
                    if(basvuranID == kullaniciID){
                        mesaj.setAlanId(ilanSahipID);
                    }else{
                        mesaj.setAlanId(basvuranID);
                    }
                    mesajGonder(mesaj);
                }else{
                    Toast.makeText(MesajActivity.this,"Boş mesaj gönderemezsiniz!",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onResume() {

        handler.postDelayed(runnable = new Runnable(){
            public void run(){
                mesajlariGetir();
                handler.postDelayed(this, 10000);
            }
        },10);
        super.onResume();
    }
    private void mesajlariGetir(){
        Call<List<Mesaj>> call = petsApi.basvuruIdIleMesajlariGetir(basvuruId);

        call.enqueue(new Callback<List<Mesaj>>() {
            @Override
            public void onResponse(Call<List<Mesaj>> call, Response<List<Mesaj>> response) {
                if(response.body() != null && response.code() == 200){
                    Log.d("1111111111111111111111", " OLAY : " +String.valueOf(response.code()));
                    if (response.body().size() > mesajlar.size()){
                        mesajlar = response.body();
                        MesajlarRecyclerViewAdapter adapter = new MesajlarRecyclerViewAdapter(MesajActivity.this,mesajlar);
                        mesajRecyclerView.setAdapter(adapter);
                        mesajRecyclerView.scrollToPosition(mesajlar.size() - 1);
                        Log.d("22222222222222222222", " OLAY : " +String.valueOf(response.code())+response.body().toString());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Mesaj>> call, Throwable t) {
                Log.d("333333333333333333", " OLAY : " + t.getMessage());

            }
        });
    }

    @Override
    protected void onPause() {
        handler.removeCallbacks(runnable); //stop handler when activity not visible
        super.onPause();
    }

    private void mesajGonder(Mesaj m){

        Log.d("MesajActivity","MESAJ :" + m.toString());
        Call<Integer> call = petsApi.mesajGonder(m);
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if(response.code() != 200){
                    Toast.makeText(MesajActivity.this,"Hay Aksi!,Bir şeyler yanlış gitti.",Toast.LENGTH_SHORT).show();
                }else{
                    editTextMesaj.getText().clear();
                    mesajlariGetir();
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Log.d("MesajActivity","HATA :" + t.getMessage());
            }
        });
    }
}