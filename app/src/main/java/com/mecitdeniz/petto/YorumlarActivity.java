package com.mecitdeniz.petto;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.mecitdeniz.petto.Objects.Yorum;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class YorumlarActivity extends AppCompatActivity {

    TextView textViewToolbarBaslik;
    RecyclerView recyclerViewYorumlar;
    private int kullaniciID;
    private PetsApi petsApi;
    private List<Yorum> yorumlar = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yorumlar);
        kullaniciID = getIntent().getIntExtra("kullaniciId", -5);
        if (kullaniciID == -5){
            SharedPreferences sharedpreferences = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
            String value = sharedpreferences.getString("id","-1");
            kullaniciID = Integer.parseInt(value);
        }
        petsApi = PetsApiClient.getClient().create(PetsApi.class);

        textViewToolbarBaslik = findViewById(R.id.textView_toolbar_title);
        recyclerViewYorumlar = findViewById(R.id.yorumlar_recyclerView);
        textViewToolbarBaslik.setText("Yorumlar");
        recyclerViewYorumlar.setLayoutManager(new LinearLayoutManager(YorumlarActivity.this));

        yorumlariGetir();

    }

    private void yorumlariGetir() {

        Call<List<Yorum>> call = petsApi.kullaniciIdIleYorumlariGetir(kullaniciID);

        call.enqueue(new Callback<List<Yorum>>() {
            @Override
            public void onResponse(Call<List<Yorum>> call, Response<List<Yorum>> response) {
                if(response.code() == 200){
                    yorumlar = response.body();
                    YorumlarRecyclerViewAdapter adapter = new YorumlarRecyclerViewAdapter(YorumlarActivity.this,yorumlar);
                    recyclerViewYorumlar.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<Yorum>> call, Throwable t) {
                Log.e("YorumlarActivity","ERROR : " + t.getMessage());
            }
        });
    }
}