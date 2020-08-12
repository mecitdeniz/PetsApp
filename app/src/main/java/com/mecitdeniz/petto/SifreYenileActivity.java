package com.mecitdeniz.petto;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mecitdeniz.petto.Objects.Kullanici;

import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SifreYenileActivity extends AppCompatActivity {

    LinearLayout parentEmailSorgula;
    LinearLayout parentSifreYenile;
    LinearLayout parentKodDenetle;
    EditText editTextEmail;
    EditText editTextSifre;
    EditText editTextSifreTekrar;
    EditText editTextKod;
    TextView tvKodUyari;
    Button btnEmailSorgula;
    Button btnKodDenetle;
    Button btnSifreYenile;
    private PetsApi petsApi;
    private String email;
    private int kod;
    private Kullanici kullanici = new Kullanici();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sifre_yenile);

        petsApi = PetsApiClient.getClient().create(PetsApi.class);

        parentEmailSorgula = findViewById(R.id.parent_email_sorgula);
        parentSifreYenile = findViewById(R.id.parent_sifre_yenile);
        parentKodDenetle = findViewById(R.id.parent_kod_denetle);
        editTextEmail = findViewById(R.id.editText_email);
        editTextSifre = findViewById(R.id.editText_sifre);
        editTextKod = findViewById(R.id.editText_kod);
        editTextSifreTekrar = findViewById(R.id.editText_sifre_tekrar);
        tvKodUyari = findViewById(R.id.tv_kod_uyarı);
        btnEmailSorgula = findViewById(R.id.btn_email_sorgula);
        btnKodDenetle = findViewById(R.id.btn_kod_denetle);
        btnSifreYenile = findViewById(R.id.btn_sifre_yenile);

        btnEmailSorgula.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(editTextEmail.getText())){
                    emailSorgula(editTextEmail.getText().toString());
                }else {
                    Toast.makeText(SifreYenileActivity.this,"Email alanı boş bırakılamaz!",Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnKodDenetle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(editTextKod.getText())){
                    kodDenetle(Integer.parseInt(editTextKod.getText().toString()));
                }else {
                    Toast.makeText(SifreYenileActivity.this,"Email alanı boş bırakılamaz!",Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnSifreYenile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(editTextSifre.getText()) && !TextUtils.isEmpty(editTextSifreTekrar.getText())){
                    if( editTextSifre.getText().toString().equals(editTextSifreTekrar.getText().toString())){
                        String yeniSifre = editTextSifre.getText().toString();
                        sifreYenile(yeniSifre);
                    }else{
                        Toast.makeText(SifreYenileActivity.this,"Girdiğiniz şifreler eşleşmiyor!",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(SifreYenileActivity.this,"Lütfen tüm alanları doldurunuz!",Toast.LENGTH_SHORT).show();

                }
            }
        });


    }



    private void mailGonder(String email){

        final int min = 1000;
        final int max = 9999;
        kod = new Random().nextInt((max - min) + 1) + min;
        String mesaj = "Petto uygulamasında şifrenizi yenileyebilmek için kullanabileceğiniz 4 haneli kodunuz : ";
        MailGonderici mg = new MailGonderici(SifreYenileActivity.this, email, "Şifre Yenileme", mesaj + String.valueOf(kod));

        mg.execute();
    }
    private void emailSorgula(String e){
        Call<Kullanici> call = petsApi.emailKayitliMi(e);

        call.enqueue(new Callback<Kullanici>() {
            @Override
            public void onResponse(Call<Kullanici> call, Response<Kullanici> response) {
                if (response.code() == 200){
                    email = editTextEmail.getText().toString();
                    kullanici = response.body();
                    mailGonder(email);
                    editTextEmail.setText("");
                    btnEmailSorgula.setEnabled(false);
                    parentEmailSorgula.setVisibility(View.GONE);
                    parentKodDenetle.setVisibility(View.VISIBLE);
                    tvKodUyari.setText("Lütfen "+ email + " adresine gönderilen 4 haneli doğrulama kodunu giriniz ");
                }else {
                    Toast.makeText(SifreYenileActivity.this,"Kayıtlarımızda böyle bir email bulamadık!",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Kullanici> call, Throwable t) {
                Toast.makeText(SifreYenileActivity.this,"Kayıtlarımızda böyle bir email bulamadık!",Toast.LENGTH_SHORT).show();
                Log.e("SifreYenileActivity","ERROR : " +t.getMessage());
            }
        });

    }

    private void kodDenetle(int girilenKod){

        if (kod == girilenKod ){
            parentKodDenetle.setVisibility(View.GONE);
            parentSifreYenile.setVisibility(View.VISIBLE);
        }else {
            Toast.makeText(SifreYenileActivity.this,"Hatalı ya da eksik kod girdiniz!",Toast.LENGTH_SHORT).show();
            editTextKod.setText("");
        }

    }

    private void sifreYenile(String yeniSifre){

        kullanici.setSifre(yeniSifre);
        Call<Integer> call = petsApi.sifreYenile(kullanici.getId(),kullanici);

        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if(response.code() == 200){
                    Toast.makeText(SifreYenileActivity.this,"Şifreniz başarılı bir şekilde değiştirildi..",Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(SifreYenileActivity.this,LoginActivity.class);
                    startActivity(i);
                    finish();
                }else {
                    Log.e("SifreYenileActivity","ERROR : " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Log.e("SifreYenileActivity","ERROR : " +t.getMessage());
            }
        });
    }
}