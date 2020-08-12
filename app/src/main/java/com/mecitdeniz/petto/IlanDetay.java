package com.mecitdeniz.petto;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mecitdeniz.petto.Objects.Basvuru;
import com.mecitdeniz.petto.Objects.Hayvan;
import com.mecitdeniz.petto.Objects.Ilan;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class IlanDetay extends AppCompatActivity {

    private int ilanID;

    PetsApi petsApi;

    GridView hayvanListesi;
    TextView kullaniciAdi;
    ImageView kullaniciResmi;
    ImageView hayvanResmi;
    TextView baslik;
    TextView aciklama;
    Button btnBasvur;


    LinearLayout parentTarihler;
    TextView textViewBaslangicTarihi;
    TextView textViewBitisTarihi;
    TextView textViewToolbarBaslik;
    TextView ilanDuzenle;
    EditText basvuruAciklama;
    Ilan ilan = new Ilan();
    LinearLayout parentBasvuruYap;
    LinearLayout parentDuzenle;
    LinearLayout parentBasvurular;
    LinearLayout parentKullanici;
    LinearLayout parentYorumla;
    private long sonTiklanmaZamani = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ilan_detay);

        SharedPreferences sharedpreferences = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);

        String value = sharedpreferences.getString("id","0");
        int kullaniciID = Integer.parseInt(value);

        petsApi = PetsApiClient.getClient().create(PetsApi.class);

        ilanID = getIntent().getIntExtra("ilanID",1);
        baslik = findViewById(R.id.tv_i_d_baslik);
        kullaniciAdi = findViewById(R.id.tv_i_d_kullanici_adi);
        aciklama = findViewById(R.id.tv_i_d_aciklama);
        kullaniciResmi = findViewById(R.id.img_i_d_kullanici_resmi);
        hayvanResmi = findViewById(R.id.img_i_d_hayvan_resmi);
        parentYorumla = findViewById(R.id.parent_ilan_detay_yorumla);
        textViewBaslangicTarihi = findViewById(R.id.tv_baslangic_tarihi);
        textViewBitisTarihi = findViewById(R.id.tv_bitis_tarihi);

        parentTarihler = findViewById(R.id.parent_ilan_detay_tarihler);

        parentKullanici = findViewById(R.id.parent_ilan_detay_kullanici);
        textViewToolbarBaslik = findViewById(R.id.textView_toolbar_title);
        textViewToolbarBaslik.setText("İlan Detay");

        basvuruAciklama = findViewById(R.id.editText_aciklama);
        hayvanListesi= findViewById(R.id.gridView_i_d_hayvanlar);
        btnBasvur = findViewById(R.id.btn_basvur);
        parentBasvuruYap = findViewById(R.id.parent_basvuru_yap);
        parentBasvurular = findViewById(R.id.parent_ilan_detay_basvurular);

        parentDuzenle = findViewById(R.id.parent_ilan_detay_duzenle);



        Call<Ilan> call = petsApi.idIleIlanGetir(ilanID);

        call.enqueue(new Callback<Ilan>() {
            @Override
            public void onResponse(Call<Ilan> call, Response<Ilan> response) {

                ilan = response.body();

                aciklama.setText(ilan.getAciklama());
                baslik.setText(ilan.getBaslik());
                kullaniciAdi.setText(ilan.getYayinlayan().getKullaniciAdi());
                Picasso.get().load(ilan.getYayinlayan().getProfilResmi()).into(kullaniciResmi);
                Picasso.get().load(ilan.getHayvanlar().get(0).getResim1()).into(hayvanResmi);
                List<Hayvan> hayvanlar = ilan.getHayvanlar();

                if(kullaniciID == ilan.getYayinlayan().getId()){
                    if (ilan.getDurumId() == 5){
                        parentYorumla.setVisibility(View.VISIBLE);
                    }else if (ilan.getDurumId() == 2){
                        parentBasvurular.setVisibility(View.VISIBLE);
                        parentDuzenle.setVisibility(View.VISIBLE);
                    }else if(ilan.getDurumId() == 1){
                        parentDuzenle.setVisibility(View.VISIBLE);
                    }
                }else {
                    parentBasvuruYap.setVisibility(View.VISIBLE);
                }

                if (ilan.getTipId() == 2){
                    parentTarihler.setVisibility(View.VISIBLE);
                    textViewBaslangicTarihi.setText(ilan.getBaslangicTarihi());
                    textViewBitisTarihi.setText(ilan.getBitisTarihi());
                }

                if (ilan.getDurumId() != 2 ){
                    parentBasvuruYap.setVisibility(View.GONE);
                }

                parentDuzenle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (SystemClock.elapsedRealtime() - sonTiklanmaZamani < 1000){
                            return;
                        }
                        sonTiklanmaZamani = SystemClock.elapsedRealtime();
                        Intent ilanDuzenle = new Intent(IlanDetay.this,IlanDuzenleActivity.class);
                        ilanDuzenle.putExtra("ilan",ilan);
                        startActivity(ilanDuzenle);
                    }
                });

                parentYorumla.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (SystemClock.elapsedRealtime() - sonTiklanmaZamani < 1000){
                            return;
                        }
                        sonTiklanmaZamani = SystemClock.elapsedRealtime();
                        Intent i = new Intent(IlanDetay.this,YorumlaActivity.class);
                        i.putExtra("ilanId",ilan.getId());
                        startActivity(i);
                    }
                });
                parentBasvurular.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (SystemClock.elapsedRealtime() - sonTiklanmaZamani < 1000){
                            return;
                        }
                        sonTiklanmaZamani = SystemClock.elapsedRealtime();
                        Intent i = new Intent(IlanDetay.this,BasvuranlarActivity.class);
                        i.putExtra("ilanId",ilan.getId());
                        i.putExtra("ilanSahipID",ilan.getYayinlayanId());
                        startActivity(i);
                    }
                });
                HayvanListesiAdapter adapter = new HayvanListesiAdapter(IlanDetay.this,hayvanlar);
                hayvanListesi.setAdapter(adapter);

                hayvanListesi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        Hayvan hayvan = hayvanlar.get(position);

                        Intent hayvanDetay = new Intent(IlanDetay.this,HayvanDetayActivity.class);
                        hayvanDetay.putExtra("hayvan",hayvan);
                        startActivity(hayvanDetay);
                    }
                });

            }

            @Override
            public void onFailure(Call<Ilan> call, Throwable t) {
                Log.e("IlanDetayActivity","İlan Getir de Hata :" +t.getMessage());
            }
        });

        parentKullanici.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - sonTiklanmaZamani < 1000){
                    return;
                }
                sonTiklanmaZamani = SystemClock.elapsedRealtime();
                if (ilan.getYayinlayanId() != kullaniciID){
                    Intent i = new Intent(IlanDetay.this,ProfilDetayActivity.class);
                    i.putExtra("kullaniciId",ilan.getYayinlayanId());
                    startActivity(i);
                }else{
                   return;
                }
            }
        });

        btnBasvur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!TextUtils.isEmpty(basvuruAciklama.getText().toString())){

                    Basvuru basvuru = new Basvuru();
                    basvuru.setAciklama(basvuruAciklama.getText().toString());
                    basvuru.setIlanId(ilanID);
                    basvuru.setBasvuranId(kullaniciID);

                    basvuruYap(basvuru);

                }else {
                    Toast.makeText(IlanDetay.this,"Lütfen bir açıklama giriniz!",Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
    private void  basvuruYap(Basvuru basvuru){

        Call<Integer> call = petsApi.bavuruYap(basvuru);

        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.code() == 201){
                    basvuruAciklama.setText("");
                    Toast.makeText(IlanDetay.this,"Basvurunuz başarılı bir şekilde oluşturuldu",Toast.LENGTH_SHORT).show();
                }else if(response.code() == 409) {
                    Toast.makeText(IlanDetay.this,"Zaten başvuru yapmışsınız",Toast.LENGTH_SHORT).show();
                }
                Log.d("IlanDetayActivity","AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA :" +response.code());
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Log.d("IlanDetayActivity","Hata :" +t.getMessage());
            }
        });
    }
}