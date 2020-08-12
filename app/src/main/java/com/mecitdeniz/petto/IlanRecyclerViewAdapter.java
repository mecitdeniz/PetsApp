package com.mecitdeniz.petto;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
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
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class IlanRecyclerViewAdapter extends RecyclerView.Adapter<IlanRecyclerViewAdapter.ViewHolder> {

    private  static  String TAG ="Adapter";
    private List<Ilan> ilanlar = new ArrayList<>();
    private Context mContext;
    private long sonTiklanmaZamani = 0;

    public IlanRecyclerViewAdapter(Context mContext,List<Ilan> ilanlar) {
        this.ilanlar = ilanlar;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ilan_layout,parent,false);
        ViewHolder holder = new ViewHolder(view);
        Log.i(TAG, "Hello From Adapter");
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        SharedPreferences sharedpreferences =  mContext.getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        String value = sharedpreferences.getString("id","0");
        int kullaniciID = Integer.parseInt(value);

        final int radius = 150;
        final int margin = 0;
        Picasso.get()
                .load(ilanlar.get(position).hayvanlar.get(0).getResim1())
                .into(holder.resim);

        final Transformation transformation = new RoundedCornersTransformation(radius, margin);
        Picasso.get()
                .load(ilanlar.get(position).getYayinlayan().getProfilResmi())
                .transform(transformation)
                .into(holder.profilResmi);

        holder.baslik.setText(ilanlar.get(position).getBaslik());
        holder.aciklama.setText(ilanlar.get(position).getAciklama());

        holder.yer.setText(ilanlar.get(position).getIl().getAd() + "  " + ilanlar.get(position).getIlce().getAd());
        holder.kullaniciAdi.setText(ilanlar.get(position).getYayinlayan().getKullaniciAdi());
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Context context = v.getContext();
                int ilanId = ilanlar.get(position).getId();
                Intent ilandetay = new Intent(context,IlanDetay.class);
                ilandetay.putExtra("ilanID",ilanId);
                context.startActivity(ilandetay);
            }
        });



        holder.parentKullanici.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - sonTiklanmaZamani < 1000){
                    return;
                }
                sonTiklanmaZamani = SystemClock.elapsedRealtime();

                if (ilanlar.get(position).getYayinlayanId() != kullaniciID){
                    Intent i = new Intent(mContext,ProfilDetayActivity.class);
                    i.putExtra("kullaniciId",ilanlar.get(position).getYayinlayanId());
                    mContext.startActivity(i);
                }else{
                    return;
                }
            }
        });

        holder.secenekler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                if (ilanlar.get(position).getYayinlayan().getId() == kullaniciID){
                    builder.setMessage("İlanı düzenle?");
                    builder.setNegativeButton("Hayır",null);
                    builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            //TODO: Düzenleme Oluşturulacak
                            Intent ilanDuzenle = new Intent(mContext,IlanDuzenleActivity.class);
                            ilanDuzenle.putExtra("ilan",ilanlar.get(position));
                            mContext.startActivity(ilanDuzenle);
                        }
                    });

                }else {
                    builder.setMessage("Şikayet Et");
                    builder.setNegativeButton("Hayır",null);
                    builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            Intent sikayetEt = new Intent(mContext,SikayetEtActivity.class);
                            sikayetEt.putExtra("ilanId",ilanlar.get(position).getId());
                            mContext.startActivity(sikayetEt);
                        }
                    });
                }
                builder.show();
            }
        });
    }


    @Override
    public int getItemCount() {
        return ilanlar.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView resim;
        ImageView profilResmi;
        TextView baslik;
        TextView yer;
        TextView aciklama;
        TextView kullaniciAdi;
        LinearLayout parentLayout;
        ImageView secenekler;
        LinearLayout parentKullanici;
        public ViewHolder(View itemView){
            super(itemView);
            resim = itemView.findViewById(R.id.ilan_imageView);
            profilResmi = itemView.findViewById(R.id.ilan_layout_profil_resmi);
            baslik = itemView.findViewById(R.id.tv_ilan_baslik);
            aciklama = itemView.findViewById(R.id.tv_ilan_aciklama);
            parentLayout = itemView.findViewById(R.id.ilan_layout_parent);
            kullaniciAdi = itemView.findViewById(R.id.tv_kullanici_adi);
            secenekler = itemView.findViewById(R.id.imageView_ilan_layout_secenekler);
            yer = itemView.findViewById(R.id.tv_ilan_yer);
            parentKullanici = itemView.findViewById(R.id.parent_ilan_layout_kullanici);

        }
    }
}
