package com.mecitdeniz.petto;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mecitdeniz.petto.Objects.Ilan;
import com.mecitdeniz.petto.Objects.Sikayet;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class IlanSikayetlerRecyclerViewAdapter extends RecyclerView.Adapter<IlanSikayetlerRecyclerViewAdapter.ViewHolder> {

    private List<Sikayet> sikayetler = new ArrayList<>();
    private Context mContext;

    private int p;
    public IlanSikayetlerRecyclerViewAdapter(Context mContext, List<Sikayet> sikayetler) {
        this.sikayetler = sikayetler;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public IlanSikayetlerRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sikayet_layout2,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull IlanSikayetlerRecyclerViewAdapter.ViewHolder holder, int position) {

        Sikayet s = sikayetler.get(position);

        holder.tvAdSoyad.setText(s.getSikayetEden().getAd() + " " + s.getSikayetEden().getSoyAd() );
        holder.tvKullaniciAdi.setText(s.getSikayetEden().getKullaniciAdi());
        holder.tvSikayetAciklama.setText(s.getAciklama());
        Picasso.get().load(s.getSikayetEden().getProfilResmi()).into(holder.imageViewSikayetEdenProfilResmi);

    }

    @Override
    public int getItemCount() {
        return sikayetler.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvAdSoyad;
        TextView tvKullaniciAdi;
        TextView tvSikayetAciklama;

        CircleImageView imageViewSikayetEdenProfilResmi;
        public ViewHolder(View itemView){
            super(itemView);

            tvKullaniciAdi = itemView.findViewById(R.id.tv_sikayet_layout2_kullanici_adi);
            tvAdSoyad = itemView.findViewById(R.id.tv_sikayet_layout2_ad_soyad);
            tvSikayetAciklama = itemView.findViewById(R.id.tv_sikayet_aciklama);
            imageViewSikayetEdenProfilResmi = itemView.findViewById(R.id.sikayet_layout2_profil_resmi);
        }
    }
}
