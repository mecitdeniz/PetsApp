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
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
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
import com.mecitdeniz.petto.Objects.Tur;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Callback;
import retrofit2.Response;

public class HayvanDuzenleActivity extends AppCompatActivity {


    TextView textViewToolBarBaslik;
    EditText editTextAd;
    RadioButton radioButtonKiz;
    RadioButton radioButtonErkek;
    Button btnKaydet;
    Spinner spinnerTur;
    Spinner spinnerCins;

    ImageView imageViewResim1;
    ImageView imageViewResim2;
    ImageView imageViewResim3;
    ImageView imageViewResim4;

    Uri path;
    String resimPath;
    private Bitmap bitmap;
    private int RESIM_ISTEK_1 = 1;
    private int RESIM_ISTEK_2 = 2;
    private int RESIM_ISTEK_3 = 3;
    private int RESIM_ISTEK_4 = 4;

    private String resim1Yol;
    private String resim2Yol;
    private String resim3Yol;
    private String resim4Yol;

    private Boolean resim1DegistiMi = false;
    private Boolean resim2DegistiMi = false;
    private Boolean resim3DegistiMi = false;
    private Boolean resim4DegistiMi = false;

    private long sonTiklanmaZamani = 0;
    private int STORAGE_PERMISSION_CODE = 1;
    private PetsApi petsApi;
    private List<Tur> turler = new ArrayList<>();
    private List<Cins> cinsler = new ArrayList<>();
    private Hayvan hayvan = new Hayvan();
    private int kullaniciID;
    private String BASE_URL = "http://192.168.1.3:3030/download/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hayvan_duzenle);

        petsApi = PetsApiClient.getClient().create(PetsApi.class);

        SharedPreferences sharedpreferences = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        String value = sharedpreferences.getString("id","0");
        kullaniciID = Integer.parseInt(value);
        editTextAd = findViewById(R.id.editText_hayvan_ad);
        radioButtonErkek = findViewById(R.id.radioButton_erkek);
        radioButtonKiz = findViewById(R.id.radioButton_disi);
        spinnerTur = findViewById(R.id.spinner_hayvan_türü);
        spinnerCins = findViewById(R.id.spinner_hayvan_cinsi);
        imageViewResim1 = findViewById(R.id.imageView_hayvan_resim1);
        imageViewResim2 = findViewById(R.id.imageView_hayvan_resim2);
        imageViewResim3 = findViewById(R.id.imageView_hayvan_resim3);
        imageViewResim4 = findViewById(R.id.imageView_hayvan_resim4);
        btnKaydet = findViewById(R.id.btn_hayvan_kaydet);
        textViewToolBarBaslik = findViewById(R.id.textView_toolbar_title);
        textViewToolBarBaslik.setText("Düzenle");

        turleriGetir();
        izinDenetle();

        hayvan = (Hayvan) getIntent().getSerializableExtra("hayvan");
        editTextAd.setText(hayvan.getAd());
        if (hayvan.getResim1() != null){
            Picasso.get().load(hayvan.getResim1()).into(imageViewResim1);
        }
        if (hayvan.getResim2() != null){
            Picasso.get().load(hayvan.getResim2()).into(imageViewResim2);
        }
        if (hayvan.getResim3() != null){
            Picasso.get().load(hayvan.getResim3()).into(imageViewResim3);
        }
        if (hayvan.getResim4() != null){
            Picasso.get().load(hayvan.getResim4()).into(imageViewResim4);
        }
        if(hayvan.getCinsiyetId() == 1){
            radioButtonErkek.setChecked(true);
        }else {
            radioButtonKiz.setChecked(true);
        }

