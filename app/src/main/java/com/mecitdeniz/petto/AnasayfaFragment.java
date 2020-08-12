package com.mecitdeniz.petto;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mecitdeniz.petto.Objects.Ilan;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class AnasayfaFragment extends Fragment {

    PetsApi petsApi;
    private static final String TAG = "MyActivity";
    RecyclerView recyclerView;
    private List<Ilan> ilanlar = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_anasayfa, container, false);
        recyclerView = view.findViewById(R.id.ilan_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //ilanListesiniGetir();

        petsApi = PetsApiClient.getClient().create(PetsApi.class);

        Call<List<Ilan>> call = petsApi.ilanlariGetir();

        call.enqueue(new Callback<List<Ilan>>() {
            @Override
            public void onResponse(Call<List<Ilan>> call, Response<List<Ilan>> response) {
                if(!response.isSuccessful()){

                    int code = response.code();
                    //Log.d("Code: " + String.valueOf(code));
                    return;
                }

                int size = response.body().size();
                ilanlar = response.body();
                IlanRecyclerViewAdapter adapter = new IlanRecyclerViewAdapter(getActivity(),ilanlar);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<Ilan>> call, Throwable t) {
                // Log.d("Error :" + String.valueOf(t.getMessage());
            }
        });

        Log.i(TAG, "İlanlar");
        return view;
    }

}
