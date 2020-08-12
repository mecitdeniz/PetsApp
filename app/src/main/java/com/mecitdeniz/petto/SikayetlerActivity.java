package com.mecitdeniz.petto;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mecitdeniz.petto.Objects.Ilan;
import com.mecitdeniz.petto.Objects.Sikayet;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SikayetlerActivity extends AppCompatActivity {

    TextView textViewToolbarBaslik;
    LinearLayout parentUyari;
    RecyclerView recyclerViewSikayetler;
    PetsApi petsApi;
    ImageView imageViewYenile;
    private List<Sikayet> sikayetler = new ArrayList<>();
    List<Ilan> ilanlar = new ArrayList<>();
    List<Integer> idler = new ArrayList<>();
    List<Integer> sikayetSayilari = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sikayetler);
        textViewToolbarBaslik = findViewById(R.id.textView_toolbar_title);
        recyclerViewSikayetler = findViewById(R.id.sikayetler_recyclerView);
        textViewToolbarBaslik.setText("Şikayetler");

        imageViewYenile = findViewById(R.id.imageView_toolbar_yenile);
        imageViewYenile.setVisibility(View.VISIBLE);
        recyclerViewSikayetler.setLayoutManager(new LinearLayoutManager(SikayetlerActivity.this));
        parentUyari = findViewById(R.id.parent_sikayet_yok_tv);
        petsApi = PetsApiClient.getClient().create(PetsApi.class);
        sikayetleriGetir();


        imageViewYenile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sikayetleriGetir();
            }
        });
    }


    private void idIleIlanGetir(int ilanId){

        Call<Ilan> call = petsApi.idIleIlanGetir(ilanId);
        call.enqueue(new Callback<Ilan>() {
            @Override
            public void onResponse(Call<Ilan> call, Response<Ilan> response) {
                if (response.body() != null && response.code() == 200){

                    if (response.body().getDurumId() == 2){
                        ilanlar.add(response.body());
                    }
                    if (ilanlar.isEmpty()) {
                        parentUyari.setVisibility(View.VISIBLE);
                    }else {
                        parentUyari.setVisibility(View.GONE);
                        SikayetlerRecyclerViewAdapter adapter = new SikayetlerRecyclerViewAdapter(SikayetlerActivity.this,ilanlar,sikayetSayilari);
                        recyclerViewSikayetler.setAdapter(adapter);
                    }

                }
            }

            @Override
            public void onFailure(Call<Ilan> call, Throwable t) {
                Log.d("SikayetlerActivity","ERROR Ilan Getir: " + t.getMessage());
            }
        });

    }
    private void sikayetleriGetir() {

        Call<List<Sikayet>> call = petsApi.sikayetleriGetir();

        call.enqueue(new Callback<List<Sikayet>>() {
            @Override
            public void onResponse(Call<List<Sikayet>> call, Response<List<Sikayet>> response) {
                if (response.body() != null && response.code() == 200){
                    sikayetler = response.body();

                    for (int i=0; i<sikayetler.size();i++){
                        if (!idler.contains(sikayetler.get(i).getIlanId())){
                            idler.add(sikayetler.get(i).getIlanId());
                        }
                        sikayetSayilari.add(sikayetler.get(i).getIlanId());
                    }
                    for (int i=0; i<idler.size();i++){
                        idIleIlanGetir(idler.get(i));
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Sikayet>> call, Throwable t) {
                Log.d("SikayetlerActivity","ERROR : " + t.getMessage());
            }
        });
    }
}