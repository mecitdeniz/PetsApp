package com.mecitdeniz.petto;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.mecitdeniz.petto.Objects.Sikayet;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IlanSikayetlerActivity extends AppCompatActivity {
    TextView textViewToolbarBaslik;
    RecyclerView recyclerViewSikayetler;
    private int ilanId;
    private PetsApi petsApi;
    private List<Sikayet> sikayetler = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ilan_sikayetler);

        petsApi = PetsApiClient.getClient().create(PetsApi.class);

        ilanId = getIntent().getIntExtra("ilanId",-1);
        textViewToolbarBaslik = findViewById(R.id.textView_toolbar_title);
        recyclerViewSikayetler = findViewById(R.id.ilan_sikayet_recylerView);
        recyclerViewSikayetler.setLayoutManager(new LinearLayoutManager(IlanSikayetlerActivity.this));
        textViewToolbarBaslik.setText("Şikayetler");

        sikayetleriGetir(ilanId);
    }

    private void sikayetleriGetir(int id) {
        if(id == -1){
            return;
        }

        Call<List<Sikayet>> call = petsApi.ilanIdIleSikayetleriGetir(id);

        call.enqueue(new Callback<List<Sikayet>>() {
            @Override
            public void onResponse(Call<List<Sikayet>> call, Response<List<Sikayet>> response) {
                if(response.body() != null && response.code() == 200){
                    sikayetler = response.body();
                    IlanSikayetlerRecyclerViewAdapter adapter = new IlanSikayetlerRecyclerViewAdapter(IlanSikayetlerActivity.this,sikayetler);
                    recyclerViewSikayetler.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<Sikayet>> call, Throwable t) {

            }
        });

    }
}