        imageViewResim1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resimSec(RESIM_ISTEK_1);
            }
        });
        imageViewResim2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resimSec(RESIM_ISTEK_2);
            }
        });
        imageViewResim3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resimSec(RESIM_ISTEK_3);
            }
        });
        imageViewResim4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resimSec(RESIM_ISTEK_4);
            }
        });
        spinnerTur.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Tur seciliTur = new Tur();
                seciliTur = turler.get(spinnerTur.getSelectedItemPosition());
                hayvan.setTurId(seciliTur.getId());
                cinsler = seciliTur.getCinsler();
                ArrayAdapter<Cins> adapterCins = new ArrayAdapter<Cins>(HayvanDuzenleActivity.this,android.R.layout.simple_spinner_item,cinsler);
                adapterCins.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerCins.setAdapter(adapterCins);

                for(int i = 0; i<cinsler.size();i++){
                    if(hayvan.getCinsId() == cinsler.get(i).getId()){
                        spinnerCins.setSelection(i);
                    }
                }
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


        btnKaydet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - sonTiklanmaZamani < 1000){
                    return;
                }
                sonTiklanmaZamani = SystemClock.elapsedRealtime();

                if (!TextUtils.isEmpty(editTextAd.getText())){
                    hayvan.setAd(editTextAd.getText().toString());
                    if (radioButtonErkek.isChecked()){
                        hayvan.setCinsiyetId(1);
                    }else {
                        hayvan.setCinsiyetId(2);
                    }
                    if (resim1DegistiMi){
                        String uniqueID = UUID.randomUUID().toString()+".jpg";
                        resimYukle(resim1Yol,uniqueID);
                        hayvan.setResim1(BASE_URL + uniqueID);
                    }
                    if (resim2DegistiMi){
                        String uniqueID = UUID.randomUUID().toString()+".jpg";
                        resimYukle(resim2Yol,uniqueID);
                        hayvan.setResim2(BASE_URL + uniqueID);
                    }
                    if (resim3DegistiMi){
                        String uniqueID = UUID.randomUUID().toString()+".jpg";
                        resimYukle(resim3Yol,uniqueID);
                        hayvan.setResim3(BASE_URL + uniqueID);
                    }
                    if (resim4DegistiMi){
                        String uniqueID = UUID.randomUUID().toString()+".jpg";
                        resimYukle(resim4Yol,uniqueID);
                        hayvan.setResim4(BASE_URL + uniqueID);
                    }
                    Log.d("HayvanDuzeleActivity","Hayvan : " + hayvan.toString());
                    hayvanGuncelle(hayvan,kullaniciID);
                }
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
                    ArrayAdapter<Tur> adapterTur = new ArrayAdapter<Tur>(HayvanDuzenleActivity.this,android.R.layout.simple_spinner_item,turler);
                    adapterTur.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerTur.setAdapter(adapterTur);
                    int turId = hayvan.getTurId();
                    spinnerTur.setSelection(turId-1);
                }
            }

            @Override
            public void onFailure(Call<List<Tur>> call, Throwable t) {

            }
        });
    }

    private void izinDenetle(){
        if (ContextCompat.checkSelfPermission(HayvanDuzenleActivity.this,
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
                            ActivityCompat.requestPermissions(HayvanDuzenleActivity.this,
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

    private void hayvanGuncelle(Hayvan h,int id){

        Call<Integer> call = petsApi.hayvanGuncelle(id,h.getId(),h);

        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.code() == 200){
                    Toast.makeText(HayvanDuzenleActivity.this,"Hayvan başarılı bir şekilde güncellendi..",Toast.LENGTH_SHORT).show();
                    finish();
                }
                if (response.code() == 405){
                    Toast.makeText(HayvanDuzenleActivity.this,"Bu işlemi gerçekleştirmeye yetkiniz yok",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Log.e("HayvanDuzenleActivity","ERROR : " + t.getMessage());
            }
        });
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

    public void resimSec(int istekKodu){
        Intent resimSec = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        resimSec.setType("image/*");
        startActivityForResult(Intent.createChooser(resimSec,"Resim seç"),istekKodu);
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
                    resim1DegistiMi = true;
                    resim1Yol = resimPath;
                    imageViewResim1.setImageBitmap(bitmap);
                }else if(requestCode == RESIM_ISTEK_2 ){
                    resim2DegistiMi = true;
                    resim2Yol = resimPath;
                    imageViewResim2.setImageBitmap(bitmap);
                }else if(requestCode == RESIM_ISTEK_3 ){
                    resim3DegistiMi = true;
                    resim3Yol = resimPath;
                    imageViewResim3.setImageBitmap(bitmap);
                }else if(requestCode == RESIM_ISTEK_4 ){
                    resim4DegistiMi = true;
                    resim4Yol = resimPath;
                    imageViewResim4.setImageBitmap(bitmap);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
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