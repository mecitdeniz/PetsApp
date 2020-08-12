package com.mecitdeniz.petto;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mecitdeniz.petto.Objects.Hayvan;
import com.mecitdeniz.petto.Objects.Il;
import com.mecitdeniz.petto.Objects.Ilan;
import com.mecitdeniz.petto.Objects.Ilce;
import com.squareup.picasso.Picasso;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class IlanEkleActivity extends AppCompatActivity {


    Spinner spinnerIlanTipi;
    Spinner spinnerIl;
    Spinner spinnerIlce;

    ImageView imageView_hayvan_1;
    ImageView imageView_hayvan_2;
    ImageView imageView_hayvan_3;
    ImageView imageView_hayvan_4;
    TextView textView_hayvan_1;
    TextView textView_hayvan_2;
    TextView textView_hayvan_3;
    TextView textView_hayvan_4;

    TextView textViewBaslanGicTarihi;
    TextView textViewBitisTarihi;
    Button btnYayinla;
    Ilan ilan = new Ilan();
    EditText editTextIlanBaslik;
    EditText editTextIlanAciklama;

    LinearLayout parentTarihler;
    int kullaniciID;
    List<Il> iller = new ArrayList<>();

    PetsApi petsApi;
    List<Ilce> ilceler = new ArrayList<>();
    List<Hayvan> hayvanlar = new ArrayList<>();
    List<Integer> hayvanIdleri = new ArrayList<>();
    private long sonTiklanmaZamani = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ilan_ekle);

        SharedPreferences sharedpreferences = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
        String value = sharedpreferences.getString("id","0");
        kullaniciID = Integer.parseInt(value);

        spinnerIl = findViewById(R.id.spinner_ilan_ekle_il);
        spinnerIlce = findViewById(R.id.spinner_ilan_ekle_ilce);

        imageView_hayvan_1 = findViewById(R.id.imageView_hayvan_1);
        imageView_hayvan_2 = findViewById(R.id.imageView_hayvan_2);
        imageView_hayvan_3 = findViewById(R.id.imageView_hayvan_3);
        imageView_hayvan_4 = findViewById(R.id.imageView_hayvan_4);

        textView_hayvan_1 = findViewById(R.id.textView_hayvan_1);
        textView_hayvan_2 = findViewById(R.id.textView_hayvan_2);
        textView_hayvan_3 = findViewById(R.id.textView_hayvan_3);
        textView_hayvan_4 = findViewById(R.id.textView_hayvan_4);
        btnYayinla = findViewById(R.id.btn_yayinla);
        editTextIlanBaslik = findViewById(R.id.editText_ilan_baslik);
        editTextIlanAciklama = findViewById(R.id.editText_ilan_aciklama);

        textViewBaslanGicTarihi = findViewById(R.id.tv_baslangic_tarihi);
        textViewBitisTarihi = findViewById(R.id.tv_bitis_tarihi);
        parentTarihler = findViewById(R.id.parent_ilan_ekle_tarihler);

        String[] ilanTipleri = {"Sahiplendirme","Bakıcı"};

        petsApi = PetsApiClient.getClient().create(PetsApi.class);

        Call<List<Il>> call = petsApi.illeriGetir();

        call.enqueue(new Callback<List<Il>>() {
            @Override
            public void onResponse(Call<List<Il>> call, Response<List<Il>> response) {

                iller = response.body();
                ArrayAdapter<Il> adapterIl = new ArrayAdapter<Il>(IlanEkleActivity.this,android.R.layout.simple_spinner_item,iller);
                adapterIl.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerIl.setAdapter(adapterIl);
            }

            @Override
            public void onFailure(Call<List<Il>> call, Throwable t) {

            }
        });

        spinnerIl.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Il seciliIl = new Il();

                seciliIl = iller.get(spinnerIl.getSelectedItemPosition());
                ilceler = seciliIl.getIlceler();
                ilan.setIlId(seciliIl.getId());
                ArrayAdapter<Ilce> adapterIlce = new ArrayAdapter<Ilce>(IlanEkleActivity.this,android.R.layout.simple_spinner_item,ilceler);
                adapterIlce.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerIlce.setAdapter(adapterIlce);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerIlce.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Ilce seciliIlce = new Ilce();
                seciliIlce = ilceler.get(spinnerIlce.getSelectedItemPosition());
                ilan.setIlceId(seciliIlce.getId());

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        textViewBaslanGicTarihi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Şimdiki zaman bilgilerini alıyoruz. güncel yıl, güncel ay, güncel gün.
                final Calendar takvim = Calendar.getInstance();
                int yil = takvim.get(Calendar.YEAR);
                int ay = takvim.get(Calendar.MONTH);
                int gun = takvim.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dpd = new DatePickerDialog(IlanEkleActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int yil, int ay, int gun) {
                                // ay değeri 0 dan başladığı için (Ocak=0, Şubat=1,..,Aralık=11)
                                // değeri 1 artırarak gösteriyoruz.
                                ay += 1;

                                textViewBaslanGicTarihi.setText(yil + "/" + ay + "/" + gun);
                            }
                        }, yil, ay, gun);
                // datepicker açıldığında set edilecek değerleri buraya yazıyoruz.
                // şimdiki zamanı göstermesi için yukarda tanmladığımz değşkenleri kullanyoruz.

                // dialog penceresinin button bilgilerini ayarlıyoruz ve ekranda gösteriyoruz.
                dpd.setButton(DatePickerDialog.BUTTON_POSITIVE, "Seç", dpd);
                dpd.setButton(DatePickerDialog.BUTTON_NEGATIVE, "İptal", dpd);
                dpd.show();
            }
        });
        textViewBitisTarihi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Şimdiki zaman bilgilerini alıyoruz. güncel yıl, güncel ay, güncel gün.
                final Calendar takvim = Calendar.getInstance();
                int yil = takvim.get(Calendar.YEAR);
                int ay = takvim.get(Calendar.MONTH);
                int gun = takvim.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dpd = new DatePickerDialog(IlanEkleActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int yil, int ay, int gun) {
                                // ay değeri 0 dan başladığı için (Ocak=0, Şubat=1,..,Aralık=11)
                                // değeri 1 artırarak gösteriyoruz.
                                ay += 1;

                                textViewBitisTarihi.setText(yil + "/" + ay + "/" + gun);
                            }
                        }, yil, ay, gun);
                // datepicker açıldığında set edilecek değerleri buraya yazıyoruz.
                // şimdiki zamanı göstermesi için yukarda tanmladığımz değşkenleri kullanyoruz.

                // dialog penceresinin button bilgilerini ayarlıyoruz ve ekranda gösteriyoruz.
                dpd.setButton(DatePickerDialog.BUTTON_POSITIVE, "Seç", dpd);
                dpd.setButton(DatePickerDialog.BUTTON_NEGATIVE, "İptal", dpd);
                dpd.show();
            }
        });

        spinnerIlanTipi = findViewById(R.id.spinner_ilan_tipi);
        ArrayAdapter<String> ilanTipiAdapter = new ArrayAdapter<String>(IlanEkleActivity.this,android.R.layout.simple_spinner_item,ilanTipleri);
        ilanTipiAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerIlanTipi.setAdapter(ilanTipiAdapter);

        spinnerIlanTipi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(spinnerIlanTipi.getSelectedItemId() == 1){
                    ilan.setTipId(2);
                    imageView_hayvan_2.setImageDrawable(getResources().getDrawable(R.drawable.ic_add));
                    imageView_hayvan_2.setVisibility(View.VISIBLE);
                    parentTarihler.setVisibility(View.VISIBLE);
                }else {
                    ilan.setTipId(1);
                    hayvanlar.clear();
                    hayvanIdleri.clear();
                    //Toast.makeText(IlanEkleActivity.this,"a :" +String.valueOf(hayvanlar.size()+"b:"+String.valueOf(hayvanIdleri.size())),Toast.LENGTH_SHORT).show();
                    parentTarihler.setVisibility(View.GONE);
                    textView_hayvan_1.setText("");
                    textView_hayvan_2.setText("");
                    textView_hayvan_3.setText("");
                    textView_hayvan_4.setText("");
                    imageView_hayvan_1.setImageDrawable(getResources().getDrawable(R.drawable.ic_add));
                    imageView_hayvan_2.setVisibility(View.GONE);
                    imageView_hayvan_3.setVisibility(View.GONE);
                    imageView_hayvan_4.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        imageView_hayvan_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(IlanEkleActivity.this,HayvanSecActivity.class);
                startActivityForResult(i,1);

            }
        });
        imageView_hayvan_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(IlanEkleActivity.this,HayvanSecActivity.class);
                startActivityForResult(i,2);

            }
        });
        imageView_hayvan_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(IlanEkleActivity.this,HayvanSecActivity.class);
                startActivityForResult(i,3);

            }
        });
        imageView_hayvan_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(IlanEkleActivity.this,HayvanSecActivity.class);
                startActivityForResult(i,4);

            }
        });

        btnYayinla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - sonTiklanmaZamani < 1000){
                    return;
                }
                sonTiklanmaZamani = SystemClock.elapsedRealtime();
                if(!TextUtils.isEmpty(editTextIlanBaslik.getText()) && !TextUtils.isEmpty(editTextIlanAciklama.getText())
                && hayvanlar.size()>0){
                    ilan.setBaslik(editTextIlanBaslik.getText().toString());
                    ilan.setAciklama(editTextIlanAciklama.getText().toString());
                    ilan.setHayvanlar(hayvanlar);
                    ilan.setYayinlayanId(kullaniciID);
                    ilan.setBaslangicTarihi(textViewBaslanGicTarihi.getText().toString());
                    ilan.setBitisTarihi(textViewBitisTarihi.getText().toString());
                    Log.d("Ilan Ekle Activity","Ilan :" + ilan);

                    Call<Integer> call = petsApi.ilanOlustur(ilan);
                    call.enqueue(new Callback<Integer>() {
                        @Override
                        public void onResponse(Call<Integer> call, Response<Integer> response) {

                            if (response.code() == 201){
                                Toast.makeText(IlanEkleActivity.this,"İlan başarılı bir şekilde oluşturuldu.",Toast.LENGTH_SHORT).show();
                                finish();
                            }
                            Log.d("IlanEkleActrivity","Sonuc"+response.code());
                        }

                        @Override
                        public void onFailure(Call<Integer> call, Throwable t) {
                            Log.d("IlanEkleActrivity","Sonuc"+t.getMessage());
                        }
                    });
                }else {
                    Toast.makeText(IlanEkleActivity.this,"Lütfen tüm alanları doldurunuz!",Toast.LENGTH_SHORT).show();
                }
            }
        });




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1){
            if(resultCode == RESULT_OK){
                Hayvan hayvan = (Hayvan) data.getSerializableExtra("hayvan");
                textView_hayvan_1.setText(hayvan.getAd().toString());
                if(!hayvanlar.isEmpty()){
                    hayvanlar.remove(0);
                }
                hayvanlar.add(0,hayvan);
                hayvanIdleri.add(hayvan.getId());
                Picasso.get().load(hayvan.getResim1()).into(imageView_hayvan_1);
                if (ilan.getTipId() == 2){
                    imageView_hayvan_2.setVisibility(View.VISIBLE);
                }
            }
        }
        if (requestCode == 2){
            if(resultCode == RESULT_OK){
                Hayvan hayvan2 = (Hayvan) data.getSerializableExtra("hayvan");
                if(!hayvanIdleri.contains(hayvan2.getId())){
                    if(hayvanlar.size() >= 1) {
                        hayvanlar.add(1,hayvan2);
                    }
                    hayvanIdleri.add(hayvan2.getId());
                    textView_hayvan_2.setText(hayvan2.getAd());
                    Picasso.get().load(hayvan2.getResim1()).into(imageView_hayvan_2);
                    if (ilan.getTipId() == 2){
                        imageView_hayvan_3.setImageDrawable(getResources().getDrawable(R.drawable.ic_add));
                        imageView_hayvan_3.setVisibility(View.VISIBLE);
                    }
                }else {
                    Toast.makeText(IlanEkleActivity.this,"Zaten seçilmiş bir hayvanı tekrar ekleyemezsiniz!",Toast.LENGTH_SHORT).show();
                }

            }
        }
        if (requestCode == 3){
            if(resultCode == RESULT_OK){
                Hayvan hayvan3 = (Hayvan) data.getSerializableExtra("hayvan");
                if(!hayvanIdleri.contains(hayvan3.getId())){
                    if(hayvanlar.size() >= 2) {
                        hayvanlar.add(2,hayvan3);
                    }
                    hayvanIdleri.add(hayvan3.getId());
                    textView_hayvan_3.setText(hayvan3.getAd());
                    Picasso.get().load(hayvan3.getResim1()).into(imageView_hayvan_3);
                    if (ilan.getTipId() == 2){
                        imageView_hayvan_4.setImageDrawable(getResources().getDrawable(R.drawable.ic_add));
                        imageView_hayvan_4.setVisibility(View.VISIBLE);
                    }
                }else {
                    Toast.makeText(IlanEkleActivity.this,"Zaten seçilmiş bir hayvanı tekrar ekleyemezsiniz!",Toast.LENGTH_SHORT).show();
                }
            }
        }
        if (requestCode == 4){
            if(resultCode == RESULT_OK){
                Hayvan hayvan4 = (Hayvan) data.getSerializableExtra("hayvan");
                if(!hayvanIdleri.contains(hayvan4.getId())){
                    if(hayvanlar.size() >= 3){
                        hayvanlar.add(3,hayvan4);
                    }
                    hayvanIdleri.add(hayvan4.getId());
                    textView_hayvan_4.setText(hayvan4.getAd());
                    Picasso.get().load(hayvan4.getResim1()).into(imageView_hayvan_4);
                }else {
                    Toast.makeText(IlanEkleActivity.this,"Zaten seçilmiş bir hayvanı tekrar ekleyemezsiniz!",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
