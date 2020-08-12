package com.mecitdeniz.petto;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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

import com.mecitdeniz.petto.Objects.Basvuru;
import com.mecitdeniz.petto.Objects.Ilan;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class BasvurularimRecyclerViewAdapter extends RecyclerView.Adapter<BasvurularimRecyclerViewAdapter.ViewHolder> {

    private List<Basvuru> basvurular = new ArrayList<>();
    private Context mContext;
    private PetsApi petsApi;

    private int p;
    public BasvurularimRecyclerViewAdapter(Context mContext, List<Basvuru> basvurular ) {
        this.basvurular = basvurular;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public BasvurularimRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.basvuru_layout2,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull BasvurularimRecyclerViewAdapter.ViewHolder holder, int position) {

        SharedPreferences sharedpreferences =  mContext.getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        String value = sharedpreferences.getString("id","0");
        int kullaniciID = Integer.parseInt(value);

        petsApi = PetsApiClient.getClient().create(PetsApi.class);


        Basvuru b = basvurular.get(position);


        String[] tarih = b.getBasvuruTarihi().toString().split(" ");

        holder.tvBasvuruAciklama.setText(b.getAciklama());
        holder.tvBasvuranKullaniciAdi.setText(b.getBasvuran().getKullaniciAdi());

        holder.tvBasvuruTarih.setText(tarih[0] + " " + tarih[1]+ " " + tarih[2]);
        holder.tvIlanBaslik.setText(b.getIlan().getBaslik());
        holder.tvIlanAciklama.setText(b.getIlan().getAciklama());

        int basvuruDurum = b.getDurumId();
        if (basvuruDurum == 1){
            holder.tvBasvuruDurum.setText("Beklemede");
            holder.tvBasvuruDurum.setTextColor(Color.BLUE);
        }else if (basvuruDurum == 2){
            if (b.getIlan().getTipId() == 2 && b.getIlan().getDurumId() != 6){
                holder.parentSil.setVisibility(View.GONE);
            }
            holder.tvBasvuruDurum.setText("Onaylandı");
            holder.tvBasvuruDurum.setTextColor(Color.GREEN);
        }else if (basvuruDurum == 3){
            holder.tvBasvuruDurum.setText("Reddedildi");
            holder.tvBasvuruDurum.setTextColor(Color.MAGENTA);
            holder.parentMesaj.setVisibility(View.GONE);
        }



        Picasso.get().load(b.getBasvuran().getProfilResmi()).into(holder.imageViewBasvuranProfilResmi);
        Picasso.get().load(b.getIlan().getHayvanlar().get(0).getResim1()).into(holder.imageViewIlan);

        holder.parentSil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setMessage("Başvurunuzu silmek istediğinize emin misiniz?");
                builder.setNegativeButton("Hayır",null);
                builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        basvuruSil(kullaniciID,b.getId());
                    }
                });
                builder.show();
            }
        });

        holder.parentMesaj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent m = new Intent(mContext,MesajActivity.class);
                m.putExtra("basvuruId",b.getId());
                m.putExtra("basvuranID",b.getBasvuranId());
                m.putExtra("ilanSahipID",b.getIlan().getYayinlayanId());
                mContext.startActivity(m);
            }
        });
        holder.parentIlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int ilanId = b.getIlanId();
                Intent ilandetay = new Intent(mContext,IlanDetay.class);
                ilandetay.putExtra("ilanID",ilanId);
                mContext.startActivity(ilandetay);
            }
        });

    }

    private void basvuruSil(int kullaniciID, int basvuruId) {

        Call<Integer> call = petsApi.basvuruSil(kullaniciID,basvuruId);

        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if(response.code() == 200){
                    Toast.makeText(mContext,"Başvurunuz başarılı bir şekilde silindi..",Toast.LENGTH_SHORT).show();
                }else if (response.code() == 405){
                    Toast.makeText(mContext,"Bu işlemi gerçekleştirmeye yetkiniz yok!",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Log.e("BasvurularimActivity","ERROR: " + t.getMessage());
            }
        });
    }

    @Override
    public int getItemCount() {
        return basvurular.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvBasvuruAciklama;
        TextView tvBasvuranKullaniciAdi;
        TextView tvIlanBaslik;
        TextView tvIlanAciklama;
        TextView tvBasvuruTarih;
        TextView tvBasvuruDurum;

        CircleImageView imageViewBasvuranProfilResmi;
        ImageView imageViewIlan;

        LinearLayout parentMesaj;
        LinearLayout parentIlan;
        LinearLayout parentSil;

        public ViewHolder(View itemView){
            super(itemView);

            tvBasvuranKullaniciAdi = itemView.findViewById(R.id.tv_basvuru_layout2_kullanici_adi);
            tvBasvuruAciklama = itemView.findViewById(R.id.tv_basvuru_aciklama);

            tvIlanBaslik = itemView.findViewById(R.id.tv_basvuru_layout2_baslik);
            tvIlanAciklama = itemView.findViewById(R.id.tv_basvuru_layout2_aciklama);

            imageViewBasvuranProfilResmi = itemView.findViewById(R.id.basvuru_layout2_profil_resmi);
            imageViewIlan = itemView.findViewById(R.id.basvuru_layout2_resim);

            parentIlan = itemView.findViewById(R.id.basvuru_layout2_body);

            parentMesaj = itemView.findViewById(R.id.parent_basvuru_layout2_mesaj);

            parentSil = itemView.findViewById(R.id.parent_basvuru_layout2_sil);

            tvBasvuruTarih = itemView.findViewById(R.id.tv_basvuru_layout2_tarih);
            tvBasvuruDurum = itemView.findViewById(R.id.tv_basvuru_layout2_durum);

        }
    }
}
