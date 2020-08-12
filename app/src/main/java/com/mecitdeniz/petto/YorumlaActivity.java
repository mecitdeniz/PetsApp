package com.mecitdeniz.petto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mecitdeniz.petto.Objects.Basvuru;
import com.mecitdeniz.petto.Objects.Sikayet;
import com.mecitdeniz.petto.Objects.Yorum;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class YorumlaActivity extends AppCompatActivity {
    EditText editTextYorumAciklama;
    Button buttonGonder;

    TextView textViewToolbarBaslik;
    TextView textViewKullaniciAdi;
    TextView textViewAdSoyad;
    CircleImageView imageViewKullaniciProfilResmi;
    RatingBar ratingBarOy;
    PetsApi petsApi;
    private Yorum yorum = new Yorum();
    private int kullaniciID;
    private int ilanId;
    private Basvuru basvuru = new Basvuru();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yorumla);

        petsApi = PetsApiClient.getClient().create(PetsApi.class);
        SharedPreferences sharedpreferences = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);

        String value = sharedpreferences.getString("id","null");
        kullaniciID = Integer.parseInt(value);
        ilanId = getIntent().getIntExtra("ilanId",-1);

        editTextYorumAciklama = findViewById(R.id.editText_yorum_aciklama);
        ratingBarOy = findViewById(R.id.ratingBar_oy);
        textViewToolbarBaslik = findViewById(R.id.textView_toolbar_title);
        textViewToolbarBaslik.setText("Yorumla");
        buttonGonder = findViewById(R.id.btn_gonder);
        imageViewKullaniciProfilResmi = findViewById(R.id.img_i_d_kullanici_resmi);
        textViewAdSoyad = findViewById(R.id.tv_i_d_kullanici_ad_soyad);
        textViewKullaniciAdi = findViewById(R.id.tv_i_d_kullanici_adi);

        basvuruGetir();

        buttonGonder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (basvuru != null && !TextUtils.isEmpty(editTextYorumAciklama.getText())){
                    yorum.setAciklama(editTextYorumAciklama.getText().toString());
                    yorum.setOy(ratingBarOy.getRating());
                    yorum.setYorumlayanId(kullaniciID);
                    yorum.setYorumlananId(basvuru.getBasvuran().getId());
                    yorum.setIlanId(ilanId);
                    yorumEkle(yorum);
                    Log.d("YorumlaActivity",yorum.toString());
                }else {
                    Toast.makeText(YorumlaActivity.this,"Lütfen tüm alanları doldurunuz!",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void yorumEkle(Yorum y){
        Call<Integer> call = petsApi.yorumEkle(y);

        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.code() == 200){
                    Toast.makeText(YorumlaActivity.this,"Yorumunuz başarılı bir şekilde kaydedildi :)",Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Log.e("YorumlaActivity","ERROR : " + t.getMessage());
            }
        });
    }
    private void basvuruGetir(){
        Call<Basvuru> call = petsApi.ilanIdIleBasvuruGetir(ilanId);

        call.enqueue(new Callback<Basvuru>() {
            @Override
            public void onResponse(Call<Basvuru> call, Response<Basvuru> response) {
                if(response.body() != null && response.code() == 200){
                    basvuru = response.body();
                    Picasso.get().load(basvuru.getBasvuran().getProfilResmi()).into(imageViewKullaniciProfilResmi);
                    textViewAdSoyad.setText(basvuru.getBasvuran().getAd()+" "+basvuru.getBasvuran().getSoyAd());
                    textViewKullaniciAdi.setText(basvuru.getBasvuran().getKullaniciAdi());
                }
            }

            @Override
            public void onFailure(Call<Basvuru> call, Throwable t) {
                Log.e("YorumlaActivity","ERROR : " + t.getMessage());
            }
        });
    }
}