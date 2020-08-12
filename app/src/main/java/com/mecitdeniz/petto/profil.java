package com.mecitdeniz.petto;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mecitdeniz.petto.Objects.Hayvan;
import com.mecitdeniz.petto.Objects.Kullanici;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class profil extends Fragment {

    private int kullaniciID ;
    ImageView imageViewCikisYap;

    private PetsApi petsApi;
    TextView tvKullaniciAdi;
    TextView tvBio;
    TextView tvAdSoyad;
    TextView tvYorumlar;
    private Kullanici kullanici;
    LinearLayout parentAdminText;
    LinearLayout parentEditorText;
    LinearLayout parentYorumlarText;
    TextView tvProfilDuzenle;
    GridView hayvanListesi;
    ImageView profilResmi;
    RatingBar ratingBarOy;

    private long sonTiklanmaZamani = 0;
    private List<Hayvan> hayvanlar = new ArrayList<Hayvan>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profil, container, false);

        petsApi = PetsApiClient.getClient().create(PetsApi.class);

        SharedPreferences sharedpreferences = this.getActivity().getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        String value = sharedpreferences.getString("id","0");
        kullaniciID = Integer.parseInt(value);

        parentAdminText = view.findViewById(R.id.parent_admin_text);
        parentEditorText = view.findViewById(R.id.parent_editor_text);
        tvKullaniciAdi = view.findViewById(R.id.tv_profil_kullanici_adi);
        tvBio = view.findViewById(R.id.tv_profil_bio);
        tvAdSoyad = view.findViewById(R.id.tv_profil_ad_soyad);
        profilResmi = (ImageView) view.findViewById(R.id.profil_resmi);
        ratingBarOy = view.findViewById(R.id.ratingBar_oy);
        tvYorumlar = view.findViewById(R.id.textView_yorumlar);
        parentYorumlarText = view.findViewById(R.id.parent_yorumlar_text);
        tvProfilDuzenle = view.findViewById(R.id.tv_profil_duzenle);
        imageViewCikisYap = view.findViewById(R.id.imageView_cikisYap);

        FloatingActionButton btnHayvanEkle = view.findViewById(R.id.btn_hayvan_ekle);

        kullaniciGetir();

        parentYorumlarText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - sonTiklanmaZamani < 1000){
                    return;
                }
                sonTiklanmaZamani = SystemClock.elapsedRealtime();

                Intent i = new Intent(getActivity(),YorumlarActivity.class);
                getActivity().startActivity(i);
            }
        });

        parentAdminText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - sonTiklanmaZamani < 1000){
                    return;
                }
                sonTiklanmaZamani = SystemClock.elapsedRealtime();
                Intent i = new Intent(getActivity(),AdminPanelActivity.class);
                getActivity().startActivity(i);
            }
        });

        tvProfilDuzenle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - sonTiklanmaZamani < 1000){
                    return;
                }
                sonTiklanmaZamani = SystemClock.elapsedRealtime();

                Intent i = new Intent(getActivity(),ProfilDuzenleActivity.class);
                i.putExtra("kullanici",kullanici);
                getActivity().startActivity(i);
            }
        });

        parentEditorText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - sonTiklanmaZamani < 1000){
                    return;
                }
                sonTiklanmaZamani = SystemClock.elapsedRealtime();
                Intent i = new Intent(getActivity(),EditorPanelActivity.class);
                getActivity().startActivity(i);
            }
        });
        LinearLayout parentLayoutBasvurular;

        btnHayvanEkle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - sonTiklanmaZamani < 1000){
                    return;
                }
                sonTiklanmaZamani = SystemClock.elapsedRealtime();
                Intent hayvanEkle = new Intent(getActivity(),HayvanEkleActivity.class);
                getActivity().startActivity(hayvanEkle);
            }
        });


        imageViewCikisYap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - sonTiklanmaZamani < 1000){
                    return;
                }
                sonTiklanmaZamani = SystemClock.elapsedRealtime();

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Petto");
                builder.setMessage("Çıkış yapmak istediğinize emin misiniz?");
                builder.setNegativeButton("Hayır",null);
                builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        editor.clear();
                        editor.apply();
                        Intent girisYap = new Intent(getActivity(),LoginActivity.class);
                        getActivity().startActivity(girisYap);
                        getActivity().finish();
                    }
                });
                builder.show();
            }
        });


        hayvanListesi = view.findViewById(R.id.gridView_hayvanlar);
        parentLayoutBasvurular = view.findViewById(R.id.parent_basvurular_text);
        parentLayoutBasvurular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent basvurular = new Intent(getContext(),BasvurularActivity.class);
                startActivity(basvurular);
            }
        });
        ortalamaPuanGetir();
        hayvanlariGetir();

        return view;
    }

    private void kullaniciGetir(){
        Call<Kullanici> call = petsApi.idIlekullaniciGetir(kullaniciID);

        call.enqueue(new Callback<Kullanici>() {
            @Override
            public void onResponse(Call<Kullanici> call, Response<Kullanici> response) {
                if (!response.body().equals(null) && response.code() == 200){
                    kullanici = response.body();
                    Picasso.get().load(kullanici.getProfilResmi()).into(profilResmi);
                    tvKullaniciAdi.setText(kullanici.getKullaniciAdi());
                    tvBio.setText(kullanici.getBio());
                    tvAdSoyad.setText(kullanici.getAd() + " " + kullanici.getSoyAd());
                    if(kullanici.getRolId() == 3){
                        parentAdminText.setVisibility(View.VISIBLE);
                    }else if(kullanici.getRolId() == 2){
                        parentEditorText.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<Kullanici> call, Throwable t) {

            }
        });
    }

    private void ortalamaPuanGetir(){
        Call<Float> call = petsApi.ortalamaOyGetir(kullaniciID);
        call.enqueue(new Callback<Float>() {
            @Override
            public void onResponse(Call<Float> call, Response<Float> response) {
                if(response.code() == 200){
                    parentYorumlarText.setVisibility(View.VISIBLE);
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
    private void hayvanlariGetir(){
        Call<List<Hayvan>> call2 = petsApi.kullaniciIdileHayvanlariGetir(kullaniciID);

        call2.enqueue(new Callback<List<Hayvan>>() {
            @Override
            public void onResponse(Call<List<Hayvan>> call, Response<List<Hayvan>> response) {

                if(!response.isSuccessful()){
                    Log.e("ProfilFragment","HATA : " + response.errorBody());
                    return;
                }

                hayvanlar = response.body();
                HayvanListesiAdapter adapter = new HayvanListesiAdapter(getActivity(),hayvanlar);
                hayvanListesi.setAdapter(adapter);

                hayvanListesi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        Hayvan hayvan = hayvanlar.get(position);

                        Intent hayvanDetay = new Intent(getActivity(),HayvanDetayActivity.class);
                        hayvanDetay.putExtra("hayvan",hayvan);
                        startActivity(hayvanDetay);
                        //Toast.makeText(getContext(),"You Clicked"+hayvanlar.get(position).getAd(),Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onFailure(Call<List<Hayvan>> call, Throwable t) {
                Log.e("ProfilFragment","HATA : " + t.getMessage());
            }
        });
    }
}
