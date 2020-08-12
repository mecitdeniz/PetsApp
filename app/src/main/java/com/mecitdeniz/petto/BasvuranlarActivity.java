package com.mecitdeniz.petto;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mecitdeniz.petto.Objects.Basvuru;
import com.mecitdeniz.petto.Objects.Ilan;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BasvuranlarActivity extends AppCompatActivity {

    TextView textViewToolbarBaslik;
    RecyclerView basvuranlarListesi;
    LinearLayout parentUyari;
    ImageView imageViewYenile;
    private List<Basvuru> basvurular = new ArrayList<>();
    private int ilanId;
    private int ilanSahipID;
    PetsApi petsApi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basvuranlar);
        textViewToolbarBaslik = findViewById(R.id.textView_toolbar_title);
        textViewToolbarBaslik.setText("Başvurular");
        imageViewYenile = findViewById(R.id.imageView_toolbar_yenile);
        imageViewYenile.setVisibility(View.VISIBLE);
        parentUyari = findViewById(R.id.parent_basvuranlar_basvuru_yok);
        basvuranlarListesi = findViewById(R.id.basvuranlar_recyclerView);
        basvuranlarListesi.setLayoutManager(new LinearLayoutManager(BasvuranlarActivity.this));
        ilanId = getIntent().getIntExtra("ilanId",-1);
        ilanSahipID = getIntent().getIntExtra("ilanSahipID",-1);

        petsApi = PetsApiClient.getClient().create(PetsApi.class);

        basvurulariGetir();

        imageViewYenile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                basvurulariGetir();
            }
        });


    }

    private void basvurulariGetir(){
        Call<List<Basvuru>> call = petsApi.ilanIdIleBasvurulariGetir(ilanId);

        call.enqueue(new Callback<List<Basvuru>>() {
            @Override
            public void onResponse(Call<List<Basvuru>> call, Response<List<Basvuru>> response) {
                if(response.body() != null && response.code() == 200){
                    basvurular = response.body();
                    BasvuranlarRecyclerViewAdapter adapter = new BasvuranlarRecyclerViewAdapter(BasvuranlarActivity.this,basvurular,ilanSahipID);
                    basvuranlarListesi.setAdapter(adapter);
                    if (basvurular.isEmpty()){
                        parentUyari.setVisibility(View.VISIBLE);
                    }else if (basvurular.size() >= 0){
                        parentUyari.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Basvuru>> call, Throwable t) {
                Log.d("BasvuranlarActivity","ERROR :"+t.getMessage());
            }
        });
    }
}
