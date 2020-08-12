package com.mecitdeniz.petto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.mecitdeniz.petto.Objects.Hayvan;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HayvanSecActivity extends AppCompatActivity {

    private int kullaniciID;
    GridView hayvanListesi;
    PetsApi petsApi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hayvan_sec);

        SharedPreferences sharedpreferences = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        String value = sharedpreferences.getString("id","0");
        kullaniciID = Integer.parseInt(value);
        List<Hayvan> hayvanlar = new ArrayList<Hayvan>();
        hayvanListesi = findViewById(R.id.hayvan_sec_liste);

        petsApi = PetsApiClient.getClient().create(PetsApi.class);

        Call<List<Hayvan>> call = petsApi.kullaniciIdileHayvanlariGetir(kullaniciID);

        call.enqueue(new Callback<List<Hayvan>>() {
            @Override
            public void onResponse(Call<List<Hayvan>> call, Response<List<Hayvan>> response) {
                for (int i=0;i<response.body().size();i++){
                    hayvanlar.add(response.body().get(i));
                    Log.d("response", String.valueOf(hayvanlar.get(i).getAd()));
                }

                HayvanListesiAdapter adapter = new HayvanListesiAdapter(HayvanSecActivity.this,hayvanlar);
                hayvanListesi.setAdapter(adapter);

                hayvanListesi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        Hayvan hayvan = hayvanlar.get(position);

                        Intent sonuc = new Intent();
                        sonuc.putExtra("hayvan",hayvan);
                        setResult(RESULT_OK,sonuc);
                        finish();

                    }
                });
            }

            @Override
            public void onFailure(Call<List<Hayvan>> call, Throwable t) {

            }
        });


    }
}
