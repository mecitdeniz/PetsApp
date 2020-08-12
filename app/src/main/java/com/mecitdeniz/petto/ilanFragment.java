package com.mecitdeniz.petto;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mecitdeniz.petto.Objects.Ilan;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ilanFragment extends Fragment {


    PetsApi petsApi;
    private static final String TAG = "İlanFragment";
    LinearLayout parentUyari;
    private List<Ilan> ilanlar = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ilan, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.ilanlarim_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        SharedPreferences sharedpreferences = this.getActivity().getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
        String value = sharedpreferences.getString("id","0");
        int kullaniciID = Integer.parseInt(value);

        petsApi = PetsApiClient.getClient().create(PetsApi.class);

        parentUyari = view.findViewById(R.id.parent_uyari);
        Call<List<Ilan>> call = petsApi.kullaniciIdIleIlanlariGetir(kullaniciID);


        call.enqueue(new Callback<List<Ilan>>() {
            @Override
            public void onResponse(Call<List<Ilan>> call, Response<List<Ilan>> response) {
                if(!response.isSuccessful()){

                    int code = response.code();
                    Log.d("Ilan Fragment","Error :" + response.code());
                    return;
                }
                if(!response.body().isEmpty()){
                    ilanlar = response.body();
                    IlanRecyclerViewAdapter2 adapter = new IlanRecyclerViewAdapter2(getActivity(),ilanlar);
                    recyclerView.setAdapter(adapter);
                    if (ilanlar.isEmpty()){
                        parentUyari.setVisibility(View.VISIBLE);
                    }else if (ilanlar.size() >= 0){
                        parentUyari.setVisibility(View.GONE);
                    }
                    //Log.i(TAG, "MyClass.getView() — get item number " + ilanlar.get(0).getBaslik());

                }

            }

            @Override
            public void onFailure(Call<List<Ilan>> call, Throwable t) {
                Log.d("Ilan Fragment","Error :" + t.getMessage().toString());
            }
        });

        FloatingActionButton btnIlanEkle = view.findViewById(R.id.btn_ilan_ekle);

        btnIlanEkle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ilanEkle = new Intent(getActivity(),IlanEkleActivity.class);
                getActivity().startActivity(ilanEkle);
            }
        });


        return view;
    }
}
