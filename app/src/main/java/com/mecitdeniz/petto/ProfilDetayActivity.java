package com.mecitdeniz.petto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.mecitdeniz.petto.Objects.Hayvan;
import com.mecitdeniz.petto.Objects.Kullanici;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfilDetayActivity extends AppCompatActivity {

    private int kullaniciId;
    private PetsApi petsApi;
    private long sonTiklanmaZamani = 0;

    List<Hayvan> hayvanlar = new ArrayList<>();
    CircleImageView imageViewProfilResmi;
    TextView textViewKullaniciAdi;
    TextView textViewBio;
    GridView hayvanListesi;
    RatingBar ratingBarOy;
    LinearLayout parentYorumlarText;
    TextView tvYorumlar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil_detay);

        petsApi = PetsApiClient.getClient().create(PetsApi.class);

        kullaniciId = getIntent().getIntExtra("kullaniciId",-1);

        textViewKullaniciAdi = findViewById(R.id.tv_profil_kullanici_adi);
        textViewBio = findViewById(R.id.tv_profil_bio);
        imageViewProfilResmi = findViewById(R.id.profil_resmi);
        hayvanListesi = findViewById(R.id.gridView_hayvanlar);
        parentYorumlarText = findViewById(R.id.parent_yorumlar_text);
        ratingBarOy = findViewById(R.id.ratingBar_oy);
        tvYorumlar = findViewById(R.id.text_view_yorum);

        kullaniciGetir(kullaniciId);

        hayvanlariGetir(kullaniciId);

        ortalamaPuanGetir();


        parentYorumlarText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - sonTiklanmaZamani < 1000){
                    return;
                }
                sonTiklanmaZamani = SystemClock.elapsedRealtime();

                Intent i = new Intent(ProfilDetayActivity.this,YorumlarActivity.class);
                i.putExtra("kullaniciId",kullaniciId);
                startActivity(i);
            }
        });

    }

    private void ortalamaPuanGetir(){
        Call<Float> call = petsApi.ortalamaOyGetir(kullaniciId);
        call.enqueue(new Callback<Float>() {
            @Override
            public void onResponse(Call<Float> call, Response<Float> response) {
                if(response.code() == 200){
                    tvYorumlar.append(" : " + response.body().toString() + "/5");
                    ratingBarOy.setRating(response.body());
                }
                Log.d("ProfilFragment","Response Code : " + String.valueOf(response.code()));
            }
            @Override
            public void onFailure(Call<Float> call, Throwable t) {
                Log.e("ProfilFragment","ERROR : " + t.getMessage());
                ratingBarOy.setVisibility(View.GONE);
                parentYorumlarText.setVisibility(View.GONE);
            }
        });
    }


    private void kullaniciGetir(int kullaniciId) {
        Call<Kullanici> call = petsApi.idIlekullaniciGetir(kullaniciId);

        call.enqueue(new Callback<Kullanici>() {
            @Override
            public void onResponse(Call<Kullanici> call, Response<Kullanici> response) {
                if (!response.body().equals(null) && response.code() == 200){
                    Kullanici kullanici = response.body();
                    Picasso.get().load(kullanici.getProfilResmi()).into(imageViewProfilResmi);
                    textViewKullaniciAdi.setText(kullanici.getKullaniciAdi());
                    textViewBio.setText(kullanici.getBio());
                }
            }
            @Override
            public void onFailure(Call<Kullanici> call, Throwable t) {

            }
        });
    }

    private void hayvanlariGetir(int kullaniciId) {

        Call<List<Hayvan>> call = petsApi.kullaniciIdileHayvanlariGetir(kullaniciId);

        call.enqueue(new Callback<List<Hayvan>>() {
            @Override
            public void onResponse(Call<List<Hayvan>> call, Response<List<Hayvan>> response) {

                if(!response.isSuccessful()){
                    return;
                }
                hayvanlar = response.body();
                HayvanListesiAdapter adapter = new HayvanListesiAdapter(ProfilDetayActivity.this,hayvanlar);
                hayvanListesi.setAdapter(adapter);

                hayvanListesi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        Hayvan hayvan = hayvanlar.get(position);

                        Intent hayvanDetay = new Intent(ProfilDetayActivity.this,HayvanDetayActivity.class);
                        hayvanDetay.putExtra("hayvan",hayvan);
                        startActivity(hayvanDetay);
                    }
                });
            }

            @Override
            public void onFailure(Call<List<Hayvan>> call, Throwable t) {
               Log.d("ProfilDetayActivity","ERROR : " + t.getMessage());
            }
        });
    }
}