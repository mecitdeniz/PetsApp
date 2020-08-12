package com.mecitdeniz.petto;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.PeriodicSync;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.mecitdeniz.petto.Objects.Kullanici;
import com.mecitdeniz.petto.Objects.Mesaj;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminPanelActivity extends AppCompatActivity {

    TextView textViewToolbarBaslik;
    EditText editTextArama;
    ImageView imageViewArama;
    ImageView imageViewIptal;
    LinearLayout parentUyari;
    RecyclerView recyclerViewAdminPanel;
    PetsApi petsApi;
    List<Kullanici> kullanicilar = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);
        textViewToolbarBaslik = findViewById(R.id.textView_toolbar_title);
        textViewToolbarBaslik.setText("Admin Paneli");
        editTextArama = findViewById(R.id.editText_arama);
        imageViewArama = findViewById(R.id.imageView_arama);
        imageViewIptal = findViewById(R.id.imageView_iptal);
        parentUyari = findViewById(R.id.parent_uyari);
        recyclerViewAdminPanel = findViewById(R.id.admin_panel_recyclerView);
        recyclerViewAdminPanel.setLayoutManager(new LinearLayoutManager(AdminPanelActivity.this));

        petsApi = PetsApiClient.getClient().create(PetsApi.class);

        kullanicilariGetir();


        imageViewIptal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextArama.setText("");
                kullanicilariGetir();
                imageViewIptal.setVisibility(View.GONE);
            }
        });
        imageViewArama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Kullanici> kullaniciList = new ArrayList<>();
                if(!TextUtils.isEmpty(editTextArama.getText())){
                   for (int i = 0; i<kullanicilar.size();i++){
                       if (kullanicilar.get(i).getKullaniciAdi().equals(editTextArama.getText().toString())){
                           Toast.makeText(AdminPanelActivity.this,"Kullanici : " + kullanicilar.get(i).getAd(),Toast.LENGTH_SHORT).show();
                            kullaniciList.clear();
                            kullaniciList.add(kullanicilar.get(i));
                            adapterOlustur(kullaniciList);
                            imageViewIptal.setVisibility(View.VISIBLE);
                            parentUyari.setVisibility(View.GONE);
                            return;
                       }
                       kullaniciList.clear();
                       adapterOlustur(kullaniciList);
                       parentUyari.setVisibility(View.VISIBLE);
                       imageViewIptal.setVisibility(View.VISIBLE);
                       Toast.makeText(AdminPanelActivity.this,"Kullanıcı bulunaadı",Toast.LENGTH_SHORT).show();
                   }

                }
            }
        });
    }

    private void adapterOlustur(List<Kullanici> kullanicilar){
        AdminRecyclerViewAdapter adapter = new AdminRecyclerViewAdapter(AdminPanelActivity.this,kullanicilar);
        recyclerViewAdminPanel.setAdapter(adapter);
    }
    private void kullanicilariGetir() {

        Call<List<Kullanici>> call = petsApi.kullanicilariGetir();

        call.enqueue(new Callback<List<Kullanici>>() {
            @Override
            public void onResponse(Call<List<Kullanici>> call, Response<List<Kullanici>> response) {
                if(response.body() != null && response.code() == 200){
                    kullanicilar = response.body();
                    adapterOlustur(kullanicilar);
                }
            }

            @Override
            public void onFailure(Call<List<Kullanici>> call, Throwable t) {

            }
        });
    }
}