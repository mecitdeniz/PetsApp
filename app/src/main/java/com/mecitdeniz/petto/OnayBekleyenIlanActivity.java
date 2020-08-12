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
import android.widget.Toast;

import com.mecitdeniz.petto.Objects.Ilan;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OnayBekleyenIlanActivity extends AppCompatActivity {

    TextView textViewToolbarBaslik;
    RecyclerView ilanListesi;
    private PetsApi petsApi;
    ImageView imageViewYenile;

    LinearLayout parentTextViewOnayBekleyebIkanYok;
    private List<Ilan> ilanlar = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onay_bekleyen_ilan);

        petsApi = PetsApiClient.getClient().create(PetsApi.class);

        imageViewYenile = findViewById(R.id.imageView_toolbar_yenile);
        imageViewYenile.setVisibility(View.VISIBLE);
        ilanListesi = findViewById(R.id.onay_bekleyen_ilan_recylerView);
        ilanListesi.setLayoutManager(new LinearLayoutManager(OnayBekleyenIlanActivity.this));
        textViewToolbarBaslik = findViewById(R.id.textView_toolbar_title);
        textViewToolbarBaslik.setText("Onay Bekleyen İlanlar");
        parentTextViewOnayBekleyebIkanYok = findViewById(R.id.parent_ilan_yok_tv);

        ilanlariGetir();

        imageViewYenile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ilanlariGetir();
            }
        });

    }

    private void ilanlariGetir() {

        Call<List<Ilan>> call = petsApi.onayBekleyenIlanlariGetir();

        call.enqueue(new Callback<List<Ilan>>() {
            @Override
            public void onResponse(Call<List<Ilan>> call, Response<List<Ilan>> response) {
                if(response.body() != null && response.code() == 200){
                    ilanlar = response.body();
                    OnayBekleyenIlanlarRecyclerViewAdapter adapter = new OnayBekleyenIlanlarRecyclerViewAdapter(OnayBekleyenIlanActivity.this,ilanlar);
                    if (ilanlar.isEmpty()){
                        parentTextViewOnayBekleyebIkanYok.setVisibility(View.VISIBLE);
                    }
                    ilanListesi.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<Ilan>> call, Throwable t) {

            }
        });

    }


}