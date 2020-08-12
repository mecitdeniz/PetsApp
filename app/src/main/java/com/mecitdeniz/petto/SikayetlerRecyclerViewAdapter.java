package com.mecitdeniz.petto;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mecitdeniz.petto.Objects.Ilan;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SikayetlerRecyclerViewAdapter extends RecyclerView.Adapter<SikayetlerRecyclerViewAdapter.ViewHolder> {

    private List<Ilan> ilanlar = new ArrayList<>();
    private List<Integer> sikayetSayilari = new ArrayList<>();
    private Context mContext;
    private PetsApi petsApi;

    private int p;
    public SikayetlerRecyclerViewAdapter(Context mContext, List<Ilan> ilanlar , List<Integer> sikayetSayilari) {
        this.ilanlar = ilanlar;
        this.sikayetSayilari = sikayetSayilari;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public SikayetlerRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sikayet_layout,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull SikayetlerRecyclerViewAdapter.ViewHolder holder, int position) {

        SharedPreferences sharedpreferences =  mContext.getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        String value = sharedpreferences.getString("id","0");
        int kullaniciID = Integer.parseInt(value);

        petsApi = PetsApiClient.getClient().create(PetsApi.class);

        Ilan i = ilanlar.get(position);

        holder.tvAdSoyad.setText(i.getYayinlayan().getAd()+ " " + i.getYayinlayan().getSoyAd());
        holder.tvSikayetKullaniciAdi.setText(i.getYayinlayan().getKullaniciAdi());

        int sikayetSayisi = 0;
        for (int index = 0; index<sikayetSayilari.size();index++){
            if(sikayetSayilari.get(index) == i.getId()){
                sikayetSayisi++;
            }
        }
        holder.tvIlanBaslik.setText(i.getBaslik());
        holder.tvIlanAciklama.setText(i.getAciklama());

        Picasso.get().load(i.getYayinlayan().getProfilResmi()).into(holder.imageViewSikayetEdenProfilResmi);
        Picasso.get().load(i.getHayvanlar().get(0).getResim1()).into(holder.imageViewIlan);

        holder.tvSikayetSayisi.setText(String.valueOf(sikayetSayisi) + " şikayet");


        holder.parentSikayetler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext,IlanSikayetlerActivity.class);
                i.putExtra("ilanId",ilanlar.get(position).getId());
                mContext.startActivity(i);
            }
        });
        holder.parentIlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int ilanId = i.getId();
                Intent ilandetay = new Intent(mContext,IlanDetay.class);
                ilandetay.putExtra("ilanID",ilanId);
                mContext.startActivity(ilandetay);
            }
        });

        holder.getParentIlanKaldir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setMessage("İlanı yayından kaldırmak istediğinize emin misiniz?");
                builder.setNegativeButton("Hayır",null);
                builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ilanYayindanKaldir(i.getId(),kullaniciID);
                    }
                });
                builder.show();

            }
        });

    }

    private void ilanYayindanKaldir(int ilanId, int kullaniciID) {

        Call<Integer> call = petsApi.ilanYayindanKaldir(ilanId,kullaniciID);

        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if(response.code() == 200){
                    Toast.makeText(mContext,"Ilan başarılı bir yayından kaldırıldı. :)",Toast.LENGTH_SHORT).show();
                }else if (response.code() == 502){
                    Toast.makeText(mContext,"Bu işlemi gerçekleştirmeye yetkiniz yok! :(",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {

            }
        });
    }
    @Override
    public int getItemCount() {
        return ilanlar.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvAdSoyad;
        TextView tvSikayetKullaniciAdi;
        TextView tvIlanBaslik;
        TextView tvIlanAciklama;
        TextView tvSikayetSayisi;
        CircleImageView imageViewSikayetEdenProfilResmi;
        ImageView imageViewIlan;

        LinearLayout parentSikayetler;
        LinearLayout parentIlan;
        LinearLayout getParentIlanKaldir;
        public ViewHolder(View itemView){
            super(itemView);

            tvSikayetKullaniciAdi = itemView.findViewById(R.id.tv_sikayet_layout_kullanici_adi);
            tvAdSoyad = itemView.findViewById(R.id.tv_sikayet_aciklama);

            tvIlanBaslik = itemView.findViewById(R.id.tv_sikayet_layout_baslik);
            tvIlanAciklama = itemView.findViewById(R.id.tv_sikayet_layout_aciklama);

            imageViewSikayetEdenProfilResmi = itemView.findViewById(R.id.sikayet_layout_profil_resmi);
            imageViewIlan = itemView.findViewById(R.id.sikayet_layout_resim);

            parentIlan = itemView.findViewById(R.id.sikayet_layout_body);
            parentSikayetler = itemView.findViewById(R.id.parent_sikayet_layout_sikayetler);
            tvSikayetSayisi = itemView.findViewById(R.id.tv_sikayet_layout_sikayet_sayisi);
            getParentIlanKaldir = itemView.findViewById(R.id.parent_sikayet_layout_ilan_kaldir);
        }
    }
}
