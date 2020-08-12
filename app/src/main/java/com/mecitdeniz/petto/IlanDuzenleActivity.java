package com.mecitdeniz.petto;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mecitdeniz.petto.Objects.Il;
import com.mecitdeniz.petto.Objects.Ilan;
import com.mecitdeniz.petto.Objects.Ilce;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class IlanDuzenleActivity extends AppCompatActivity {


    LinearLayout parentTarihler;
    EditText ilanAciklama;
    EditText ilanBaslik;
    TextView tvBaslangicTarihi;
    TextView tvBitisTarihi;

    Spinner spinnerIl;
    Spinner spinnerIlce;
    GridView hayvanListesi;

    Button btnYayinla;
    PetsApi petsApi;
    List<Il> iller = new ArrayList<>();
    List<Ilce> ilceler = new ArrayList<>();

    private int kullaniciID;
    private Ilan ilan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ilan_duzenle);

        petsApi = PetsApiClient.getClient().create(PetsApi.class);
        SharedPreferences sharedpreferences = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
        String value = sharedpreferences.getString("id","0");
        kullaniciID = Integer.parseInt(value);
        spinnerIl = findViewById(R.id.spinner_ilan_duzenle_il);
        spinnerIlce = findViewById(R.id.spinner_ilan_duzenle_ilce);
        ilanAciklama = findViewById(R.id.editText_ilan_duzenle_aciklama);
        ilanBaslik = findViewById(R.id.editText_ilan_duzenle_baslik);
        hayvanListesi = findViewById(R.id.gridView_ilan_düzenle_hayvanlar);
        btnYayinla = findViewById(R.id.btn_yayinla);
        parentTarihler = findViewById(R.id.parent_ilan_duzenle_tarihler);
        tvBaslangicTarihi = findViewById(R.id.tv_baslangic_tarihi);
        tvBitisTarihi = findViewById(R.id.tv_bitis_tarihi);


        ilan = (Ilan) getIntent().getSerializableExtra("ilan");

        ilanBaslik.setText(ilan.getBaslik().toString());
        ilanAciklama.setText(ilan.getAciklama().toString());

        HayvanListesiAdapter adapter = new HayvanListesiAdapter(IlanDuzenleActivity.this,ilan.getHayvanlar());
        hayvanListesi.setAdapter(adapter);

        illleriGetir();

        if(ilan.getTipId() == 2){
            parentTarihler.setVisibility(View.VISIBLE);
            tvBaslangicTarihi.setText(ilan.getBaslangicTarihi());
            tvBitisTarihi.setText(ilan.getBitisTarihi());
        }

        tvBaslangicTarihi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Şimdiki zaman bilgilerini alıyoruz. güncel yıl, güncel ay, güncel gün.
                final Calendar takvim = Calendar.getInstance();
                int yil = takvim.get(Calendar.YEAR);
                int ay = takvim.get(Calendar.MONTH);
                int gun = takvim.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dpd = new DatePickerDialog(IlanDuzenleActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int yil, int ay, int gun) {
                                // ay değeri 0 dan başladığı için (Ocak=0, Şubat=1,..,Aralık=11)
                                // değeri 1 artırarak gösteriyoruz.
                                ay += 1;

                                tvBaslangicTarihi.setText(yil + "/" + ay + "/" + gun);
                                ilan.setBaslangicTarihi(tvBaslangicTarihi.getText().toString());
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

        tvBitisTarihi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Şimdiki zaman bilgilerini alıyoruz. güncel yıl, güncel ay, güncel gün.
                final Calendar takvim = Calendar.getInstance();
                int yil = takvim.get(Calendar.YEAR);
                int ay = takvim.get(Calendar.MONTH);
                int gun = takvim.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dpd = new DatePickerDialog(IlanDuzenleActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int yil, int ay, int gun) {
                                // ay değeri 0 dan başladığı için (Ocak=0, Şubat=1,..,Aralık=11)
                                // değeri 1 artırarak gösteriyoruz.
                                ay += 1;

                                tvBitisTarihi.setText(yil + "/" + ay + "/" + gun);
                                ilan.setBitisTarihi(tvBitisTarihi.getText().toString());
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

        spinnerIl.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Il seciliIl = new Il();

                seciliIl = iller.get(spinnerIl.getSelectedItemPosition());
                ilceler = seciliIl.getIlceler();
                ilan.setIlId(seciliIl.getId());
                ArrayAdapter<Ilce> adapterIlce = new ArrayAdapter<Ilce>(IlanDuzenleActivity.this,android.R.layout.simple_spinner_item,ilceler);
                adapterIlce.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerIlce.setAdapter(adapterIlce);
                for(int i = 0; i<ilceler.size();i++){
                    if(ilan.getIlceId() == ilceler.get(i).getId()){
                        spinnerIlce.setSelection(i);
                    }
                }
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

        btnYayinla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(ilanBaslik.getText()) && !TextUtils.isEmpty(ilanAciklama.getText())){
                    ilan.setBaslik(ilanBaslik.getText().toString());
                    ilan.setAciklama(ilanAciklama.getText().toString());
                    ilanYayinla();
                }else {
                    Toast.makeText(IlanDuzenleActivity.this,"Lütfen tüm alanları doldurunuz!",Toast.LENGTH_SHORT).show();
                }
            }
        });


    }


    private void ilanYayinla(){
        Log.d("IlanDüzenleActivity",ilan.toString());

        Call<Integer> call = petsApi.ilanDuzenle(ilan,kullaniciID,ilan.getId());

        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if(response.code() == 200){
                    Toast.makeText(IlanDuzenleActivity.this,"Ilan başarılı bir şekilde güncellendi :)",Toast.LENGTH_SHORT).show();
                    finish();
                }else if(response.code() == 405) {
                    Toast.makeText(IlanDuzenleActivity.this,"Bu işlemi gerçekleştirmeye yetkiniz yok!",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Log.e("IlanDuzenleActivity","ERROR : " + t.getMessage());
            }
        });
    }
    private void illleriGetir(){
        Call<List<Il>> call = petsApi.illeriGetir();

        call.enqueue(new Callback<List<Il>>() {
            @Override
            public void onResponse(Call<List<Il>> call, Response<List<Il>> response) {

                iller = response.body();
                ArrayAdapter<Il> adapterIl = new ArrayAdapter<Il>(IlanDuzenleActivity.this,android.R.layout.simple_spinner_item,iller);
                adapterIl.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerIl.setAdapter(adapterIl);
                int ilId = ilan.getIlId();
                spinnerIl.setSelection(ilId-1);
            }
            @Override
            public void onFailure(Call<List<Il>> call, Throwable t) {
            }
        });
    }
}
