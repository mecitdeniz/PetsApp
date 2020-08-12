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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mecitdeniz.petto.Objects.Ilan;
import com.mecitdeniz.petto.Objects.Kullanici;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfilDuzenleActivity extends AppCompatActivity {

    private Kullanici kullanici = new Kullanici();
    private long sonTiklanmaZamani = 0;
    private int STORAGE_PERMISSION_CODE = 1;
    private PetsApi petsApi;
    private int RESIM_ISTEK = 1;
    Uri path;
    String resimPath;
    private Bitmap bitmap;
    private Boolean resimDegistiMi = false;
    private int kullaniciID;
    private String BASE_URL = "http://192.168.1.3:3030/download/";

    TextView textViewToolBarBaslik;
    EditText editTextKullaniciAdi;
    EditText editTextAd;
    EditText editTextSoyAd;
    CircleImageView imageViewProfilResmi;
    EditText editTextBio;
    Button btnKaydet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil_duzenle);

        petsApi = PetsApiClient.getClient().create(PetsApi.class);

        SharedPreferences sharedpreferences = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        String value = sharedpreferences.getString("id","0");
        kullaniciID = Integer.parseInt(value);
        textViewToolBarBaslik = findViewById(R.id.textView_toolbar_title);
        editTextAd = findViewById(R.id.editText_ad);
        editTextSoyAd = findViewById(R.id.editText_soyAd);
        editTextBio = findViewById(R.id.editText_bio);
        editTextKullaniciAdi = findViewById(R.id.editText_kullanici_adi);
        imageViewProfilResmi = findViewById(R.id.imageView_profil_resmi);
        btnKaydet = findViewById(R.id.btn_kaydet);

        textViewToolBarBaslik.setText("Profili Düzenle");

        kullanici = (Kullanici) getIntent().getSerializableExtra("kullanici");

        Picasso.get().load(kullanici.getProfilResmi()).into(imageViewProfilResmi);
        editTextKullaniciAdi.setText(kullanici.getKullaniciAdi());
        editTextAd.setText(kullanici.getAd());
        editTextSoyAd.setText(kullanici.getSoyAd());
        if (kullanici.getBio() != null){
            editTextBio.setText(kullanici.getBio());
        }

        imageViewProfilResmi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resimSec(RESIM_ISTEK);
            }
        });

        btnKaydet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - sonTiklanmaZamani < 1000){
                    return;
                }
                sonTiklanmaZamani = SystemClock.elapsedRealtime();

                if (!TextUtils.isEmpty(editTextAd.getText()) && !TextUtils.isEmpty(editTextSoyAd.getText())
                        && !TextUtils.isEmpty(editTextKullaniciAdi.getText())){
                    kullanici.setAd(editTextAd.getText().toString());
                    kullanici.setSoyAd(editTextSoyAd.getText().toString());

                    if (resimDegistiMi){
                        String uniqueID = UUID.randomUUID().toString()+".jpg";
                        kullanici.setProfilResmi(BASE_URL + uniqueID);
                        resimYukle(resimPath,uniqueID);
                    }

                    if (!editTextKullaniciAdi.getText().equals(kullanici.getKullaniciAdi())){
                        kullanici.setKullaniciAdi(editTextKullaniciAdi.getText().toString());
                    }
                    if (!TextUtils.isEmpty(editTextBio.getText())){
                        kullanici.setBio(editTextBio.getText().toString());

                    }
                    kullaniciGuncelle(kullanici,kullaniciID);
                    Log.d("ProfilDuzenleActivity","Kullanici : " + kullanici.toString());
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if( resultCode == RESULT_OK && data != null){
            path = data.getData();

            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            resimDegistiMi = true;
            Cursor cursor = getContentResolver().query(path,filePathColumn,null,null,null);
            assert cursor !=null;
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            resimPath = cursor.getString(columnIndex);

            cursor.close();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),path);
                if(requestCode == RESIM_ISTEK ){
                    imageViewProfilResmi.setImageBitmap(bitmap);
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
    private void izinIste() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {
            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed because of this and that")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(ProfilDuzenleActivity.this,
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
    private void izinDenetle(){
        if (ContextCompat.checkSelfPermission(ProfilDuzenleActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
        } else {
            izinIste();
        }
    }

    public void resimSec(int istekKodu){
        Intent resimSec = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        resimSec.setType("image/*");
        startActivityForResult(Intent.createChooser(resimSec,"Resim seç"),istekKodu);
    }

    public void resimYukle(String resim, String uniqueID){

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
                Log.d("ProfilDuzenleActivity","ERROR : " + t.getMessage());
            }
        });
    }

    public void kullaniciGuncelle(Kullanici k, int id){
        Call<Integer> call = petsApi.kullaniciGuncelle(k,id);
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.code() == 200){
                    Toast.makeText(ProfilDuzenleActivity.this,"Profiliniz başarılı bir şekilde güncellendi :)",Toast.LENGTH_SHORT).show();
                    finish();
                }else if (response.code() == 409){
                    Toast.makeText(ProfilDuzenleActivity.this,"Seçmiş olduğunuz kullanıcı adı zaten bir başka hesap tarafından kullanılmakta..",Toast.LENGTH_SHORT).show();
                }else {
                    Log.e("ProfilDuzenleActivity","ERROR : response code :" + String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Log.e("ProfilDuzenleActivity","ERROR : " + t.getMessage());
            }
        });

    }

}