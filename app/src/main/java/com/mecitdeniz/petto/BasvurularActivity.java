package com.mecitdeniz.petto;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mecitdeniz.petto.Objects.Basvuru;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BasvurularActivity extends AppCompatActivity {

    private List<Basvuru> basvurular = new ArrayList<>();
    private int kullaniciID;

    TextView textViewToolbarBaslik;
    TextView textViewUyari;
    RecyclerView basvuruListesi;

    ImageView imageViewYenile;
    private PetsApi petsApi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basvurular);
        SharedPreferences sharedpreferences = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
        String value = sharedpreferences.getString("id","0");
        kullaniciID = Integer.parseInt(value);

        textViewToolbarBaslik = findViewById(R.id.textView_toolbar_title);
        textViewToolbarBaslik.setText("Başvurularım");

        imageViewYenile = findViewById(R.id.imageView_toolbar_yenile);
        imageViewYenile.setVisibility(View.VISIBLE);
        basvuruListesi = findViewById(R.id.basvurular_recyclerView);
        textViewUyari = findViewById(R.id.tv_basvuru_uyari);
        basvuruListesi.setLayoutManager(new LinearLayoutManager(BasvurularActivity.this));

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
        Call<List<Basvuru>> call = petsApi.kullaniciIdIleBasvurulariGetir(kullaniciID);

        call.enqueue(new Callback<List<Basvuru>>() {
            @Override
            public void onResponse(Call<List<Basvuru>> call, Response<List<Basvuru>> response) {
                if (response.body() != null && response.code() == 200){
                    basvurular = response.body();
                    if (basvurular.isEmpty()){
                        textViewUyari.setVisibility(View.VISIBLE);
                    }else {
                        BasvurularimRecyclerViewAdapter adapter = new BasvurularimRecyclerViewAdapter(BasvurularActivity.this,basvurular);
                        basvuruListesi.setAdapter(adapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Basvuru>> call, Throwable t) {

            }
        });
    }
}
