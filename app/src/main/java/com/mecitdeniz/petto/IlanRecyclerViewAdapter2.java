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

import com.mecitdeniz.petto.Objects.Ilan;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class IlanRecyclerViewAdapter2 extends RecyclerView.Adapter<IlanRecyclerViewAdapter2.ViewHolder> {

    private List<Ilan> ilanlar = new ArrayList<>();
    private Context mContext;

    PetsApi petsApi;

    private int p;
    public IlanRecyclerViewAdapter2(Context mContext, List<Ilan> ilanlar ) {
        this.ilanlar = ilanlar;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public IlanRecyclerViewAdapter2.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ilan_layout2,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull IlanRecyclerViewAdapter2.ViewHolder holder, int position) {

        SharedPreferences sharedpreferences =  mContext.getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        String value = sharedpreferences.getString("id","0");
        int kullaniciID = Integer.parseInt(value);
        Picasso.get()
                .load(ilanlar.get(position).hayvanlar.get(0).getResim1())
                .into(holder.resim);

        String[] tarih = ilanlar.get(position).getYayinTarihi().toString().split(" ");

        holder.baslik.setText(ilanlar.get(position).getBaslik());
        holder.aciklama.setText(ilanlar.get(position).getAciklama());
        holder.tarih.setText(tarih[0] + " " + tarih[1]+ " " + tarih[2]);

        int ilanDurum = ilanlar.get(position).getDurumId();

        if (ilanDurum == 1){
            holder.durum.setText("Beklemede");
            holder.durum.setTextColor(Color.BLUE);
        }else if (ilanDurum == 2){
            holder.durum.setText("Yayınlandı");
            holder.durum.setTextColor(Color.GREEN);
        }else if (ilanDurum == 3){
            holder.durum.setText("Reddedildi");
            holder.durum.setTextColor(Color.MAGENTA);
        }else if (ilanDurum == 4){
            holder.durum.setText("Yayından Kaldırıldı");
            holder.durum.setTextColor(Color.RED);
        }else if (ilanDurum == 5){
            holder.durum.setText("Tamamlandı");
            holder.parentDuzenle.setVisibility(View.GONE);
            holder.parentSil.setVisibility(View.GONE);
            holder.parentYorumla.setVisibility(View.VISIBLE);
            holder.durum.setTextColor(Color.GRAY);
        }else if (ilanDurum == 6){
            holder.parentDuzenle.setVisibility(View.GONE);
            holder.durum.setText("Kapandı");
            holder.durum.setTextColor(Color.BLUE);
        }

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ilanDurum == 4){
                    return;
                }
                Context context = v.getContext();
                int ilanId = ilanlar.get(position).getId();
                Intent ilandetay = new Intent(context,IlanDetay.class);
                ilandetay.putExtra("ilanID",ilanId);
                context.startActivity(ilandetay);
            }
        });

        holder.parentYorumla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext,YorumlaActivity.class);
                i.putExtra("ilanId",ilanlar.get(position).getId());
                mContext.startActivity(i);
            }
        });
        holder.parentDuzenle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ilanDuzenle = new Intent(mContext,IlanDuzenleActivity.class);
                ilanDuzenle.putExtra("ilan",ilanlar.get(position));
                mContext.startActivity(ilanDuzenle);
            }
        });


        holder.parentSil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setMessage("İlanı silmek istediğinizden emin misiniz?");
                builder.setNegativeButton("Hayır",null);
                builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        petsApi = PetsApiClient.getClient().create(PetsApi.class);

                        Call<Integer> call = petsApi.ilanSil(kullaniciID,ilanlar.get(position).getId());

                        call.enqueue(new Callback<Integer>() {
                            @Override
                            public void onResponse(Call<Integer> call, Response<Integer> response) {
                                if(response.code() == 200){
                                    Toast.makeText(mContext,"İlan başarılı bir şekilde silindi.",Toast.LENGTH_SHORT).show();
                                }else if (response.code() == 405){
                                    Toast.makeText(mContext,"Bu işlemi gerçekleştirmeye yetkiniz yok!",Toast.LENGTH_SHORT).show();
                                }
                                Log.d("Ilanlarım","ERROR :"+ String.valueOf(response.code()));
                            }

                            @Override
                            public void onFailure(Call<Integer> call, Throwable t) {
                                Log.d("Ilanlarım","ERROR :"+ t.getMessage());
                            }
                        });
                    }
                });
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
        TextView baslik;
        TextView aciklama;
        TextView tarih;
        TextView durum;
        LinearLayout parentDuzenle;
        LinearLayout parentSil;
        LinearLayout parentLayout;
        LinearLayout parentYorumla;

        public ViewHolder(View itemView){
            super(itemView);
            resim = itemView.findViewById(R.id.ilan_layout2_resim);
            baslik = itemView.findViewById(R.id.tv_ilan_layout2_baslik);
            aciklama = itemView.findViewById(R.id.tv_ilan_layout2_aciklama);
            tarih = itemView.findViewById(R.id.tv_ilan_layout2_tarih);
            durum = itemView.findViewById(R.id.tv_ilan_layout2_durum);

            parentYorumla = itemView.findViewById(R.id.parent_ilan_layout2_yorumla);
            parentLayout = itemView.findViewById(R.id.ilan_layout2_body);
            parentDuzenle = itemView.findViewById(R.id.parent_ilan_layout2_duzenle);
            parentSil = itemView.findViewById(R.id.parent_ilan_layout2_sil);

        }
    }
}
