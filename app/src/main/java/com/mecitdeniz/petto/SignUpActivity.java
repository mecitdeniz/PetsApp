package com.mecitdeniz.petto;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.mecitdeniz.petto.Objects.Kullanici;

import java.nio.channels.InterruptedByTimeoutException;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignUpActivity extends AppCompatActivity {

    TextView girisYap;
    Button kayitOl;
    EditText editTextAd;
    EditText editTextSoyAd;
    EditText editTextKullaniciAdi;
    TextView tvDogumTarihi;
    EditText editTextEmail;
    EditText editTextSifre;
    EditText editTextSifreTekrar;
    RadioButton radioButtonKiz;
    RadioButton radioButtonErkek;

    PetsApi petsApi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        editTextAd = findViewById(R.id.editText_ad);
        editTextSoyAd = findViewById(R.id.editText_soyAd);
        editTextKullaniciAdi = findViewById(R.id.editText_kullanici_adi);
        editTextEmail = findViewById(R.id.editText_email);
        editTextSifre = findViewById(R.id.editText_sifre);
        editTextSifreTekrar = findViewById(R.id.editText_sifre_tekrar);
        tvDogumTarihi = findViewById(R.id.tv_dogum_tarihi);
        radioButtonErkek = findViewById(R.id.radioButton_erkek);
        radioButtonKiz = findViewById(R.id.radioButton_kiz);

        petsApi = PetsApiClient.getClient().create(PetsApi.class);

        tvDogumTarihi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Şimdiki zaman bilgilerini alıyoruz. güncel yıl, güncel ay, güncel gün.
                final Calendar takvim = Calendar.getInstance();
                int yil = takvim.get(Calendar.YEAR);
                int ay = takvim.get(Calendar.MONTH);
                int gun = takvim.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dpd = new DatePickerDialog(SignUpActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int yil, int ay, int gun) {
                                // ay değeri 0 dan başladığı için (Ocak=0, Şubat=1,..,Aralık=11)
                                // değeri 1 artırarak gösteriyoruz.
                                ay += 1;

                                tvDogumTarihi.setText(yil + "/" + ay + "/" + gun);
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


        kayitOl = findViewById(R.id.btn_kayit_ol);

        kayitOl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!TextUtils.isEmpty(editTextAd.getText()) && !TextUtils.isEmpty(editTextSoyAd.getText())
                        && !TextUtils.isEmpty(tvDogumTarihi.getText()) && !TextUtils.isEmpty(editTextKullaniciAdi.getText())
                        && !TextUtils.isEmpty(editTextEmail.getText()) && !TextUtils.isEmpty(editTextSifre.getText())
                        && !TextUtils.isEmpty(editTextSifreTekrar.getText())) {

                    if (!editTextSifre.getText().toString().equals(editTextSifreTekrar.getText().toString())) {
                        Toast.makeText(SignUpActivity.this, "Girdiğiniz şifreler eşleşmiyor!", Toast.LENGTH_SHORT).show();
                    } else {

                        Kullanici kullanici = new Kullanici();
                        kullanici.setAd(editTextAd.getText().toString());
                        kullanici.setSoyAd(editTextSoyAd.getText().toString());
                        kullanici.setKullaniciAdi(editTextKullaniciAdi.getText().toString());
                        kullanici.setRolId(1);
                        kullanici.setProfilResmi("http://192.168.1.3:3030/download/defaultProfilResmi.jpg");
                        kullanici.setEmail(editTextEmail.getText().toString());
                        kullanici.setSifre(editTextSifre.getText().toString());
                        if (!radioButtonErkek.isChecked()) {
                            kullanici.setCinsiyetId("2");
                        } else {
                            kullanici.setCinsiyetId("1");
                        }
                        kaydol(kullanici);
                    }

                } else {
                    Toast.makeText(SignUpActivity.this, "Lütfen tüm alanları doldurunuz!", Toast.LENGTH_SHORT).show();
                }

                /*
                Intent ilanDetay = new Intent(SignUpActivity.this,IlanDetay.class);
                startActivity(ilanDetay);
                finish();*/
            }
        });

        girisYap = findViewById(R.id.tv_girisYap);
        girisYap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                girisYap();
            }
        });
    }

    private void kaydol(Kullanici kullanici) {

        Log.d("Kayıt Ol", "Kullanıcı :" + kullanici.toString());

        Call<Integer> call = petsApi.kaydol(kullanici);

        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {

                if (response.code() == 302){
                    Toast.makeText(SignUpActivity.this, "Opps! Girdiğiniz email zaten kayıtlı!", Toast.LENGTH_SHORT).show();
                }else if(response.code() == 409){
                    Toast.makeText(SignUpActivity.this, "Opps!Kullanıcı adı çoktan alınmış!", Toast.LENGTH_SHORT).show();
                }else if(response.code() == 201){
                    Toast.makeText(SignUpActivity.this, "Kaydınız başarılı bir şekilde greçekleşti!", Toast.LENGTH_SHORT).show();
                    editTextAd.setText("");
                    editTextSoyAd.setText("");
                    editTextEmail.setText("");
                    editTextKullaniciAdi.setText("");
                    editTextSifre.setText("");
                    editTextSifreTekrar.setText("");
                    tvDogumTarihi.setText("");
                    girisYap();
                }
                Log.d("Kayıt Ol", "Response :" + response.body()+ " code :" + response.code());
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Log.d("Kayıt Ol", "Error :" + t.getMessage());
            }
        });
    }

    private void girisYap(){
        Intent girisYap = new Intent(SignUpActivity.this, LoginActivity.class);
        startActivity(girisYap);
        finish();
    }
}
