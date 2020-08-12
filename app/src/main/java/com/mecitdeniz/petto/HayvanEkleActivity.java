package com.mecitdeniz.petto;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.HandlerThread;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mecitdeniz.petto.Objects.Cins;
import com.mecitdeniz.petto.Objects.Hayvan;
import com.mecitdeniz.petto.Objects.Il;
import com.mecitdeniz.petto.Objects.Ilce;
import com.mecitdeniz.petto.Objects.Kullanici;
import com.mecitdeniz.petto.Objects.Tur;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HayvanEkleActivity extends AppCompatActivity {

    private int STORAGE_PERMISSION_CODE = 1;

    private int RESIM_ISTEK_1 = 1;
    private int RESIM_ISTEK_2 = 2;
    private int RESIM_ISTEK_3 = 3;
    private int RESIM_ISTEK_4 = 4;
    private int kullaniciID;
    TextView textViewToolbarBaslik;
    ImageView resim1;
    ImageView resim2;
    ImageView resim3;
    ImageView resim4;
    EditText editTextHayvanAd;
    RadioButton radioButtonDisi;
    RadioButton radioButtonErkek;

    Spinner spinnerTur;
    Spinner spinnerCins;
    private PetsApi petsApi;
    List<String> resimYollari = new ArrayList<>();
    List<String> resimler = new ArrayList<>();
    Uri path;
    String resimPath;
    Button btnHayvanEkle;
    private String BASE_URL = "http://192.168.1.3:3030/download/";
    private Bitmap bitmap;
    private Hayvan hayvan = new Hayvan();
    private List<Tur> turler = new ArrayList<>();
    private List<Cins> cinsler = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hayvan_ekle);

        SharedPreferences sharedpreferences = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        String value = sharedpreferences.getString("id","0");
        kullaniciID = Integer.parseInt(value);

        petsApi = PetsApiClient.getClient().create(PetsApi.class);

        textViewToolbarBaslik = findViewById(R.id.textView_toolbar_title);
        textViewToolbarBaslik.setText("Hayvan Ekle");
        radioButtonErkek = findViewById(R.id.radioButton_erkek);
        radioButtonDisi = findViewById(R.id.radioButton_disi);

        spinnerTur = findViewById(R.id.spinner_hayvan_türü);
        spinnerCins = findViewById(R.id.spinner_hayvan_cinsi);

        editTextHayvanAd = findViewById(R.id.editText_hayvan_ad);
        turleriGetir();
        izinDenetle();


        spinnerTur.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Tur seciliTur = new Tur();
                seciliTur = turler.get(spinnerTur.getSelectedItemPosition());
                hayvan.setTurId(seciliTur.getId());
                cinsler = seciliTur.getCinsler();
                ArrayAdapter<Cins> adapterCins = new ArrayAdapter<Cins>(HayvanEkleActivity.this,android.R.layout.simple_spinner_item,cinsler);
                adapterCins.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerCins.setAdapter(adapterCins);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerCins.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Cins seciliCins = new Cins();
                seciliCins = cinsler.get(spinnerCins.getSelectedItemPosition());
                hayvan.setCinsId(seciliCins.getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        resim1 = findViewById(R.id.imageView_hayvan_resim1);
        resim2 = findViewById(R.id.imageView_hayvan_resim2);
        resim3 = findViewById(R.id.imageView_hayvan_resim3);
        resim4 = findViewById(R.id.imageView_hayvan_resim4);

        btnHayvanEkle = findViewById(R.id.btn_hayvan_ekle);

        btnHayvanEkle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!TextUtils.isEmpty(editTextHayvanAd.getText())){
                    for (int i=0; i<resimYollari.size();i++){
                        String uniqueID = UUID.randomUUID().toString()+".jpg";
                        switch (i){
                            case 0:
                                hayvan.setResim1(BASE_URL + uniqueID);
                                break;
                            case 1:
                                hayvan.setResim2(BASE_URL + uniqueID);
                                break;
                            case 2:
                                hayvan.setResim3(BASE_URL + uniqueID);
                                break;
                            case 3:
                                hayvan.setResim4(BASE_URL + uniqueID);
                                break;
                        }
                        resimYukle(resimYollari.get(i),uniqueID);
                    }
                    hayvanKaydet(hayvan);
                }else {
                    Toast.makeText(HayvanEkleActivity.this,"Lütfen formu eksiksiz doldurunuz!",Toast.LENGTH_SHORT).show();
                }
            }
        });

        resim1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resimSec(RESIM_ISTEK_1);
            }
        });
        resim2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resimSec(RESIM_ISTEK_2);
            }
        });
        resim3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resimSec(RESIM_ISTEK_3);
            }
        });
        resim4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resimSec(RESIM_ISTEK_4);
            }
        });

    }

    private void turleriGetir(){

        Call<List<Tur>> call = petsApi.turleriGetir();
        call.enqueue(new Callback<List<Tur>>() {
            @Override
            public void onResponse(Call<List<Tur>> call, Response<List<Tur>> response) {
                if (response.code() == 200){
                    turler = response.body();
                    ArrayAdapter<Tur> adapterTur = new ArrayAdapter<Tur>(HayvanEkleActivity.this,android.R.layout.simple_spinner_item,turler);
                    adapterTur.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerTur.setAdapter(adapterTur);
                }
            }

            @Override
            public void onFailure(Call<List<Tur>> call, Throwable t) {

            }
        });
    }

    private void hayvanKaydet(Hayvan h) {
        h.setSahipId(kullaniciID);
        h.setAd(editTextHayvanAd.getText().toString());
        if (!radioButtonErkek.isChecked()) {
            h.setCinsiyetId(2);
        } else {
            h.setCinsiyetId(1);
        }

        Call<Integer> call = petsApi.hayvanEkle(hayvan);

        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if(response.code() == 201){
                    Toast.makeText(HayvanEkleActivity.this,"Hayvan başarı ile eklendi :)",Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    Toast.makeText(HayvanEkleActivity.this,"Hay Aksi! Bir şeyler ters gitti :(, Lütfen daha sonra tekrar dene",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Log.d("HayvanEkleActivity","ERROR :" + t.getMessage());
            }
        });

    }

    public void resimSec(int istekKodu){
        Intent resimSec = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        resimSec.setType("image/*");
        startActivityForResult(Intent.createChooser(resimSec,"Resim seç"),istekKodu);
    }
    public void resimYukle(String resim,String uniqueID){

        File image = new File(resim);

        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), image);
        MultipartBody.Part part = MultipartBody.Part.createFormData("imageFile",uniqueID,requestBody);

        Call<Integer> call = petsApi.resimYukle(part);

        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {

                if(response.code() == 200){
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Log.d("HayvanEkleActivity","ERROR"+t.getMessage());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if( resultCode == RESULT_OK && data != null){
            path = data.getData();

            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(path,filePathColumn,null,null,null);
            assert cursor !=null;
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            resimPath = cursor.getString(columnIndex);

            cursor.close();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),path);
                if(requestCode == RESIM_ISTEK_1 ){
                    if (resimYollari.size() >= 1){
                        resimYollari.remove(0);
                    }
                    resimYollari.add(0,resimPath);
                    //Log.d("HayvanEkle","Sayısı : " + resimYollari.size());
                    resim1.setImageBitmap(bitmap);
                }else if(requestCode == RESIM_ISTEK_2 ){
                    if (resimYollari.size() >= 2){
                        resimYollari.remove(1);
                    }
                    resimYollari.add(resimPath);
                    resim2.setImageBitmap(bitmap);
                }else if(requestCode == RESIM_ISTEK_3 ){
                    if (resimYollari.size() >= 3){
                        resimYollari.remove(2);
                    }
                    resimYollari.add(resimPath);
                    resim3.setImageBitmap(bitmap);
                }else if(requestCode == RESIM_ISTEK_4 ){
                    if (resimYollari.size() >= 4){
                        resimYollari.remove(3);
                    }
                    resimYollari.add(resimPath);
                    resim4.setImageBitmap(bitmap);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void izinDenetle(){
        if (ContextCompat.checkSelfPermission(HayvanEkleActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
        } else {
            izinIste();
        }
    }
    private void izinIste() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {
            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed because of this and that")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(HayvanEkleActivity.this,
                                    new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_CODE)  {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "İzin Verildi", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Erişim Reddedildi", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
