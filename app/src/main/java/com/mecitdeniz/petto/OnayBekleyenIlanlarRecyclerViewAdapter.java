package com.mecitdeniz.petto;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
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


public class OnayBekleyenIlanlarRecyclerViewAdapter extends RecyclerView.Adapter<OnayBekleyenIlanlarRecyclerViewAdapter.ViewHolder> {

    private List<Ilan> ilanlar = new ArrayList<>();
    private Context mContext;
    private PetsApi petsApi;

    private int p;
    public OnayBekleyenIlanlarRecyclerViewAdapter(Context mContext, List<Ilan> ilanlar) {
        this.ilanlar = ilanlar;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public OnayBekleyenIlanlarRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ilan_layout3,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull OnayBekleyenIlanlarRecyclerViewAdapter.ViewHolder holder, int position) {

        SharedPreferences sharedpreferences =  mContext.getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        String value = sharedpreferences.getString("id","0");
        int kullaniciID = Integer.parseInt(value);

        petsApi = PetsApiClient.getClient().create(PetsApi.class);

        Ilan i = ilanlar.get(position);
        String[] tarih = ilanlar.get(position).getYayinTarihi().toString().split(" ");

        holder.tvTarih.setText(tarih[0] + " " + tarih[1]+ " " + tarih[2]);
        holder.tvAdSoyad.setText(i.getYayinlayan().getAd() + " " + i.getYayinlayan().getSoyAd() );
        holder.tvKullaniciAdi.setText(i.getYayinlayan().getKullaniciAdi());
        holder.tvIlanAciklama.setText(i.getAciklama());
        holder.tvIlanBaslik.setText(i.getBaslik());
        holder.tvIlIlce.setText(i.getIl()+ " " + i.getIlce());
        Picasso.get().load(i.getYayinlayan().getProfilResmi()).into(holder.imageViewKullaniciProfilResmi);
        Picasso.get().load(i.getHayvanlar().get(0).getResim1()).into(holder.imageViewIlan);

        holder.parentTextViewOnayla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setMessage("İlanı onaylamak istediğinize emin misiniz?");
                builder.setNegativeButton("Hayır",null);
                builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       ilanOnayla(i.getId(),kullaniciID);
                    }
                });
                builder.show();
            }
        });

        holder.parentIlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ilandetay = new Intent(mContext,IlanDetay.class);
                ilandetay.putExtra("ilanID",i.getId());
                mContext.startActivity(ilandetay);
            }
        });
        holder.parentTextViewReddet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setMessage("İlanı reddetmek istediğinize emin misiniz?");
                builder.setNegativeButton("Hayır",null);
                builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ilanReddet(i.getId(),kullaniciID);
                    }
                });
                builder.show();
            }
        });

    }

    private void ilanOnayla( int ilanId, int kullaniciID) {

        Call<Integer> call = petsApi.ilanOnayla(ilanId,kullaniciID);

        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if(response.code() == 200){
                    Toast.makeText(mContext,"Ilan başarılı bir şekilde onaylandı. :)",Toast.LENGTH_SHORT).show();
                }else if (response.code() == 502){
                    Toast.makeText(mContext,"Bu işlemi gerçekleştirmeye yetkiniz yok! :(",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Log.d("OnayBekleyenIlanar", "ERROR : "+t.getMessage());
            }
        });

    }

    private void ilanReddet( int ilanId, int kullaniciID) {

        Call<Integer> call = petsApi.ilanReddet(ilanId,kullaniciID);

        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if(response.code() == 200){
                    Toast.makeText(mContext,"Ilan başarılı bir şekilde reddedildi. :)",Toast.LENGTH_SHORT).show();
                }else if (response.code() == 502){
                    Toast.makeText(mContext,"Bu işlemi gerçekleştirmeye yetkiniz yok! :(",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Log.d("OnayBekleyenIlanar", "ERROR : "+t.getMessage());
            }
        });

    }

    @Override
    public int getItemCount() {
        return ilanlar.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvAdSoyad;
        TextView tvKullaniciAdi;
        TextView tvIlanBaslik;
        TextView tvIlanAciklama;
        TextView tvTarih;
        TextView tvIlIlce;
        ImageView imageViewIlan;

        LinearLayout parentTextViewOnayla;
        LinearLayout parentTextViewReddet;
        LinearLayout parentIlan;
        CircleImageView imageViewKullaniciProfilResmi;
        public ViewHolder(View itemView){
            super(itemView);

            tvKullaniciAdi = itemView.findViewById(R.id.tv_ilan_layout3_kullanici_adi);
            tvAdSoyad = itemView.findViewById(R.id.tv_ilan_layout3_ad_soyad);
            tvIlanAciklama = itemView.findViewById(R.id.tv_ilan_layout3_aciklama);
            tvIlanBaslik = itemView.findViewById(R.id.tv_ilan_layout3_baslik);
            tvTarih = itemView.findViewById(R.id.tv_ilan_layout3_tarih);
            imageViewKullaniciProfilResmi = itemView.findViewById(R.id.ilan_layout3_profil_resmi);
            imageViewIlan = itemView.findViewById(R.id.ilan_layout3_resim);
            tvIlIlce = itemView.findViewById(R.id.tv_ilan_layout3_il_ilce);
            parentIlan = itemView.findViewById(R.id.ilan_layout3_body);

            parentTextViewOnayla = itemView.findViewById(R.id.parent_ilan_layout3_onayla);
            parentTextViewReddet = itemView.findViewById(R.id.parent_ilan_layout3_reddet);

        }
    }
}
