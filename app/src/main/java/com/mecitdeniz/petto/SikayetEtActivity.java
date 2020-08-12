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
import android.widget.TextView;
import android.widget.Toast;

import com.mecitdeniz.petto.Objects.Sikayet;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SikayetEtActivity extends AppCompatActivity {



    EditText editTextSikayetAciklama;
    Button buttonSikayetEt;

    TextView textViewToolbarBaslik;

    PetsApi petsApi;
    private Sikayet sikayet = new Sikayet();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sikayet_et);
        SharedPreferences sharedpreferences = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);

        String value = sharedpreferences.getString("id","null");
        int kullaniciID = Integer.parseInt(value);
        int ilanId = getIntent().getIntExtra("ilanId",-1);

        editTextSikayetAciklama = findViewById(R.id.editText_sikayet_aciklama);

        textViewToolbarBaslik = findViewById(R.id.textView_toolbar_title);
        textViewToolbarBaslik.setText("Şikayet Et");
        buttonSikayetEt = findViewById(R.id.btn_sikayet_et);

        buttonSikayetEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(editTextSikayetAciklama.getText())){
                    sikayet.setIlanId(ilanId);
                    sikayet.setSikayetEdenId(kullaniciID);
                    sikayet.setAciklama(editTextSikayetAciklama.getText().toString());
                    sikayetOlustur(sikayet);
                }else {
                    Toast.makeText(SikayetEtActivity.this,"Lütfen bir açıklama giriniz!",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void sikayetOlustur(Sikayet sikayet) {
        petsApi = PetsApiClient.getClient().create(PetsApi.class);
        Call<Integer> call = petsApi.sikayetOlustur(sikayet);

        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.code() == 201){
                    Toast.makeText(SikayetEtActivity.this,"Görüşlerinizi bize bildirdiğiniz için teşekkür ederiz!",Toast.LENGTH_SHORT).show();
                    finish();

                } else if (response.code() == 409){
                    Toast.makeText(SikayetEtActivity.this,"Zaten daha önce şikayet etmişsiniz!",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Log.e("Sikayet Activity ", "HATA :" , t);
            }
        });


    }
}
