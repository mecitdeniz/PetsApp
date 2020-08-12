package com.mecitdeniz.petto;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.ImageView;
import android.view.View;
import android.util.Log;
import android.widget.LinearLayout;
import android.content.Intent;
import com.mecitdeniz.petto.Objects.Hayvan;
import android.widget.Toast;
import android.content.Context;
import android.content.SharedPreferences;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
import com.mecitdeniz.petto.Objects.Hayvan;

import com.mecitdeniz.petto.Objects.Kullanici;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


import java.util.ArrayList;
import java.util.List;

public class HayvanDetayActivity extends AppCompatActivity {

    TextView hayvanAdi;
    TextView hayvanTuru;
    TextView hayvanCinsiyeti;
    TextView hayvanCinsi;

    ImageSlider imageSlider;

    ImageView kullaniciResmi;
    TextView kullaniciAdi;

    LinearLayout parentDuzenle;
    LinearLayout parentSil;
    private Hayvan hayvan;
    private PetsApi petsApi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hayvan_detay);

        petsApi = PetsApiClient.getClient().create(PetsApi.class);

        SharedPreferences sharedpreferences = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        String value = sharedpreferences.getString("id","0");
        int kullaniciID = Integer.parseInt(value);

        hayvanAdi = findViewById(R.id.tv_hayvan_detay_hayvan_adi);
        hayvanTuru = findViewById(R.id.tv_hayvan_detay_hayvan_turu);
        hayvanCinsiyeti = findViewById(R.id.tv_hayvan_detay_hayvan_cinsiyeti);
        hayvanCinsi = findViewById(R.id.tv_hayvan_detay_hayvan_cinsi);
        kullaniciResmi = findViewById(R.id.img_hayvan_detay_kullanici_resmi);
        kullaniciAdi = findViewById(R.id.tv_hayvan_detay_kullanici_adi);

        parentDuzenle = findViewById(R.id.parent_hayvan_detay_duzenle);
        parentSil = findViewById(R.id.parent_hayvan_detay_sil);

        hayvan = (Hayvan) getIntent().getSerializableExtra("hayvan");

        if(hayvan.getSahipId() == kullaniciID){
            parentDuzenle.setVisibility(View.VISIBLE);
            parentSil.setVisibility(View.VISIBLE);
        }

        imageSlider = findViewById(R.id.img_hayvan_detay_slider);

        List<SlideModel> slideModels = new ArrayList<>();

        String resim;
        if(hayvan.getResim1() != null){
            resim = hayvan.getResim1();
            slideModels.add(new SlideModel(resim));
        }
        if(hayvan.getResim2() != null) {
            resim = hayvan.getResim2();
            slideModels.add(new SlideModel(resim));
        }

        if(hayvan.getResim3() != null){
            resim = hayvan.getResim3();
            slideModels.add(new SlideModel(resim));
        }

        if(hayvan.getResim4() != null){
            resim = hayvan.getResim4();
            slideModels.add(new SlideModel(resim));
        }

        imageSlider.setImageList(slideModels,true);

        hayvanAdi.append(hayvan.getAd());
        int turId = hayvan.getTurId();
        hayvanTuru.append(hayvan.getTur().getAd());
        String cinsiyet = hayvan.getCinsiyet().getAdi();
        hayvanCinsiyeti.append(hayvan.getCinsiyet().getAdi());
        hayvanCinsi.append(hayvan.getCins().getAd());
        kullaniciGetir();

        parentDuzenle.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HayvanDetayActivity.this,HayvanDuzenleActivity.class);
                i.putExtra("hayvan",hayvan);
                startActivity(i);
            }

        });
        parentSil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(HayvanDetayActivity.this);
                builder.setMessage(hayvan.getAd()+"'ı silmek" +
                        " istediğinize emin misiniz?");
                builder.setNegativeButton("Hayır",null);
                builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        hayvanSil(kullaniciID,hayvan.getId());
                    }
                });
                builder.show();
            }
        });
    }

    private void hayvanSil(int kullaniciId,int hayvanId){
        Call<Integer> call = petsApi.hayvanSil(kullaniciId,hayvanId);
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if(response.code() == 200){
                    Toast.makeText(HayvanDetayActivity.this,"Hayvan başarı ile silindi..",Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Log.e("HayvanDetayActivity","ERROR :" +t.getMessage());
            }
        });

    }
    private void kullaniciGetir(){
        Call<Kullanici> call = petsApi.idIlekullaniciGetir(hayvan.getSahipId());

        call.enqueue(new Callback<Kullanici>() {
            @Override
            public void onResponse(Call<Kullanici> call, Response<Kullanici> response) {

                Kullanici kullanici = response.body();

                Picasso.get().load(kullanici.getProfilResmi()).into(kullaniciResmi);
                kullaniciAdi.setText(kullanici.getKullaniciAdi());
            }

            @Override
            public void onFailure(Call<Kullanici> call, Throwable t) {

            }
        });
    }

}