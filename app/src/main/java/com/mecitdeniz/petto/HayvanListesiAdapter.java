package com.mecitdeniz.petto;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mecitdeniz.petto.Objects.Hayvan;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HayvanListesiAdapter  extends BaseAdapter {

    private Context mContext;
    private List<Hayvan> hayvanlar ;
    private LayoutInflater inflater;
    private PetsApi petsApi;

    public HayvanListesiAdapter(Context mContext, List<Hayvan> hayvanlar) {
        this.mContext = mContext;
        this.hayvanlar = hayvanlar;
    }

    @Override
    public int getCount() {
        return hayvanlar.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null){
            inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if(convertView == null){
            convertView = inflater.inflate(R.layout.hayvan_listesi_item,null);
        }

        SharedPreferences sharedpreferences =  mContext.getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        String value = sharedpreferences.getString("id","0");
        int kullaniciID = Integer.parseInt(value);

        petsApi = PetsApiClient.getClient().create(PetsApi.class);
        ImageView resim = convertView.findViewById(R.id.hayvan_item_resim);
        TextView isim = convertView.findViewById(R.id.hayvan_item_isim);
/*
        resim.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setMessage(hayvanlar.get(position).getAd() + "'ı silmek istediğinize emin misiniz?");
                builder.setNegativeButton("Hayır",null);
                builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        hayvanSil(hayvanlar.get(position).getId(),kullaniciID);
                    }
                });
                builder.show();

                return false;
            }
        });*/
        Picasso.get().load(hayvanlar.get(position).getResim1()).into(resim);
        isim.setText(hayvanlar.get(position).getAd());
        return convertView;
    }
    private void hayvanSil(int hayvanId, int kullaniciID) {

        Call<Integer> call = petsApi.hayvanSil(kullaniciID,hayvanId);

        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if(response.code() == 200){
                    Toast.makeText(mContext,"Hayvan başarılı bir şekide silindi..",Toast.LENGTH_SHORT).show();
                }else if (response.code() == 405){
                    Toast.makeText(mContext,"Bu işlemi gerçekleştirmeye yetkiniz yok!.",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Log.d("HayvanListesiAdapter","ERROR : " + t.getMessage());
            }
        });
    }
}
