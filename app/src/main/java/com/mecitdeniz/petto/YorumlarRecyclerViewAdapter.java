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
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mecitdeniz.petto.Objects.Ilan;
import com.mecitdeniz.petto.Objects.Yorum;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class YorumlarRecyclerViewAdapter extends RecyclerView.Adapter<YorumlarRecyclerViewAdapter.ViewHolder> {

    private List<Yorum> yorumlar = new ArrayList<>();
    private Context mContext;
    private PetsApi petsApi;

    private int p;
    public YorumlarRecyclerViewAdapter(Context mContext, List<Yorum> yorumlar) {
        this.yorumlar = yorumlar;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public YorumlarRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.yorum_layout,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull YorumlarRecyclerViewAdapter.ViewHolder holder, int position) {

        SharedPreferences sharedpreferences =  mContext.getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        String value = sharedpreferences.getString("id","0");
        int kullaniciID = Integer.parseInt(value);

        petsApi = PetsApiClient.getClient().create(PetsApi.class);

        Yorum y = yorumlar.get(position);

        holder.tvAdSoyad.setText(y.getYorumlayan().getAd()+" "+y.getYorumlayan().getSoyAd());
        holder.tvKullaniciAdi.setText(y.getYorumlayan().getKullaniciAdi());
        holder.tvYorumAciklama.setText(y.getAciklama());
        Picasso.get().load(y.getYorumlayan().getProfilResmi()).into(holder.imageViewProfilResmi);
        holder.ratingBarOy.setRating(y.getOy());
        holder.tvTarih.setText(y.getYorumlamaTarihi());


    }

    @Override
    public int getItemCount() {
        return yorumlar.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvAdSoyad;
        TextView tvKullaniciAdi;
        TextView tvYorumAciklama;
        TextView tvTarih;
        RatingBar ratingBarOy;
        CircleImageView imageViewProfilResmi;

        public ViewHolder(View itemView){
            super(itemView);

            tvAdSoyad = itemView.findViewById(R.id.tv_yorum_layout_ad_soyad);
            tvKullaniciAdi = itemView.findViewById(R.id.tv_yorum_layout_kullanici_adi);
            tvYorumAciklama = itemView.findViewById(R.id.tv_yorum_aciklama);
            imageViewProfilResmi = itemView.findViewById(R.id.yorum_layout_profil_resmi);
            ratingBarOy = itemView.findViewById(R.id.yorum_layout_ratingBar_oy);
            tvTarih = itemView.findViewById(R.id.tv_yorum_layout_tarih);
        }
    }
}
