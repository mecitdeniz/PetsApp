package com.mecitdeniz.petto;

import android.app.Activity;
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


public class BasvuranlarRecyclerViewAdapter extends RecyclerView.Adapter<BasvuranlarRecyclerViewAdapter.ViewHolder> {

    private List<Basvuru> basvurular;
    private Context mContext;

    private int ilanSahipID;
    private PetsApi petsApi;

    public BasvuranlarRecyclerViewAdapter(Context mContext,List<Basvuru> basvurular,int ilanSahipID) {
        this.basvurular = basvurular;
        this.mContext = mContext;
        this.ilanSahipID = ilanSahipID;
    }

    @NonNull
    @Override
    public BasvuranlarRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.basvuru_layout,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull BasvuranlarRecyclerViewAdapter.ViewHolder holder, int position) {

        SharedPreferences sharedpreferences =  mContext.getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        String value = sharedpreferences.getString("id","0");
        int kullaniciID = Integer.parseInt(value);

        petsApi = PetsApiClient.getClient().create(PetsApi.class);

        Picasso.get().load(basvurular.get(position).getBasvuran().getProfilResmi()).into(holder.imageViewKullaniciResmi);
        holder.textViewKullaniciAdi.setText(basvurular.get(position).getBasvuran().getKullaniciAdi());
        holder.textViewBasvuruAciklama.setText(basvurular.get(position).getAciklama());

        holder.parentReddet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setMessage(basvurular.get(position).getBasvuran().getKullaniciAdi()+"'ın başvurusunu reddetmek" +
                        " istediğinize emin misiniz?");
                builder.setNegativeButton("Hayır",null);
                builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        basvuruReddet(basvurular.get(position).getId(),kullaniciID);
                    }
                });
                builder.show();
            }
        });

        if(basvurular.get(position).getDurumId() == 2){
            holder.parentKabulet.setVisibility(View.GONE);
            holder.parentReddet.setVisibility(View.GONE);
        }
        holder.parentKabulet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setMessage(basvurular.get(position).getBasvuran().getKullaniciAdi()+"'ın başvurusunu onaylamak" +
                        " istediğinize emin misiniz?");
                builder.setNegativeButton("Hayır",null);
                builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        basvuruOnayla(basvurular.get(position).getId(),kullaniciID);
                    }
                });
                builder.show();
            }
        });

        holder.parentBasvuru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent m = new Intent(mContext,MesajActivity.class);
                m.putExtra("basvuruId",basvurular.get(position).getId());
                m.putExtra("basvuranID",basvurular.get(position).getBasvuranId());
                m.putExtra("ilanSahipID",ilanSahipID);
                mContext.startActivity(m);
            }
        });

    }

    private void basvuruReddet(int basvuruId, int kullaniciID) {

        Call<Integer>call = petsApi.basvuruReddet(basvuruId,kullaniciID);

        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if(response.code() == 200){
                    Toast.makeText(mContext,"Basvuru başarılı bir şekilde reddedildi..",Toast.LENGTH_SHORT).show();
                }else if (response.code() == 405){
                    Toast.makeText(mContext,"Bu işlemi gerçekleştirmeye yetkiniz yok!",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Log.e("BasvuranlarActivity","ERROR : " + t.getMessage());
            }
        });
    }

    private void basvuruOnayla(int basvuruId, int kullaniciID) {

        Call<Integer>call = petsApi.basvuruOnayla(basvuruId,kullaniciID);

        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if(response.code() == 200){
                    Toast.makeText(mContext,"Basvuru başarılı bir şekilde oanylandı..",Toast.LENGTH_SHORT).show();
                    ((Activity)mContext).finish();
                }else if (response.code() == 405){
                    Toast.makeText(mContext,"Bu işlemi gerçekleştirmeye yetkiniz yok!",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Log.e("BasvuranlarActivity","ERROR : " + t.getMessage());
            }
        });
    }
    @Override
    public int getItemCount() {
        return basvurular.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView textViewKullaniciAdi;
        TextView textViewBasvuruAciklama;
        CircleImageView imageViewKullaniciResmi;
        LinearLayout parentBasvuru;
        LinearLayout parentReddet;
        LinearLayout parentKabulet;
        public ViewHolder(View itemView){
            super(itemView);

            textViewKullaniciAdi = itemView.findViewById(R.id.tv_kullanici_adi);
            textViewBasvuruAciklama = itemView.findViewById(R.id.tv_basvuru_aciklama);
            imageViewKullaniciResmi = itemView.findViewById(R.id.basvuru_layout_profil_resmi);
            parentBasvuru = itemView.findViewById(R.id.parent_basvuru_layout);
            parentKabulet = itemView.findViewById(R.id.parent_basvuru_layout_kabulet);
            parentReddet = itemView.findViewById(R.id.parent_basvuru_layout_reddet);
        }
    }
}
