package com.mecitdeniz.petto;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mecitdeniz.petto.Objects.Basvuru;
import com.mecitdeniz.petto.Objects.Mesaj;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class MesajlarRecyclerViewAdapter extends RecyclerView.Adapter<MesajlarRecyclerViewAdapter.ViewHolder> {

    private List<Mesaj> mesajlar;
    private Context mContext;

    private int p;

    public MesajlarRecyclerViewAdapter(Context mContext, List<Mesaj> mesajlar) {
        this.mesajlar = mesajlar;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public MesajlarRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mesaj_layout_alici,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MesajlarRecyclerViewAdapter.ViewHolder holder, int position) {

        SharedPreferences sharedpreferences =  mContext.getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        String value = sharedpreferences.getString("id","0");
        int kullaniciID = Integer.parseInt(value);
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) holder.textViewMesaj.getLayoutParams();
        holder.textViewMesaj.setText(mesajlar.get(position).getMesaj());

        String[] gonderilmeTarihi = mesajlar.get(position).getGonderilmeTarihi().toString().split(" ");
        String[] gonderilmeSaati = gonderilmeTarihi[3].split(":");
        holder.textViewTarih.setText(gonderilmeSaati[0]+":"+gonderilmeSaati[1]);
        if(mesajlar.get(position).getGonderenId() == kullaniciID){
            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            holder.textViewMesaj.setBackgroundResource(R.drawable.fab_arka_plan);
            holder.textViewMesaj.setLayoutParams(lp);
        }

    }

    @Override
    public int getItemCount() {
        return mesajlar.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView textViewMesaj;
        TextView textViewTarih;
        public ViewHolder(View itemView){
            super(itemView);
            textViewMesaj = itemView.findViewById(R.id.tv_mesaj_layout_mesaj);
            textViewTarih = itemView.findViewById(R.id.tv_mesaj_layout_tarih);

        }
    }
}
