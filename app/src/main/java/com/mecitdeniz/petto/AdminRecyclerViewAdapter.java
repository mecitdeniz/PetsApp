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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mecitdeniz.petto.Objects.Kullanici;
import com.mecitdeniz.petto.Objects.Mesaj;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AdminRecyclerViewAdapter extends RecyclerView.Adapter<AdminRecyclerViewAdapter.ViewHolder> {

    private List<Kullanici> kullanicilar;
    private Context mContext;
    private long sonTiklanmaZamani = 0;
    PetsApi petsApi;
    private int p;

    public AdminRecyclerViewAdapter(Context mContext, List<Kullanici> kullanicilar) {
        this.kullanicilar = kullanicilar;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public AdminRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.kullanici_layout,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdminRecyclerViewAdapter.ViewHolder holder, int position) {

        SharedPreferences sharedpreferences =  mContext.getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        String value = sharedpreferences.getString("id","0");
        int kullaniciID = Integer.parseInt(value);

        petsApi = PetsApiClient.getClient().create(PetsApi.class);

        Kullanici k = kullanicilar.get(position);
        String adSoyad = k.getAd()+" "+k.getSoyAd();

        Picasso.get().load(k.getProfilResmi()).into(holder.imageViewProfilResmi);
        holder.textViewAdSoyad.setText(adSoyad);
        holder.textViewKullaniciAdi.setText(k.getKullaniciAdi());
        holder.textViewRol.setText(k.getRol().getAd());

        holder.parentKullanici.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - sonTiklanmaZamani < 1000){
                    return;
                }
                sonTiklanmaZamani = SystemClock.elapsedRealtime();
                if (k.getId() != kullaniciID){
                    Intent i = new Intent(mContext,ProfilDetayActivity.class);
                    i.putExtra("kullaniciId",k.getId());
                    mContext.startActivity(i);
                }else{
                    return;
                }
            }
        });

        holder.textViewRol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - sonTiklanmaZamani < 1000){
                    return;
                }
                sonTiklanmaZamani = SystemClock.elapsedRealtime();
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                if (k.getId() != kullaniciID){
                    if (k.getRolId() == 1){
                        builder.setMessage(k.getKullaniciAdi()+"'ı editör Yap?");
                        builder.setNegativeButton("Hayır",null);
                        builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                kullaniciRolGuncelle(kullaniciID,k.getId());
                            }
                        });
                    } else if (k.getRolId() == 2){
                        builder.setMessage(k.getKullaniciAdi()+"'ı editörlükten çıkar?");
                        builder.setNegativeButton("Hayır",null);
                        builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                kullaniciRolGuncelle(kullaniciID,k.getId());
                            }
                        });
                    }
                    builder.show();
                }
            }
        });
    }

    private void kullaniciRolGuncelle(int adminId, int kullaniciId) {

        Call<Integer> call = petsApi.kullaniciRolGuncelle(adminId,kullaniciId);

        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {

                if(response.code() == 200){
                    Toast.makeText(mContext,"Kullanici Rolü Başarıyla Güncellendi! :)",Toast.LENGTH_SHORT).show();
                }else if (response.code() == 405){
                    Toast.makeText(mContext,"Bu işlem için yetkiniz yok!",Toast.LENGTH_SHORT).show();
                }else {
                    Log.d("AdminPanelActivity",String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Log.d("AdminPanelActivity","ERROR :" +t.getMessage());
            }
        });
    }

    @Override
    public int getItemCount() {
        return kullanicilar.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        CircleImageView imageViewProfilResmi;

        TextView textViewAdSoyad;
        TextView textViewRol;
        TextView textViewKullaniciAdi;

        LinearLayout parentKullanici;
        public ViewHolder(View itemView){
            super(itemView);
            textViewAdSoyad = itemView.findViewById(R.id.tv_kullanici_layout_ad_soyad);
            textViewKullaniciAdi = itemView.findViewById(R.id.tv_kullanici_layout_kullanici_adi);
            textViewRol = itemView.findViewById(R.id.tv_kullanici_layout_rol);
            imageViewProfilResmi = itemView.findViewById(R.id.imageView_kullanici_layout_profil_resmi);
            parentKullanici = itemView.findViewById(R.id.parent_kullanici_layout_kullanici);
        }
    }
}
